package com.android.banana.commlib.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.library.Utils.LogUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by zhouyi on 2016/1/5 11:45.
 */
public class URLImageParser implements Html.ImageGetter {

    private final String TAG = URLImageParser.class.getName();

    Context c;

    TextView container;

    public URLImageParser(TextView t, Context c) {

        this.c = c;

        this.container = t;


    }

    @Override
    public Drawable getDrawable(String source) {

        URLDrawable urlDrawable = new URLDrawable();

        ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);

        asyncTask.execute(source);

        return urlDrawable;

    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

        URLDrawable urlDrawable;

        public ImageGetterAsyncTask(URLDrawable d) {

            this.urlDrawable = d;

        }

        @Override
        protected Drawable doInBackground(String... params) {

            if (params == null)
                return null;

            URL url;

            Drawable drawable = null;

            int i1 = params[0].lastIndexOf("/");

            int i2 = params[0].indexOf(".gif");

            int i3 = params[0].lastIndexOf(".png");
            LogUtils.e(TAG,"i1="+i1+"  i2="+i2+"  i3="+i3);
            if (i3 == -1 || i1 == -1) {
                if (i3 != -1) {
                    try {

                        url = new URL(params[0]);

                        drawable = Drawable.createFromStream(url.openStream(), null);

                        drawable.setBounds(0, 0, (int) c.getResources().getDimension(R.dimen.sp14), (int) c.getResources().getDimension(R.dimen.sp14));

                    } catch (MalformedURLException e) {

                        e.printStackTrace();
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    return drawable;
                } else {
                    try {

                        url = new URL(params[0]);

                        drawable = Drawable.createFromStream(url.openStream(), null);

                        if (drawable != null)
                            drawable.setBounds(0, -6, (int) c.getResources().getDimension(R.dimen.sp15), (int) c.getResources().getDimension(R.dimen.sp15) - 6);

                    } catch (MalformedURLException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                    return drawable;
                }
            } else {

                String imageName = params[0].substring(i1 + 1, i3);

                String identifier = "emoj_" + imageName;

                String packageName = c.getPackageName();

                int resId = c.getResources().getIdentifier(identifier, "drawable", packageName);

                if (resId != 0) {

                    Drawable drawable1 = c.getResources().getDrawable(resId);
                    if (drawable1 != null)
                        drawable1.setBounds(0, 0, (int) c.getResources().getDimension(R.dimen.sp14), (int) c.getResources().getDimension(R.dimen.sp14));

                    return drawable1;

                } else {

                    try {
                        url = new URL(params[0]);

                        drawable = Drawable.createFromStream(url.openStream(), null);
                        if (drawable != null)
                            drawable.setBounds(0, 0, (int) c.getResources().getDimension(R.dimen.sp14), (int) c.getResources().getDimension(R.dimen.sp14));

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return drawable;
                }
            }

        }


        @Override
        protected void onPostExecute(Drawable result) {
            if (result == null) {
                return;
            }
            urlDrawable.setBounds(0, 0, (int) c.getResources().getDimension(R.dimen.sp14), (int) c.getResources().getDimension(R.dimen.sp14));
            urlDrawable.drawable = result;
            container.setText(container.getText());
        }

    }
}
