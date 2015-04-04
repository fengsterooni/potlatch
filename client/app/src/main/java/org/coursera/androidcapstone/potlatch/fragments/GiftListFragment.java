package org.coursera.androidcapstone.potlatch.fragments;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.GiftDisplayActivity;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.adapters.GiftAdapter;
import org.coursera.androidcapstone.potlatch.interfaces.GiftSvcApi;
import org.coursera.androidcapstone.potlatch.interfaces.TaskCallback;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.services.CallableTask;
import org.coursera.androidcapstone.potlatch.services.GiftListService;
import org.coursera.androidcapstone.potlatch.services.GiftSvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GiftListFragment extends Fragment {
    private static final String TAG = GiftListFragment.class.getSimpleName();

    private ArrayList<Gift> giftList;
    private ArrayList<Gift> displayList;
    private ArrayAdapter<Gift> adapterGifts;
    @InjectView(R.id.gvGifts)
    StaggeredGridView sgvGifts;
    private boolean noObscene;

    public GiftListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayList = new ArrayList<Gift>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        noObscene = pref.getBoolean(PotlatchApplication.PREF_NO_OBSCENE, true);
        Log.i(TAG, "Got No Obscene value " + noObscene);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_gift_list, container, false);
        ButterKnife.inject(this, v);
        onGiftListService();
        return v;
    }

    public void onGiftListService() {
        Intent intent = new Intent(getActivity(), GiftListService.class);
        getActivity().startService(intent);
    }

    private BroadcastReceiver giftReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                giftList = intent.getParcelableArrayListExtra("resultValue");

                displayList.clear();
                for (Gift gift : giftList) {
                    if (noObscene && gift.isObscene()) {
                        continue;
                    }
                    else {
                        displayList.add(gift);
                    }
                }
                
                adapterGifts = new GiftAdapter(getActivity(), displayList);

                sgvGifts.setAdapter(adapterGifts);
                sgvGifts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), GiftDisplayActivity.class);
                        Gift gift = giftList.get(position);
                        intent.putExtra("gift", gift);
                        startActivity(intent);
                    }
                });
                Log.i(TAG, "GIFT List Service result received. " + giftList.size());

            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(GiftListService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(giftReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(giftReceiver);
    }

    public void performSearch(final String query) {
        final GiftSvcApi svc = GiftSvc.getOrShowLogin(getActivity());

        if (svc != null) {
            CallableTask.invoke(new Callable<Collection<Gift>>() {

                @Override
                public Collection<Gift> call() throws Exception {

                    return svc.findByTitle(query);

                }
            }, new TaskCallback<Collection<Gift>>() {
                @Override
                public void success(Collection<Gift> result) {
                    Log.i(TAG, "Got following gifts...............");
                    giftList.clear();
                    for (Gift gift : result) {
                        giftList.add(gift);

                        Log.i(TAG, "Gift: " + gift.getDataUrl());
                    }
                    adapterGifts = new GiftAdapter(getActivity(), giftList);
                    sgvGifts.setAdapter(adapterGifts);
                }

                @Override
                public void error(Exception e) {
                    Toast.makeText(
                            getActivity(),
                            "Unable to fetch the gift list.",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
