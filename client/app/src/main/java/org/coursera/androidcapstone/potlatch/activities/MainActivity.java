package org.coursera.androidcapstone.potlatch.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.fragments.GiftListFragment;
import org.coursera.androidcapstone.potlatch.fragments.GiftPreferenceDialog;
import org.coursera.androidcapstone.potlatch.receivers.AlarmReceiver;

public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private SearchView searchView;
    boolean auto;
    boolean noObscene;
    int rate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        auto = pref.getBoolean(PotlatchApplication.PREF_AUTO_UPDATE, false);
        noObscene = pref.getBoolean(PotlatchApplication.PREF_NO_OBSCENE, true);
        rate = pref.getInt(PotlatchApplication.PREF_UPDATE_RATE, 0);

        if (auto) {
            int[] updateRate = getResources().getIntArray(R.array.updateRateValue);
            int rateIndex = pref.getInt(PotlatchApplication.PREF_UPDATE_RATE, 0);
            int rate = updateRate[rateIndex];
            Log.i(TAG, "HERE IS THE UPDATE INTERVAL =======> " + rate);

            scheduleAlarm(rate);
        } else {
            cancelAlarm();
        }
    }

    private void scheduleAlarm(int rate) {
        cancelAlarm();
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        int intervalMillis = rate * 60 * 1000;
        AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis, intervalMillis, pendingIntent);
    }

    public void cancelAlarm() {
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, AlarmReceiver.REQUEST_CODE,
                intent,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarm.cancel(pendingIntent);
        Log.i("UpdateService", "Service cancelled");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (searchView != null)
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("INFO", "Search Started");
                    GiftListFragment fragment = (GiftListFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.giftFragment);
                    //fragment.performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, NewGiftActivity.class);
            intent.putExtra("parentId", 0L);
            startActivity(intent);
        }

        if (id == R.id.action_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_settings) {
            //Intent intent = new Intent(this, SettingsActivity.class);
            //startActivity(intent);
            FragmentManager fm = getSupportFragmentManager();
            GiftPreferenceDialog dialog = GiftPreferenceDialog.newInstance(noObscene, auto, rate);
            dialog.show(fm, "Personal Preferences");
        }

        return super.onOptionsItemSelected(item);
    }
}
