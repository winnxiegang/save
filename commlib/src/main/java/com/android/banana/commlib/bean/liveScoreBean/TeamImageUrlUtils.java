package com.android.banana.commlib.bean.liveScoreBean;

import com.android.banana.commlib.http.AppParam;

/**
 * Created by lingjiu on 2017/12/18.
 */

public class TeamImageUrlUtils {

    public static String getFTGuestLogoUrl(long teamId) {
        String url = AppParam.FT_IMAGE_DOWNLOAD_URL.substring(0, AppParam.FT_IMAGE_DOWNLOAD_URL.indexOf("?"));
        String guestLogoUrl = String.format(url, teamId) + "?type=g";
        return guestLogoUrl;
    }


    public static String getFTHomeLogoUrl(long teamId) {
        String url = AppParam.FT_IMAGE_DOWNLOAD_URL.substring(0, AppParam.FT_IMAGE_DOWNLOAD_URL.indexOf("?"));
        String homeLogoUrl = String.format(url, teamId) + "?type=h";
        return homeLogoUrl;
    }

    public static String getBTGuestLogoUrl(long teamId) {
        String url = AppParam.BT_IMAGE_DOWNLOAD_URL.substring(0, AppParam.BT_IMAGE_DOWNLOAD_URL.indexOf("?"));
        String guestLogoUrl = String.format(url, teamId) + "?type=g";
        return guestLogoUrl;
    }


    public static String getBTHomeLogoUrl(long teamId) {
        String url = AppParam.BT_IMAGE_DOWNLOAD_URL.substring(0, AppParam.BT_IMAGE_DOWNLOAD_URL.indexOf("?"));
        String homeLogoUrl = String.format(url, teamId) + "?type=h";
        return homeLogoUrl;
    }

    /**
     * 适用于不区分主客队的情况
     * @param teamId
     * @return
     */
    public static String getBTLogoUrl(long teamId) {
        String homeLogoUrl = String.format(AppParam.BT_IMAGE_DOWNLOAD_URL, teamId);
        return homeLogoUrl;
    }


}
