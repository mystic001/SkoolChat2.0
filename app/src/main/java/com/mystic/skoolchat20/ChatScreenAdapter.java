package com.mystic.skoolchat20;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private static  final int RIGHT = 1;
    private static  final int LEFT = 0;

    private FirebaseUser user;
    private List<Chat> listOfChats;
    Context context;


    public ChatScreenAdapter(List<Chat> listOfChats,Context context) {
        this.listOfChats = listOfChats;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == RIGHT){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.senderview,parent,false);
            return new SenderView(view);
        }
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiversview,parent,false);
        return new ReceiverView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final Chat chat = listOfChats.get(position);

        if(chat.getSenderId().equals(user.getUid())){
            SenderView senderView = (SenderView) holder;
            senderView.textView.setText(chat.getMessage());
        }else{
            final ReceiverView receiverView = (ReceiverView) holder;
            receiverView.txt.setText(chat.getMessage());
            String receiverpic = chat.getSenderId();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference(SkoolChatRepo.USERS);
            reference.child(receiverpic).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    String pic = user.getImage_url();
                    Glide.with(context)
                            .asBitmap()
                            .placeholder(R.drawable.doctor)
                            .load(Uri.parse(pic))
                            .into(receiverView.imageView);

                    Log.d("Imaage",pic);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }

    }

    @Override
    public int getItemCount() {
        return listOfChats.size();
    }


    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        if(listOfChats.get(position).getSenderId().equals(user.getUid())){
            return RIGHT;
        }
        return LEFT;
    }

    public static class SenderView extends RecyclerView.ViewHolder{
        TextView textView;
        public SenderView(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.senderText);
        }
    }

    public static class ReceiverView extends RecyclerView.ViewHolder{
        CircleImageView imageView;
        TextView txt;
        public ReceiverView(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.receiverText);
            imageView = itemView.findViewById(R.id.circleimage);
        }
    }
}
