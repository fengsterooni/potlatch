package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.models.User;

import java.util.ArrayList;
import java.util.Collection;

public class UserListService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.UserListService";
    private static final String TAG = UserListService.class.getSimpleName();

    public UserListService() {
        super("UserListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;
        final ArrayList<User> userList = new ArrayList<User>();

        if (svc != null) {
            Collection<User> result = svc.getUserList();
            userList.clear();
            for (User user : result) {
                userList.add(user);
                Log.i(TAG, "User: " + user.getName());
            }

            publishResults(userList);
        }
    }

    private void publishResults(ArrayList<User> users) {
            Intent in = new Intent(ACTION);
            in.putExtra("resultCode", Activity.RESULT_OK);
            in.putExtra("resultValue", users);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
