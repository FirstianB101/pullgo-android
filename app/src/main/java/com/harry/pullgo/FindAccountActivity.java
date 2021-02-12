package com.harry.pullgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import lib.kingja.switchbutton.SwitchMultiButton;

public class FindAccountActivity extends AppCompatActivity implements SwitchMultiButton.OnSwitchListener {

    LinearLayout findUserName, findPassword;
    SwitchMultiButton findWhich;
    TextInputEditText usernameFullname, usernamePhone;
    TextInputEditText passwordUsername, passwordPhone;
    Button buttonUsername, buttonPassword;

    private String inputFullname = "";
    private String inputPhone = "";
    private String inputUsername = "";

    private void attachViewById() {
        findUserName = findViewById(R.id.layout_findAccount_findUsername);
        findPassword = findViewById(R.id.layout_findAccount_findPassword);
        findWhich = findViewById(R.id.switch_find_id_password);
        usernameFullname = findViewById(R.id.textInputEditText_findUsername_fullName);
        usernamePhone = findViewById(R.id.textInputEditText_findUsername_phone);
        passwordUsername = findViewById(R.id.textInputEditText_findPassword_userName);
        passwordPhone = findViewById(R.id.textInputEditText_findPassword_phone);
        buttonUsername = findViewById(R.id.button_find_username);
        buttonPassword = findViewById(R.id.button_find_password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        attachViewById();
        Intent intent = getIntent();

        boolean isStudent = intent.getBooleanExtra("isStudent", true);

        findWhich.setOnSwitchListener(this);
        usernameFullname.addTextChangedListener(usernameFullnameWatcher);
        usernamePhone.addTextChangedListener(usernamePhoneWatcher);

        Toast.makeText(this, isStudent ? "student" : "teacher", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSwitch(int position, String tabText) {
        //0 -> 아이디 찾기, 1 -> 비밀번호 찾기
        if (position == 0) {
            findUserName.setVisibility(View.VISIBLE);
            findPassword.setVisibility(View.INVISIBLE);
        } else if (position == 1) {
            findUserName.setVisibility(View.INVISIBLE);
            findPassword.setVisibility(View.VISIBLE);
        }
    }

    private void setButtonValidate() {
        //형식이 올바르면 버튼 활성화
    }

    private final TextWatcher usernameFullnameWatcher = new TextWatcher() {
        //아이디 찾기 - 이름 입력
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputFullname = s.toString();
            setButtonValidate();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    private final TextWatcher usernamePhoneWatcher = new TextWatcher() {
        //아이디 찾기 - 전화번호 입력
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            inputPhone = s.toString();
            setButtonValidate();
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}