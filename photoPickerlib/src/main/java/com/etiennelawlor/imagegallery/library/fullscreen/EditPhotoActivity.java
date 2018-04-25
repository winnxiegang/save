package com.etiennelawlor.imagegallery.library.fullscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.library.Utils.LogUtils;
import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.etiennelawlor.imagegallery.library.view.CropImageView;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.io.File;


public class EditPhotoActivity extends BaseActivity {
    private String mPath;
    CropImageView mCropView;
    TextView finishTv;



    public static void startEditPhotoActivity(Activity activity, String path) {

        Intent intent = new Intent();

        intent.setClass(activity, EditPhotoActivity.class);

        intent.putExtra("path", path);

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_edit_photo);

        mCropView = (CropImageView) findViewById(R.id.cropImageView);
        finishTv = (TextView) findViewById(R.id.finishTv);
        finishTv.setText("确认");
        finishTv.setVisibility(View.VISIBLE);

        setTitleBar(true, "剪裁",null);

        mPath = getIntent().getStringExtra("path");

        int width = ImageGalleryUtils.getScreenWidth(this);
        Picasso.with(getApplicationContext())
                .load(new File(mPath))
                .resize(width, 0)
                .error(com.etiennelawlor.imagegallery.library.R.drawable.full_preview_image_empty)
                .placeholder(com.etiennelawlor.imagegallery.library.R.drawable.full_preview_image_empty)
                .into(mCropView);


        finishTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(mCropView.getCroppedBitmap());
                LogUtils.e("kk",mCropView.getCroppedBitmap()+"-----------*****-------");
                finish();
            }
        });

    }
}
