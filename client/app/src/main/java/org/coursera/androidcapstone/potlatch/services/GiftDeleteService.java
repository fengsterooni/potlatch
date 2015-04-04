package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;

import java.util.List;

public class GiftDeleteService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.GiftDeleteService";
    private static final String TAG = GiftDeleteService.class.getSimpleName();

    public GiftDeleteService() {
        super("GiftDeleteService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;

        long id = intent.getLongExtra("id", 0l);
        if (svc != null) {
            List<Long> result = svc.deleteGift(id);
            PotlatchApplication.removeGiftCache(result);
            publishResult();
        }
    }

    private void publishResult() {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
