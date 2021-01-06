package com.mystic.skoolchat20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SkoolActivity extends AppCompatActivity {


    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private SkoolChatRepo skoolChatRepo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skool);

        User user = (User) getIntent().getSerializableExtra(SkoolChatRepo.REAL_USER);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        skoolChatRepo = SkoolChatRepo.getInstanceOfSkoolchatRepo(this);
        viewPager2 = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);
        SectionPagerAdapter pagerAdapter = new SectionPagerAdapter(this,user);
        viewPager2.setAdapter(pagerAdapter);



        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Profile");
                        tab.setIcon(R.drawable.ic_baseline_account_circle_24);
                        break;
                    case 1:
                        tab.setText("Recent Chat");
                        tab.setIcon(R.drawable.ic_baseline_chat_24);
                        break;
                    case 2:
                        tab.setText("Chat");
                        tab.setIcon(R.drawable.ic_baseline_contactless_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.colorAccent));
                        badgeDrawable.setVisible(true);
                        badgeDrawable.setNumber(100);
                        badgeDrawable.setMaxCharacterCount(3);
                        break;
                }
            }
        });

        mediator.attach();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            skoolChatRepo.logOut(this);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

}