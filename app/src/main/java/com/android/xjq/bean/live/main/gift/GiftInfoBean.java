package com.android.xjq.bean.live.main.gift;

import com.android.xjq.bean.live.BaseOperator;
import com.google.gson.annotations.Expose;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lingjiu on 2017/3/8.
 */

public class GiftInfoBean implements BaseOperator {


    private String name;

    private double price;

    private int id;

    //判断礼物是否是爆奖池礼物
    private HashMap<String, String> properties;

    private String code;

    //礼物标签(爆奖池礼物或者周星礼物等)
    @Expose
    private String giftFlag;

    /**
     * 静态图
     */
    private String giftImageUrl;

    /**
     * 动态图
     */
    private String dynamicUrl;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDynamicUrl() {
        return dynamicUrl;
    }

    public void setDynamicUrl(String dynamicUrl) {
        this.dynamicUrl = dynamicUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getGiftImageUrl() {
        return giftImageUrl;
    }

    public void setGiftImageUrl(String giftImageUrl) {
        this.giftImageUrl = giftImageUrl;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    public void setProperties(HashMap<String, String> properties) {
        this.properties = properties;
    }


    public String getGiftFlag() {
        if (properties != null && properties.size() > 0) {
            if (properties.containsKey("PRIZE_POOL") && Boolean.valueOf(properties.get("PRIZE_POOL"))) {
                giftFlag = "PRIZE_POOL";
            }
            if (properties.containsKey("WEEK") && Boolean.valueOf(properties.get("WEEK"))) {
                giftFlag = "WEEK";
            }
        }
        return giftFlag;
    }

    public void setPrizeGift(String prizeGift) {
        giftFlag = prizeGift;
    }

    @Override
    public void operatorData() {

    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GiftInfoBean) {
            GiftInfoBean giftInfoBean = (GiftInfoBean) obj;
            return id == giftInfoBean.getId();
        }
        return super.equals(obj);
    }
}
