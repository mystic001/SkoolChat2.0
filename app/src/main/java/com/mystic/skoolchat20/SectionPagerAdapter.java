package com.mystic.skoolchat20;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SectionPagerAdapter extends FragmentStateAdapter {

    private Context context;

    public SectionPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new ProfileFragment();
            case 1:
                return new RecentFragment();

            case 2:
                return new ChatFragment();
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
                return new ChatFragment();
            case 2:
                return new RecentFragment();
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
