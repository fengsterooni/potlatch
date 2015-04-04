package org.coursera.androidcapstone.potlatch.services;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.models.Gift;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import retrofit.client.Response;

public class GiftListService extends IntentService {
    public static final String ACTION = "org.coursera.androidcapstone.potlatch.services.GiftListService";
    private static final String TAG = GiftListService.class.getSimpleName();

    public GiftListService() {
        super("GiftListService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final GiftSvcApi svc = PotlatchApplication.svc;
        final ArrayList<Gift> giftList = new ArrayList<Gift>();


        if (svc != null) {
            Collection<Gift> result = svc.getGiftList();

            Log.i(TAG, "Got following gifts...............");
            giftList.clear();
            for (Gift gift : result) {
                giftList.add(gift);
                Long id = gift.getId();
                Log.i(TAG, "Image id: " + id);
                if (PotlatchApplication.getBitmapFromGiftCache(id) == null) {
                    Response response = svc.getData(id);
                    Log.i(TAG, "Downloading the image #" + id);
                    try {
                        InputStream is = response.getBody().in();
                        byte[] imageData = com.google.common.io.ByteStreams.toByteArray(is);
                        Bitmap image = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        PotlatchApplication.addBitmapToGiftCache(id, image);
                        Log.i(TAG, "Image #" + id + " download done");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "Image " + id + " exists. Skip downloading");
                }

                Log.i(TAG, "Gift: " + gift.getDataUrl());
            }
            
            publishResults(giftList);
        }
    }

    private void publishResults(ArrayList<Gift> gifts) {

        Intent in = new Intent(ACTION);
        in.putExtra("resultCode", Activity.RESULT_OK);
        in.putExtra("resultValue", gifts);
        Log.i(TAG, "Publishing the results");

        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(in);

    }
}
