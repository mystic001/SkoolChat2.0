package com.mystic.skoolchat20;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SkoolViewModel extends AndroidViewModel {
    public SkoolViewModel(@NonNull Application application) {
        super(application);
        SkoolChatRepo repository = SkoolChatRepo.getInstanceOfSkoolchatRepo(application);
    }
}
