package com.harry.pullgo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    LoginFragment loginFragment;
    TabButton focusedButton;
    TabButton buttonStudent;
    TabButton buttonTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FragmentManager manager=getSupportFragmentManager();
        loginFragment=(LoginFragment)manager.findFragmentById(R.id.loginFragment);

        buttonStudent=findViewById(R.id.buttonStudent);
        buttonTeacher=findViewById(R.id.buttonTeacher);

        buttonStudent.setOnClickListener(this);
        buttonTeacher.setOnClickListener(this);

        focusedButton=buttonStudent;
    }

    //학생과 선생님 로그인 과정이 동일해 보여서 일단 로그인 프래그먼트 교체는 하지 않았음.
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.buttonStudent:
                setFocus(focusedButton,buttonStudent);
                break;

            case R.id.buttonTeacher:
                setFocus(focusedButton,buttonTeacher);
                break;
        }
    }

    private void setFocus(TabButton btn_unfocus,TabButton btn_focus){
        Resources resources=getResources();
        btn_unfocus.setBackgroundColor(Color.WHITE);
        btn_unfocus.setTextColor(resources.getColor(R.color.main_color));

        btn_focus.setBackgroundColor(getResources().getColor(R.color.main_color));
        btn_focus.setTextColor(Color.WHITE);
        focusedButton=btn_focus;
    }
}