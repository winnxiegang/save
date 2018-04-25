package com.android.xjq.bean.comment;

import android.text.SpannableStringBuilder;

import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.banana.commlib.bean.NormalObject;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.groupchat.chatenum.DiscoveryEnum;
import com.android.xjq.bean.JcSnapshotBean;
import com.android.xjq.bean.order.PurchaseSnapshotBean;
import com.android.xjq.bean.race.RacePredictStatusEnum;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * 各个详情页评论文件
 * Created by zhouyi on 2016/1/8 10:56.
 */
public class UserCommentBean {

    private PaginatorBean paginator;

    private String nowDate;

    private List<CommentListBean> commentList;

    private HashMap<String, List<CommentReplyBean>> commentReplyMap;

    private HashMap<String, UserRacePredictBean> userRacePredictMap;

    private HashMap<String, String> userIdAndMoneyMap;

    private CommentObjectBean commentObject;

    private HashMap<String, PurchaseSnapshotBean> purchaseOrderMap;

    private HashMap<String, JcSnapshotBean> jczqDataMap;

    private HashMap<String, JcSnapshotBean> jclqDataMap;

    public HashMap<String, PurchaseSnapshotBean> getPurchaseOrderMap() {
        return purchaseOrderMap;
    }

    public void setPurchaseOrderMap(HashMap<String, PurchaseSnapshotBean> purchaseOrderMap) {
        this.purchaseOrderMap = purchaseOrderMap;
    }

    public HashMap<String, JcSnapshotBean> getJczqDataMap() {
        return jczqDataMap;
    }

    public void setJczqDataMap(HashMap<String, JcSnapshotBean> jczqDataMap) {
        this.jczqDataMap = jczqDataMap;
    }

    public HashMap<String, JcSnapshotBean> getJclqDataMap() {
        return jclqDataMap;
    }

    public void setJclqDataMap(HashMap<String, JcSnapshotBean> jclqDataMap) {
        this.jclqDataMap = jclqDataMap;
    }

    /**
     * 新增勋章信息
     */

    private HashMap<String, List<JczjMedalBean>> userMedalClientSimpleMap;

    public UserCommentBean(JSONObject jo) {

        UserCommentBean userCommentBean = new Gson().fromJson(jo.toString(), UserCommentBean.class);

        this.paginator = userCommentBean.getPaginator();

        this.commentList = userCommentBean.getCommentList();

        this.nowDate = userCommentBean.nowDate;

        this.commentReplyMap = userCommentBean.commentReplyMap;

        this.userRacePredictMap = userCommentBean.getUserRacePredictMap();

        this.userIdAndMoneyMap = userCommentBean.userIdAndMoneyMap;

        this.purchaseOrderMap = userCommentBean.purchaseOrderMap;

        this.jczqDataMap = userCommentBean.jczqDataMap;

        this.jclqDataMap = userCommentBean.jclqDataMap;

        this.commentObject = userCommentBean.commentObject;

        this.userMedalClientSimpleMap = userCommentBean.userMedalClientSimpleMap;

        for (int i = 0; i < commentList.size(); i++) {
            CommentListBean commentListBean = commentList.get(i);
            commentListBean.setGmtCreate(TimeUtils.formatTime(nowDate, commentListBean.getGmtCreate()));
            //二级评论
            if (commentReplyMap != null) {
                if (commentReplyMap.containsKey(commentListBean.getCommentId())) {
                    commentListBean.replyComment = commentReplyMap.get(commentListBean.getCommentId());
                    for (CommentReplyBean bean : commentListBean.replyComment) {
                        bean.setGmtCreate(TimeUtils.formatTime(nowDate, bean.getGmtCreate()));
                        //二级评论添加勋章
                        if (userMedalClientSimpleMap != null && userMedalClientSimpleMap.containsKey(bean.getUserId())) {
                            bean.setUserMedalBeanList(userMedalClientSimpleMap.get(bean.getUserId()));
                        }
                    }
                }
            }
            if (userIdAndMoneyMap != null) {
                if (userIdAndMoneyMap.get(commentListBean.getUserId()) != null)
                    commentListBean.setCouponAmount(new Money(userIdAndMoneyMap.get(commentListBean.getUserId())).toString());
            }
            //一级评论添加勋章
            if (userMedalClientSimpleMap != null && userMedalClientSimpleMap.containsKey(commentListBean.getUserId())) {
                commentListBean.setUserMedalBeanList(userMedalClientSimpleMap.get(commentListBean.getUserId()));
            }
        }

        if (userRacePredictMap != null) {

            for (int i = 0; i < commentList.size(); i++) {

                CommentListBean commentListBean = commentList.get(i);

                String commentId = commentListBean.getCommentId();

                if (userRacePredictMap.get(commentId) != null) {

                    UserRacePredictBean userRacePredictBean = userRacePredictMap.get(commentId);

                    if (userRacePredictBean != null) {

                        commentListBean.setPredictBean(userRacePredictMap.get(commentId));

                        commentListBean.setPredictMessage(formatPredictInfoMessage(userRacePredictBean));

                      commentListBean.setContent(getHtmlSpanMessage(commentListBean.getPredictBean().getPredictStatus().getName(), commentListBean.getPredictMessage()) + commentListBean.getContent());

//                        commentListBean.predictSpanMessage= getSpanMessage(userRacePredictBean.getPredictStatus(),commentListBean.getPredictMessage());

                    }

                }

            }
        }
        if(commentList!=null){

            for (int i = 0; i < commentList.size(); i++) {

                CommentListBean commentListBean = commentList.get(i);

                if(commentListBean.getProperties()!=null){

                    if ( purchaseOrderMap != null&&commentListBean.getProperties().getPurchaseNo() != null) {

                        PurchaseSnapshotBean purchaseSnapshotBean = purchaseOrderMap.get(commentListBean.getProperties().getPurchaseNo());

                        commentListBean.setPurchaseSnapshotBean(purchaseSnapshotBean);

                        commentListBean.setDiscoveryEnum(DiscoveryEnum.PURCHASE.name());

                    }if (commentListBean.getProperties().getJczqBizId() != null && jczqDataMap != null) {

                        JcSnapshotBean jcSnapshotBean = jczqDataMap.get(commentListBean.getProperties().getJczqBizId());

                        commentListBean.setJcSnapshotBean(jcSnapshotBean);

                        commentListBean.setDiscoveryEnum(DiscoveryEnum.JCZQ_MATCH.name());

                    }if (commentListBean.getProperties().getJclqBizId() != null && jclqDataMap != null) {

                        JcSnapshotBean jcSnapshotBean = jclqDataMap.get(commentListBean.getProperties().getJclqBizId());

                        commentListBean.setJcSnapshotBean(jcSnapshotBean);

                        commentListBean.setDiscoveryEnum(DiscoveryEnum.JCLQ_MATCH.name());

                    }

                }
            }
        }
    }

