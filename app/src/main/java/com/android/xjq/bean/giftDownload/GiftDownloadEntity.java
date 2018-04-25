package com.android.xjq.bean.giftDownload;

import java.util.HashMap;

/**
 * Created by lingjiu on 2017/12/4.
 */

public class GiftDownloadEntity {

    private HashMap<String, GiftDownloadBean> giftConfigCodeAndResourceUrlMap;

    private HashMap<String, GiftDownloadBean> modelResourceTypeAndResourceUrlMap;

    public HashMap<String, GiftDownloadBean> getModelResourceTypeAndResourceUrlMap() {
        return modelResourceTypeAndResourceUrlMap;
    }

    public void setModelResourceTypeAndResourceUrlMap(HashMap<String, GiftDownloadBean> modelResourceTypeAndResourceUrlMap) {
        this.modelResourceTypeAndResourceUrlMap = modelResourceTypeAndResourceUrlMap;
    }

    public HashMap<String, GiftDownloadBean> getGiftConfigCodeAndResourceUrlMap() {
        return giftConfigCodeAndResourceUrlMap;
    }

    public void setGiftConfigCodeAndResourceUrlMap(HashMap<String, GiftDownloadBean> giftConfigCodeAndResourceUrlMap) {
        this.giftConfigCodeAndResourceUrlMap = giftConfigCodeAndResourceUrlMap;
    }
}
