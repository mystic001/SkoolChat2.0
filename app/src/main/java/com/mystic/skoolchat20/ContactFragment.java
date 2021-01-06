package com.mystic.skoolchat20;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
            skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(getContext());
            if(userFrom.getRole().equals("teacher")){
                users = skoolChatRepo.loadStudOrTeaTree(userFrom.getSchoolName(),"student");
            }else if(userFrom.getRole().equals("student")){
                users = skoolChatRepo.loadStudOrTeaTree(userFrom.getSchoolName(),"teacher");
            }else{
                Toast.makeText(getActivity(),"There is nothing for you yet even though u are the owner or and Admin",Toast.LENGTH_LONG).show();
            }
            contactAdapter = new ContactAdapter(users,getActivity());
        }else{
            Toast.makeText(getActivity(),"Argumments are null",Toast.LENGTH_LONG).show();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        TextView textView = view.findViewById(R.id.nocontacts);

        if(users.size()>0){
            textView.setVisibility(View.GONE);
        }else{
            textView.setVisibility(View.VISIBLE);
        }
        recyclerView = view.findViewById(R.id.recike);
        recyclerView.setAdapter(contactAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
}