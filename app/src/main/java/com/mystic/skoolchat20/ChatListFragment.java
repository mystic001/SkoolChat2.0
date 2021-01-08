package com.mystic.skoolchat20;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatListFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private User userFrom;
    private List<String> userList;
    private List<User> mUser;
    private ContactAdapter contactAdapter;
    private DatabaseReference referenceChat;
    private  DatabaseReference referenceUsers;
    private  RecyclerView recyclerView;
    private TextView textView;

    public ChatListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    // TODO: Rename and change types and number of parameters
    public static ChatListFragment newInstance(User user) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putSerializable(SkoolChatRepo.USER_BUNDLE,user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userFrom = (User) getArguments().getSerializable(SkoolChatRepo.USER_BUNDLE);
        }

        userList = new ArrayList<>();
        referenceChat= FirebaseDatabase.getInstance().getReference(SkoolChatRepo.CHAT);
        referenceUsers = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS);

        if(contactAdapter != null){
            contactAdapter.moveToChatScreen(new ContactAdapter.MyListener() {
                @Override
                public void respond(int pos) {
                    Intent intent = new Intent(getActivity(),ChatScreenActivity.class);
                    User user = mUser.get(pos);
                    intent.putExtra(ChatScreenActivity.RECEIVER_USER,user);
                    getActivity().startActivity(intent);
                }
            });
        }

        getListOfChats();

    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerrrr);
        textView = view.findViewById(R.id.nochats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //getList of chats gets all the data needed to display users you have already chatted with;
        return view;
    }

    private void getListOfChats() {
        referenceChat.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot snap : snapshot.getChildren()){
                    Chat chat = snap.getValue(Chat.class);
                    assert chat != null;
                    if(chat.getSenderId().equals(userFrom.getUid())){
                        userList.add(chat.getReceiverId());
                    }

                    if(chat.getReceiverId().equals(userFrom.getUid())){
                        userList.add(chat.getSenderId());
                    }
                }
                readChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void setUprecycler() {
        contactAdapter = new ContactAdapter(mUser,getContext());
        recyclerView.setAdapter(contactAdapter);

        if(mUser.size()>0){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
        }
    }

    private void readChats() {
        mUser = new ArrayList<>();
        referenceUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUser.clear();
                for(DataSnapshot snapp : snapshot.getChildren()){
                    User user = snapp.getValue(User.class);
                    for(String id : userList){
                        if(userFrom.getUid().equals(id)){
                            if(mUser.size() != 0){
                                for(User user1 : mUser){
                                    assert user != null;
                                    if(!user.getUid().equals(user1.getUid())){
                                        mUser.add(user);
                                    }
                                }
                            }else{
                                mUser.add(user);
                            }

                        }
                    }

                }

                setUprecycler();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}