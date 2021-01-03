package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private List<Chat> chatList;
    private RecyclerView recyclerView;
    private AdminViewAdapter adminViewAdapter;
    private DatabaseReference mDatabaseChat;
    private SkoolChatRepo skoolChatRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);
        //This loads the chat from the database
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        mDatabaseChat = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.CHAT);

        chatList = new ArrayList<>();
        loadChats();
        adminViewAdapter = new AdminViewAdapter(chatList);
        recyclerView = findViewById(R.id.recycle);
        recyclerView.setAdapter(adminViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adminViewAdapter.adapterListener(new AdminViewAdapter.MyViewListener() {
            @Override
            public void onClickAccept(int pos) {
                Chat chat = chatList.get(pos);
                skoolChatRepo.addTeachersInRepo(chat.getUser(),AdminDashboardActivity.this);
                Toast.makeText(AdminDashboardActivity.this,"teacher was added",Toast.LENGTH_LONG).show();
                skoolChatRepo.removeChatRepo(chat.getChatId());
                chatList.remove(pos);
                adminViewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onClickdelete(int pos) {
                Chat chat = chatList.get(pos);
                skoolChatRepo.removeChatRepo(chat.getChatId());
                Toast.makeText(AdminDashboardActivity.this,"teacher was not permitted",Toast.LENGTH_LONG).show();
                chatList.remove(pos);
                adminViewAdapter.notifyDataSetChanged();
            }
        });

    }

//For temporary user
    public void loadChats(){
        mDatabaseChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for(DataSnapshot shot : snapshot.getChildren()){
                    Chat chat = shot.getValue(Chat.class);
                    chatList.add(chat);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminDashboardActivity.this,error.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });
    }
}