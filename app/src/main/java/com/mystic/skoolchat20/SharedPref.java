package com.mystic.skoolchat20;

import android.content.Context;
import android.preference.PreferenceManager;

public class SharedPref {

    private static final String FIRST_TIME = "FirstTime";
    public  static void setUserState(Context context, boolean state){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(FIRST_TIME, state)
                .apply();
    }

    public static boolean getUserState(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(FIRST_TIME, true);
    }
}
