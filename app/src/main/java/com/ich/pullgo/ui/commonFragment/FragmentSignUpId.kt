package com.ich.pullgo.ui.commonFragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ich.pullgo.R
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentSignupIdBinding
import com.ich.pullgo.ui.signUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class FragmentSignUpId(private val isTeacher: Boolean): Fragment() {
    private val binding by lazy{FragmentSignupIdBinding.inflate(layoutInflater)}
    private val ID_TYPE_EXPRESSION = "^[a-z0-9]{1}[a-z0-9-_]*$"
    private val ID_MAX_LENGTH = 16
    private val ID_MIN_LENGTH = 8
    private var idFormatSuccess = false

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: SignUpViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initViewModel()
        setListeners()

        return binding.root
    }

    private fun initViewModel(){
        viewModel.usernameExist.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if(!it.data?.exists!!){
                        showNextButton()
                    }else{
                        binding.signUpIdLayout.error = "중복된 아이디입니다"
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(),"인터넷 연결을 확인해 주세요(${it.message})",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setListeners(){
        binding.signUpId.addTextChangedListener(idWatcher)

        binding.buttonSignUpIdOverlap.setOnClickListener{
            if(!idFormatSuccess)
                Toast.makeText(context,"사용할 수 없는 아이디입니다.",Toast.LENGTH_SHORT).show()
            else {
                val username = binding.signUpId.text.toString()

                if(isTeacher) viewModel.teacherUsernameExists(username)
                else viewModel.studentUsernameExists(username)
            }
        }

        binding.buttonSignUpIdNext.setOnClickListener {
            viewModel.signUpId.postValue(binding.signUpId.text.toString())
        }
    }

    private fun showNextButton(){
        if(binding.buttonSignUpIdNext.visibility != View.VISIBLE) {
            binding.buttonSignUpIdNext.visibility = View.VISIBLE
            val anim = AnimationUtils.loadAnimation(context, R.anim.alpha)
            binding.buttonSignUpIdNext.startAnimation(anim)
        }
    }

    fun checkId(inputId: String):Boolean{
        if(inputId == "")
            return false

        if(inputId[0] == '-' || inputId[0] == '_'){
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
            binding.signUpIdLayout.error = "아이디가 너무 길어요!"
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
                binding.signUpIdLayout.setHelperTextColor(ColorStateList.valueOf(ContextCompat.getColor(requireContext(),
                    R.color.material_700_green
                )))
            }
            else {
                idFormatSuccess=false
            }
        }

    }
}