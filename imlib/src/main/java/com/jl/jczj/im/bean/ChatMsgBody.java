package com.jl.jczj.im.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.view.CenterVerticalImageSpan;
import com.google.gson.annotations.Expose;
import com.jl.jczj.im.MessageType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrs on 2017/4/7.
 * 比分直播聊天记录bean
 * <p>
 * <p>
 * result from immapi:bug
 */

public class ChatMsgBody implements Serializable {

    public ChatMsgBody(String groupId, String content, String messageType, String sendUserName, String sendUserId, String roleCode) {
        this(groupId, content, messageType, sendUserName, sendUserId, null, roleCode);
    }


    //红包类型的可直接使用
    public ChatMsgBody(String groupId, String content, String messageType, String sendUserName, String sendUserId, String groupCouponId, String roleCode) {
        ChatMsgBody.Body body = new ChatMsgBody.Body();

        this.typeCode = messageType;
        this.sendUserId = sendUserId;
        this.sendUserLoginName = sendUserName;
        this.gmtCreate = TimeUtils.getCurrentTime();
        this.groupId = groupId;

        Body.Parameters parameters = new Body.Parameters();
        parameters.groupCouponId = groupCouponId;
        parameters.sendUserName = sendUserName;
        parameters.sendUserId = sendUserId;

        body.parameters = parameters;

        properties = new Properties();
        body.properties = properties;

        if (TextUtils.equals(messageType, MessageType.NORMAL)) {
            body.content = content;
        } else if (TextUtils.equals(messageType, MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT)) {
            parameters.title = content;
        } else if (TextUtils.equals(messageType, MessageType.IMAGE_VIEWABLE)) {
            parameters.url = "file://" + content;
        }

        bodies = new ArrayList<>();
        bodies.add(body);
    }

    /**
     * 组ID
     */
    public String groupId;

    /**
     * 发送会员ID
     */
    public String sendUserId;

    public String sendUserLoginName;

    public String sendUserNickName;

    public String sendUserLogoUrl;
    /**
     * 消息ID
     */
    public String id;

    /**
     * 消息类型ID
     */
    public long typeId;

    /**
     * 消息类型代码
     */
    public String typeCode;

    /**
     * 系统删除
     */
    public boolean systemDeleted;

    /**
     * 唯一ID
     */
    public String uniqueId;

    /**
     * 消息描述
     */
    public String description;

    /**
     * 创建时间
     */
    public String gmtCreate;

    /**
     * 修改时间
     */
    public String gmtModified;

    /**
     * 消息序号
     */
    public long messageSequence;


    public Properties properties;

    /**
     * 消息体集合
     */
    public List<Body> bodies;

    //群聊名称
    public String groupName;

    //"GROUP"
    public String groupTypeCode;

    //是否是系统消息
    private boolean systemMessage;


    /*人为添加的 群聊角色code*/
    @Expose
    private String roleCode;
    //私聊才会有这个三个字段
    private String soleId;
    private String targetUserId;
    private String targetUserName;

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setSoleId(String soleId) {
        this.soleId = soleId;
    }

    public String getSoleId() {
        return soleId;
    }

