package com.harry.pullgo;

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.harry.pullgo.databinding.ActivityLoginBinding

import java.util.ArrayList;

class LoginActivity: AppCompatActivity(){
    private val binding by lazy{ActivityLoginBinding.inflate(layoutInflater)}
    var isStudent=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loginSwitchButton.setOnSwitchListener{ position, _ ->
            isStudent = (position==0)
        }

        binding.buttonSignUp.setOnClickListener {
            val intent=Intent(applicationContext,SignUpActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }

        binding.buttonFindAccount.setOnClickListener {
            val intent=Intent(applicationContext,FindAccountActivity::class.java)
            intent.putExtra("isStudent",isStudent)

            startActivity(intent)
        }

        binding.buttonLogin.setOnClickListener {
            val intent=Intent(applicationContext,StudentMainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }
}