package org.coursera.androidcapstone.potlatch.activities;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.models.User;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PotlatchApplication extends Application {
    public static final String PREF_NAME = "Preference";
    public static final String PREF_NO_OBSCENE = "PREF_NO_OBSCENE";
    public static final String PREF_AUTO_UPDATE = "PREF_AUTO_UPDATE";
    public static final String PREF_UPDATE_RATE = "PREF_UPDATE_RATE";
    public static final String PREF_TOP_GIVER = "PREF_TOP_GIVER";

    public static Context context;
    private static LruCache<Long, Bitmap> giftCache;
    public static User currentUser = null;
    public static SharedPreferences pref;
    public static Context getContext() {
        return context;
    }
    
    public static GiftSvcApi svc;

    public static ArrayList<Long> getTouched() {
        return touched;
    }
    public static void setTouched(ArrayList<Long> touched) {
        PotlatchApplication.touched = touched;
    }

    public static ArrayList<Long> touched;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        setupMemoryCache();
        setupPreferences();
        touched = new ArrayList<Long>();
    }

    private void setupPreferences() {
        pref = getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_NO_OBSCENE, true).putBoolean(PREF_AUTO_UPDATE, false)
                .putInt(PREF_UPDATE_RATE, 1).putInt(PREF_TOP_GIVER, 5);
        editor.apply();
    }

    private void setupMemoryCache() {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        giftCache = new LruCache<Long, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(Long id, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };


        Log.i("INFO", "=====================================");
        Log.i("INFO", "INITIAL Cache size: " + cacheSize);
        Log.i("INFO", "=====================================");
    }

    public static void addBitmapToGiftCache(Long id, Bitmap bitmap) {
       // if (getBitmapFromGiftCache(id) == null) {

            Log.i("INFO", "=====================================");
            Log.i("INFO", "BEFORE Cache size: " + giftCache.size() + " id: " + id);
            Log.i("INFO", "=====================================");
            giftCache.put(id, bitmap);

            Log.i("INFO", "=====================================");
            Log.i("INFO", "AFTER Cache size: " + giftCache.size() + " id: " + id);
            Log.i("INFO", "=====================================");

       // }
    }

    public static Bitmap getBitmapFromGiftCache(Long id) {
        Log.i("INFO", "=====================================");
        Log.i("INFO", "Cache hit: " + giftCache.hitCount() + "   Cache miss: " + giftCache.missCount());
        Log.i("INFO", "=====================================");
        return giftCache.get(id);
    }

    public static void removeBitmapFromGiftCache(Long id) {
        if (getBitmapFromGiftCache(id) != null) {
            giftCache.remove(id);

            Log.i("INFO", "Cache size: " + giftCache.size());
        }
    }

    public static void removeGiftCache(List<Long> list) {
        if (list != null) {
            Iterator<Long> it = list.iterator();
            while (it.hasNext()) {
                removeBitmapFromGiftCache(it.next());
            }
        }
    }
}
