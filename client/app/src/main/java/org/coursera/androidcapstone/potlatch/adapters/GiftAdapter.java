package org.coursera.androidcapstone.potlatch.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.models.Gift;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class GiftAdapter extends ArrayAdapter<Gift> {
    private static final String TAG = GiftAdapter.class.getSimpleName();
    
    @InjectView(R.id.ivImage) ImageView ivImage;
    @InjectView(R.id.tvUserG) TextView tvUser;
    @InjectView(R.id.ivObsceneG) ImageView ivObscene;
    @InjectView(R.id.ivTouchedG) ImageView ivTouched;
    @InjectView(R.id.tvTitle) TextView tvTitle;
    @InjectView(R.id.tvTouchCount) TextView tvTouchCount;
    @InjectView(R.id.tvAdpText) TextView tvText;
    
    public GiftAdapter(Context context, List<Gift> gifts) {
        super(context, R.layout.gift_item, gifts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        Gift gift = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.gift_item, parent, false);

        ButterKnife.inject(this, convertView);
        
        ivImage.setImageResource(0);
        tvUser.setText(gift.getUser().getName());
        tvTitle.setText(Html.fromHtml(gift.getTitle()));
        tvTouchCount.setText(Long.toString(gift.getTouches()));
        ivTouched.setImageResource(R.drawable.touched_yes);
        if (gift.isObscene()) 
            ivObscene.setImageResource(R.drawable.obscene_yes);
        else
            ivObscene.setImageResource(R.drawable.obscene_no);
        String text = gift.getText();
        if (!TextUtils.isEmpty(text))
            tvText.setText(Html.fromHtml(text));

        Long id = gift.getId();
        Bitmap bitmap = PotlatchApplication.getBitmapFromGiftCache(id);
        ivImage.setImageBitmap(bitmap);

        return convertView;
    }
}
