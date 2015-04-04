package org.coursera.androidcapstone.potlatch.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import org.coursera.androidcapstone.potlatch.R;
import org.coursera.androidcapstone.potlatch.activities.MainActivity;
import org.coursera.androidcapstone.potlatch.activities.PotlatchApplication;
import org.coursera.androidcapstone.potlatch.models.Gift;
import org.coursera.androidcapstone.potlatch.services.NewGiftService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NewGiftFragment extends Fragment {
    private static final String TAG = NewGiftFragment.class.getSimpleName();
    private static final int CAMERA_PIC_REQUEST = 1;
    private static final int CROP_PIC_REQUEST = 2;

    Uri photoUri;
    Bitmap photoBitmap;
    File photoFile;
    Long parentId;
    @InjectView(R.id.ivPhoto) ImageView imageView;
    @OnClick(R.id.ivPhoto)
    void takePic () {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getOutputMediaFile();
        photoUri = Uri.fromFile(getOutputMediaFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }
    
    @InjectView(R.id.etTitle) EditText etTitle;
    @InjectView(R.id.etText) EditText editText;

    public NewGiftFragment() {
        // Required empty public constructor
    }

    public static NewGiftFragment newInstance(Long pid) {
        NewGiftFragment newGiftFragment = new NewGiftFragment();
        Bundle args = new Bundle();
        args.putLong("parentId", pid);
        newGiftFragment.setArguments(args);
        return newGiftFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        parentId = getArguments().getLong("parentId");
        photoFile = getOutputMediaFile();
        photoUri = Uri.fromFile(getOutputMediaFile());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_new_gift, container, false);
        ButterKnife.inject(this, v);

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_PIC_REQUEST) {
                cropPhoto(photoUri);
            } else if (requestCode == CROP_PIC_REQUEST) {
                photoBitmap = data.getParcelableExtra("data");
                imageView.setImageBitmap(photoBitmap);
                storeImage(photoBitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void storeImage(Bitmap image) {
        try {
            FileOutputStream fos = new FileOutputStream(photoFile);
            image.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private void cropPhoto(Uri photoUri) {
        
        //call the standard crop action intent (the user device may not support it)
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(photoUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);
        //indicate output X and Y
        cropIntent.putExtra("outputX", 300);
        cropIntent.putExtra("outputY", 300);
        //retrieve data on return
        cropIntent.putExtra("return-data", true);
        
        //start the activity - we handle returning in onActivityResult
        startActivityForResult(cropIntent, CROP_PIC_REQUEST);
    }

    private static File getOutputMediaFile() {
        
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "potlatch");
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            return null;
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).
                format(new Date());
        
        return new File(mediaStorageDir.getPath() + File.separator +
                "img" + timeStamp + ".jpg");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_gift, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        int id = item.getItemId();
        if (id == R.id.action_done) {
            String title = etTitle.getText().toString();
            if (title != null && !title.equals("")) {
                String text = editText.getText().toString();
                Gift g = new Gift(title, parentId, "image/jpeg", text, PotlatchApplication.currentUser, 0, false, false);
                Intent intent = new Intent(getActivity(), NewGiftService.class);
                intent.putExtra("new", g);
                intent.putExtra("file", photoFile);
                getActivity().startService(intent);
            }
        }

        if (id == R.id.action_cancel) {
            getActivity().finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(NewGiftService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newGiftReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(newGiftReceiver);
    }
    
    private BroadcastReceiver newGiftReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra("resultCode", Activity.RESULT_CANCELED);
            if (resultCode == Activity.RESULT_OK) {
                Log.i(TAG, "Done with the new Gifts");
                Intent in = new Intent(getActivity(), MainActivity.class);
                startActivity(in);
            }
            
        }
    };
}