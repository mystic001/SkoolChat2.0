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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterAsAdminActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText fullName,email,phoneNumber,word;
    private Button buttonCancel, buttonSig;
    private SkoolChatRepo skoolChatRepo;
    private FirebaseAuth firebaseAuth;
    private ProgressBar bar ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_as_admin);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        firebaseAuth = FirebaseAuth.getInstance();
        defineView();
        buttonCancel.setOnClickListener(this);
        buttonSig.setOnClickListener(this);
    }



    private void defineView(){
        fullName = findViewById(R.id.fullName);
        email = findViewById(R.id.emailED);
        phoneNumber = findViewById(R.id.phoneNumber);
        word = findViewById(R.id.password);
        buttonCancel = findViewById(R.id.button7);
        buttonSig = findViewById(R.id.button8);
        bar = findViewById(R.id.progressBar);
    }

    @Override
    public void onClick(View view) {


        switch(view.getId()){

            case R.id.button7:
               Intent intent = new Intent(RegisterAsAdminActivity.this,SelectUserTypeActivity.class);
               startActivity(intent);
                break;

            case R.id.button8:
                String name = fullName.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String password = word.getText().toString().trim();
                bar.setVisibility(View.VISIBLE);
                registerAdmin(name,mail,password);
                break;
        }

    }


    private void registerAdmin(final String username, final String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(RegisterAsAdminActivity.this,"We could not add you",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    assert firebaseUser != null;
                                    String userId = firebaseUser.getUid();
                                    String phone = phoneNumber.getText().toString();
                                    String receiverId = "HnOcgQnjO2XMT6XwTUzSCwGfq9j2";
                                    User user = new User(username, email, username);
                                    user.setUid(userId);
                                    user.setPhoneNumber(phone);
                                    skoolChatRepo.sendAdminRequest(userId, receiverId, RegisterAsAdminActivity.this, bar, user);
                                }

                                 } ;
                        });



    }

}