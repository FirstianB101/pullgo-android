package com.harry.pullgo

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.harry.pullgo.databinding.FragmentSignupIdBinding
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern
import kotlin.concurrent.fixedRateTimer
import kotlin.coroutines.CoroutineContext

class FragmentSignUpId(): Fragment() {
    private val binding by lazy{FragmentSignupIdBinding.inflate(layoutInflater)}
    private val ID_TYPE_EXPRESSION="^[a-z0-9]{1}[a-z0-9-_]*$"
    private val ID_MAX_LENGTH=16
    private val ID_MIN_LENGTH=8
    private var idFormatSuccess=false

    var callbackListener: SignUpFragmentSwitch?=null
    var teacherList: List<Teacher>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackListener=context as SignUpFragmentSwitch
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.signUpId.addTextChangedListener(idWatcher)

        binding.buttonSignUpIdOverlap.setOnClickListener{
            if(!idFormatSuccess)
                Toast.makeText(context,"사용할 수 없는 아이디입니다.",Toast.LENGTH_SHORT).show()
            else if(checkIdUnique(binding.signUpId.text.toString())){
                if(binding.buttonSignUpIdNext.visibility!=View.VISIBLE) {
                    binding.buttonSignUpIdNext.visibility = View.VISIBLE
                    val anim = AnimationUtils.loadAnimation(context, R.anim.alpha)
                    binding.buttonSignUpIdNext.startAnimation(anim)
                }
            }
        }

        binding.buttonSignUpIdNext.setOnClickListener {
            val bundle=Bundle()
            bundle.putString("signUpId",binding.signUpId.text.toString())
            callbackListener?.onDataPass(1,bundle)
        }

        return binding.root
    }

    fun checkIdUnique(id: String):Boolean{
        if(teacherOverlap(id)){
            Toast.makeText(context,"중복된 아이디입니다",Toast.LENGTH_SHORT).show()
            return false
        }else{
            return true
        }
    }

    fun teacherOverlap(userId:String):Boolean{
        var isOverlap=false
        //val retrofit=RetrofitClient.getInstance()
        //val service=retrofit.create(RetrofitService::class.java)

        //val call:Call<List<Teacher>> = service.getTeachersList()

        return isOverlap
    }

    fun checkId(inputId: String):Boolean{
        if(inputId == "")
            return false

        if(inputId[0]=='-' || inputId[0]=='_'){
            binding.signUpIdLayout.error = "첫 글자는 특수문자를 사용할 수 없습니다";
            return false
        }

        if(!Pattern.matches(ID_TYPE_EXPRESSION,inputId)){
            binding.signUpIdLayout.error = "영문 소문자, 숫자, 특수문자(- _)만 사용 가능합니다";
            return false
        }

        if(ID_MIN_LENGTH > inputId.length ){
            binding.signUpIdLayout.error = "아이디가 너무 짧아요!";
            return false
        }

        if(inputId.length>ID_MAX_LENGTH){
            binding.signUpIdLayout.error="아이디가 너무 길어요!"
            return false
        }

        return true;
    }
    private val idWatcher=object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.buttonSignUpIdNext.visibility = View.GONE
            if(checkId(s.toString())){
                idFormatSuccess=true
                binding.signUpIdLayout.helperText="사용 가능한 아이디입니다!"
                binding.signUpIdLayout.setHelperTextColor(ColorStateList.valueOf(resources.getColor(R.color.material_700_green)))
            }
            else {
                idFormatSuccess=false
            }
        }

    }
}