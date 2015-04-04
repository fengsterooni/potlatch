package org.coursera.androidcapstone.potlatch.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.MainActivity;
import org.coursera.androidcapstone.potlatch.activities.NewGiftActivity;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.services.GiftObsceneService;
import org.coursera.androidcapstone.potlatch.services.GiftTouchService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class DisplayGiftFragment extends Fragment {
    private static final String TAG = DisplayGiftFragment.class.getSimpleName();
    
    Gift gift = null;
    Long parentId = 0L;
    Long id = 0L;
    
    @InjectView(R.id.tvUser) TextView tvUser;
    @InjectView(R.id.ivTouched) ImageView ivTouched;
    @OnClick(R.id.ivTouched) void touch() {
        if (!gift.isObscene()) {
            long id = gift.getId();
            if (list.contains(id)) {
                Toast.makeText(
                        getActivity(),
                        "You were already touched by this Gift",
                        Toast.LENGTH_SHORT).show();
            } else {
                onTouchGift();
                list.add(id);
            }
        }
    }
            
    @InjectView(R.id.ivObscene) ImageView ivObscene;
    @OnClick(R.id.ivObscene) void obscene() {
        if (!gift.isObscene()) {
            gift.setTouched(false);
            ivTouched.setImageResource(R.drawable.touched_no);
            onObsceneGift();
        }    
    }
    
    @InjectView(R.id.ivGiftImage) ImageView ivGiftImage;
    @InjectView(R.id.tvText) TextView text;
    @InjectView(R.id.tvTitle) TextView title;
    @InjectView(R.id.tvTouches) TextView touches;
    
    ArrayList<Long> list = null;

    public static DisplayGiftFragment newInstance(Gift gift) {
        DisplayGiftFragment displayGiftFragment = new DisplayGiftFragment();
        Bundle args = new Bundle();
        args.putParcelable("gift", gift);
        displayGiftFragment.setArguments(args);
        return displayGiftFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        
        gift = getArguments().getParcelable("gift");
        id = gift.getId();
        parentId = gift.getParentId();
        if (parentId == 0)
            parentId = id;

        list = PotlatchApplication.getTouched();
        Log.i(TAG, "In General Fragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gift_display, container, false);
        ButterKnife.inject(this, view);
        
        tvUser.setText("" + gift.getUser().getName());

        if (gift.isObscene()) {
            ivObscene.setImageResource(R.drawable.obscene_yes);
            ivTouched.setImageResource(R.drawable.touched_no);
        }
        else {
            ivObscene.setImageResource(R.drawable.obscene_no);
            if (list.contains(gift.getId()))
                ivTouched.setImageResource(R.drawable.touched_yes);
            else
                ivTouched.setImageResource(R.drawable.touched_no);
        }

        title.setText(gift.getTitle());
        text.setText(gift.getText());
        touches.setText(String.valueOf(gift.getTouches()));

        Log.i(TAG, "Item: " + gift.getId() + " " + gift.getDataUrl());
        
        Bitmap bitmap = PotlatchApplication.getBitmapFromGiftCache(id);
        if (bitmap != null)
            ivGiftImage.setImageBitmap(bitmap);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gift_display, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_add) {
            Intent intent = new Intent(getActivity(), NewGiftActivity.class);
            intent.putExtra("parentId", parentId);
            startActivity(intent);
        }
        
        if (itemId == R.id.action_cancel) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    public void onTouchGift() {
        Intent intent = new Intent(getActivity(), GiftTouchService.class);
        intent.putExtra("id", id);
        getActivity().startService(intent);      
    }

    public void onObsceneGift() {
        Intent intent = new Intent(getActivity(), GiftObsceneService.class);
        intent.putExtra("id", id);
        getActivity().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GiftTouchService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(touchReceiver, filter);
        filter = new IntentFilter(GiftObsceneService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(obsceneReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(touchReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(obsceneReceiver);
    }

    private BroadcastReceiver touchReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                long count = intent.getLongExtra("count", 0);
                if (count > 0) {
                    touches.setText("" + count);
                    ivTouched.setImageResource(R.drawable.touched_yes);
                }
            }
        }
    };

    private BroadcastReceiver obsceneReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                boolean obscene = intent.getBooleanExtra("obscene", false);
                if (obscene == true) {
                    if (gift.isTouched())
                        ivTouched.setImageResource(R.drawable.touched_no);
                    ivObscene.setImageResource(R.drawable.obscene_yes);
                }
            }
        }
    };

}
