package org.coursera.androidcapstone.potlatch.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.fragments.UserListFragment;

public class ProfileActivity extends ActionBarActivity {
    private static final String TAG = ProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fProfileContainer, new UserListFragment())
                    .commit();

        }
    }
}
