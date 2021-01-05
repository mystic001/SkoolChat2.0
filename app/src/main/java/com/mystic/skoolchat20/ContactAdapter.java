package com.mystic.skoolchat20;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewContain> {
    private List<User> users;
    private Context context;
    private MyListener listener;

    public ContactAdapter(List<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewContain onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contactsview,parent,false);
        return new ViewContain(view);
    }


    public void moveToChatScreen(MyListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewContain holder, int position) {

        User user = users.get(position);
        Glide.with(context)
                .asBitmap()
                .load(Uri.parse("https://cdn.pixabay.com/photo/2016/11/22/06/05/girl-1848454__340.jpg"))
                .into(holder.contactImage);

        holder.txtName.setText(user.getName());
        holder.email.setText(user.getEmail());
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewContain extends RecyclerView.ViewHolder {
        CircleImageView contactImage;
        TextView txtName;
        TextView email;

        public ViewContain(@NonNull View itemView) {
            super(itemView);
            contactImage = itemView.findViewById(R.id.profile_image);
            txtName = itemView.findViewById(R.id.txtName);
            email = itemView.findViewById(R.id.emailTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int pos = getAdapterPosition();
                        if(pos != RecyclerView.NO_POSITION){
                            listener.respond(pos);
                        }

                    }
                }
            });
        }



    }


    public interface MyListener{
        void respond(int pos);
    }
}
