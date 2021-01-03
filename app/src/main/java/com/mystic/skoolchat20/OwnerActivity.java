package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class OwnerActivity extends AppCompatActivity {

    private List<Chat> chatList;
    private RecyclerView recyclerView;
    private OwnerAdapter ownerAdapter;
    private SkoolChatRepo skoolChatRepo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        chatList = new ArrayList<>();
        chatList = skoolChatRepo.loadChatsForOwner();

        ownerAdapter = new OwnerAdapter(chatList,this);

        ownerAdapter.setAdapterListener(new OwnerAdapter.MyListener() {
            @Override
            public void onClickAccept(int pos) {
                Chat chat = chatList.get(pos);
                skoolChatRepo.addAdminFromRepo(chat.getUser(),OwnerActivity.this);
                skoolChatRepo.removeAdminOwnerChatRepo(chat.getChatId());
                ///remove from the arraylist
                chatList.remove(pos);
                ownerAdapter.notifyDataSetChanged();
                //skoolChatRepo.addTeachersInRepo(chat.getUser(),AdminDashboardActivity.this);
                //Toast.makeText(AdminDashboardActivity.this,"teacher was added",Toast.LENGTH_LONG).show();
                //skoolChatRepo.removeChatRepo(chat.getChatId());
            }

            @Override
            public void onClickdelete(int pos) {
                Chat chat = chatList.get(pos);
                skoolChatRepo.removeAdminOwnerChatRepo(chat.getChatId());
                chatList.remove(pos);
                ownerAdapter.notifyDataSetChanged();

            }
        });
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(ownerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}