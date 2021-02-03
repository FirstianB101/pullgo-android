package com.harry.pullgo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

public class StudentSignUpActivity extends AppCompatActivity {
    public static final int ID_MAX_LENGTH=16;
    public static final int ID_MIN_LENGTH=5;
    public static final int PW_MAX_LENGTH=16;
    public static final int PW_MIN_LENGTH=8;
    public static final String ID_EXPRESSION="^[a-z0-9]{1}[a-z0-9-_]{4,16}$";
    public static final String PW_EXPRESSION="^[\\x00-\\x7F]{7,16}$";
    //시작은 영문 or 숫자, 영문 숫자 - _로만 조합된 5~16자리 가능

    TextInputEditText fullName;
    TextInputEditText userName;
    TextInputEditText password;
    TextInputEditText passwordCheck;
    TextInputEditText phone;
    TextInputEditText phoneVerification;

    ImageView passwordSameCheckImage;
    ImageView passwordCheckImage;

    Button signupButton;
    AppCompatSpinner gradeSpinner;

    boolean isPasswordSame=false;   //입력된 비밀번호와 비밀번호 확인이 같은지
    boolean idFormatSuccess=false;  //입력된 id가 형식에 맞는지(5~16 영문 소문자, 숫자, -, _) 단 특수문자가 처음에 올 수 없다
    boolean pwFormatSuccess=false;  //입력된 비밀번호가 형식에 맞는지(8~16자 아스키코드)
    //boolean phoneVerificationSuccess=false;   //휴대폰 인증 성공 여부

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_student);

        fullName=findViewById(R.id.editTextName);
        userName=findViewById(R.id.editTextId);
        password=findViewById(R.id.editTextPw);
        passwordCheck=findViewById(R.id.editTextPwCheck);
        phone=findViewById(R.id.editTextPhone);
        phoneVerification=findViewById(R.id.editTextPhoneVerification);
        passwordSameCheckImage=findViewById(R.id.passwordSameCheck);
        passwordCheckImage=findViewById(R.id.passwordCheck);
        signupButton=findViewById(R.id.buttonSignUp);
        gradeSpinner=findViewById(R.id.schoolGradeSpinner);

        passwordCheckImage.setVisibility(View.INVISIBLE);
        passwordSameCheckImage.setVisibility(View.INVISIBLE);

        userName.addTextChangedListener(idWatcher);
        password.addTextChangedListener(pwWatcher);
        passwordCheck.addTextChangedListener(pwSameWatcher);

        gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public boolean isEmpty(TextInputEditText editText){
        String input=editText.getText().toString();
        return input.isEmpty();
    }

    public void idOverlapButtonClicked(View v){
        //중복 확인 버튼 눌렸을 때
    }

    public void sendVerificationNumberClicked(View v){
        //본인 인증 버튼 눌렸을 때
    }

    public void verificationButtonClicked(View v){
        //인증 확인 버튼 눌렸을 때
    }

    public void signupSuccessButtonClicked(View v){
        //회원 가입 버튼 눌렸을 때
        if(checkEmptyBoxExist()){
            Toast.makeText(this,"완료되지 않은 항목이 존재합니다",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"회원가입 성공",Toast.LENGTH_SHORT).show();
        }
    }

    //입력된 id가 형식에 맞는지 확인
    public void checkId(String inputId){
        idFormatSuccess=false;
        if(inputId.equals("")){
            return;
        }

        if(inputId.charAt(0)=='-' || inputId.charAt(0)=='_'){
            userName.setError("첫 글자는 특수문자를 사용할 수 없습니다");
            return;
        }

        if(ID_MIN_LENGTH > inputId.length() || inputId.length() > ID_MAX_LENGTH){
            userName.setError("아이디는 5자리 이상 16자리 이하여야 합니다");
            return;
        }

        if(!Pattern.matches(ID_EXPRESSION,inputId)){
            userName.setError("영문 소문자, 숫자, 특수문자(- _)만 사용 가능합니다");
            return;
        }

        idFormatSuccess=true;
    }

    //입력된 비밀번호가 형식에 맞는지 확인
    public void checkPw(String inputPw){
        passwordCheckImage.setVisibility(View.INVISIBLE);
        passwordSameCheckImage.setVisibility(View.INVISIBLE);
        pwFormatSuccess=false;

        if(PW_MIN_LENGTH > inputPw.length() || inputPw.length() > PW_MAX_LENGTH){
            password.setError("비밀번호는 8자리 이상 16자리 이하여야 합니다");
            return;
        }

        if(!Pattern.matches(PW_EXPRESSION,inputPw)){
            password.setError("사용할 수 없는 비밀번호 입니다");
            return;
        }

        passwordCheckImage.setVisibility(View.VISIBLE);
        pwFormatSuccess=true;
    }

    //빈 항목 or 체크 안된 항목 있을 경우 true
    public boolean checkEmptyBoxExist(){
        //나중에 휴대폰 인증 추가 시 phoneVerificationSuccess 넣고 phone은 제외
        if(idFormatSuccess && isPasswordSame && pwFormatSuccess
                && !isEmpty(fullName) && !isEmpty(phone) && !isEmpty(phoneVerification)
        ){
            return false;
        }else{
            return true;
        }
    }

    //아이디 비번 비번확인 위한 TextWatcher
    private final TextWatcher idWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputString=s.toString();
            checkId(inputString);
        }
    };
    private final TextWatcher pwWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputString=s.toString();
            checkPw(inputString);
        }
    };
    private final TextWatcher pwSameWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String inputString=s.toString();
            String inputPassword=password.getText().toString();

            if(inputString.equals(inputPassword)){
                passwordSameCheckImage.setVisibility(View.VISIBLE);
                isPasswordSame=true;
                return;
            }

            passwordSameCheckImage.setVisibility(View.INVISIBLE);
            isPasswordSame=false;
        }
    };
}

