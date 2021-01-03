package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterSchoolActivity extends AppCompatActivity {

    EditText schoolName, schoolEmail,phoneNumber,passwordStud,passwordteach;
    Button button5, buttonSignUp;
    SkoolChatRepo skoolChatRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_school);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        defineViews();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = schoolName.getText().toString().trim();
                String teachPass = passwordteach.getText().toString().trim().toLowerCase();
                String studentPass = passwordStud.getText().toString().trim().toLowerCase();
                String phone = phoneNumber.getText().toString().trim();
                skoolChatRepo.addSchoolRepo(RegisterSchoolActivity.this,name,phone,teachPass,studentPass);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterSchoolActivity.this,SkoolActivity.class);
                startActivity(intent);
            }
        });

    }


    private void defineViews(){
        schoolName = findViewById(R.id.EdtSchoolName);
        schoolEmail = findViewById(R.id.EdtEmail);
        phoneNumber = findViewById(R.id.phoneNumber);
        passwordStud = findViewById(R.id.studentpass);
        passwordteach = findViewById(R.id.teacherspass);
        button5 = findViewById(R.id.button5);
        buttonSignUp = findViewById(R.id.button6);
    }
}