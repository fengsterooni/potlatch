package org.coursera.androidcapstone.potlatch.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class UpdateService extends IntentService {
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent updateGiftList = new Intent(this, GiftListService.class);
        startService(updateGiftList);
        Log.i("UpdateService", "Gift List Service started");
        Intent updateUserList = new Intent(this, UserListService.class);
        startService(updateUserList);
        Log.i("UpdateService", "User List Service started");
    }
}
