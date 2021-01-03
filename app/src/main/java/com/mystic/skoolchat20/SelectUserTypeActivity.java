package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SelectUserTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView regAdmin, regStud,regTeacher, quit, login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        defineViews();

        regAdmin.setOnClickListener(this);
        regStud.setOnClickListener(this);
        regTeacher.setOnClickListener(this);
        login.setOnClickListener(this);
        quit.setOnClickListener(this);
    }


    private void defineViews(){
        regAdmin = findViewById(R.id.regAdmin);
        regStud = findViewById(R.id.right);
        regTeacher = findViewById(R.id.left);
        quit = findViewById(R.id.quit);
        login = findViewById(R.id.login);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.regAdmin:
                Intent intent = new Intent(SelectUserTypeActivity.this,RegisterAsAdminActivity.class);
                startActivity(intent);
                break;

            case R.id.right:
            case R.id.left:
                Intent intenting = new Intent(SelectUserTypeActivity.this,RegistrationActivity.class);
                startActivity(intenting);
                break;

            case R.id.quit:
                Intent intents = new Intent(SelectUserTypeActivity.this,RegisterAsOwnerActivity.class);
                startActivity(intents);
                break;


            case R.id.login:
                Intent intel = new Intent(SelectUserTypeActivity.this,LoginActivity.class);
                startActivity(intel);
            default:

        }
    }


}