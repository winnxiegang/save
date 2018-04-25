package com.android.xjq.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bean.LtTypeData;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.residemenu.lt_lib.enumdata.core.LotteryTypeEnum;
import com.android.xjq.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by zaozao on 2017/12/1.
 */

public class XjqUtils {


    public static int getLtLogoResId(String name) {

        int resId = 0;

        switch (LotteryTypeEnum.valueOf(name)) {
            case JCLQ:
                resId = R.drawable.icon_jclq_no_bg;
                break;
            case JCZQ:
                resId = R.drawable.icon_jczq_no_bg;
                break;
            case ZC:
            case ZUCAI:
                resId = R.drawable.icon_zc_no_bg;
                break;
        }
        return resId;
    }

    public static int getNewLtLogoResId(String name) {

        int resId = 0;

        switch (LotteryTypeEnum.valueOf(name)) {
            case JCLQ:
                resId = R.drawable.icon_order_detail_jclq;
                break;
            case JCZQ:
                resId = R.drawable.icon_order_detail_jczq;
                break;
            case ZC:
                resId = R.drawable.icon_order_detail_zc;
                break;
            case ZUCAI:
                resId = R.drawable.icon_order_detail_zc;
                break;
        }
        return resId;
    }

    public static List<LtTypeData> initTypeData(LotteryTypeEnum[] lotteryList) {

        if (lotteryList == null) {
            return null;
        }

        List<LtTypeData> dataList = new ArrayList<>();

        for (LotteryTypeEnum aLotteryList : lotteryList) {

            LtTypeData data = null;

            data = new LtTypeData();

            switch (aLotteryList) {
                case JCLQ:
                    data.setSelectedDrawable(new int[]{R.drawable.icon_lt_type_jclq_normal, R.drawable.icon_lt_type_jclq_selected});
                    break;
                case JCZQ:
                    data.setSelectedDrawable(new int[]{R.drawable.icon_lt_type_jczq_normal, R.drawable.icon_lt_type_jczq_selected});
                    break;
                case ZUCAI:
                    data.setSelectedDrawable(new int[]{R.drawable.icon_lt_type_zc_normal, R.drawable.icon_lt_type_zc_selected});
                    break;
            }

            data.setCode(aLotteryList.name());

            data.setName(aLotteryList.message());

            dataList.add(data);
        }

        return dataList;

    }

    public static int getLotteryIcon(NormalObject lotteryType) {
        if (lotteryType == null) {
            return 0;
        }
        if (lotteryType.getName().equals(LotteryTypeEnum.JCLQ.name())) {
            return R.drawable.icon_order_detail_jclq;
        } else if (lotteryType.getName().equals(LotteryTypeEnum.JCZQ.name())) {
            return R.drawable.icon_order_detail_jczq;
        } else if (lotteryType.getName().equals(LotteryTypeEnum.ZC.name())) {
            return R.drawable.icon_order_detail_zc;
        } else if (lotteryType.getName().equals(LotteryTypeEnum.ZUCAI.name())) {
            return R.drawable.icon_order_detail_zc;
        }
        return 0;
    }


    public static void loadUserLogo(Context context, ImageView iv, String url) {
        Picasso.with(context)
                .load(url)
                .centerCrop()
                .into(iv);
    }

    public static String formatReplyString(String str) {

        String replyStr = str.replace("<br>", "").replace("</p>", "").replace("<p>", "");

        return replyStr;

    }

