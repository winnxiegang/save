package com.android.xjq.bean.message;

import com.android.xjq.bean.JcSnapshotBean;
import com.android.xjq.bean.comment.CommentReplyBean;
import com.android.xjq.bean.order.PurchaseSnapshotBean;

/**
 * Created by zhouyi on 2017/1/12 16:59.
 */

public class BaseTopicsBean {

   private CommentBean comment;

    private CommentReplyBean commentReply;

    private CommentReplyBean commentParentReply;

    private SubjectsBean subject;

    private JcSnapshotBean jczqRaceData;

    private InfoBean info;

 //   private RaceSimpleBean raceSimple;

    private PurchaseSnapshotBean purchaseOrder;

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }
    public JcSnapshotBean getJczqRaceData() {
        return jczqRaceData;
    }

    public void setJczqRaceData(JcSnapshotBean jczqRaceData) {
        this.jczqRaceData = jczqRaceData;
    }

    public SubjectsBean getSubject() {
        return subject;
    }

    public void setSubject(SubjectsBean subject) {
        this.subject = subject;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

//    public RaceSimpleBean getRaceSimple() {
//        return raceSimple;
//    }
//
//    public void setRaceSimple(RaceSimpleBean raceSimple) {
//        this.raceSimple = raceSimple;
//    }

    public CommentReplyBean getCommentParentReply() {
        return commentParentReply;
    }

    public void setCommentParentReply(CommentReplyBean commentParentReply) {
        this.commentParentReply = commentParentReply;
    }

    public CommentReplyBean getCommentReply() {
        return commentReply;
    }

    public void setCommentReply(CommentReplyBean commentReply) {
        this.commentReply = commentReply;
    }

    public PurchaseSnapshotBean getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseSnapshotBean purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}
