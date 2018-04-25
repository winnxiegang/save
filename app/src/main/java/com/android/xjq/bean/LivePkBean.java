package com.android.xjq.bean;

import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;

import java.util.List;

/**
 * Created by ajiao on 2018\3\16 0016.
 */

public class LivePkBean {

    /**
     * jumpLogin : false
     * multipleList : [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199]
     * nowDate : 2018-03-16 10:30:32
     * pkGameBoard : {"gmtGamePause":"2018-03-16 12:22:11","gmtGameStart":"2018-03-16 10:22:14","id":"4000703573432107890000006570","lotteryStatus":{"message":"初始","name":"INIT"},"maxOptionCount":1,"minOptionCount":1,"optionOneEntry":{"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40109&timestamp=1513062884000","betFormName":"柏林赫塔","betFormNo":"40109","betFormSingleFee":100,"boardId":"4000703573432107890000006570","currentUserTotalMultiple":0,"id":"4000703573470107890000009661","optionCode":"OPTION_ONE","optionName":"马云","rankUserList":[],"totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0},"optionTwoEntry":{"betFormImageUrl":"http://mapi1.xjq.net/giftImageUrl.htm?giftConfigId=40111&timestamp=1513063236000","betFormName":"贝内文托","betFormNo":"40111","betFormSingleFee":100,"boardId":"4000703573432107890000006570","currentUserTotalMultiple":0,"id":"4000703573470407890000001484","optionCode":"OPTION_TWO","optionName":"马化腾","rankUserList":[],"totalFee":0,"totalMultiple":0,"totalPaidFee":0,"totalPlayUserCount":0},"playType":{"message":"PK","name":"PK"},"prizeStatus":"WAIT","saleStatus":{"message":"进行中","name":"PROGRESSING"},"sourceId":"100266","sourceType":{"message":"直播","name":"LIVE"},"title":"马化腾马云谁钱多？"}
     * success : true
     */

    public boolean jumpLogin;
    public String nowDate;
    public PkGameBoarInfoBean pkGameBoard;
    public boolean success;
    public List<Integer> multipleList;

}
