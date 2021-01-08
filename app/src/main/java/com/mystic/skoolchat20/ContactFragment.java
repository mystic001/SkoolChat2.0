package com.mystic.skoolchat20;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match

   private User userFrom;
   private List<User> users;
   private SkoolChatRepo skoolChatRepo;
   private RecyclerView recyclerView;
   private ContactAdapter contactAdapter;
   private TextView textView;
   private LinearLayoutManager linearLayoutManager;
    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(User user) {
        ContactFragment fragment = new ContactFragment();
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
            //contactAdapter = new ContactAdapter(users,getActivity());
            //recyclerView.setAdapter(contactAdapter);
            users = new ArrayList<>();
            skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(getContext());
            if(userFrom.getRole().equals("teacher")){
                //users = skoolChatRepo.loadStudOrTeaTree(userFrom.getSchoolName(),"student");
                //This loads all the students for the teachers
                users = skoolChatRepo.teacherOrStudent(userFrom.getSchoolName(),"student");
            }else if(userFrom.getRole().equals("student")){
                //users = skoolChatRepo.loadStudOrTeaTree(userFrom.getSchoolName(),"teacher");
                //This loads all the teachers for the students
                users = skoolChatRepo.teacherOrStudent(userFrom.getSchoolName(),"teacher");
            }else if(userFrom.getRole().equals("admin")){
                //This loads all the members of a school
                users = skoolChatRepo.specifiUser(userFrom.getSchoolName());
            }else if(userFrom.getRole().equals("owner")){
                //This loads all the people registered on the platform
                //users = skoolChatRepo.loadAllUsers(recyclerView,getActivity());
                loadAllUsers();
                // Log.d("Users",""+users.size());
            }

            else{
                Toast.makeText(getActivity(),"There is no role attached to u",Toast.LENGTH_LONG).show();
            }

        }else{
            Toast.makeText(getActivity(),"Argumments are null",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        textView = view.findViewById(R.id.nocontacts);

        recyclerView = view.findViewById(R.id.recike);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);


        return view;
    }



    public void loadAllUsers(){
        DatabaseReference refeUsers = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS);
        refeUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    String userId = FirebaseAuth.getInstance().getUid();
                    User user = dataSnapshot.getValue(User.class);
                    assert user != null;
                    if(!user.getUid().equals(userId)){
                        users.add(user);
                    }

                }
                Log.d("UsersForowner",""+users.size());
                contactAdapter = new ContactAdapter(users,getActivity());
                recyclerView.setAdapter(contactAdapter);
                recyclerView.setLayoutManager(linearLayoutManager);

                if(users.size()>0){
                    textView.setVisibility(View.GONE);
                }else{
                    textView.setVisibility(View.VISIBLE);
                }

                contactAdapter.moveToChatScreen(new ContactAdapter.MyListener() {
                    @Override
                    public void respond(int pos) {
                        Intent intent = new Intent(getActivity(),ChatScreenActivity.class);
                        User user = users.get(pos);
                        intent.putExtra(ChatScreenActivity.RECEIVER_USER,user);
                        getActivity().startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_LONG)
                        .show();
            }
        });

    }
}