package org.coursera.androidcapstone.potlatch.activities;


import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.fragments.NewGiftFragment;

public class NewGiftActivity extends ActionBarActivity {
    private static final String TAG = NewGiftActivity.class.getSimpleName();

    NewGiftFragment fragment;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_gift);
        
        final Long pId = getIntent().getLongExtra("parentId", 0);

        if (savedInstanceState == null) {
            fragment = NewGiftFragment.newInstance(pId);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fNewContainer, fragment)
                    .commit();

        }
    }
}
