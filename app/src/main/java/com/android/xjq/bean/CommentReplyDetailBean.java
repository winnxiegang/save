package com.android.xjq.bean;


import com.android.banana.commlib.bean.JczjMedalBean;
import com.android.banana.commlib.bean.PaginatorBean;
import com.android.xjq.bean.comment.CommentReplyBean;
import com.android.xjq.bean.comment.UserCommentBean;
import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouyi on 2016/1/11 13:50.
 */
public class CommentReplyDetailBean {

    private PaginatorBean paginator;

    private UserCommentBean.CommentListBean comment;

    private String nowDate;

    @Expose
    private String predictMessage;

    @Expose
    private UserCommentBean.UserRacePredictBean predictBean;

    private UserCommentBean.CommentObjectBean commentObject;
    /**
     * 二级评论列表
     */
    private List<CommentReplyBean> commentReplyList;

    private HashMap<String, String> userIdAndMoney;

    private HashMap<String, UserCommentBean.UserRacePredictBean> userRacePredictMap;

    /**
     * 用户勋章
     */
    private HashMap<String, List<JczjMedalBean>> userMedalClientSimpleMap;

    public CommentReplyDetailBean(JSONObject jo) {

        CommentReplyDetailBean bean = new Gson().fromJson(jo.toString(), CommentReplyDetailBean.class);

        this.comment = bean.comment;

        this.nowDate = bean.nowDate;

        this.commentReplyList = bean.commentReplyList;

        this.userRacePredictMap = bean.userRacePredictMap;

        this.userIdAndMoney = bean.userIdAndMoney;

        this.commentObject = bean.commentObject;

        this.userMedalClientSimpleMap = bean.userMedalClientSimpleMap;

        if (userRacePredictMap != null) {

            String commentId = String.valueOf(comment.getCommentId());

            if (userRacePredictMap.get(commentId) != null) {

                UserCommentBean.UserRacePredictBean userRacePredictBean = userRacePredictMap.get(commentId);

                if (userRacePredictBean != null) {

                    comment.setPredictBean(userRacePredictMap.get(commentId));

                    comment.setPredictMessage(UserCommentBean.formatPredictInfoMessage(userRacePredictBean));

                    comment.setContent(UserCommentBean.getHtmlSpanMessage(comment.getPredictBean().getPredictStatus().getName(), comment.getPredictMessage()) + comment.getContent());

                }

            }

        }

        //第一次请求楼主的评论勋章
        if (comment != null && userMedalClientSimpleMap != null && userMedalClientSimpleMap.containsKey(comment.getUserId())) {

            comment.setUserMedalBeanList(userMedalClientSimpleMap.get(comment.getUserId()));
        }


    }


    public HashMap<String, List<JczjMedalBean>> getUserMedalClientSimpleMap() {
        return userMedalClientSimpleMap;
    }

    public void setUserMedalClientSimpleMap(HashMap<String, List<JczjMedalBean>> userMedalClientSimpleMap) {
        this.userMedalClientSimpleMap = userMedalClientSimpleMap;
    }


    public void setPaginator(PaginatorBean paginator) {
        this.paginator = paginator;
    }

    public UserCommentBean.CommentListBean getComment() {
        return comment;
    }

    public void setComment(UserCommentBean.CommentListBean comment) {
        this.comment = comment;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public void setCommentReplyList(List<CommentReplyBean> commentReplyList) {
        this.commentReplyList = commentReplyList;
    }

    public PaginatorBean getPaginator() {
        return paginator;
    }

    public String getNowDate() {
        return nowDate;
    }

    public List<CommentReplyBean> getCommentReplyList() {
        return commentReplyList;
    }

    public UserCommentBean.CommentObjectBean getCommentObject() {
        return commentObject;
    }

    public void setCommentObject(UserCommentBean.CommentObjectBean commentObject) {
        this.commentObject = commentObject;
    }


    public HashMap<String, String> getUserIdAndMoney() {
        return userIdAndMoney;
    }


    public String getPredictMessage() {
        return predictMessage;
    }

    public void setUserIdAndMoney(HashMap<String, String> userIdAndMoney) {
        this.userIdAndMoney = userIdAndMoney;
    }

    public void setPredictMessage(String predictMessage) {
        this.predictMessage = predictMessage;
    }

    public UserCommentBean.UserRacePredictBean getPredictBean() {
        return predictBean;
    }

    public void setPredictBean(UserCommentBean.UserRacePredictBean predictBean) {
        this.predictBean = predictBean;
    }
}

