package com.android.banana.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.android.banana.R;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.commlib.utils.picasso.PicUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2017/6/22.
 */

public class DialogHelper {

    public static void showListDialog(FragmentManager fm, ListSelectDialog.OnClickListener listener, int theme, CharSequence... message) {
        ArrayList<CharSequence> messageList = new ArrayList<>();
        for (CharSequence aMessage : message) {
            messageList.add(aMessage);
        }
        showListDialog(fm, "", true, messageList, theme, listener);
    }

    public static void showListDialog(FragmentManager fm, ListSelectDialog.OnClickListener listener, CharSequence... message) {
        showListDialog(fm, listener, 0, message);
    }


    public static void showListDialog(FragmentManager fm, String header, boolean appendFooter, List<CharSequence> messageList, int theme, ListSelectDialog.OnClickListener listener) {
        showListDialog(fm, TextUtils.isEmpty(header) ? null : new ListSelectDialog.Title(header), messageList, appendFooter ? "取消" : "", false, theme, listener);

    }

    /**
     * 列表弹出选择
     *
     * @param fm          FragmentManager对象
     * @param listener    回调
     * @param title       列表头部
     * @param messageList 消息体集合
     * @param overScroll  是否超过一定高度后滚动  默认false
     */
    public static void showListDialog(FragmentManager fm, ListSelectDialog.Title title, List<CharSequence> messageList, String footer, boolean overScroll, int theme, ListSelectDialog.OnClickListener listener) {
        ListSelectDialog.Builder builder = new ListSelectDialog.Builder()
                .setTheme(theme)
                .setItemList(messageList)
                .setTitle(title)
                .setTextSize(14)
                .setTextColor(R.color.gray_text_color)
                .setOverScroll(overScroll)
                .setCancelBtnText(footer);

        ListSelectDialog listSelectDialog = builder.build();
        listSelectDialog.show(fm);
        listSelectDialog.setOnItemClickListener(listener);
    }

    public static void showConfirmDialog(Context ctx, String message, OnMyClickListener listener) {
        ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(ctx, message, listener);
    }

    public static void showConfirmDialog(Context ctx, String message, OnMyClickListener listener, OnMyClickListener listener1) {
        ShowMessageDialog dialog = new ShowMessageDialog(ctx, listener, listener1, message);
    }


    public static void showSavePicDialog(final AppCompatActivity activity, final String url) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.tip);
        builder.setMessage(R.string.save_pic);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                PicUtils.savePic(activity, url);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            // 自动dismiss
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
