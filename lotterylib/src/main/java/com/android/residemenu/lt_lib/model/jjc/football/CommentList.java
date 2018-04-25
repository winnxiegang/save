package com.android.residemenu.lt_lib.model.jjc.football;

import java.util.ArrayList;

//楼主发表评论列表
public class CommentList {

	private String imagePath; // 头像位置

	private String louzhuNickName;// 楼主昵称

	private String commentPushDate;//发布日期

	private String comment; //评论

	private int zanNumber;  //赞的数量

	private ArrayList<GenLou> genLouList = new ArrayList<GenLou>();//跟楼列表
	
	private boolean isAddGenLou; //是否已近添加跟楼
	
	private boolean isShowMoreComment;

	public boolean isShowMoreComment() {
		return isShowMoreComment;
	}

	public void setShowMoreComment(boolean isShowMoreComment) {
		this.isShowMoreComment = isShowMoreComment;
	}

	public boolean isAddGenLou() {
		return isAddGenLou;
	}

	public void setAddGenLou(boolean isAddGenLou) {
		this.isAddGenLou = isAddGenLou;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getLouzhuNickName() {
		return louzhuNickName;
	}

	public void setLouzhuNickName(String louzhuNickName) {
		this.louzhuNickName = louzhuNickName;
	}

	public String getCommentPushDate() {
		return commentPushDate;
	}

	public void setCommentPushDate(String commentPushDate) {
		this.commentPushDate = commentPushDate;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getZanNumber() {
		return zanNumber;
	}

	public void setZanNumber(int zanNumber) {
		this.zanNumber = zanNumber;
	}

	public ArrayList<GenLou> getGenLouList() {
		return genLouList;
	}

	public void setGenLouList(ArrayList<GenLou> genLouList) {
		this.genLouList = genLouList;
	}

}
