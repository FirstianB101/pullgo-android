package com.harry.pullgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class TeacherSignUpActivity extends AppCompatActivity {
    EditText fullName;
    EditText userName;
    EditText password;
    EditText passwordCheck;
    EditText phone;
    EditText phoneVerification;
    ImageView passwordCheckImage;

    boolean isPasswordCheckSuccess=false;   //입력된 비밀번호와 비밀번호 확인이 같은지
    boolean idFormatSuccess=false;  //입력된 id가 형식에 맞는지(5~16 영문 소문자, 숫자, 특수문자)
    boolean pwFormatSuccess=false;  //입력된 비밀번호가 형식에 맞는지(8~16자 영문 소문자, 숫자, 특수문자)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_teacher);

        fullName=findViewById(R.id.editTextName);
        userName=findViewById(R.id.editTextId);
        password=findViewById(R.id.editTextPw);
        passwordCheck=findViewById(R.id.editTextPwCheck);
        phone=findViewById(R.id.editTextPhone);
        phoneVerification=findViewById(R.id.editTextPhoneVerification);
        passwordCheckImage=findViewById(R.id.passwordCheck);
    }

    public void idOverlapButtonClicked(View v){

    }

    public void sendVerificationNumberClicked(View v){

    }

    public void verificationButtonClicked(View v){

    }

    public void signupSuccessButtonClicked(View v){

    }

    private final TextWatcher idWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputText=s.toString();

        }
    };
    public final TextWatcher pwWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}