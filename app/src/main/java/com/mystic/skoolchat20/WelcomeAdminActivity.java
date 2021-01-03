package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WelcomeAdminActivity extends AppCompatActivity {

    private User user;
    private TextView textView;
    private LinearLayout layout;
    private Button butt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_admin);
        textView = findViewById(R.id.texxT);
        layout = findViewById(R.id.layouting);
        butt = findViewById(R.id.btoSkool);
        user = (User) getIntent().getSerializableExtra(SkoolChatRepo.ADMIN);
        if(user != null){
            if(user.isUserVerified()){
                textView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }else{
                textView.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
            }
        }

        butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WelcomeAdminActivity.this,SkoolActivity.class);
                startActivity(intent);
            }
        });
    }
}