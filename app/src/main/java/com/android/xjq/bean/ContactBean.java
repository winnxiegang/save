package com.android.xjq.bean;
import com.android.banana.commlib.utils.LibAppUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zhouyi on 2015/12/18 17:03.
 */
public class ContactBean implements Serializable {

    private boolean vip;

    private String userName;

    private String userLogoUrl;

    private boolean mySelf;

    private boolean focus;

    private boolean subscribe;

    private String userId;

    private String firstAlphabet;

    private boolean selected;

    private int position;

    private List<ContactBean> contactList = new ArrayList<>();

    private List<ContactBean> recentMentionList = new ArrayList<>();

    private boolean showFirstAlphabet;

    public ContactBean(JSONObject jo) throws JSONException {

        if (jo.has("userFollowInfos")) {

            JSONArray userFollowInfosJson = jo.getJSONArray("userFollowInfos");

            for (int i = 0; i < userFollowInfosJson.length(); i++) {

                ContactBean bean = new ContactBean();
                JSONObject userInfoJson = userFollowInfosJson.getJSONObject(i);
                boolean vip = userInfoJson.getBoolean("vip");
                String userName = userInfoJson.getString("userName");
                String userLogoUrl = userInfoJson.getString("userLogoUrl");
                boolean mySelf = userInfoJson.getBoolean("mySelf");
                boolean focus = userInfoJson.getBoolean("focus");
                boolean subscribe = userInfoJson.getBoolean("subscribe");
                String userId = userInfoJson.getString("userId");

                bean.vip = vip;
                bean.userName = userName;
                bean.userLogoUrl = userLogoUrl;
                bean.mySelf = mySelf;
                bean.focus = focus;
                bean.subscribe = subscribe;
                bean.userId = userId;
                bean.firstAlphabet = LibAppUtil.getFirstAlphabet(userName).toUpperCase();

                contactList.add(bean);

            }

            Collections.sort(contactList, new Comparator<ContactBean>() {
                @Override
                public int compare(ContactBean lhs, ContactBean rhs) {

                     return lhs.firstAlphabet.compareTo(rhs.firstAlphabet);

                }
            });

            List<ContactBean> tempList = new ArrayList<>();


            for(int i=0;i<contactList.size();i++){
                if(!"#".equals(contactList.get(i).getFirstAlphabet())){
                    tempList.add(contactList.get(i));
                }
            }

            for(int i=0;i<contactList.size();i++){
                if("#".equals(contactList.get(i).getFirstAlphabet())){
                    tempList.add(contactList.get(i));
                }
            }

            contactList = tempList;
        }

        if (jo.has("recentMentions")) {

            JSONArray userFollowInfosJson = jo.getJSONArray("recentMentions");

            for (int i = 0; i < userFollowInfosJson.length(); i++) {

                ContactBean bean = new ContactBean();
                JSONObject userInfoJson = userFollowInfosJson.getJSONObject(i);
                boolean vip = userInfoJson.getBoolean("vip");
                String userName = userInfoJson.getString("userName");
                String userLogoUrl = userInfoJson.getString("userLogoUrl");
                String userId = userInfoJson.getString("userId");

                bean.vip = vip;
                bean.userName = userName;
                bean.userLogoUrl = userLogoUrl;
                bean.userId = userId;
                bean.firstAlphabet = "*";

                recentMentionList.add(bean);

            }
        }

    }

    public ContactBean() {
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        ContactBean bean = (ContactBean) o;
        if (firstAlphabet.equals(bean.getFirstAlphabet())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isShowFirstAlphabet() {
        return showFirstAlphabet;
    }

    public void setShowFirstAlphabet(boolean showFirstAlphabet) {
        this.showFirstAlphabet = showFirstAlphabet;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogoUrl() {
        return userLogoUrl;
    }

    public void setUserLogoUrl(String userLogoUrl) {
        this.userLogoUrl = userLogoUrl;
    }

    public boolean isMySelf() {
        return mySelf;
    }

    public void setMySelf(boolean mySelf) {
        this.mySelf = mySelf;
    }

    public boolean isFocus() {
        return focus;
    }

    public void setFocus(boolean focus) {
        this.focus = focus;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstAlphabet() {
        return firstAlphabet;
    }

    public void setFirstAlphabet(String firstAlphabet) {
        this.firstAlphabet = firstAlphabet;
    }

    public List<ContactBean> getContactList() {
        return contactList;
    }

    public void setContactList(List<ContactBean> contactList) {
        this.contactList = contactList;
    }

    public List<ContactBean> getRecentMentionList() {
        return recentMentionList;
    }

    public void setRecentMentionList(List<ContactBean> recentMentionList) {
        this.recentMentionList = recentMentionList;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }



}
