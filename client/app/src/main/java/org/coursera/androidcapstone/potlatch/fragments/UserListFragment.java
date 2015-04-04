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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.adapters.UserAdapter;
import org.coursera.androidcapstone.potlatch.models.User;
import org.coursera.androidcapstone.potlatch.services.UserListService;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserListFragment extends Fragment {
    private static final String TAG = UserListFragment.class.getSimpleName();

    private ArrayList<User> userList;
    private ArrayList<User> displayList;
    private ArrayAdapter<User> adapterUsers;
    @InjectView(R.id.lvUser)
    ListView lvUser;
    int topGiver;

    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userList = new ArrayList<User>();
        displayList = new ArrayList<>();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        topGiver = pref.getInt(PotlatchApplication.PREF_TOP_GIVER, 5);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(UserListService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(userReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(userReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_list, container, false);
        ButterKnife.inject(this, v);
        onUserListService();
        return v;
    }

    public void onUserListService() {
        Intent intent = new Intent(getActivity(), UserListService.class);
        getActivity().startService(intent);
    }

    private BroadcastReceiver userReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                userList = intent.getParcelableArrayListExtra("resultValue");
                
                displayList.clear();
                int i = 0;
                for (User user : userList) {
                    if (i < topGiver) {
                        displayList.add(user);
                        i++;
                    }
                }

                adapterUsers = new UserAdapter(getActivity(), displayList);
                lvUser.setAdapter(adapterUsers);
                Log.i(TAG, "User List Service result received. " + userList.size());
            }
        }
    };
}
