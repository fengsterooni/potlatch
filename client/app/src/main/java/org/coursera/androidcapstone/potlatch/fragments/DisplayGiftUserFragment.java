package org.coursera.androidcapstone.potlatch.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.MainActivity;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.services.GiftDeleteService;
import org.coursera.androidcapstone.potlatch.services.GiftUpdateService;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class DisplayGiftUserFragment extends Fragment {
    private static final String TAG = DisplayGiftUserFragment.class.getSimpleName();
    
    Gift gift = null;
    Long parentId = 0L;

    @InjectView(R.id.tvUserDU) TextView tvUser;
    @InjectView(R.id.etTitleDU) EditText title;
    @InjectView(R.id.etTextDU) EditText text;
    @InjectView(R.id.ivTouchedDU) ImageView ivTouched;
    @InjectView(R.id.ivObsceneDU) ImageView ivObscene;
    @InjectView(R.id.tvTouchesDU) TextView tvTouchCount;
    @InjectView(R.id.ivGiftImageDU) ImageView ivGiftImage;

    public static DisplayGiftUserFragment newInstance(Gift gift) {
        DisplayGiftUserFragment displayGiftUserFragment = new DisplayGiftUserFragment();
        Bundle args = new Bundle();
        args.putParcelable("gift", gift);
        displayGiftUserFragment.setArguments(args);
        return displayGiftUserFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gift = getArguments().getParcelable("gift");
        parentId = gift.getParentId();
        Log.i(TAG, "In User Fragment");
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_gift_display_user, container, false);

        //getActivity().setTitle("Gift from you");
        ButterKnife.inject(this, rootView);

        tvUser.setText("" + gift.getUser().getName());
        if (gift.isObscene())
            ivObscene.setImageResource(R.drawable.obscene_yes);
        else
            ivObscene.setImageResource(R.drawable.obscene_no);

        ivTouched.setImageResource(R.drawable.touched_yes);
        tvTouchCount.setText(Long.toString(gift.getTouches()));

        title.setText(gift.getTitle());
        text.setText(gift.getText());

        Log.i(TAG, "Item: " + gift.getId() + " " + gift.getDataUrl());
        Long id = gift.getId();
        Bitmap bitmap = PotlatchApplication.getBitmapFromGiftCache(id);
        if (bitmap != null)
            ivGiftImage.setImageBitmap(bitmap);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_gift_display_user, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            String nTitle = title.getText().toString();
            if (!nTitle.equals(gift.getTitle()))
                gift.setTitle(nTitle);
            String nText = text.getText().toString();
            if (!nText.equals(gift.getText()))
                gift.setText(nText);
            //updateGift(gift);
            onUpdateGift(gift);
        }
        
        if (id == R.id.action_delete) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.dialog_message);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Long id = gift.getId();
                    //deleteGift(id);
                    onDeleteGift(id);
                }
            });
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.i(TAG, "Delete Gift Cancelled");
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        if (id == R.id.action_cancel) {
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onDeleteGift(long id) {
        Intent intent = new Intent(getActivity(), GiftDeleteService.class);
        intent.putExtra("id", id);
        getActivity().startService(intent);
    }
    
    public void onUpdateGift(Gift gift) {
        Intent intent = new Intent(getActivity(), GiftUpdateService.class);
        intent.putExtra("gift", gift);
        getActivity().startService(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GiftDeleteService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(deleteReceiver, filter);
        filter = new IntentFilter(GiftUpdateService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(updateReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(deleteReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(updateReceiver);
    }

    private BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        }
    };

    private BroadcastReceiver updateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
        }
    };
}