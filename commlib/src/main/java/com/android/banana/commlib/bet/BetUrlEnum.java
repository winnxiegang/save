package com.android.banana.commlib.bet;

import com.android.banana.commlib.http.AppParam;
import com.android.httprequestlib.BaseRequestHttpName;

/**
 * Created by lingjiu on 2017/11/6.
 */

public enum BetUrlEnum implements BaseRequestHttpName {


    GIFT_QUERY("GIFT_QUERY",AppParam.S_URL),

    PURCHASE_BASKETBALL_NORMAL("PURCHASE_BASKETBALL_NORMAL",AppParam.S_URL),

    PURCHASE_FOOTBALL_NORMAL("PURCHASE_FOOTBALL_NORMAL",AppParam.S_URL);

    private String name;

    private String url;

    BetUrlEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}