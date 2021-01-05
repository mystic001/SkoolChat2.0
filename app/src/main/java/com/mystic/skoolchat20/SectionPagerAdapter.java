package com.mystic.skoolchat20;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionPagerAdapter extends FragmentStateAdapter {

    private Context context;
    private User user;

    public SectionPagerAdapter(@NonNull FragmentActivity fragmentActivity,User user) {
        super(fragmentActivity);
        this.user = user;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return ProfileFragment.newInstance(user);
            case 1:
                return ContactFragment.newInstance(user);

            case 2:
                return ChatListFragment.newInstance(user);
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
    /*public SectionPagerAdapter(@NonNull FragmentManager fm, int behavior,Context context) {
        super(fm, behavior);
        this.context = context;

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new ProfileFragment();
            case 1:
                return new ChatListFragment();
            case 2:
                return new ContactFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return context.getApplicationContext().getResources().getText(R.string.home_tab);
        }
        return null;
    }*/
}
