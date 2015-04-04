package org.coursera.androidcapstone.potlatch.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.models.User;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UserAdapter  extends ArrayAdapter<User> {
    private static final String TAG = UserAdapter.class.getSimpleName();
    
    @InjectView(R.id.tvUserName) TextView tvUserName;
    @InjectView(R.id.tvNumGift) TextView tvNumGift;
    @InjectView(R.id.tvNumTouched) TextView tvNumTouched;    
   
    public UserAdapter(Context context, List<User> users) {
        super(context, R.layout.gift_item, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        User user  = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user_item, parent, false);

        ButterKnife.inject(this, convertView);

        tvUserName.setText(Html.fromHtml(user.getName()));
        tvNumGift.setText(" " + user.getNumGifts());
        tvNumTouched.setText(" " + user.getTouchedCount());

        return convertView;
    }

}