    public String getTargetUserId() {
        return targetUserId;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserIdName(String targetUserId, String targetUserName) {
        this.targetUserId = targetUserId;
        this.targetUserName = targetUserName;
    }

    public static class Body implements Serializable {

        /**
         * id : 1705051438060461980039064392
         * bodySequence : 0
         * content : 11111
         * bold : false
         * italic : false
         * properties : {}
         * <p>
         * "imageId": "2976464325508690980053825699",
         * "byteLength": 96798,
         * "extension": "jpg"
         */
        private String messageId;
        public String id;
        public int bodySequence;
        public String content;
        public boolean bold;
        public boolean italic;
        public Parameters parameters;
        public Properties properties;
        public String imageUrl;

        /*图片类型专属字段*/
        public String imageId;
        public long byteLength;
        public String extension;

        @Expose
        private CharSequence emojiContent;


        public static class Parameters implements Serializable {
            /**
             * amount : 2.00
             * receiveUserId : 8201610254805874
             * sendUserName : 糯米团子
             * receiveUserName : 萌萌哒
             * sendUserId : 8201607074315768
             * messageType : coupon
             * groupCouponId:2017
             * title:""
             * couponTypeCode": "LUCKY_GROUP_COUPON"
             * <p>
             * "imageId": "2976464325508690980053825699",
             * "imageBodyId": "2976464323604610980058984202",
             * "imageMessageId": "2976464321104610980057837395",
             * "url": "http://test-xjq-resource.cdn.configsvr.com/IMCORE/RESOURCE/GROUP_MESSAGE/IMAGE/2976464323604610980058984202_atv_0"
             */

           /*红包 系统通知，打赏专属字段*/
            public String amount;
            public String receiveUserId;
            public String sendUserName;
            public String receiveUserName;
            public String sendUserId;
            public String messageType;
            public String groupCouponId;
            public String content;
            public String title;
            public String couponTypeCode;


            /*图片专属字段*/
            public String imageId;
            public String imageBodyId;
            public String imageMessageId;
            public String url;


            /*转发类型专属字段*/
            //动态转发类型字段
            public String sourceMemo;
            public String summary;//: "//@小和尚:<emotion image_name="common/64APP/shengqi.png" image_alt="angry">angry</emotion><emotion image_name="common/64APP/dalian.png" image_alt="dl">dl</emotion>",
            //public String title;//: "",
            public String sourceTitle;//: "",
            public String memo;//: "",
            public String sourceUserName;// "小和尚",
            public String subjectId;//: "15702901",
            public String userId;//: "8201711278725840",
            public String sourceUserId;//: "8201711278725821",
            public String userName;//: "我是追光少年",
            public String sourceSummary;//: "<bet_article_race race_type="FOOTBALL" race_id="4000558938125107890980017159">[卡后备 赛利亚U23VS恩沙巴U23]</bet_article_race>qweqwe224rwet45ry56u756556566<emotion image_name="normal/deyi.gif" image_alt="得意">得意</emotion>@qweqwe224rwet45ry56u756556566@qweqwe224rwet45ry56u756556566@qweqwe224rwet45ry56u756556566@qweqwe224rwet45ry56u756556566@qweqwe224rwet45ry56u75655656",
            public String objectType;//: "TRANSMIT_SUBJECT",
            public String sourceSubjectId;//: "15702898"
            public String imageUrl;
            public String subjectType;

            //转发直播间PK类型
            public String optionTwo_GiftUrl;//: "http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=40111&timestamp=1513063236000",
            public String optionOne_OptionName;//: "能",
            public String sourceType;//: "LIVE",
            //public String title;//: "这次创建能否一发入魂？",
            public String optionOne_UserCount;//: "0",
            public String pkGameBoardId;//: "4000573883354207890000009115",
            public String optionOne_GiftUrl;//: "http://mapi.xjq.net/giftImageUrl.htm?giftConfigId=40109&timestamp=1513062884000",
            public String optionTwo_UserCount;//: "0",
            public String optionTwo_OptionName;//: "不能",
            public String sourceId;//: "8201711028675848"

            //直播间极限手速转发字段
            // public String content;//: "测试转发极限手速",
            public String prizeItemName;//: "0909",
            public String patronUserlogoUrl;//: "http://mapi.xjq.net/userLogoUrl.htm?userId=8201712058726021&timestamp=1520670379445",
            public String patronUserName;//: "Mickey",
            public String prizeTotalCount;//: "2",
            public String patronMedalInfos;//: "[{"userId":"","medalConfigCode":"GLOBAL_GRADE_MEDAL","medalType":{"message":"等级勋章","name":"GRADE_MEDAL"},"currentMedalLevelConfigCode":"Lv7"},{"userId":"","medalConfigCode":"GAME_MEDAL","medalType":{"message":"领域勋章","name":"FIELD_MEDAL"},"currentMedalLevelConfigCode":"Lv1"}]"
            public ArrayList<ChatMsgMedalBean> patronMedalList;
            public String giftUrl;//打赏礼物url
            public String platformObjectId;//: "100252",
            public String platformObjectType;//: "CHANNEL_AREA"
        }

    }

    //打赏
    public static class Properties implements Serializable {
        /**
         * subjectId : 14892795
         * voteCode : HOME_FANS
         * medalcodes："MANAGER"
         * <p>
         * medalInfos: "[{"labelInfoList":[],"medalConfigCode":"GROUP_VIP_MEDAL","medalLevelConfigCode":"Lv1","ownerId":"","ownerName":""}]"
         */
        public String subjectId;
        public String voteCode;
        public String medalcodes;//角色

        public String rewardAmount;
        public String receiveUserLoginName;

        //群聊消息战袍标签
        private String medalInfos;
        // public List<ChatMsgMedalBean> medalInfos;
        public List<ChatMsgMedalBean> medealInfoList;

        public List<ChatMsgMedalBean> getChatMedalList() {

            return medealInfoList;
        }

    }

    //手动解析 战袍标签
    public void parseMedalInfos() {
        if (properties == null || TextUtils.isEmpty(properties.medalInfos))
            return;
        try {
            JSONArray jsonArray = new JSONArray(properties.medalInfos);
            int length = jsonArray.length();
            properties.medealInfoList = new ArrayList<>();
            for (int i = 0; i < length; i++) {
                JSONObject medalObject = jsonArray.getJSONObject(i);
                ChatMsgMedalBean medalBean = new ChatMsgMedalBean();
                if (medalObject.has("medalConfigCode"))
                    medalBean.medalConfigCode = medalObject.getString("medalConfigCode");
                if (medalObject.has("medalLevelConfigCode"))
                    medalBean.medalLevelConfigCode = medalObject.getString("medalLevelConfigCode");
                if (medalObject.has("ownerId"))
                    medalBean.ownerId = medalObject.getString("ownerId");

                JSONArray labelArray = medalObject.getJSONArray("labelInfoList");
                if (labelArray != null) {
                    int labelLen = labelArray.length();
                    for (int j = 0; j < labelLen; j++) {
                        JSONObject labelObject = labelArray.getJSONObject(j);
                        ChatMsgMedalBean.MedalLabelConfigListBean labelBean = new ChatMsgMedalBean.MedalLabelConfigListBean();
                        if (labelObject.has("content"))
                            labelBean.setContent(labelObject.getString("content"));

                        medalBean.labelInfoList.add(labelBean);
                    }
                }
                properties.medealInfoList.add(medalBean);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //手动解析 转发过来的消息带的动态勋章
    public void parsePatronMedalInfos() {
        Body body = bodies.get(0);
        if (body.parameters == null || TextUtils.isEmpty(body.parameters.patronMedalInfos))
            return;
        body.parameters.patronMedalList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(body.parameters.patronMedalInfos);
            int length = jsonArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject medalObject = jsonArray.getJSONObject(i);
                ChatMsgMedalBean medalBean = new ChatMsgMedalBean();
                if (medalObject.has("medalConfigCode"))
                    medalBean.medalConfigCode = medalObject.getString("medalConfigCode");
                if (medalObject.has("medalLevelConfigCode"))
                    medalBean.medalLevelConfigCode = medalObject.getString("medalLevelConfigCode");
                if (medalObject.has("userId"))
                    medalBean.userId = medalObject.getString("userId");
                if (medalObject.has("medalType")) {
                    JSONObject medalType = medalObject.getJSONObject("medalType");
                    medalBean.medalType = new NormalObject(medalType.optString("name"), medalType.optString("message"));
                }
                body.parameters.patronMedalList.add(medalBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //因为有表情  所以用这个方法只会解析一遍
    public CharSequence getEmojiContent(Context context) {
        if (bodies == null || bodies.size() <= 0)
            return "";
        Body body = bodies.get(0);
        if (!TextUtils.isEmpty(body.emojiContent))
            return body.emojiContent;

        CharSequence emojiContent = parseEmoji(context, com.android.banana.commlib.escapeUtils.StringEscapeUtils.unescapeHtml(body.content));
        body.emojiContent = emojiContent;
        return emojiContent;
    }

    CharSequence parseEmoji(Context context, CharSequence input) {
        if (TextUtils.isEmpty(input))
            return input;

        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*\\]");
        Matcher matcher = pattern.matcher(input);
        SpannableString sp = new SpannableString(input);
        String packageName = context.getPackageName();
        while (matcher.find()) {
            String group = matcher.group();
            int start = matcher.start();
            int end = matcher.end();
            group = group.substring(1, group.length() - 1);
            int resid = context.getResources().getIdentifier("emoj_" + group, "drawable", packageName);
            if (resid != 0) {
                Drawable drawable = ContextCompat.getDrawable(context, resid);
                if (drawable != null) {
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    sp.setSpan(new CenterVerticalImageSpan(drawable), start, end, ImageSpan.ALIGN_BASELINE);
                }
            }
        }
        return sp;

    }
}
