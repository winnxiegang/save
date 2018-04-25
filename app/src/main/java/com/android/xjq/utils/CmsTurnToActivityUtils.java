package com.android.xjq.utils;

import android.content.Context;

import java.util.HashMap;

/**
 * Created by qiaomu on 2017/11/29.
 */

public class CmsTurnToActivityUtils {

    public static void toTurn(Context context, HashMap<String,String> propMap, String url, String id){

        if (propMap!=null) {

            if (propMap.containsKey("subject")) {

                //SubjectDetailActivity.startSubjectDetailActivity(((Activity) context),propMap.get("subject"));

            } else if (propMap.containsKey("cms")) {

               // NewsDetailActivity.startNewsDetailActivity(((Activity) context),propMap.get("cms"));
            } else if (propMap.containsKey("project")) {

                //OrderDetailsActivity.startOrderDetailsActivity(((Activity) context),propMap.get("project"));
            }
        } else if (url!=null) {

            //ToAdvertisementByWebActivity.startToAdvertisementByWebActivity(((Activity) context),url,"");

        }else if(id!=null){

            //NewsDetailActivity.startNewsDetailActivity((Activity)context,id);
        }
        else{

            return;
        }
    }
}
