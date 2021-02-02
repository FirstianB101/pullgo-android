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

public class LoginFragment extends Fragment {
    RadioButton studentButton;
    RadioButton teacherButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_login, container, false);

        studentButton=rootView.findViewById(R.id.studentButton);
        teacherButton=rootView.findViewById(R.id.teacherButton);

        Button signUpButton=rootView.findViewById(R.id.buttonSignUp);
        signUpButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                if(studentButton.isChecked()) {
                    intent = new Intent(getContext(), StudentSignUpActivity.class);
                }else if(teacherButton.isChecked()) {
                    intent = new Intent(getContext(), TeacherSignUpActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        Button findAccountButton=rootView.findViewById(R.id.buttonFindAccount);
        findAccountButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //찾기 버튼 눌렸을 때 동작
            }
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