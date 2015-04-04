package org.coursera.androidcapstone.potlatch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.fragments.DisplayGiftFragment;
import org.coursera.androidcapstone.potlatch.fragments.DisplayGiftUserFragment;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.models.User;

public class GiftDisplayActivity extends ActionBarActivity {
    private static final String TAG = GiftDisplayActivity.class.getSimpleName();

    Gift gift;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift_display);
        gift = getIntent().getParcelableExtra("gift");

        User user = gift.getUser();
        if (user != null) {

            if (PotlatchApplication.currentUser.equals(user)) {

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, DisplayGiftUserFragment.newInstance(gift))
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, DisplayGiftFragment.newInstance(gift))
                        .commit();
            }
        }

    }
}