    public static String formatPredictInfoMessage(UserRacePredictBean userRacePredictBean) {

        String predictDetailMessage;

        List<NormalObject> options = userRacePredictBean.getOptions();

        boolean isRq = false; //用户预测是否让球

        String predictMessage = null;//预测信息

        int plate = userRacePredictBean.getRqPlate();//预测时的盘口

        for (int j = 0; j < options.size(); j++) {
            if (userRacePredictBean.getOptionSpMap() != null) {
                double sp = userRacePredictBean.getOptionSpMap().get(options.get(j).getName());
                userRacePredictBean.setSp(sp);
                if (options.get(j).getName().contains("RQSPF")) {
                    isRq = true;
                }
                predictMessage = options.get(j).getMessage();
            }
        }

        if (isRq) {
            if (predictMessage.contains("让")) {
                predictDetailMessage = "让[" + LibAppUtil.getFormatPlateMessage(plate) + "]" + predictMessage.substring(1, predictMessage.length()) + "(" + userRacePredictBean.getSp() + ")";
            } else {
                predictDetailMessage = "让[" + LibAppUtil.getFormatPlateMessage(plate) + "]" + predictMessage + "(" + userRacePredictBean.getSp() + ")";
            }
        } else {
            predictDetailMessage = predictMessage + "(" + userRacePredictBean.getSp() + ")";
        }

        return predictDetailMessage;
    }

