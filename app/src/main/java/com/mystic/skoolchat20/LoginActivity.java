package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username , password;
    private Button btn_Sig, btn_Cancel;
    private SkoolChatRepo skoolChatRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        defineViews();

        btn_Sig.setOnClickListener(this);
        btn_Cancel.setOnClickListener(this);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
    }

    public void defineViews(){
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        btn_Sig = findViewById(R.id.logSig);
        btn_Cancel = findViewById(R.id.logCancel);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.logSig:
                ProgressBar bar = findViewById(R.id.progre);
                String email = username.getText().toString().trim();
                String pass = password.getText().toString().trim();
                skoolChatRepo.loginToBase(email,pass,this,bar);
                break;

            case R.id.logCancel:
                Toast.makeText(this,"You really want to go back ",Toast.LENGTH_LONG).show();
                break;
        }
    }
}