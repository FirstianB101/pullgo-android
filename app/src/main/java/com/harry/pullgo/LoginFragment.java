package com.harry.pullgo;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import lib.kingja.switchbutton.SwitchMultiButton;

public class LoginFragment extends Fragment {
    SwitchMultiButton studentOrTeacher;
    boolean isStudent=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        studentOrTeacher=rootView.findViewById(R.id.switchButton);
        studentOrTeacher.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                if(position==0){
                    isStudent=true;
                }else if(position==1){
                    isStudent=false;
                }
            }
        });

        Button signUpButton=rootView.findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                if(isStudent) {
                    intent = new Intent(getContext(), StudentSignUpActivity.class);
                }else if(!isStudent) {
                    intent = new Intent(getContext(), TeacherSignUpActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button findAccountButton=rootView.findViewById(R.id.buttonFindAccount);
        findAccountButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FindAccountActivity.class);
            intent.putExtra("isStudent", isStudent);

            startActivity(intent);
        });

        Button loginButton=rootView.findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getContext(),"로그인 버튼 눌림",Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
    }
}