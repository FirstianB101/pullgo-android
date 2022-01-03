package com.ich.pullgo.ui.commonFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ich.pullgo.application.PullgoApplication
import com.ich.pullgo.data.api.OnCheckPwListener
import com.ich.pullgo.data.utils.Status
import com.ich.pullgo.databinding.FragmentChangeInfoCheckPwBinding
import com.ich.pullgo.domain.model.Account
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChangeInfoCheckPwFragment(private val pwCheckListener: OnCheckPwListener?): Fragment() {
    private val binding by lazy{FragmentChangeInfoCheckPwBinding.inflate(layoutInflater)}

    @Inject
    lateinit var app: PullgoApplication

    private val viewModel: ChangeInfoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.authUser.observe(requireActivity()){
            when(it.status){
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    app.loginUser = it.data!!
                    binding.changeInfoCheckPwLayout.error = null
                    pwCheckListener?.onPasswordChecked()
                }
                Status.ERROR -> {
                    binding.changeInfoCheckPwLayout.error =
                        if(it.message == "401" || it.message == "403") "비밀번호가 일치하지 않습니다"
                        else "서버와 연결할 수 없습니다"
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding.buttonInfoCheckPw.setOnClickListener {
            getAccountWithInputPw()?.let { account -> viewModel.requestAuth(account) }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.changeInfoCheckPw.setText("")
    }

    private fun getAccountWithInputPw(): Account?{
        val account = if(app.loginUser.student?.account == null) app.loginUser.teacher?.account
                    else app.loginUser.student?.account
        account?.password = binding.changeInfoCheckPw.text.toString()

        return account
    }
}