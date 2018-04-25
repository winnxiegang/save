package com.etiennelawlor.imagegallery.library.fullscreen;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.library.Utils.LogUtils;
import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.adapters.ImageGalleryAdapter;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.PhotoDirectory;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.event.EventChangeSelectPicStatus;
import com.etiennelawlor.imagegallery.library.event.EventFinishSelectPic;
import com.etiennelawlor.imagegallery.library.util.AlbumStorageDirFactory;
import com.etiennelawlor.imagegallery.library.util.BaseAlbumDirFactory;
import com.etiennelawlor.imagegallery.library.util.FroyoAlbumDirFactory;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.etiennelawlor.imagegallery.library.util.MediaStoreHelper;
import com.etiennelawlor.imagegallery.library.util.PopupWindowHelper;
import com.etiennelawlor.imagegallery.library.view.GridSpacesItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ImageGalleryActivity extends BaseActivity implements ImageGalleryAdapter.OnImageClickListener {

    public static final int INSERT_IMAGE = 1;

    public static final int SELECT_ICON_IMAGE = 2;

    public static final int SELECTED_PREVIEW_CLICK_TV = 0;  //预览选中的所有图片

    //以下是照相使用的参数
    private static final int ACTION_TAKE_PHOTO_B = 1;

    private String mCurrentPhotoPath;

    private static final String JPEG_FILE_PREFIX = "IMG_";

    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private RecyclerView mRecyclerView;

    private TextView titleTv;

    private ImageView arrowDownIv;

    private View titleLayout;

    private View moreFunctionLayout;

    private List<PhotoDirectory> directoryList = new ArrayList<>();

    private ArrayList<Photo> photoList = new ArrayList<>();

    private ArrayList<Photo> backUpSelectList = new ArrayList<>();

    private PopupWindowHelper helper;

    private int mSelectCount = 0;

    private Button mFinishBtn;

    private ArrayList<Photo> mSelectList = new ArrayList<>();

    private ImageGalleryAdapter imageGalleryAdapter;

    private int maxInsert;//最多插入几张图片

    private int mCurrentOption;


    public static void startImageGalleryActivity(Activity activity, int maxInsert, int operation) {

        Intent intent = new Intent();

        intent.setClass(activity, ImageGalleryActivity.class);

        intent.putExtra("maxInsert", maxInsert);

        intent.putExtra("operation", operation);

        activity.startActivity(intent);

    }

    private View.OnClickListener mDirectorySelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            int position = (int) v.getTag();

            int i;

            for (Photo photo : photoList) {
                photo.setSelected(false);
            }
            photoList.clear();
            //mSelectList.clear();
            photoList.addAll(directoryList.get(position).getPhotos());
            imageGalleryAdapter.notifyDataSetChanged();
            titleTv.setText(directoryList.get(position).getName());
            //imageGalleryAdapter.clearSelectImage();
            helper.dismiss();
        }
    };

    private View.OnClickListener mStartTakePhotoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //TODO
            if (isIntentAvailable(ImageGalleryActivity.this, MediaStore.ACTION_IMAGE_CAPTURE)) {
                ImageGalleryActivityPermissionsDispatcher.dispatchTakePictureIntentWithCheck(ImageGalleryActivity.this, ACTION_TAKE_PHOTO_B);
                //dispatchTakePictureIntent(ACTION_TAKE_PHOTO_B);
            } else {
                Toast.makeText(ImageGalleryActivity.this, "当前系统不可以拍照", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener mSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            if (photoList.get(position).isSelected()) {
                mSelectCount += 1;
                mSelectList.add(photoList.get(position));
            } else {
                mSelectCount -= 1;
                mSelectList.remove(photoList.get(position));
            }
            mFinishBtn.setText("完成(" + mSelectCount + ")");
        }
    };

    private View.OnClickListener finishClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO
            if (mCurrentOption == SELECT_ICON_IMAGE) {

                String path = (String) v.getTag();

                EditPhotoActivity.startEditPhotoActivity(ImageGalleryActivity.this, path);

                finish();

                return;
            }
            if (mSelectList.size() <= maxInsert) {
                StoreSelectBean image = new StoreSelectBean(StoreSelectBean.STORE_IMAGE, mSelectList);
                EventBus.getDefault().post(image);
                finish();
            } else {
                Toast.makeText(ImageGalleryActivity.this, "最多只能插入" + maxInsert + "张", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //setIsSupportSlideFinish(false);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_gallery);

        bindViews();

        helper.setDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                arrowDownIv.setImageResource(R.drawable.icon_arrow_down);
            }
        });

        maxInsert = getIntent().getIntExtra("maxInsert", 5);

        mCurrentOption = getIntent().getIntExtra("operation", INSERT_IMAGE);

        titleTv.setText("所有图片");

        ImageGalleryActivityPermissionsDispatcher.getAllImageWithCheck(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        if (mCurrentOption == SELECT_ICON_IMAGE) {
            moreFunctionLayout.setVisibility(View.GONE);
        }

        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

        setUpRecyclerView();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (outState != null)
            outState.putString("path", mCurrentPhotoPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null)
            mCurrentPhotoPath = savedInstanceState.getString("path");
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onImageClick(int position) {

        if (mCurrentOption == SELECT_ICON_IMAGE) {

            EditPhotoActivity.startEditPhotoActivity(ImageGalleryActivity.this, photoList.get(position).getPath());

        } else if (mCurrentOption == INSERT_IMAGE) {

            Intent intent;

            intent = new Intent(ImageGalleryActivity.this, FullScreenImageGalleryActivity.class);

            intent.putParcelableArrayListExtra("images", photoList);

            intent.putParcelableArrayListExtra("selectImages", mSelectList);

            intent.putExtra("position", position);

            intent.putExtra("maxInsert", maxInsert);

            startActivity(intent);

        }

    }

    private void bindViews() {

        moreFunctionLayout = findViewById(R.id.moreFunctionLayout);

        mFinishBtn = (Button) findViewById(R.id.finishBtn);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        titleTv = (TextView) findViewById(R.id.titleTv);

        arrowDownIv = (ImageView) findViewById(R.id.arrowDownIv);

        arrowDownIv.setVisibility(View.VISIBLE);

        titleLayout = findViewById(R.id.titleLayout);

        helper = new PopupWindowHelper(this, directoryList, titleLayout, mDirectorySelectListener);

        arrowDownIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowDownIv.setImageResource(R.drawable.icon_arrow_up);
                helper.showPopupWindow();
            }
        });


        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrowDownIv.setImageResource(R.drawable.icon_arrow_up);
                helper.showPopupWindow();

            }
        });


        TextView previewTv = (TextView) findViewById(R.id.previewTv);

        previewTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mSelectList.size() == 0) {
                    Toast.makeText(ImageGalleryActivity.this, "当前没有选择的图片", Toast.LENGTH_SHORT).show();
                    return;
                }

                backUpSelectList.clear();

                backUpSelectList.addAll(mSelectList);

                Intent intent = new Intent(ImageGalleryActivity.this, FullScreenImageGalleryActivity.class);

                intent.putParcelableArrayListExtra("images", mSelectList);

                intent.putExtra("type", SELECTED_PREVIEW_CLICK_TV);

                intent.putExtra("position", 0);

                startActivity(intent);
            }
        });

        TextView cancelTv = (TextView) findViewById(R.id.cancelTv);

        cancelTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mFinishBtn.setOnClickListener(finishClickListener);
    }

    private void setUpRecyclerView() {

        int numOfColumns;

        if (ImageGalleryUtils.isInLandscapeMode(this)) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(ImageGalleryActivity.this, numOfColumns));

        mRecyclerView.addItemDecoration(new GridSpacesItemDecoration(ImageGalleryUtils.dp2px(this, 2), numOfColumns));

        //TODO
        imageGalleryAdapter = new ImageGalleryAdapter(photoList, maxInsert, this);

        imageGalleryAdapter.setSelectType(mCurrentOption);

        //当是设置头像的时候 才设置这个单一图片选择的监听。
        if (mCurrentOption == SELECT_ICON_IMAGE)
            imageGalleryAdapter.setOnImageClickListener(this);

        imageGalleryAdapter.setTakePhotoClickListener(mStartTakePhotoClickListener);

        imageGalleryAdapter.setSelectListener(mSelectListener);

        mRecyclerView.setAdapter(imageGalleryAdapter);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    void getAllImage() {

        if (directoryList.size() > 0) {
            return;
        }
        MediaStoreHelper.getPhotoDirs(this, new Bundle(),
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directoryList.clear();

                        directoryList.addAll(dirs);

                        photoList.clear();

                        photoList.addAll(directoryList.get(0).getPhotos());

                        imageGalleryAdapter.notifyDataSetChanged();
                    }
                });
    }

    /* Photo album for this application */
    private String getAlbumName() {
        return getString(R.string.album_name);
    }


    private File getAlbumDir() {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {
            Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
        }

        return storageDir;
    }

    public void onEvent(EventChangeSelectPicStatus bean) {

        if (!bean.isClickPreviewTv()) {
            photoList.get(bean.getPosition()).setSelected(bean.isSelect());
            if (bean.isSelect()) {
                mSelectList.add(photoList.get(bean.getPosition()));
                mSelectCount++;
            } else {
                mSelectList.remove(photoList.get(bean.getPosition()));
                mSelectCount--;
            }
        } else {
            if (bean.isSelect()) {
                Photo photo = backUpSelectList.get(bean.getPosition());
                photo.setSelected(true);
                mSelectList.add(photo);
                mSelectCount++;
            } else {
                mSelectList.get(bean.getPosition()).setSelected(bean.isSelect());
                mSelectList.remove(bean.getPosition());
                mSelectCount--;
            }

        }

        mFinishBtn.setText("完成(" + mSelectCount + ")");
        imageGalleryAdapter.setmSelectCount(mSelectCount);
        imageGalleryAdapter.notifyDataSetChanged();
    }

    public void onEvent(EventFinishSelectPic bean) {

        StoreSelectBean image = new StoreSelectBean(StoreSelectBean.STORE_IMAGE, mSelectList);

        EventBus.getDefault().post(image);

        finish();

    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_" + JPEG_FILE_SUFFIX;

        File albumF = getAlbumDir();

        File imageF = new File(albumF, imageFileName);//File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);

        return imageF;

    }

    private File setUpPhotoFile() throws IOException {

        File f = createImageFile();

        mCurrentPhotoPath = f.getAbsolutePath();

        return f;
    }

    private void setPic() {

        LogUtils.e("当前路径", mCurrentPhotoPath);

        Photo photo = new Photo();

        photo.setSelected(false);

        photo.setPath(mCurrentPhotoPath);

        photoList.add(0, photo);

        LogUtils.e("张数", String.valueOf(photoList.size()));

        imageGalleryAdapter.notifyDataSetChanged();

    }

    private void galleryAddPic() {

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");

        File f = new File(mCurrentPhotoPath);

        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);

        this.sendBroadcast(mediaScanIntent);

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void dispatchTakePictureIntent(int actionCode) {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        switch (actionCode) {
            case ACTION_TAKE_PHOTO_B:
                File f = null;
                try {
                    f = setUpPhotoFile();
                    mCurrentPhotoPath = f.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                    f = null;
                    mCurrentPhotoPath = null;
                }
                break;
        }

        startActivityForResult(takePictureIntent, actionCode);
    }

    private void handleBigCameraPhoto() {

        if (mCurrentPhotoPath != null) {

            Photo photo = new Photo();

            photo.setPath(mCurrentPhotoPath);

            mSelectList.clear();

            mSelectList.add(photo);

            // setPic();

            galleryAddPic();

            View view = new View(this);

            view.setTag(mCurrentPhotoPath);

            finishClickListener.onClick(view);

            mCurrentPhotoPath = null;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ACTION_TAKE_PHOTO_B: {
                if (resultCode == RESULT_OK) {
                    handleBigCameraPhoto();
                }
                break;
            }
        }
    }


    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // NOTE: delegate the permission handling to generated method
        ImageGalleryActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }


}