    public static String getHtmlSpanMessage(String status, String predictMessage) {

        RacePredictStatusEnum statusEnum = RacePredictStatusEnum.valueOf(status);
        String message = "";
        switch (statusEnum) {
            case CORRECT:
                message = "  <span class=\"predict-wrap\" ><img src = \"file:///android_asset/p-correct.png\" ><i\n" +
                        "        class=\"i-bg i-correct\" ></i ><span\n" +
                        "        class=\"predict-correct\" ></i >" + predictMessage + " </span ></span >";
                break;
            case READY_CALCULATE:
                message = " <span class=\"predict-wrap\" ><img src = \"file:///android_asset/p-result.png\" ><i\n" +
                        "        class=\"i-bg i-result\" ></i ><span\n" +
                        "        class=\"predict-result\" ></i > " + predictMessage + " </span ></span >";
                break;
            case NOT_CORRECT:
                message = "<span class=\"predict-wrap\"><img src=\"file:///android_asset/p-none.png\"><i\n" +
                        "        class=\"i-bg i-none\"></i><span\n" +
                        "        class=\"predict-none\"></i>" + predictMessage + "</span></span>";
                break;
        }
        return message;
    }

    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public void setCommentList(List<CommentListBean> commentList) {
        this.commentList = commentList;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public String getNowDate() {
        return nowDate;
    }

    public List<CommentListBean> getCommentList() {
        return commentList;
    }

    public HashMap<String, List<CommentReplyBean>> getCommentReplyMap() {
        return commentReplyMap;
    }

    public void setCommentReplyMap(HashMap<String, List<CommentReplyBean>> commentReplyMap) {
        this.commentReplyMap = commentReplyMap;
    }


    public HashMap<String, UserRacePredictBean> getUserRacePredictMap() {
        return userRacePredictMap;
    }

    public void setUserRacePredictMap(HashMap<String, UserRacePredictBean> userRacePredictMap) {
        this.userRacePredictMap = userRacePredictMap;
    }

    public HashMap<String, String> getUserIdAndMoneyMap() {
        return userIdAndMoneyMap;
    }

    public void setUserIdAndMoneyMap(HashMap<String, String> userIdAndMoneyMap) {
        this.userIdAndMoneyMap = userIdAndMoneyMap;
    }


    public CommentObjectBean getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(CommentObjectBean commentObject) {
        this.commentObject = commentObject;
    }


    public static class CommentListBean{

        private String content;

        private boolean vip;

        private String userLogoUrl;

        private boolean userDeleted;

        private int replyCount;

        private int likeCount;

        private String gmtCreate;

        private int floor;

        private String userId;

        private boolean liked;

        private boolean systemDeleted;

        private String commentId;

        private String loginName;

        private boolean setTop;

        private int num;

        @Expose
        private String predictMessage;

        @Expose
        private UserRacePredictBean predictBean;

        @Expose
        private int layoutHeight;

        @Expose
        private List<CommentReplyBean> replyComment;

        @Expose
        private String couponAmount;

        @Expose
        private SpannableStringBuilder predictSpanMessage;

        @Expose
        private List<JczjMedalBean> userMedalBeanList;

        private String summary;

        private Properties properties;

        @Expose
        private JcSnapshotBean jcSnapshotBean;

        @Expose
        private String discoveryEnum;

        @Expose
        public boolean popLayoutVisible;

        @Expose
        private PurchaseSnapshotBean purchaseSnapshotBean;

        public JcSnapshotBean getJcSnapshotBean() {
            return jcSnapshotBean;
        }

        public void setJcSnapshotBean(JcSnapshotBean jcSnapshotBean) {
            this.jcSnapshotBean = jcSnapshotBean;
        }
        public PurchaseSnapshotBean getPurchaseSnapshotBean() {
            return purchaseSnapshotBean;
        }

        public String getDiscoveryEnum() {
            return discoveryEnum;
        }

        public void setDiscoveryEnum(String discoveryEnum) {
            this.discoveryEnum = discoveryEnum;
        }

        public void setPurchaseSnapshotBean(PurchaseSnapshotBean purchaseSnapshotBean) {
            this.purchaseSnapshotBean = purchaseSnapshotBean;
        }

        public Properties getProperties() {
            return properties;
        }

        public void setProperties(Properties properties) {
            this.properties = properties;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<JczjMedalBean> getUserMedalBeanList() {

            return userMedalBeanList;
        }

        public void setUserMedalBeanList(List<JczjMedalBean> userMedalBeanList) {
            this.userMedalBeanList = userMedalBeanList;
        }

        public SpannableStringBuilder getPredictSpanMessage() {
            return predictSpanMessage;
        }

        public void setPredictSpanMessage(SpannableStringBuilder predictSpanMessage) {
            this.predictSpanMessage = predictSpanMessage;
        }



        public void setContent(String content) {
            this.content = content;
        }

        public void setVip(boolean vip) {
            this.vip = vip;
        }

        public void setUserLogoUrl(String userLogoUrl) {
            this.userLogoUrl = userLogoUrl;
        }

        public void setUserDeleted(boolean userDeleted) {
            this.userDeleted = userDeleted;
        }

        public void setReplyCount(int replyCount) {
            this.replyCount = replyCount;
        }

        public void setLikeCount(int likeCount) {
            this.likeCount = likeCount;
        }

        public void setGmtCreate(String gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public void setFloor(int floor) {
            this.floor = floor;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public void setLiked(boolean liked) {
            this.liked = liked;
        }

        public void setSystemDeleted(boolean systemDeleted) {
            this.systemDeleted = systemDeleted;
        }

        public String getContent() {
            return content;
        }

        public boolean isVip() {
            return vip;
        }

        public String getUserLogoUrl() {
            return userLogoUrl;
        }

        public boolean isUserDeleted() {
            return userDeleted;
        }

        public int getReplyCount() {
            return replyCount;
        }

        public int getLikeCount() {
            return likeCount;
        }

        public String getGmtCreate() {
            return gmtCreate;
        }

        public int getFloor() {
            return floor;
        }

        public String getUserId() {
            return userId;
        }

        public boolean isLiked() {
            return liked;
        }

        public boolean isSystemDeleted() {
            return systemDeleted;
        }

        public String getCommentId() {
            return commentId;
        }

        public void setCommentId(String commentId) {
            this.commentId = commentId;
        }

        public List<CommentReplyBean> getReplyComment() {
            return replyComment;
        }

        public void setReplyComment(List<CommentReplyBean> replyComment) {
            this.replyComment = replyComment;
        }

        public int getLayoutHeight() {
            return layoutHeight;
        }

        public void setLayoutHeight(int layoutHeight) {
            this.layoutHeight = layoutHeight;
        }

        public String getUserName() {
            return loginName;
        }

        public void setUserName(String userName) {
            this.loginName = userName;
        }

        public UserRacePredictBean getPredictBean() {
            return predictBean;
        }

        public void setPredictBean(UserRacePredictBean predictBean) {
            this.predictBean = predictBean;
        }

        public String getPredictMessage() {
            return predictMessage;
        }

        public void setPredictMessage(String predictMessage) {
            this.predictMessage = predictMessage;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public boolean isSetTop() {
            return setTop;
        }

        public void setSetTop(boolean setTop) {
            this.setTop = setTop;
        }


    }


    public static class UserRacePredictBean {

        private NormalObject raceType;

        private int id;

        private int raceId;

        private NormalObject predictStatus;

        private int rqPlate;

        private List<NormalObject> options;

        private HashMap<String, Double> optionSpMap;

        @Expose
        private double sp;

        public NormalObject getRaceType() {
            return raceType;
        }

        public void setRaceType(NormalObject raceType) {
            this.raceType = raceType;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public NormalObject getPredictStatus() {
            return predictStatus;
        }

        public void setPredictStatus(NormalObject predictStatus) {
            this.predictStatus = predictStatus;
        }

        public int getRaceId() {
            return raceId;
        }

        public void setRaceId(int raceId) {
            this.raceId = raceId;
        }

        public int getRqPlate() {
            return rqPlate;
        }

        public void setRqPlate(int rqPlate) {
            this.rqPlate = rqPlate;
        }

        public List<NormalObject> getOptions() {
            return options;
        }

        public void setOptions(List<NormalObject> options) {
            this.options = options;
        }

        public HashMap<String, Double> getOptionSpMap() {
            return optionSpMap;
        }

        public void setOptionSpMap(HashMap<String, Double> optionSpMap) {
            this.optionSpMap = optionSpMap;
        }

        public double getSp() {
            return sp;
        }

        public void setSp(double sp) {
            this.sp = sp;
        }
    }

    public static class CommentObject {

        private int allReplyCount;

        private NormalObject commentObjectType;

        private boolean commentOff;

        public int getAllReplyCount() {
            return allReplyCount;
        }

        public void setAllReplyCount(int allReplyCount) {
            this.allReplyCount = allReplyCount;
        }

        public NormalObject getCommentObjectType() {
            return commentObjectType;
        }

        public void setCommentObjectType(NormalObject commentObjectType) {
            this.commentObjectType = commentObjectType;
        }

        public boolean isCommentOff() {
            return commentOff;
        }

        public void setCommentOff(boolean commentOff) {
            this.commentOff = commentOff;
        }
    }


    public static class CommentObjectBean {

        private boolean commentOff;

        private NormalObject commentObjectType;

        private int allReplyCount;

        public boolean isCommentOff() {
            return commentOff;
        }

        public void setCommentOff(boolean commentOff) {
            this.commentOff = commentOff;
        }

        public NormalObject getCommentObjectType() {
            return commentObjectType;
        }

        public void setCommentObjectType(NormalObject commentObjectType) {
            this.commentObjectType = commentObjectType;
        }

        public int getAllReplyCount() {
            return allReplyCount;
        }

        public void setAllReplyCount(int allReplyCount) {
            this.allReplyCount = allReplyCount;
        }
    }

}

