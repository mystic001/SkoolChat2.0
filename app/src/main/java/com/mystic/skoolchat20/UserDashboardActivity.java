package com.mystic.skoolchat20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UserDashboardActivity extends AppCompatActivity {
    private TextView notVerified ;
    private LinearLayout linearLayout;
    private Button but;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        notVerified = findViewById(R.id.textView2);
        linearLayout = findViewById(R.id.lin);
        but = findViewById(R.id.but);

        User user =(User) getIntent().getSerializableExtra(SkoolChatRepo.USR);
        if (user != null) {
            if(user.isUserVerified()){
                notVerified.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);

            }else{
                notVerified.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.GONE);
            }
        }

        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserDashboardActivity.this,SkoolActivity.class);
                intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });



    }
}