package com.android.xjq.controller.live;

import android.content.res.AssetManager;
import android.view.View;

import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.DoubleHitShowBean;
import com.android.xjq.bean.live.GiftViewShowBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.main.gift.GiftShowConfigBean;
import com.android.xjq.utils.live.DoubleHitUtils;
import com.android.xjq.utils.liveGiftShow.GiftImagePathUtils;
import com.android.xjq.view.LandscapeGiftShow;
import com.android.xjq.view.PathAnimView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import git.dzc.downloadmanagerlib.download.service.DownloadHelper;

/**
 * Created by zhouyi on 2017/3/16.
 */

public class GiftController extends BaseLiveController<LiveActivity> {

    private LandscapeGiftShow mGiftShow;

    private PathAnimView pathAnimView;

    private DoubleHitUtils doubleHitUtils;

    private DownloadHelper downloadHelper;

    public GiftController(LiveActivity context) {

        super(context);
    }

    @Override
    public void init(View view) {
        if (mGiftShow == null) {
            mGiftShow = (LandscapeGiftShow) view;
            doubleHitUtils = new DoubleHitUtils(mGiftShow);
            downloadHelper = new DownloadHelper(XjqApplication.getContext());
        }

    }

    public void changeOrientation(boolean portrait) {
        mGiftShow.changeOrientation(portrait);
    }

    //礼物赠送成功效果
    public void addSendGiftSuccessShow(String imgUrl, int number) {
        //mGiftShow.showSendGiftSuccessAnim(imgUrl, number);
        try {
            if (pathAnimView == null) {
                pathAnimView = ((PathAnimView) mGiftShow.findViewById(R.id.giftLayout));
            }
            pathAnimView.sendGiftSuccess(imgUrl, number);
        } catch (Exception e) {
            LogUtils.e(TAG, "e=" + e.toString());
        }

    }

    public void addImperialEdict(LiveCommentBean liveCommentBean) {
        mGiftShow.showImperialEdict(liveCommentBean);
    }

    //触发连击效果
    public void addDoubleHit(LiveCommentBean bean) {
        DoubleHitShowBean doubleHitShowBean = new DoubleHitShowBean();
        doubleHitShowBean.setAllDoubleHitCount(Integer.parseInt(bean.getContent().getBody().getDoubleHit()));
        doubleHitShowBean.setGiftName(bean.getContent().getBody().getGiftConfigName());
        doubleHitShowBean.setTotalGiftCount(Integer.parseInt(bean.getContent().getBody().getTotalCount()));
        doubleHitShowBean.setAddTime(System.currentTimeMillis());
        doubleHitShowBean.setId(bean.getContent().getBody().getDoubleHitGroupNo());
        doubleHitShowBean.setLiveCommentBean(bean);
        createDoubleShowImageUrl(doubleHitShowBean);
        parseDoubleHitTextColor(doubleHitShowBean, bean);
        doubleHitUtils.addData(doubleHitShowBean);
    }

    //触发频道效果
    public void addChannelGiftShow(LiveCommentBean bean) {
        GiftViewShowBean giftViewShowBean = new GiftViewShowBean();
        giftViewShowBean.setSendName(bean.getSenderName());
        giftViewShowBean.setGiftName(bean.getContent().getBody().getGiftConfigName());
        giftViewShowBean.setTotalGiftCount(Integer.parseInt(bean.getContent().getBody().getTotalCount()));
        ArrayList<String> giftImageUrl = createGiftShowImageUrl(giftViewShowBean, bean);
        giftViewShowBean.setLiveCommentBean(bean);
        if (giftImageUrl.size() > 0) {
            giftViewShowBean.setImageUrl(giftImageUrl);
        }
        parseGiftBannerTextColor(giftViewShowBean, bean);
        mGiftShow.showGift(giftViewShowBean);
    }

    //触发全平台效果
    public void addPlatformGiftShow(LiveCommentBean bean) {
        GiftViewShowBean giftViewShowBean = new GiftViewShowBean();
        giftViewShowBean.setReceiveName(bean.getContent().getBody().getReceiveUserName());
        giftViewShowBean.setRoomNumber(bean.getContent().getBody().getPlatformObjectId());
        giftViewShowBean.setSendName(bean.getSenderName());
        giftViewShowBean.setGiftName(bean.getContent().getBody().getGiftConfigName());
        giftViewShowBean.setTotalGiftCount(Integer.parseInt(bean.getContent().getBody().getTotalCount()));
        giftViewShowBean.setLiveCommentBean(bean);
        ArrayList<String> giftImageUrl = createGiftShowImageUrl(giftViewShowBean, bean);
        if (giftImageUrl.size() > 0) {
            giftViewShowBean.setImageUrl(giftImageUrl);
        }
        parseGiftBannerTextColor(giftViewShowBean, bean);
        mGiftShow.showGift(giftViewShowBean);
    }

