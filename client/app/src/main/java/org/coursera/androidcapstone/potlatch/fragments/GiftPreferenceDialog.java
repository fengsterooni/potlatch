package org.coursera.androidcapstone.potlatch.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.MainActivity;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class GiftPreferenceDialog extends DialogFragment {
    private final String TAG = GiftPreferenceDialog.class.getSimpleName();
    Context context = PotlatchApplication.getContext();
    SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor editor = pref.edit();

    @InjectView(R.id.cbNoObscene)
    CheckBox cbNoObscene;
    @InjectView(R.id.cbAuto)
    CheckBox cbAuto;
    @InjectView(R.id.spUpdateRate)
    Spinner spUpdateRate;
    @InjectView(R.id.btSavePref)
    Button btSave;

    @OnClick(R.id.btSavePref)
    void save() {
        editor.putBoolean(PotlatchApplication.PREF_NO_OBSCENE, cbNoObscene.isChecked());
        editor.putBoolean(PotlatchApplication.PREF_AUTO_UPDATE, cbAuto.isChecked());
        editor.putInt(PotlatchApplication.PREF_UPDATE_RATE, spUpdateRate.getSelectedItemPosition());
        editor.commit();
        int rate = spUpdateRate.getSelectedItemPosition();
        int[] updateRate = getResources().getIntArray(R.array.updateRateValue);
        int rateIndex = pref.getInt(PotlatchApplication.PREF_UPDATE_RATE, 0);
        int value = updateRate[rateIndex];

        Log.i(TAG, "No Obscene has been changed to " + cbNoObscene.isChecked());
        Log.i(TAG, "Auto update has been changed to " + cbAuto.isChecked());
        Log.i(TAG, "Selected rate: " + rate);
        Log.i(TAG, "Selected rate value: " + value);
        Log.i(TAG, "Preferences saved");

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @InjectView(R.id.btCancelPref)
    Button btCancel;

    @OnClick(R.id.btCancelPref)
    void cancel() {
        Log.i(TAG, "Preferences change cancelled");

        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);

    }

    private boolean bNoObscene = false;
    private boolean bAuto = false;
    private int updateRate = 0;

    public GiftPreferenceDialog() {
        // Required empty public constructor
    }

    public static GiftPreferenceDialog newInstance(boolean noObscene, boolean auto, int rate) {
        GiftPreferenceDialog frag = new GiftPreferenceDialog();
        Bundle args = new Bundle();
        args.putBoolean(PotlatchApplication.PREF_NO_OBSCENE, noObscene);
        args.putBoolean(PotlatchApplication.PREF_AUTO_UPDATE, auto);
        args.putInt(PotlatchApplication.PREF_UPDATE_RATE, rate);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pref_dialog, container, false);
        getDialog().setTitle("Personal Preference");
        ButterKnife.inject(this, view);

        Bundle args = getArguments();

        bNoObscene = args.getBoolean(PotlatchApplication.PREF_NO_OBSCENE, true);
        cbNoObscene.setChecked(bNoObscene);

        populateSpinner();
        updateRate = args.getInt(PotlatchApplication.PREF_UPDATE_RATE, 1);
        spUpdateRate.setSelection(updateRate);

        bAuto = args.getBoolean(PotlatchApplication.PREF_AUTO_UPDATE, false);
        cbAuto.setChecked(bAuto);

        cbAuto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spUpdateRate.getSelectedView().setEnabled(isChecked);
                spUpdateRate.setEnabled(isChecked);
            }
        });

        return view;
    }

    private void populateSpinner() {
        ArrayAdapter<CharSequence> updateAdapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.updateRate, android.R.layout.simple_spinner_item);
        int spinner_dd_item = android.R.layout.simple_spinner_dropdown_item;
        updateAdapter.setDropDownViewResource(spinner_dd_item);
        spUpdateRate.setAdapter(updateAdapter);
    }

}
