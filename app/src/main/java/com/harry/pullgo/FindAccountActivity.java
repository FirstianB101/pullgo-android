package com.harry.pullgo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class FindAccountActivity extends AppCompatActivity {

    boolean isStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_account);

        Intent intent = getIntent();

        isStudent = (boolean) intent.getBooleanExtra("isStudent", true);

        if (isStudent)
    }
}