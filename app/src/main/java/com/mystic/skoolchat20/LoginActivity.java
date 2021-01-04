package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText username , password;
    private Button btn_Sig, btn_Cancel;
    private SkoolChatRepo skoolChatRepo;
    private FirebaseAuth mAuth;
    private User realUser;

    @Override
    protected void onStart() {
        super.onStart();
        automaticLogin();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        defineViews();

        mAuth = FirebaseAuth.getInstance();
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
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginActivity.this,"There was an error"+error.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
            Intent intent = new Intent(LoginActivity.this,SkoolActivity.class);
            intent.putExtra(SkoolChatRepo.REAL_USER,realUser);
            startActivity(intent);
        }
    }
}