    private void createDoubleShowImageUrl(DoubleHitShowBean doubleHitShowBean) {
        try {
            LiveCommentBean.ContentBean.BodyBean body = doubleHitShowBean.getLiveCommentBean().getContent().getBody();
            if (body.isUseModelResource()) {
                String resourceConfigList = body.getModelResourceConfigList();
                JSONArray jsonArray = new JSONArray(resourceConfigList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String resourceName = jsonObject.getString("modelResourceType");
                    String parentPath = downloadHelper.getFile(resourceName);
                    String imageUrl;
                    if (parentPath != null) {
                        imageUrl = parentPath + "/" + resourceName + "_0000.webp";
                        doubleHitShowBean.setImageUrl(imageUrl);
                        doubleHitShowBean.setImageFromAssert(false);
                    } else {
                        //没下载下来,默认取第一个模板
                        imageUrl = "GIFT/MODEL_ONE/MODEL_ONE_0000.webp";
                        doubleHitShowBean.setImageUrl(imageUrl);
                    }
                    LogUtils.e(TAG, "imageUrl=" + imageUrl);
                    break;
                }
            } else {
                String resourceConfigList = body.getResourceConfigList();
                JSONArray jsonArray = new JSONArray(resourceConfigList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String clientType = jsonObject.getString("clientType");
                    if (!"ANDROID".equals(clientType)) continue;
                    JSONObject resourceType = jsonObject.getJSONObject("resourceType");
                    if ("PNG".equals(resourceType.getString("name"))) {
                        String giftConfigId = doubleHitShowBean.getLiveCommentBean().getContent().getBody().getGiftConfigCode();
                        int resourceVersion = jsonObject.getInt("resourceVersion");
                        String parentPath = downloadHelper.getFile(giftConfigId + "_V" + resourceVersion);
                        //LogUtils.e(TAG, "parentPath=" + parentPath + "  giftConfigId=" + giftConfigId);
                        String imageUrl;
                        if (parentPath != null) {
                            imageUrl = parentPath + "/" + giftConfigId + "_0000.webp";
                            doubleHitShowBean.setImageUrl(imageUrl);
                            doubleHitShowBean.setImageFromAssert(false);
                        } else {
                            imageUrl = GiftImagePathUtils.getAssertGiftParent(giftConfigId, resourceVersion)
                                    + "/" + giftConfigId + "_0000.webp";
                            doubleHitShowBean.setImageUrl(imageUrl);
                        }
                        LogUtils.e(TAG, "imageUrl=" + imageUrl);
                        break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void parseGiftBannerTextColor(GiftViewShowBean giftViewShowBean, LiveCommentBean bean) {
        try {
            LiveCommentBean.ContentBean.BodyBean body = bean.getContent().getBody();
            JSONArray ja;
            if (body.isUseModelResource()) {
                String modelRsPropertyList = body.getModelRsPropertyList();
                ja = new JSONArray(modelRsPropertyList);
            } else {
                String giftEffectPropertyList = bean.getContent().getBody().getGiftEffectPropertyList();
                ja = new JSONArray(giftEffectPropertyList);
            }

            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                String code = jsonObject.getString("propertyCode");
                String value = jsonObject.getString("propertyValue");
                if ("BANNER_SEND_USER_FONT_COLOR".equals(code)) {//BANNER_SEND_USER_FONT_COLOR  SEND_USER_FONT_COLOR
                    giftViewShowBean.setTextColor(value);
                } else if ("BANNER_PLATFORM_OBJECT_ID_FONT_COLOR".equals(code)) {//BANNER_PLATFORM_OBJECT_ID_FONT_COLOR  PLATFORM_OBJECT_ID_FONT_COLOR
                    giftViewShowBean.setRoomNumberColor(value);
                } else if ("BANNER_COUNT_FONT_COLOR".equals(code)) {//BANNER_COUNT_FONT_COLOR  COUNT_FONT_COLOR
                    giftViewShowBean.setGiftNumberColor(value);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void parseDoubleHitTextColor(DoubleHitShowBean giftViewShowBean, LiveCommentBean bean) {
        try {
            LiveCommentBean.ContentBean.BodyBean body = bean.getContent().getBody();
            JSONArray ja;
            if (body.isUseModelResource()) {
                String modelRsPropertyList = body.getModelRsPropertyList();
                ja = new JSONArray(modelRsPropertyList);
            } else {
                String giftEffectPropertyList = body.getGiftEffectPropertyList();
                ja = new JSONArray(giftEffectPropertyList);
            }
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject = ja.getJSONObject(i);
                String code = jsonObject.getString("propertyCode");
                String value = jsonObject.getString("propertyValue");
                if ("SIDE_SEND_USER_FONT_COLOR".equals(code)) {//SIDE_SEND_USER_FONT_COLOR  SEND_USER_FONT_COLOR
                    giftViewShowBean.setGiftNormalColor(value);
                } else if ("SIDE_COUNT_FONT_COLOR".equals(code)) {//SIDE_COUNT_FONT_COLOR  COUNT_FONT_COLOR
                    giftViewShowBean.setGiftCountColor(value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> createGiftShowImageUrl(GiftViewShowBean giftViewShowBean, LiveCommentBean bean) {
        ArrayList<String> imageList = new ArrayList<>();
        try {
            LiveCommentBean.ContentBean.BodyBean body = bean.getContent().getBody();
            if (body.isUseModelResource()) {
                String resourceConfigList = body.getModelResourceConfigList();
                JSONArray jsonArray = new JSONArray(resourceConfigList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String resourceName = jsonObject.getString("modelResourceType");
                    String parentPath = downloadHelper.getFile(resourceName);
                    String imageUrl;
                    if (parentPath != null) {
                        imageUrl = parentPath + "/" + resourceName + "_0000.webp";
                        giftViewShowBean.setImageFromAsserts(false);
                    } else {
                        imageUrl = "GIFT/MODEL_ONE/MODEL_ONE_0001.webp";
                    }
                    imageList.add(imageUrl);
                    LogUtils.e(TAG, "imageUrl=" + imageUrl);
                    break;
                }
            } else {
                String resourceConfigList = bean.getContent().getBody().getResourceConfigList();
                JSONArray jsonArray = new JSONArray(resourceConfigList);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String clientType = jsonObject.getString("clientType");
                    if (!"ANDROID".equals(clientType)) continue;
                    JSONObject resourceType = jsonObject.getJSONObject("resourceType");
                    if ("PNG".equals(resourceType.getString("name"))) {

                        String giftConfigId = bean.getContent().getBody().getGiftConfigCode();
                        int resourceVersion = jsonObject.getInt("resourceVersion");

                        String file = downloadHelper.getFile(giftConfigId + "_V" + resourceVersion);
                        if (file != null) {
                            File[] files = new File(file).listFiles();
                            for (int j = 1; j < files.length; j++) {
                                if (files[j].getName().contains("0000")) {
                                    continue;
                                }
                                String imageUrl = files[j].getPath();
                                imageList.add(imageUrl);
                            }
                            giftViewShowBean.setImageFromAsserts(false);
                        } else {
                            AssetManager assets = context.getAssets();
                            String assertParentPath = GiftImagePathUtils.getAssertGiftParent(giftConfigId, resourceVersion);
                            String[] assertList = assets.list(assertParentPath);
                            if (assertList != null) {
                                for (int j = 1; j < assertList.length; j++) {
                                    if (assertList[j].contains("0000")) {
                                        continue;
                                    }
                                    String imageUrl = assertParentPath + "/" + assertList[j];
                                    imageList.add(imageUrl);
                                }
                            }
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public void setGiftConfig(GiftShowConfigBean giftShowConfigBean) {
        doubleHitUtils.setGiftShowConfig(giftShowConfigBean);
    }

    public void showDoubleHitShow() {
        mGiftShow.showDoubleHitShow();
    }

    public void showKeyBoard(boolean showKeyBoard) {
        mGiftShow.showKeyBoard(showKeyBoard);
    }

    public void hideDoubleHitShow() {
        mGiftShow.hideDoubleHitShow();
    }


    @Override
    void setView() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
