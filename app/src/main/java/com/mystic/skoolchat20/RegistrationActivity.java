package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.gms.tasks.OnFailureListener;
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
import java.util.List;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText fullName,email,password,passforSchool;

    private Spinner schoolName;
    private List<String> schoolList;
    private List<School> realSchools;
    private ProgressBar bar;
    private FirebaseAuth firebaseAuth;
    private Button button;
    private SkoolChatRepo instanceOfChatRepo;
    private int pos = 0;
    private String schoolNames = "First item";
    private static final String STUDENT = "student";
    private static final String TEACHER = "teacher";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrationactivity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        schoolList = new ArrayList<>();
        realSchools = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.SCHOOL_NAME);

        Log.d("Test","testing");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()){
                    School school = snapshot1.getValue(School.class);
                    realSchools.add(school);
                    assert school != null;
                    schoolList.add(school.getSchoolName());
                }
                spinnerMethod();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        instanceOfChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        defineViews();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullame = fullName.getText().toString().trim();
                String mail = email.getText().toString().trim();
                String passwords = password.getText().toString().trim();
                bar.setVisibility(View.VISIBLE);
                register(fullame,mail,passwords);
            }
        });




    }

    private void defineViews(){
        fullName = findViewById(R.id.EdtFullName);
        email = findViewById(R.id.EdtEmail);
        passforSchool = findViewById(R.id.passwordFromSchool);
        schoolName = findViewById(R.id.spinnerSchoolNames);
        button = findViewById(R.id.button2);
        password = findViewById(R.id.password);
        bar = findViewById(R.id.progre);

    }

    private void spinnerMethod() {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schoolList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schoolName.setAdapter(dataAdapter);
        schoolName.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        schoolNames = schoolList.get(i);
        Log.d("SchoolName",schoolNames);
        Log.d("OntimeSelected","This is working");

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }





    private void register(final String username, final String email, final String password){
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegistrationActivity.this,"There was an error"+e,Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            for(int i = 0 ; i < realSchools.size() ; i++){
                                if(realSchools.get(i).getSchoolName().equals(schoolNames)){
                                    String passwordfromSch = passforSchool.getText().toString().trim().toLowerCase();
                                    assert firebaseUser != null;
                                    String userId = firebaseUser.getUid();
                                    if(realSchools.get(i).getStudentPassword().equals(passwordfromSch)){
                                        User user = new User(username,email,schoolNames,STUDENT,userId);
                                        instanceOfChatRepo.registerUserToUserTree(userId,user,RegistrationActivity.this,bar);//Adds user to userTree
                                        instanceOfChatRepo.registerUserToTheSchoolTree(schoolNames,STUDENT,userId,user,RegistrationActivity.this,bar);//Adds user to the schooltree

                                    }else if(realSchools.get(i).getTeacherpassword().equals(passwordfromSch)){
                                        User user = new User(username,email,schoolNames,TEACHER,userId);
                                        instanceOfChatRepo.registerUserToUserTree(userId,user,RegistrationActivity.this,bar);//Adds user to userTree
                                        instanceOfChatRepo.registerUserToTheSchoolTree(schoolNames,TEACHER,userId,user,RegistrationActivity.this,bar);//Adds user to the schooltree

                                    } else{
                                        Toast.makeText(RegistrationActivity.this,"Password you supplied does not match",Toast.LENGTH_LONG).show();
                                    }
                                }else {
                                    Toast.makeText(RegistrationActivity.this,"Your school Name could not be found ",Toast.LENGTH_LONG).show();
                                }
                            }

                        }else{
                            Toast.makeText(RegistrationActivity.this,"Sorry you cant register",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }



}
