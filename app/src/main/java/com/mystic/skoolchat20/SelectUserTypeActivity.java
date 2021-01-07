package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SelectUserTypeActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView regAdmin, regStud,regTeacher, quit, login;
    private FirebaseAuth mAuth;
    private User realUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user_type);
        mAuth = FirebaseAuth.getInstance();
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
                automaticLogin();
            default:

        }
    }



    public void automaticLogin(){
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            String userId = user.getUid();
            DatabaseReference mDatabaserefUser = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS).child(userId);
            //This line of code helps us to get the specific user from firebase base
            mDatabaserefUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    realUser = snapshot.getValue(User.class);
                    Intent intent = new Intent(SelectUserTypeActivity.this,SkoolActivity.class);
                    intent.putExtra(SkoolChatRepo.REAL_USER,realUser);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(SelectUserTypeActivity.this,"There was an error"+error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });

        }else{
            Intent intel = new Intent(SelectUserTypeActivity.this,LoginActivity.class);
            startActivity(intel);
        }
    }


}