    public static void showHttpError(Context context, JSONObject jo, boolean showTip) {

        ErrorBean bean = null;

        try {
            bean = new ErrorBean(jo);

            String name = bean.getError().getName();

            String tip = bean.getError().getMessage();

            if ("USER_LOGIN_EXPIRED".equals(name)) {
                LibAppUtil.showTip(context.getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);

            } else if ("USER_ID_REQUIRED".equals(name)) {
                LibAppUtil.showTip(context.getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
            } else {
                if (showTip) {
                    LibAppUtil.showTip(context.getApplicationContext(), tip, Toast.LENGTH_SHORT, -1);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String, String> getSpecialChar() {

        LinkedHashMap<String, String> map = new LinkedHashMap<>();

        map.put("&", "&amp;");

        map.put("<", "&lt;");

        map.put(">", "&gt;");

        map.put("\"", "&quot;");

        map.put("€", "&euro;");

        map.put("⊕", "&oplus;");

        return map;
    }

    public static String replaceSpecialChar(String content) {
        //替换特殊字符
        HashMap<String, String> specialChar = getSpecialChar();

        Set<String> keys = specialChar.keySet();

        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            if (content.contains(key)) {
                content = content.replace(key, specialChar.get(key));
            }
        }

        return content;
    }

    /**
     * 过滤回复的信息
     * @param message
     * @return
     */
    public static String filterReplyMeMessage(String message) {
        if (message.indexOf("：") != -1) {
            return message.substring(message.indexOf("：") + 1, message.length());
        }
        return message;

    }


    public static boolean operateOverloadingUrl(Activity activity, String url) {

        String str = null;

        try {
            str = URLDecoder.decode(url, "utf-8");
            if (str.lastIndexOf("/") != str.length() - 1) {
                str = str + "/";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (str.toLowerCase().contains("purchaseno=")) {

            str = str.toLowerCase();

            int index = str.indexOf("purchaseno=");

            int length = "purchaseno=".length();

            String purchaseNo = str.substring(index + length, str.length() - 1);

//            OrderDetailsActivity.startOrderDetailsActivity(activity, purchaseNo);

            return true;

        } else if (str.toLowerCase().contains("userid=")) {

            str = str.toLowerCase();

            int index = str.indexOf("userid=");

            int length = "userid=".length();

            String userId = "";

            userId = str.substring(index + length, str.length() - 1);

//            HomepageActivity.startHomePageActivity(activity, userId);

            return true;

        } else if (str.toLowerCase().contains("app_purchase_no=")) {

            str = str.toLowerCase();

            int index = str.indexOf("app_purchase_no=");

            int length = "app_purchase_no=".length();

            String purchaseNo = str.substring(index + length, str.length() - 1);

            try {
                String no = purchaseNo.split(":")[0];
                String createTypeName = purchaseNo.split(":")[1];
                String createTypeMessage = purchaseNo.split(":")[2];

                NormalObject createType = new NormalObject();
                createType.setName(createTypeName.toUpperCase());
                createType.setMessage(createTypeMessage);

//                if (ToCaiZhanUitls.is91CaiZhanPlan(activity, createType)) {
//                    ToCaiZhanUitls.toCopyPurchaseActivity(activity, LoginHelper.getUserNickname(activity), LoginHelper.getRoleId(activity), no, createType);
//                }
            } catch (Exception e) {

            }

            return true;
        } else {
            str = str.substring(0, str.length() - 1);
//            ToAdvertisementByWebActivity.startToAdvertisementByWebActivity(activity, str, "");
        }

        LogUtils.e("url", "------------------" + str);
        return true;
    }

    private static void show17Dialog(final Activity activity) {

        ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(activity, "本方案源自17联盟，暂不支持该方案的复制，请到网站上操作");

    }

    public static void setRedPoint(TextView tv, int count) {
        if (count == 0) {
            tv.setVisibility(View.INVISIBLE);
            return;
        }
        tv.setVisibility(View.VISIBLE);

        if (count <= 9) {
            tv.setText(String.valueOf(count));
            tv.requestLayout();
            tv.setBackgroundResource(R.drawable.shape_message_point_oval);
        } else {
            if (count > 99) {
                tv.setText("99+");
            } else {
                tv.setText(String.valueOf(count));
            }
            tv.requestLayout();
            tv.setBackgroundResource(R.drawable.shape_message_point_radius_rect);
        }
    }

    public static String fullToHalf(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) //全角空格
            {
                c[i] = (char) 32;
                continue;
            }

            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String halfToFull(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 32) //半角空格
            {
                c[i] = (char) 12288;
                continue;
            }

            //根据实际情况，过滤不需要转换的符号
            //if (c[i] == 46) //半角点号，不转换
            // continue;

            if (c[i] > 32 && c[i] < 127)    //其他符号都转换为全角
                c[i] = (char) (c[i] + 65248);
        }
        return new String(c);
    }

    public static Drawable getImageSpanDrawable(TextView tv, int resId) {

        Drawable d = tv.getResources().getDrawable(resId);

        d.setBounds(0, 0, (int) tv.getTextSize(), (int) tv.getTextSize());

        return d;
    }

}
