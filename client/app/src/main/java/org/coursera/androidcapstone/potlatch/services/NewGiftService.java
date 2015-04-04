package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.models.Gift;

import java.io.File;

import retrofit.mime.TypedFile;

public class NewGiftService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.NewGiftService";
    private static final String TAG = NewGiftService.class.getSimpleName();

    public NewGiftService() {
        super("NewGiftService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;

        Gift gift = intent.getParcelableExtra("new");
        File photoFile = (File) intent.getExtras().get("file");

        if (svc != null) {
            Gift g = svc.addGift(gift);
            svc.setGiftData(g.getId(), new TypedFile("image/jpeg", photoFile));
            publishResults();
        }
    }

    private void publishResults() {
        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);
    }
}
