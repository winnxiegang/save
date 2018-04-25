package com.android.banana.commlib.dialog;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.android.banana.commlib.R;
import com.squareup.picasso.Picasso;

/**
 * Created by zhouyi on 2015/4/23.
 */
public abstract class DialogBase {

    protected AlertDialog dialog;

    protected View window;

    public DialogBase(Context context, int resId) {

        dialog = new AlertDialog.Builder(context).create();

        window = LayoutInflater.from(context).inflate(resId, null);

        dialog.setView(window);
        dialog.setCancelable(false);

        dialog.show();
    }

    //修改主题，抢红包失败时的弹窗使用透明主题
    public DialogBase(Context context, int resId,int theme) {

        dialog = new AlertDialog.Builder(context,theme).create();

        window = LayoutInflater.from(context).inflate(resId, null);

        dialog.setView(window);

        dialog.setCancelable(false);

        dialog.show();
    }

    protected void setImageShow(ImageView imageView, String url) {

        if (url == null) return;

        Picasso.with(dialog.getContext().getApplicationContext())
                .load(Uri.parse(url))
                .error(R.drawable.user_default_logo)
                .into(imageView);
    }
}
