package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterAsOwnerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fullName,email, password,phone;
    private Button btn_SignUp, btn_Cancel,btn_Upload;
    private SkoolChatRepo skoolChatRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_owner);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        defineViews();
        btn_Upload.setOnClickListener(this);
        btn_SignUp.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
    }




    public void defineViews(){
        fullName = findViewById(R.id.ownername);
        email = findViewById(R.id.owneremail);
        phone = findViewById(R.id.ownerphone);
        password = findViewById(R.id.ownerpass);
        btn_SignUp = findViewById(R.id.button11);
        btn_Cancel = findViewById(R.id.button12);
        btn_Upload = findViewById(R.id.button13);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button11:
                String name = fullName.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String word = password.getText().toString().trim();
                String phoneno = phone.getText().toString().trim();
                User user = new User(name,mail);
                user.setPassword(word);
                user.setPhoneNumber(phoneno);
                user.setUserVerified(true);
                skoolChatRepo.addOwner(mail,word,user,this);
                break;

            case R.id.button12:
                Intent intent = new Intent(RegisterAsOwnerActivity.this,SelectUserTypeActivity.class);
                startActivity(intent);
                finish();
                break;


            case R.id.button13:
                Intent intentUpload = new Intent(RegisterAsOwnerActivity.this,RegisterSchoolActivity.class);
                startActivity(intentUpload);
                finish();
                break;
        }
    }
}