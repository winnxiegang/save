package com.android.xjq.controller;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.AppParam;
import com.android.xjq.view.banner.ImageCycleView;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/4/17.
 */

public class BannerController {

    private int width, height;

    private ArrayList<String> bannerList = new ArrayList<>();

    private ImageCycleView bannerView;

    private LinearLayout indicator;

    private ImageView bannerDefault;

    private Context context;

    public BannerController(final Context context, final ImageCycleView bannerView, LinearLayout indicator,
                            FrameLayout mainBannerLL, ImageView bannerDefault) {

        this.bannerView = bannerView;

        this.indicator = indicator;

        this.bannerDefault = bannerDefault;

        this.context = context;

        WindowManager wm = ((BaseActivity) context).getWindowManager();

        width = wm.getDefaultDisplay().getWidth();

//        height = width * 200 / 640;

        height = width * 458 / 1224;

        mainBannerLL.setLayoutParams(new LinearLayout.LayoutParams(width, height));
    }

    public void handleData(JSONObject jsonObject, String requestType) throws JSONException {
        if (AppParam.REFRESH_DATA == requestType) {
            bannerList.clear();
        }
        if (jsonObject.has("imageUrlList")) {
            JSONArray imageUrlList = jsonObject.getJSONArray("imageUrlList");
            List<String> urlList = new Gson().fromJson(imageUrlList.toString(), new TypeToken<List<String>>() {
            }.getType());

            bannerList.addAll(urlList);
        }


        judgePictureCount();
    }

    /**
     * 根据图片的个数显示不同的结果
     */
    private void judgePictureCount() {

        if (bannerList != null) {
            if (bannerList.size() > 0) {
                bannerDefault.setVisibility(View.GONE);
                bannerView.setVisibility(View.VISIBLE);

                if (bannerList.size() == 1) {

                    indicator.setVisibility(View.GONE);

                    bannerView.pushImageCycle();

                } else {

                    indicator.setVisibility(View.VISIBLE);
                }


                bannerView.setImageResources(bannerList, mAdCycleViewListener);

            } else {

                if (bannerView != null) {

                    bannerView.setVisibility(View.GONE);

                    bannerView.pushImageCycle();
                }

                bannerDefault.setVisibility(View.VISIBLE);
            }
        } else {

            bannerView.setVisibility(View.GONE);

            bannerView.pushImageCycle();

            bannerDefault.setVisibility(View.VISIBLE);
        }

    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            if (imageURL != null) {
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) imageView;

                simpleDraweeView.setImageURI(Uri.parse(imageURL));
            }
        }

        @Override
        public void onImageClick(String url, int postion, View imageView) {

        }
    };
}
