package com.android.xjq.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;

import com.android.xjq.R;
import com.android.xjq.bean.AddressListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qiaomu on 2018/3/4.
 */

public class AddressSelectUtils {
    public static final String ADDR_PRO = "PROVINCE";
    public static final String ADDR_CITY = "CITY";
    public static final String ADDR_COUNTY = "COUNTY";
    public static final String ADDR_TOWN = "TOWN";

    public static void onProvinceCityCountyQueryResult(Context context, List<AddressListBean.ProvinceCityCounty> address, String addressType, DialogInterface.OnClickListener listener) {
        if (address == null || address.size() <= 0)
            return;
        String title = null;
        List<CharSequence> items = new ArrayList<>();
        for (AddressListBean.ProvinceCityCounty province : address) {
            items.add(province.name);
        }
        switch (addressType) {
            case ADDR_PRO:
                title = context.getString(R.string.address_title_province);
                break;
            case ADDR_CITY:
                title = context.getString(R.string.address_title_city);
                break;
            case ADDR_COUNTY:
                title = context.getString(R.string.address_title_county);
                break;
            case ADDR_TOWN:
                title = context.getString(R.string.address_title_town);
        }
        showSingleChoiceListDialog(context, items, title, listener);
    }

    public static void showSingleChoiceListDialog(Context context, List<CharSequence> sequenceList, CharSequence title, final DialogInterface.OnClickListener listener) {
        if (sequenceList == null || sequenceList.size() <= 0)
            return;

        CharSequence[] sequencesArray = sequenceList.toArray(new CharSequence[0]);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context)
                .setSingleChoiceItems(sequencesArray, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (listener != null)
                            listener.onClick(dialog, which);
                    }
                }).setTitle(title);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        WindowManager.LayoutParams attributes = alertDialog.getWindow().getAttributes();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        attributes.width = (int) (0.85f * metrics.widthPixels);
        attributes.height = (int) (0.85f * metrics.heightPixels);
        attributes.gravity = Gravity.CENTER;
        alertDialog.getWindow().setAttributes(attributes);
    }
}
