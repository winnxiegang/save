package com.android.xjq.bean.draw;

import com.android.banana.commlib.utils.Money;
import com.android.xjq.model.gift.PayType;

import java.util.HashMap;

/**
 * Created by lingjiu on 2018/3/7.
 */
public class LuckyDrawParticipateConditionSimpleBean {
    /**
     * currencyTypeAndPrice : {"GIFT_COIN":1,"POINT_COIN":0.01}
     * defaultCurrencyType : GIFT_COIN
     * id : 4000626974829300000980054420
     * linkObjectId : 40103
     * linkObjectType : GIFT
     * perCount : 1
     * perPrice : 1.0
     * supportSide : NEUTRAL
     */

    public HashMap<String, Double> currencyTypeAndPrice;
    public String defaultCurrencyType;
    public String id;
    public String linkObjectId;
    public String linkObjectType;
    public int perCount;
    public double perPrice;
    public String supportSide;

    public String getRocketPerPay(){
        Double value = currencyTypeAndPrice.get(defaultCurrencyType);

        if(PayType.GIFT_COIN.name().equals(defaultCurrencyType)){

            return  new Money(value).toSimpleString()+PayType.GIFT_COIN.getMessage();

        }else if(PayType.GOLD_COIN.name().equals(defaultCurrencyType)){

            return  new Money(value).toSimpleString()+PayType.GOLD_COIN.getMessage();

        }else{

            return new Money(value).toSimpleString()+PayType.POINT_COIN.getMessage();
        }
    }

}
