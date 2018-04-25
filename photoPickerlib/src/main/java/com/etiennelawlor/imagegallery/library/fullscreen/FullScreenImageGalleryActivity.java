package com.etiennelawlor.imagegallery.library.fullscreen;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.utils.toast.ToastUtil;
import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.event.EventChangeSelectPicStatus;
import com.etiennelawlor.imagegallery.library.event.EventFinishSelectPic;
import com.etiennelawlor.imagegallery.library.util.AlbumStorageDirFactory;
import com.etiennelawlor.imagegallery.library.util.BaseAlbumDirFactory;
import com.etiennelawlor.imagegallery.library.util.FroyoAlbumDirFactory;
import com.etiennelawlor.imagegallery.library.util.ImageGalleryUtils;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imageformat.DefaultImageFormats;
import com.facebook.imageformat.ImageFormat;
import com.facebook.imageformat.ImageFormatChecker;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
//todo 图片查看的工具 也可以实现图片的保存功能
@RuntimePermissions
public class FullScreenImageGalleryActivity extends AppCompatActivity {

    public static final int SELECTED_PREVIEW = 1;

    public static final int SELECTED_PREVIEW_CLICK_TV = 0;  //预览选中的所有图片

    public static final int PREVIEW = 2;

    public static final int NETWORK_PREVIEW = 3;

    public static final String TYPE = "type";

    private List<Photo> mImages = new ArrayList<>();

    private List<Photo> mSelectImages = new ArrayList<>();

    private List<String> mNetworkImages;

    private int mPosition;

    private int mSelectCount;

    private ViewPager mViewPager;

    private Button mFinishBtn;

    private TextView numTitleTv;

    private ImageView selectIv;

    private LinearLayout backLayout;

    public Button saveImageBtn;

    private int mType;

    private int maxInsert;//最多插入几张图片

    private FullScreenImageGalleryAdapter fullScreenImageGalleryAdapter = null;

    private String mCurrentImageUrl = null;

    private static final String JPEG_FILE_PREFIX = "IMG_";

    private AlbumStorageDirFactory mAlbumStorageDirFactory = null;

    private File finalSavePath;

    private boolean isSaveSuccess;
    private final ViewPager.OnPageChangeListener mViewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (mViewPager != null) {
                setActionBarTitle(position);
                fullScreenImageGalleryAdapter.setCurrentSelectedPosition(position);
                if (fullScreenImageGalleryAdapter.getDownloadStatus(position)) {
                    saveImageBtn.setEnabled(true);
                    saveImageBtn.setTextColor(Color.WHITE);
                } else {
                    saveImageBtn.setTextColor(Color.parseColor("#585858"));
                    saveImageBtn.setEnabled(false);
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public static void startFullScreenImageGalleryActivity(Context from, ArrayList<String> list, int position) {

        Intent intent = new Intent(from, FullScreenImageGalleryActivity.class);

        intent.putExtra("images", list);

        intent.putExtra("position", position);

        intent.putExtra(TYPE, NETWORK_PREVIEW);

        from.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image_gallery);

        bindViews();

        Intent intent = getIntent();

        maxInsert = getIntent().getIntExtra("maxInsert", 5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            mAlbumStorageDirFactory = new FroyoAlbumDirFactory();
        } else {
            mAlbumStorageDirFactory = new BaseAlbumDirFactory();
        }

        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mType = extras.getInt(TYPE, 1);
                if (mType == NETWORK_PREVIEW) {
                    saveImageBtn.setVisibility(View.VISIBLE);
                    mNetworkImages = extras.getStringArrayList("images");
                    for (int i = 0; i < mNetworkImages.size(); i++) {
                        Photo p = new Photo();
                        p.setPath(mNetworkImages.get(i));
                        mImages.add(p);
                    }
                } else if (mType == SELECTED_PREVIEW_CLICK_TV) {
                    mImages = extras.getParcelableArrayList("images");
                    mSelectImages.clear();
                    mSelectImages.addAll(mImages);
                    mSelectCount = mSelectImages.size();
                } else {
                    mImages = extras.getParcelableArrayList("images");
                    if (null != extras.getParcelableArrayList("selectImages")) {
                        mSelectImages = extras.getParcelableArrayList("selectImages");
                        mSelectCount = mSelectImages.size();
                    }
                }
                mPosition = extras.getInt("position");
            }
        }
        if (mType == SELECTED_PREVIEW) {
            mFinishBtn.setText("完成(" + mSelectImages.size() + ")");
        } else if (mType == PREVIEW) {
            mFinishBtn.setVisibility(View.GONE);
            selectIv.setEnabled(false);
        } else if (mType == NETWORK_PREVIEW) {
            mFinishBtn.setVisibility(View.GONE);
            selectIv.setVisibility(View.GONE);
        } else if (mType == SELECTED_PREVIEW_CLICK_TV) {
            mFinishBtn.setText("完成(" + mImages.size() + ")");
        }

