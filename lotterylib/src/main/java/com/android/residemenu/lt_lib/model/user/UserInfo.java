package com.android.residemenu.lt_lib.model.user;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * @author leslie
 * @version $Id: UserInfo.java, v 0.1 2014年5月21日 下午5:20:58 leslie Exp $
 */
public class UserInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1567051135047729713L;

    private String userId;

    private String userName;

    private String token;

    private String branchName;

    private String userNickName;

    private String loginKey;

    public UserInfo() {

    }

    public UserInfo(JSONObject jo) throws JSONException {

        JSONObject branchInfo = jo.optJSONObject("branchInfo");

        String branchName = null;

        if (branchInfo != null) {
            branchName = branchInfo.optString("branchName");
        } else {
            branchName = "";
        }

        String loginKey = jo.optString("loginKey");

        String userId = jo.getString("userId");

        String signKey = jo.getString("signKey");

        String userName = jo.optString("loginName");

        setUserId(userId);

        setToken(signKey);

        setBranchName(branchName);

        setUserNickName(userName);

        setLoginKey(loginKey);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    @Override

    public String toString() {

        return "{userId=" + userId + ",userName=" + userName + ",token=" + token + "}";
    }
}
