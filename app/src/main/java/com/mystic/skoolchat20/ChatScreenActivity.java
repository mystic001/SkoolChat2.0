package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreenActivity extends AppCompatActivity {

    private List<Chat> chatContainer;
    private ImageButton send;
    private EditText editText;
    private SkoolChatRepo skoolChatRepo;
    private CircleImageView imageView;
    private TextView username;
    private RecyclerView recyclerView;
    public static final String RECEIVER_USER = "receiver";

    private  ChatScreenAdapter chatScreenAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_screen);
        Toolbar toolbar = findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        //chatContainer = skoolChatRepo.loadMesages();
        chatContainer = new ArrayList<>();

        //this is expected to come from the activity that comes before the chatscreen most preferrably the contact screen
        User userReceiver = (User) getIntent().getSerializableExtra(RECEIVER_USER);
        assert userReceiver != null;
        final String receiverId = userReceiver.getUid();


        username = findViewById(R.id.usernamee);
        imageView = findViewById(R.id.profileImage);
        Glide.with(this).asBitmap().load(userReceiver.getImage_url()).into(imageView);
        username.setText(userReceiver.getName());

        /*DatabaseReference ref = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS).child(receiverId);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                user.getName()
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/




        FirebaseUser realUser = FirebaseAuth.getInstance().getCurrentUser();
        assert realUser != null;
        String uid = realUser.getUid();
        loadMesages(uid,receiverId);


        //chatScreenAdapter = new ChatScreenAdapter(chatContainer);
        recyclerView = findViewById(R.id.cycler);


        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(chatScreenAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        send = findViewById(R.id.imageButton);


        editText = findViewById(R.id.edtMess);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String message = editText.getText().toString().trim();

                String senderId = user.getUid();
                if(!message.equals("")){
                    Chat chat = new Chat(senderId,receiverId);
                    chat.setMessage(message);
                    skoolChatRepo.sendMessge(chat);
                }else{
                    Toast.makeText(ChatScreenActivity.this,"Message cant be sent",Toast.LENGTH_LONG).show();
                }
                editText.setText("");

            }
        });
    }


    public void loadMesages(final String receiverId, final String userId){

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.CHAT);
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

                    chatScreenAdapter = new ChatScreenAdapter(chatContainer);
                    chatScreenAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}