package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.models.Gift;

public class GiftUpdateService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.GiftUpdateService";
    private static final String TAG = GiftUpdateService.class.getSimpleName();

    public GiftUpdateService() {
        super("GiftUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;

        Gift gift = intent.getParcelableExtra("gift");

        if (svc != null) {
            svc.updateGift(gift);
            publishResult();
        }
    }

    private void publishResult() {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
