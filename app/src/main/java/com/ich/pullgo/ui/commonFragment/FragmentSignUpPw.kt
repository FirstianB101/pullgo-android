package com.ich.pullgo.ui.commonFragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ich.pullgo.R
import com.ich.pullgo.databinding.FragmentSignupPwBinding
import com.ich.pullgo.ui.signUp.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.regex.Pattern

@AndroidEntryPoint
class FragmentSignUpPw: Fragment() {
    private val binding by lazy{FragmentSignupPwBinding.inflate(layoutInflater)}
    private val PW_MAX_LENGTH = 16
    private val PW_MIN_LENGTH = 8
    private val PW_EXPRESSION = "^[\\x00-\\x7F]*$"
    private var pwFormatSuccess=false
    private var pwSame=false

    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        initViewModel()
        setListeners()

        return binding.root
    }

    private fun initViewModel(){
        viewModel = ViewModelProvider(requireActivity()).get(SignUpViewModel::class.java)
    }

    private fun setListeners(){
        binding.signUpPw.addTextChangedListener(pwWatcher)
        binding.signUpPwCheck.addTextChangedListener(pwSameWatcher)

        binding.buttonSignUpPwNext.setOnClickListener {
            viewModel.signUpPw.postValue(binding.signUpPw.text.toString())
        }
    }

    fun checkPw(inputPw: String):Boolean{
        if(!Pattern.matches(PW_EXPRESSION,inputPw)){
            binding.signUpPwLayout.error="사용할 수 없는 문자가 포함됐어요!"
            return false
        }

        if(PW_MIN_LENGTH>inputPw.length){
            binding.signUpPwLayout.error="비밀번호가 너무 짧아요!"
            return false
        }

        if(PW_MAX_LENGTH<inputPw.length){
            binding.signUpPwLayout.error="비밀번호가 너무 길어요!"
            return false
        }

        return true
    }

    fun checkPwSame(inputPw: String):Boolean{
        if(binding.signUpPw.text.toString()==inputPw){
            return true
        }
        return false
    }

    private val pwWatcher=object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            binding.buttonSignUpPwNext.visibility=View.GONE
            if(checkPw(s.toString())){
                pwFormatSuccess=true
                binding.signUpPwLayout.helperText="사용 가능한 비밀번호입니다!"
                binding.signUpPwLayout.setHelperTextColor(ColorStateList.valueOf(resources.getColor(
                    R.color.material_700_green
                )))
            }
            else pwFormatSuccess=false

            if(!checkPwSame(binding.signUpPwCheck.text.toString())){
                binding.signUpPwCheckLayout.error="비밀번호가 일치하지 않습니다!"
                pwSame=false
            }else{
                pwSame=true
                binding.signUpPwCheckLayout.helperText="비밀번호가 일치합니다!"
                binding.signUpPwCheckLayout.setHelperTextColor(ColorStateList.valueOf(resources.getColor(
                    R.color.material_700_green
                )))

                binding.buttonSignUpPwNext.visibility=View.VISIBLE
                val anim = AnimationUtils.loadAnimation(context, R.anim.alpha)
                binding.buttonSignUpPwNext.startAnimation(anim)
            }
        }
    }

    private val pwSameWatcher=object:TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            binding.buttonSignUpPwNext.visibility=View.GONE
            pwSame=false
            if(!pwFormatSuccess){
                binding.signUpPwCheckLayout.error="사용할 수 없는 비밀번호입니다!"
            }
            else if(checkPwSame(s.toString())){
                pwSame=true
                binding.signUpPwCheckLayout.helperText="비밀번호가 일치합니다!"
                binding.signUpPwCheckLayout.setHelperTextColor(ColorStateList.valueOf(resources.getColor(
                    R.color.material_700_green
                )))

                binding.buttonSignUpPwNext.visibility=View.VISIBLE
                val anim = AnimationUtils.loadAnimation(context, R.anim.alpha)
                binding.buttonSignUpPwNext.startAnimation(anim)
            }
            else{
                binding.signUpPwCheckLayout.error="비밀번호가 일치하지 않습니다!"
            }
        }
    }
}