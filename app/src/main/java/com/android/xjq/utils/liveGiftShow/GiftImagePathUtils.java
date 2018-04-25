package com.android.xjq.utils.liveGiftShow;

/**
 * Created by zhouyi on 2017/5/15.
 */

public class GiftImagePathUtils {

    //获取assert文件夹下礼物的父目录
    public static String getAssertGiftParent(String giftName, int giftVersion) {
        return "GIFT/" + giftName + "_V" + giftVersion;
    }

    //获取assert文件夹下礼物的父目录(模板礼物)
    public static String getAssertGiftModelParent(String resourceName) {
        return "GIFT/" + resourceName;
    }

}
