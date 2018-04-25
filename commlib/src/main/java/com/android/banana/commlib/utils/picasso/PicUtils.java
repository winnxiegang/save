package com.android.banana.commlib.utils.picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.library.Utils.LogUtils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by mrs on 2017/4/7.
 */

public class PicUtils {
    /**
     * 如果要加载本地文件图片 最好使用这个方法, Picasso.with(context).load(file).into(img); 同一文件路径会使用缓存，也就是加载出来的不会变
     *
     * @param context
     * @param img
     * @param file
     */
    public static void load(Context context, ImageView img, File file, boolean noCache) {
        if (noCache) {
            Picasso.with(context).load(file).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).into(img);
        } else {
            Picasso.with(context).load(file).into(img);
        }
    }

    public static void load(Context context, ImageView img, File file, int placeHolder, boolean noCache) {
        if (noCache) {
            Picasso.with(context).load(file).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).placeholder(placeHolder).into(img);
        } else {
            Picasso.with(context).load(file).placeholder(placeHolder).into(img);
        }
    }

    public static void load(Context context, final ImageView img, String path, final int placeHolder) {
        if (TextUtils.isEmpty(path) && placeHolder != 0)
            img.setImageResource(placeHolder);
        else {
            if (placeHolder != 0)
                Picasso.with(context).load(path).placeholder(placeHolder).error(placeHolder).into(img);
            else
                Picasso.with(context).load(path).into(img);
        }

    }

    public static void load(Context context, ImageView img, String path) {
        Picasso.with(context).load(path).fit().into(img);
    }

    public static void loadWithResize(Context context, ImageView img, String path, int resizeW, int resizeH) {
        Picasso.with(context)
                .load(path)
                .resize(resizeW, resizeH)
                .centerCrop()
                .into(img);
    }

    public static void loadCircle(Context context, ImageView img, String path) {
        Picasso.with(context)
                .load(path)
                .transform(new CircleTransform())
                .into(img);
    }

    public static void loadWithBitmap(Context context, String path, final PicassoLoadCallback callback) {
        Picasso.with(context).load(path).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if (callback != null)
                    callback.onBitmapLoaded(bitmap);
                LogUtils.e("onBitmapLoaded", "onBitmapLoaded");
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                LogUtils.e("onBitmapFailed", "onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                LogUtils.e("onPrepareLoad", "onPrepareLoad");
            }
        });
    }

    public static void savePic(final Context context, final String url) {
        loadWithBitmap(context, url, new PicassoLoadCallback() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap) {
                try {
                    File appDir = new File(Environment.getExternalStorageDirectory(), "xjq");
                    if (!appDir.exists()) {
                        appDir.mkdir();
                    }
                    File file = new File(appDir, System.currentTimeMillis() + ".png");
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
                    Toast.makeText(context, "图片保存图库成功", Toast.LENGTH_LONG).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    public static void start2PickImage(Activity from, int requestCode) {
        //调用相册
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        from.startActivityForResult(intent, requestCode);
    }


}
