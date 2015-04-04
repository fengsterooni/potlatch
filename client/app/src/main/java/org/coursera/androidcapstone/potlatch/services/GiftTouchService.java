package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;

public class GiftTouchService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.GiftTouchService";
    private static final String TAG = GiftTouchService.class.getSimpleName();

    public GiftTouchService() {
        super("GiftTouchService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;

        long id = intent.getLongExtra("id", 0l);

        if (svc != null) {
            long touchCount = svc.touchGift(id);
            publishResult(touchCount);
        }
    }

    public void publishResult(long count) {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("count", count);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
