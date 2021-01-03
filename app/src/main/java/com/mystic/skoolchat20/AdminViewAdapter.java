package com.mystic.skoolchat20;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdminViewAdapter extends RecyclerView.Adapter<AdminViewAdapter.ViewContain> {

    List<Chat> listOfChats;
    MyViewListener listener;


    public AdminViewAdapter(List<Chat> listOfChats){
        this.listOfChats = listOfChats;
    }
    @NonNull
    @Override
    public ViewContain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adminadapterview,parent,false);
        return new ViewContain(view);
    }

    public void adapterListener(MyViewListener listener){
        this.listener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewContain holder, int position) {

        Chat chat = listOfChats.get(position);
        holder.textView.setText(chat.getUser().getName());

    }

    @Override
    public int getItemCount() {
        return listOfChats.size();
    }

    public class ViewContain extends RecyclerView.ViewHolder {
        ImageView image;
        Button buttonAccept, buttonDecline;
        TextView textView;
        public ViewContain(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView2);
            buttonAccept = itemView.findViewById(R.id.button3);
            buttonDecline = itemView.findViewById(R.id.button4);
            textView = itemView.findViewById(R.id.Name);

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

    public interface MyViewListener{
        void onClickAccept(int pos);
        void onClickdelete(int pos);
    }

}
