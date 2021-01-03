package com.mystic.skoolchat20;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class OwnerAdapter extends RecyclerView.Adapter<OwnerAdapter.InsideOwner> {

    List<Chat> chatList;
    Context context;
    MyListener listener;
    public OwnerAdapter(List<Chat> chatList,Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public InsideOwner onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specownerview,parent,false);
        return new InsideOwner(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InsideOwner holder, int position) {
        Chat chat = chatList.get(position);
        holder.textView.setText(chat.getUser().getName());
        Glide.with(context)
                .asBitmap()
                .circleCrop()
                .load(Uri.parse("https://cdn.pixabay.com/photo/2020/12/20/10/01/advent-5846564__340.jpg"))
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public void setAdapterListener(MyListener listener){
        this.listener = listener;
    }

    public class InsideOwner extends RecyclerView.ViewHolder {
        ImageView imageView;
        Button buttonAccept, buttonDecline;
        TextView textView;
        public InsideOwner(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView3);
            buttonAccept = itemView.findViewById(R.id.button9);
            buttonDecline = itemView.findViewById(R.id.button10);
            textView = itemView.findViewById(R.id.textView3);

            buttonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onClickAccept(position);
                        }
                    }

                }
            });


            buttonDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onClickdelete(position);
                        }
                    }
                }
            });
        }
    }


    public interface MyListener{
        void onClickAccept(int pos);
        void onClickdelete(int pos);
    }
}



