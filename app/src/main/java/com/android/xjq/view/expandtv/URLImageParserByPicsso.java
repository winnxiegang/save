package com.android.xjq.view.expandtv;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.banana.commlib.view.URLDrawable;
import com.android.xjq.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by zhouyi on 2016/1/5 11:45.
 */
public class URLImageParserByPicsso implements Html.ImageGetter {

    private final String TAG = URLImageParserByPicsso.class.getName();

    Context c;

    TextView container;

    public URLImageParserByPicsso(TextView t, Context c) {

        this.c = c;

        this.container = t;
    }

    @Override
    public Drawable getDrawable(String source) {

        URLDrawable drawable = null;

        getNetworkImg(drawable, source);

        return drawable;
    }

    private void getNetworkImg(URLDrawable drawable, String source) {
        if (TextUtils.isEmpty(source)) return;
        Picasso.with(c).load(source).into(new MyTarget(drawable));
    }

    public class MyTarget implements Target {
        URLDrawable urlDrawable;

        public MyTarget(URLDrawable drawable) {
            urlDrawable = drawable;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            urlDrawable.setBounds(0, 0, (int) c.getResources().getDimension(R.dimen.sp14), (int) c.getResources().getDimension(R.dimen.sp14));

            urlDrawable.drawable = new BitmapDrawable(c.getResources(), bitmap);

            container.setText(container.getText());
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    }
}
