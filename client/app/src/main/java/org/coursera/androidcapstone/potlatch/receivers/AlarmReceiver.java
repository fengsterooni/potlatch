package org.coursera.androidcapstone.potlatch.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.coursera.androidcapstone.potlatch.services.UpdateService;

public class AlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, UpdateService.class);
        context.startService(i);
    }
}
