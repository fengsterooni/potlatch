package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;

public class GiftObsceneService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.GiftObsceneService";
    private static final String TAG = GiftObsceneService.class.getSimpleName();

    public GiftObsceneService() {
        super("GiftObsceneService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;

        long id = intent.getLongExtra("id", 0l);

        if (svc != null) {
            boolean obscene = svc.obsceneGift(id);
            publishResult(obscene);
        }
    }

    public void publishResult(boolean obscene) {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("obscene", obscene);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