        setUpViewPager();

        setActionBarTitle(mPosition);

    }

    @Override
    protected void onDestroy() {

        removeListeners();

        mViewPager.removeAllViews();

        setContentView(new View(this));

        super.onDestroy();

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

    private View.OnClickListener mSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mImages.get(mPosition).isSelected()) {
                mSelectCount += 1;
                mSelectImages.add(mImages.get(mPosition));
                if (mType != SELECTED_PREVIEW_CLICK_TV) {
                    EventChangeSelectPicStatus bean = new EventChangeSelectPicStatus(mPosition, true);
                    EventBus.getDefault().post(bean);
                } else {
                    EventChangeSelectPicStatus bean = new EventChangeSelectPicStatus(mPosition, true, true);
                    EventBus.getDefault().post(bean);
                }
            } else {
                mSelectCount -= 1;
                int indexOfRemove = mSelectImages.indexOf(mImages.get(mPosition));
                mSelectImages.remove(mImages.get(mPosition));
                if (mType != SELECTED_PREVIEW_CLICK_TV) {
                    EventChangeSelectPicStatus bean = new EventChangeSelectPicStatus(mPosition, false);
                    EventBus.getDefault().post(bean);
                } else {
                    EventChangeSelectPicStatus bean = new EventChangeSelectPicStatus(indexOfRemove, false, true);
                    EventBus.getDefault().post(bean);
                }
            }
            mFinishBtn.setText("完成(" + mSelectCount + ")");
        }
    };

    // region Helper Methods
    private void bindViews() {

        mViewPager = (ViewPager) findViewById(R.id.vp);

        mFinishBtn = (Button) findViewById(R.id.finishBtn);

        numTitleTv = (TextView) findViewById(R.id.numTitleTv);

        selectIv = (ImageView) findViewById(R.id.selectIv);

        backLayout = (LinearLayout) findViewById(R.id.backLayout);

        saveImageBtn = (Button) findViewById(R.id.saveBtn);

        backLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.exit(0);
                finish();
            }
        });

        selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mImages.get(mPosition).isSelected() && mSelectCount == maxInsert) {
                    if (0 == maxInsert) {
                        Toast.makeText(selectIv.getContext(), "你已经选满5张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(selectIv.getContext(), "最多只能选择" + maxInsert + "张", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                if (mImages.get(mPosition).isSelected()) {
                    selectIv.setImageResource(R.drawable.icon_insert_photo_normal);
                    mImages.get(mPosition).setSelected(false);
                } else {
                    selectIv.setImageResource(R.drawable.icon_insert_photo_selected);
                    mImages.get(mPosition).setSelected(true);
                }
                if (mSelectListener != null) {
                    mSelectListener.onClick(v);
                }
            }
        });

        mFinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectImages.size() <= maxInsert) {
                    EventFinishSelectPic bean = new EventFinishSelectPic();
                    EventBus.getDefault().post(bean);
                    finish();
                } else {
                    if (0 == maxInsert) {
                        Toast.makeText(FullScreenImageGalleryActivity.this, "你已经选满5张图片", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FullScreenImageGalleryActivity.this, "最多只能选择" + maxInsert + "张", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        saveImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FullScreenImageGalleryActivityPermissionsDispatcher.saveImageWithCheck(FullScreenImageGalleryActivity.this);
            }
        });

        int a = 10;

        int b = 20;

    }

    private void setUpViewPager() {

        if (mType == NETWORK_PREVIEW) {
            fullScreenImageGalleryAdapter = new FullScreenImageGalleryAdapter(this, mImages, NETWORK_PREVIEW);
        } else {
            fullScreenImageGalleryAdapter = new FullScreenImageGalleryAdapter(this, mImages);
        }

        mViewPager.setAdapter(fullScreenImageGalleryAdapter);

        mViewPager.addOnPageChangeListener(mViewPagerOnPageChangeListener);

        mViewPager.setCurrentItem(mPosition);

        setActionBarTitle(mPosition);
    }

    private void setActionBarTitle(int position) {
        if (mViewPager != null && mImages != null && mImages.size() > 0) {
            int totalPages = mViewPager.getAdapter().getCount();
            if (numTitleTv != null) {
                numTitleTv.setText(String.format("%d / %d", (position + 1), totalPages));
            }
            if (mImages.get(position).isSelected()) {
                selectIv.setImageResource(R.drawable.icon_insert_photo_selected);
            } else {
                selectIv.setImageResource(R.drawable.icon_insert_photo_normal);
            }
            mPosition = position;
        }
    }

    private void removeListeners() {

        mViewPager.removeOnPageChangeListener(mViewPagerOnPageChangeListener);

    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void saveImage() {

        final Context context = FullScreenImageGalleryActivity.this.getApplicationContext();

        mCurrentImageUrl = mNetworkImages.get(mViewPager.getCurrentItem());

        if (mCurrentImageUrl != null) {

            if (mCurrentImageUrl.lastIndexOf(".gif") != -1) {

                new AsyncTask<Void, Void, Boolean>() {

                    @Override
                    protected Boolean doInBackground(Void... params) {

                        boolean saveStatus = saveGifImage(Uri.parse(mCurrentImageUrl));

                        return saveStatus;

                    }

                    @Override
                    protected void onPostExecute(Boolean saveStatus) {
                        super.onPostExecute(saveStatus);
                        if (saveStatus) {
                            ToastUtil.show(context, "保存图片成功", R.drawable.icon_save_success);
                        } else {
                            ToastUtil.show(context, "保存失败", Toast.LENGTH_SHORT);
                        }
                    }
                }.execute();

            } else {

                File savePath = null;

                try {
                    savePath = createImageFile(context, ".jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finalSavePath = savePath;
                Picasso.with(context).load(mCurrentImageUrl).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        if (bitmap != null) {
                            saveImage(bitmap);
                        } else {
                            isSaveSuccess = false;
                            Toast.makeText(FullScreenImageGalleryActivity.this.getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        isSaveSuccess = false;
                        Toast.makeText(FullScreenImageGalleryActivity.this.getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }
        }
    }

    private synchronized void saveImage(final Bitmap bitmap) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(finalSavePath);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                    isSaveSuccess = true;
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    isSaveSuccess = false;
                } catch (IOException e) {
                    e.printStackTrace();
                    isSaveSuccess = false;
                }
                return isSaveSuccess;
            }

            @Override
            protected void onPostExecute(Boolean saveStatus) {
                super.onPostExecute(saveStatus);
                if (saveStatus) {
                    ToastUtil.show(FullScreenImageGalleryActivity.this.getApplicationContext(), "保存图片成功", R.drawable.icon_save_success);
                    galleryAddPic(finalSavePath);
                } else {
                    Toast.makeText(FullScreenImageGalleryActivity.this.getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private synchronized boolean saveGifImage(Uri uri) {

        FileOutputStream fos = null;

        FileInputStream fis = null;

        boolean saveStatus = false;

        try {

            ImageRequest downloadRequest = ImageRequest.fromUri(uri);

            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(downloadRequest, this);

            if (ImagePipelineFactory.getInstance().getMainFileCache().hasKey(cacheKey)) {

                BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);

                File cacheFile = ((FileBinaryResource) resource).getFile();

                fis = new FileInputStream(cacheFile);

                ImageFormat imageFormat = null;

                imageFormat = ImageFormatChecker.getImageFormat(fis);

                File saveImageUrl = createImageFile(this, ".gif");

                fos = new FileOutputStream(saveImageUrl);

                if (DefaultImageFormats.GIF.getName().equals(imageFormat.getName())) {

                    byte[] read = resource.read();

                    fos.write(read);

                    saveStatus = true;

                    galleryAddPic(saveImageUrl);

                }

            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            ImageGalleryUtils.closeQuietly(fis);
            ImageGalleryUtils.closeQuietly(fos);
        }
        return saveStatus;
    }

    private void galleryAddPic(File f) {

        Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");

        Uri contentUri = Uri.fromFile(f);

        mediaScanIntent.setData(contentUri);

        this.sendBroadcast(mediaScanIntent);

    }

    private File createImageFile(Context context, String suffix) throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";

        File albumF = getAlbumDir(context);

        File imageF = File.createTempFile(imageFileName, suffix, albumF);

        return imageF;

    }

    private File getAlbumDir(Context context) {

        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

            storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName(context));

            if (storageDir != null) {
                if (!storageDir.mkdirs()) {
                    if (!storageDir.exists()) {
                        Log.d("CameraSample", "failed to create directory");
                        return null;
                    }
                }
            }

        } else {

        }

        return storageDir;
    }

    /* Photo album for this application */
    private String getAlbumName(Context context) {
        return context.getString(R.string.album_name);
    }

    /* 动态申请写sd卡权限 */

    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForCamera(PermissionRequest request) {
        showRationaleDialog("使用此功能需要打开写SD卡的权限", request);
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onCameraDenied() {
        Toast.makeText(FullScreenImageGalleryActivity.this, "你拒绝了权限，该功能不可用", Toast.LENGTH_SHORT).show();
    }

    @OnNeverAskAgain(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void onCameraNeverAskAgain() {
        Toast.makeText(FullScreenImageGalleryActivity.this, "不再允许询问该权限，该功能不可用", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FullScreenImageGalleryActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

    }

    private void showRationaleDialog(String messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
