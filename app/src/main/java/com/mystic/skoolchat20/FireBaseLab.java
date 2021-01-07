package com.mystic.skoolchat20;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FireBaseLab {
    private static FireBaseLab sfirebaseLab;
    private MutableLiveData<List<User>> liveUsers;
    private MutableLiveData<List<School>> liveSchools;
    private MutableLiveData<List<Chat>> liveChats;
    private List<User> userList;
    private List<Chat> chats;
    private List<User> teacherOrStudentList;
    private List<User> specificList;
    private List<User> allUsers;
    private List<Chat> chatsBtwOwnerAndAdmin;
    private List<School> schools;
    private FirebaseAuth mAuth;
    private final DatabaseReference AdminFireBase;
    public final DatabaseReference firebaseDatabaseUsers;
    private final DatabaseReference mDatabasepermChat ;
    public final DatabaseReference mDatabasetempChat;
    public final DatabaseReference mDatabaseReference_AdminChat;
    private final DatabaseReference mDatabaseSchools;
    private final DatabaseReference teachersDatabase;
    private Context mContext;


    private FireBaseLab(Context context) {
        liveUsers = new MutableLiveData<>();
        liveSchools = new MutableLiveData<>();
        liveChats = new MutableLiveData<>();
        mContext = context.getApplicationContext();
        chatsBtwOwnerAndAdmin = new ArrayList<>();
        allUsers = new ArrayList<>();
        userList = new ArrayList<>();
        chats = new ArrayList<>();
        teacherOrStudentList = new ArrayList<>();
        specificList = new ArrayList<>();
        schools = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        AdminFireBase = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.ADMIN_TREE);
        firebaseDatabaseUsers = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS);
        mDatabasetempChat = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.TEMP);
        mDatabasepermChat = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.CHAT);
        mDatabaseSchools = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.SCHOOL_NAME);
        teachersDatabase = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.CONFIRMED_TEACHERS);
        mDatabaseReference_AdminChat = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.OWNER_CHAT);
    }

    public static FireBaseLab getInstanceOfFireBaseLab(Context context){

        if(sfirebaseLab == null){
            sfirebaseLab = new FireBaseLab(context);
            return  sfirebaseLab;
        }

        return sfirebaseLab;
    };

   //This is the method for loading schools from database
    public void loadSchools(){
        mDatabaseSchools.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                schools.clear();
                for(DataSnapshot sch : snapshot.getChildren()){
                    School school = sch.getValue(School.class);
                    schools.add(school);
                }
                liveSchools.postValue(schools);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });
    }



    public void addTeachers(final User user, final Context context){
        String userId = teachersDatabase.push().getKey();
        teachersDatabase.child(userId)
                .setValue(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"We could not add to the database",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(context,user.getName()+"was added to teachers",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }




    public void addSchool(final Context context,String schName,String phn,String teachersPass, String studentsPass){
        String schoolId = mDatabaseSchools.push().getKey();
        School school = new School(schoolId);
        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        school.setAdminEmail(user.getEmail());
        school.setSchoolName(schName);
        school.setPhnoneNumber(phn);
        school.setTeacherpassword(teachersPass);
        school.setStudentPassword(studentsPass);
        assert schoolId != null;
        mDatabaseSchools.child(schoolId).setValue(school)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"There was an error",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(context,"Your school was succefully added",Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    //This is the method for loading chats from database
    public void loadChats(){
        mDatabasepermChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chats.clear();
                for(DataSnapshot shot : snapshot.getChildren()){
                    Chat chat = shot.getValue(Chat.class);
                    chats.add(chat);
                }

                liveChats.postValue(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
    }



    public void removeTempChats(String chatId){
        DatabaseReference chatRemoved = mDatabasetempChat.child(chatId);
        chatRemoved.removeValue();
    }


    public void removeOwnerAdminChat(String chatId){
        DatabaseReference chatRemoved = mDatabaseReference_AdminChat.child(chatId);
        chatRemoved.removeValue();
    }



    //This loads the users in the database
    public void loadUsers() {

        firebaseDatabaseUsers.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    Log.d("Flow","Performing loading");
                    userList.add(user);
                }

                liveUsers.postValue(userList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
    }



    public List<Chat> loadChatbtwOwnerAndAdmin(){
        mDatabaseReference_AdminChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatsBtwOwnerAndAdmin.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Chat chat = snapshot1.getValue(Chat.class);
                    chatsBtwOwnerAndAdmin.add(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return chatsBtwOwnerAndAdmin;
    }




    public LiveData<List<User>> getAllUsers() {
        loadUsers();//The load data might be here so it can always load the new data for us
        return liveUsers;
    }


    public void addAdmin(final User user, final Context context){
        String adminId = AdminFireBase.push().getKey();
        user.setUserVerified(true);
        AdminFireBase
                .child(adminId)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                          Toast.makeText(context,"Succesfully added to admin list",Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"An error occurred",Toast.LENGTH_LONG).show();
                    }
                });
    }


    //This loads all users for a particular school
    public List<User> loadUsersForSpecificSchools(final String schname){
        firebaseDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                specificList.clear();
                for(DataSnapshot snapsh : snapshot.getChildren()){
                    User user = snapsh.getValue(User.class);
                    assert user != null;
                    if(user.getSchoolName().equals(schname)){
                        specificList.add(user);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return specificList;
    }


    //This loads either the teachers or students of a particular school based on the value of role supplied to it
    public List<User> teachersOrStudent(final String schname, final String role){
        firebaseDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                teacherOrStudentList.clear();
                for(DataSnapshot snapsh : snapshot.getChildren()){
                    User user = snapsh.getValue(User.class);
                    assert user != null;
                    if(user.getSchoolName().equals(schname) && user.getRole().equals(role)){
                        teacherOrStudentList.add(user);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return teacherOrStudentList;
    }

    //This loads all users for the owner to see
    public List<User> loadAllUsers(final RecyclerView recyclerView, final Context context){
        firebaseDatabaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allUsers.clear();
                String userId = mAuth.getCurrentUser().getUid();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if(!user.getUid().equals(userId)){
                        allUsers.add(user);
                    }
                }

                ContactAdapter adapter = new ContactAdapter(allUsers,context);
                recyclerView.setAdapter(adapter);
                Log.d("USERS",""+allUsers.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(mContext,error.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
        return  allUsers;
    }


}