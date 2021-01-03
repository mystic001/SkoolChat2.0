package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText fullName,email,password;
    private Spinner role,schoolName;
    private List<String> schoolList;
    private ProgressBar bar;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private Button button;
    private SkoolChatRepo instanceOfChatRepo;
    private ArrayAdapter<String> dataAdapter;
    private int pos = 0;
    private String schoolNames = "First item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationactivity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        schoolList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.SCHOOL_NAME);

        Log.d("Test","testing");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    School school = snapshot1.getValue(School.class);
                    schoolList.add(school.getSchoolName());
                }
                Log.d("School Names ",""+schoolList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        instanceOfChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        defineViews();
        spinnerMethod();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullame = fullName.getText().toString();
                String mail = email.getText().toString();
                String passwords = password.getText().toString();
                bar.setVisibility(View.VISIBLE);
                register(fullame,mail,passwords);
            }
        });




    }

    private void defineViews(){
        fullName = findViewById(R.id.EdtFullName);
        email = findViewById(R.id.EdtEmail);
        role = findViewById(R.id.spinnerRole);
        schoolName = findViewById(R.id.spinnerSchoolNames);
        button = findViewById(R.id.button2);
        password = findViewById(R.id.password);
        bar = findViewById(R.id.prog);

    }

    private void spinnerMethod() {
        dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schoolList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolName.setAdapter(dataAdapter);
        schoolName.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d("OntimeSelected","This is not working");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





    private void register(final String username, final String email, String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            School schoolAdmin = new School(schoolNames);
                            String schoolId = schoolAdmin.getSchoolId();
                            String rolee = role.getSelectedItem().toString();
                            User user = new User(username,email,schoolNames,rolee,userId);
                            if(rolee.equals("teacher")){
                                //this where the operation to send the admin of the school happens////
                                instanceOfChatRepo.sendRequest(userId,schoolId,RegistrationActivity.this,bar,user);

                            } else{
                                reference = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS).child(userId);
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Intent intent = new Intent(RegistrationActivity.this,SkoolActivity.class);
                                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }
                                });
                            }

                        }else{
                            Toast.makeText(RegistrationActivity.this,"Sorry you cant register",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }



}
