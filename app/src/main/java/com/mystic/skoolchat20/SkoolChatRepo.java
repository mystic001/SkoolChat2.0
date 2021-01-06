package com.mystic.skoolchat20;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

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

public class SkoolChatRepo {
    private static SkoolChatRepo skoolChatRepo;
    public static final String USERS = "users";
    public static final String REAL_USER = "real_user";
    public static final String ADMIN_TREE = "admin_list";
    public static final String SCHOOL_NAME = "school_name";
    public static final String CHAT = "chat";
    public static final String OWNER_CHAT = "chatwithowner";
    public static final String USR = "USERSO";
    public static final String ADMIN = "admin";
    public static  final String CONFIRMED_TEACHERS = "teachers";
    public static final String TEMP = "temporaryChats";
    public static final String USER_BUNDLE = "user_bundle";

    //private final DatabaseReference mDatabaseRef;
    private final DatabaseReference mDatabaseRefUsers;
    private final DatabaseReference mDatabaseChat;
    //private static FarmRepository farmRepository;
    private final FirebaseAuth mAuth;
    private FireBaseLab fireBaseLabBase;
    private LiveData<List<User>> users;
    private List<User> usersFromSchool;
    private User realUser;
    private List<Chat> chatContainer;

    private SkoolChatRepo(Context context){

        mDatabaseRefUsers = FirebaseDatabase.getInstance().getReference(USERS);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference(CHAT);
        mAuth =FirebaseAuth.getInstance();
        fireBaseLabBase = FireBaseLab.getInstanceOfFireBaseLab(context);
        users = fireBaseLabBase.getAllUsers();
        usersFromSchool = new ArrayList<>();

    }

    public static SkoolChatRepo getInstanceOfSkoolchatRepo(Context context){
        if(skoolChatRepo == null ){
            skoolChatRepo = new SkoolChatRepo(context);
            return skoolChatRepo;
        }
        return  skoolChatRepo;
    }




    public void sendAdminRequest(String senderId, String receiverId, final Context context, final ProgressBar bar, final User admin){
        bar.setVisibility(View.VISIBLE);
        Chat chat = new Chat(senderId,receiverId);
        fireBaseLabBase.mDatabaseReference_AdminChat.push()
                .setValue(chat)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"We could not add u",Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            Intent intent = new Intent(context,WelcomeAdminActivity.class);
                            intent.putExtra(ADMIN,admin);
                            context.startActivity(intent);
                        }
                    }
                });
    }


    public void addOwner(String email, String password, final User user, final Context context){
       mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful()){
                           FirebaseUser firebaseUser = mAuth.getCurrentUser();
                           assert firebaseUser != null;
                           user.setUid(firebaseUser.getUid());
                           String treeId = mDatabaseRefUsers.push().getKey();
                           assert treeId != null;
                           mDatabaseRefUsers
                                   .child(treeId)
                                   .setValue(user)
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           if(task.isSuccessful()){
                                               Toast.makeText(context,"Succefully added",Toast.LENGTH_LONG).show();
                                           }
                                       }
                                   })
                                   .addOnFailureListener(new OnFailureListener() {
                                       @Override
                                       public void onFailure(@NonNull Exception e) {

                                       }
                                   });
                       }
                   }
               })
               .addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {

                   }
               });

    }


    /*public void sendRequest(String senderId, String receiverId, final Context context, final ProgressBar bar, final User user){
        bar.setVisibility(View.VISIBLE);
        String chatId = mDatabaseChat.push().getKey();
        Chat chat = new Chat(senderId,receiverId,chatId,user);
        assert chatId != null;
        mDatabaseChat
                .child(chatId).setValue(chat)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    bar.setVisibility(View.GONE);
                    Intent intent = new Intent(context, UserDashboardActivity.class);
                    intent.putExtra(USR,user);
                    intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FarmRepo",e.getMessage());
                Toast.makeText(context,"Farm could not be aadded",Toast.LENGTH_LONG).show();
            }
        });

    }*/


    public void addTeachersInRepo(User user,Context context){
        user.setUserVerified(true);
        fireBaseLabBase.addTeachers(user,context);
    }

    public void removeChatRepo(String chatId){
        fireBaseLabBase.removeTempChats(chatId);
    }


    public void removeAdminOwnerChatRepo(String chatId){
        fireBaseLabBase.removeOwnerAdminChat(chatId);
    }


    public void addSchoolRepo(Context context, String schName, String phone,String teacher,String student){
        fireBaseLabBase.addSchool(context,schName,phone,teacher,student);
    }

    public List<Chat> loadChatsForOwner(){
        return fireBaseLabBase.loadChatbtwOwnerAndAdmin();
    }


    public void addAdminFromRepo(User user,Context context){
        fireBaseLabBase.addAdmin(user,context);
    }



    public void registerUserToUserTree(String userId, User user, final Context context, final ProgressBar bar){
        bar.setVisibility(View.VISIBLE);
        DatabaseReference mDatabaserefUser = FirebaseDatabase.getInstance().getReference(USERS).child(userId);
        mDatabaserefUser
                .setValue(user)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        bar.setVisibility(View.GONE);
                        Toast.makeText(context,"Error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            Toast.makeText(context,"Succesfully added to base",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


    public void registerUserToTheSchoolTree(String schoolName, String role, String userId, final User user, final Context context, final ProgressBar bar){
        bar.setVisibility(View.VISIBLE);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(schoolName);
        databaseReference
                .child(role)
                .child(userId)
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bar.setVisibility(View.GONE);
                            Intent intent = new Intent(context,SkoolActivity.class);
                            intent.putExtra(ADMIN,user);
                            intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"there was an error"+e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });

    }




        public void loginToBase(String email, String password, final Context context, final ProgressBar bar){
            if(email != null && password != null ){
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context,"There was an error"+e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //Go to the next Activity

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    assert user != null;
                                    String userId = user.getUid();

                                    DatabaseReference mDatabaserefUser = FirebaseDatabase.getInstance().getReference(USERS).child(userId);
                                    //This line of code helps us to get the specific user from firebase base
                                    mDatabaserefUser.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                          realUser = snapshot.getValue(User.class);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                            Toast.makeText(context,"There was an error"+error.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    bar.setVisibility(View.GONE);
                                    Intent intent = new Intent(context,SkoolActivity.class);
                                    intent.putExtra(REAL_USER,realUser);
                                    context.startActivity(intent);
                                }
                            }
                        });

            }else{
                Toast.makeText(context,"Email or password is empty",Toast.LENGTH_LONG).show();
            }
        }



        public List<User> loadStudOrTeaTree(String schname, String role){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(schname).child(role);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot: snapshot.getChildren())        {
                        User user = dataSnapshot.getValue(User.class);
                        usersFromSchool.add(user);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return  usersFromSchool;

        }



        public void sendMessge(Chat chat){
        DatabaseReference referenceMessage = FirebaseDatabase.getInstance().getReference(CHAT);
        referenceMessage.push().setValue(chat);
        }


        public List<Chat> loadMesages(final String receiverId, final String userId){
            chatContainer = new ArrayList<>();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(CHAT);

            databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatContainer.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getSenderId().equals(userId) && chat.getReceiverId().equals(receiverId) ||
                    chat.getSenderId().equals(receiverId)&& chat.getReceiverId().equals(userId)){
                        chatContainer.add(chat);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

            return chatContainer;
        }


    public void logOut(Context context){
        mAuth.signOut();
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }






}
