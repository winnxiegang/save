package com.android.banana.groupchat.msglist;

import android.text.TextUtils;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.event.JczjMessageEvent;
import com.android.banana.groupchat.bean.AllMyGroupBean;
import com.android.banana.groupchat.bean.GroupChatMessageBean;
import com.android.banana.groupchat.bean.MessageListBean;
import com.android.banana.groupchat.chatenum.GroupChatMessageType;
import com.android.library.Utils.LogUtils;
import com.jl.jczj.im.MessageType;
import com.android.banana.groupchat.msglist.realm.MyRealmHelper;
import com.google.gson.Gson;
import com.jl.jczj.im.bean.ChatMsgBody;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by zaozao on 2017/11/6.
 * 数据的处理
 */

public class GroupChatMessageHelper {

    public List<MessageListBean> showMessageList = new ArrayList<>();

    public List<MessageListBean> toTopMessageList = new ArrayList<>();

    public List<MessageListBean> toBottomMessageList = new ArrayList<>();

    public List<MessageListBean> saveExistMessageList = new ArrayList<>();

    public List<String> allMyGroupId = new ArrayList<>();

    public List<AllMyGroupBean.PayloadBean> allTopGroupList = new ArrayList<>();

    public GroupChatMessageBean groupChatMessageBean;

    public String nowDate = null;

    MyRealmHelper myRealmHelper;

    private String mChatGroupId;

    private String selfUserId;

    private String deleteGroupId = null;

    public GroupChatMessageHelper(MyRealmHelper myRealmHelper) {
        EventBus.getDefault().register(this);
        this.myRealmHelper = myRealmHelper;
        showMessageList.addAll(myRealmHelper.queryAll());
        LogUtils.e("kk","myRealmHelper.queryAll()===="+myRealmHelper.queryAll().size());
        sortList();
        selfUserId = LoginInfoHelper.getInstance().getUserId();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupChatEvent(JczjMessageEvent event) {
        mChatGroupId = event.message;
    }


    //排序根据时间，最新的在最上面
    public void sortList() {

        if (showMessageList.size() > 1) {
            Collections.sort(showMessageList, new Comparator<MessageListBean>() {
                @Override
                public int compare(MessageListBean lhs, MessageListBean rhs) {
                    if (rhs.getLastMessageCreateTime() == null || lhs.getLastMessageCreateTime() == null)
                        return 0;
                    return rhs.getLastMessageCreateTime().compareTo(lhs.getLastMessageCreateTime());//降序
                }
            });
        }
        toTopMessageList.clear();

        toBottomMessageList.clear();

        //将置顶的数据提取出来
        for (int i = 0; i < showMessageList.size(); i++) {
            if ((!"0".equals(showMessageList.get(i).getOrderValueByUser() + "")) && !showMessageList.get(i).isSystemMessage()) {
                toTopMessageList.add(showMessageList.get(i));
            } else {
                toBottomMessageList.add(showMessageList.get(i));
            }
        }
        sortTopList();//按排序值重新排列
    }


    //排序根据时间，最新的在最上面
    public void sortTopList() {
        //将置顶数据放到最上面
        if (toTopMessageList.size() > 1) {
            Collections.sort(toTopMessageList, new Comparator<MessageListBean>() {//按排序值倒序
                @Override
                public int compare(MessageListBean lhs, MessageListBean rhs) {

                    if (rhs.getOrderValueByUser() > lhs.getOrderValueByUser()) {
                        return 1;
                    } else if (rhs.getOrderValueByUser() < lhs.getOrderValueByUser()) {
                        return -1;
                    }
                    return 0;
                }
            });
        }
        showMessageList.clear();
        showMessageList.addAll(toTopMessageList);
        showMessageList.addAll(toBottomMessageList);//showMessageList重新组装

        LogUtils.e("kk",showMessageList.size()+"数据个数");
    }



    public boolean requestSuccessMessageListRequest(JSONObject jo) {

        groupChatMessageBean = new Gson().fromJson(jo.toString(), GroupChatMessageBean.class);

        groupChatMessageBean.operatorData();

        nowDate = groupChatMessageBean.getNowDate();

        if (groupChatMessageBean.getEntries() != null && groupChatMessageBean.getEntries().size() > 0) {

            if (showMessageList.size() > 0) {//不是第一次请求

                if (groupChatMessageBean.getSystemMessage() != null) {//如果有新的群聊通知的话，将之前的移除

                    for (int i = 0; i < showMessageList.size(); i++) {
                        if (showMessageList.get(i).isSystemMessage()) {
                            myRealmHelper.delete(showMessageList.get(i).getGroupIdAndType());
                            showMessageList.remove(i);
                            break;
                        }
                    }
                }

                //去重
                for (int i = 0; i < groupChatMessageBean.getEntries().size(); i++) {
                    if (groupChatMessageBean.getEntries().get(i).isDeleteFromGroup()) {
                        deleteGroupId = groupChatMessageBean.getEntries().get(i).getGroupId();
                        groupChatMessageBean.getEntries().get(i).setDeleteFromGroup(false);//保险一点
                    }
                    for (int j = 0; j < showMessageList.size(); j++) {
                        if (TextUtils.equals(groupChatMessageBean.getEntries().get(i).getGroupIdAndType(),
                                showMessageList.get(j).getGroupIdAndType())) {
                            groupChatMessageBean.getEntries().get(i).setOpen(showMessageList.get(j).isOpen());
                            showMessageList.remove(j);
                        }
                    }
                }
            }
            //showMessageList此时数据还不包括新请求的，
            if (nowDate != null) {
                //主要是考虑到之前返回的数据，对数据的时间刷新一下，之后保存到本地以后也要考虑
                for (int i = 0; i < showMessageList.size(); i++) {
                    showMessageList.get(i).setGmtCreate(
                            TimeUtils.formatMessageTime(nowDate, showMessageList.get(i).getOriginGmtCreate()));
                }
            }

            //  新增------------------------保存或更新本地消息记录
            myRealmHelper.saveMessageAsync(groupChatMessageBean.getEntries());

            showMessageList.addAll(groupChatMessageBean.getEntries());

            //收到被移除通知时，移除
            for (int i = 0; i < showMessageList.size(); i++) {
                if ("GROUP_CHAT".equals(showMessageList.get(i).getGroupChatMessageType())
                        && TextUtils.equals(deleteGroupId, showMessageList.get(i).getGroupId())) {
                    String groupIdAndType = LoginInfoHelper.getInstance().getUserId() + "_" + deleteGroupId + "_" + GroupChatMessageType.GROUP_CHAT.name();
                    myRealmHelper.delete(groupIdAndType);
                    showMessageList.remove(i);
                    deleteGroupId = null;
                    break;
                }
            }

            sortList();//排序并刷新

            return true;
        }
        return false;
    }


    /**
     * 处理我所有的groupId
     */
    public void responseSuccessMessageCount(JSONObject jsonObject) {

        AllMyGroupBean allMyGroupBean = new Gson().fromJson(jsonObject.toString(), AllMyGroupBean.class);

        if (allMyGroupBean.getGroupIdByUserJoinList() != null && allMyGroupBean.getGroupIdByUserJoinList().size() > 0) {
            allMyGroupId.addAll(allMyGroupBean.getGroupIdByUserJoinList());
        }

        if (allMyGroupBean.getPayloadOrderByUserList() != null && allMyGroupBean.getPayloadOrderByUserList().size() > 0) {
            allTopGroupList.addAll(allMyGroupBean.getPayloadOrderByUserList());
        }

    }


    /**
     * 判断消息列表是否已经存在该聊天室
     *
     * @param groupIdAndType
     * @return
     */
    public int isExist(String groupIdAndType) {
        int position = -1;
        if (showMessageList.size() > 0) {//不是第一次请求

            for (int m = 0; m < showMessageList.size(); m++) {
                if (showMessageList.get(m).getGroupIdAndType().equals(groupIdAndType)) {
                    position = m;
                }
            }
        }
        return position;
    }


    public boolean addNewGroupMessage(ChatMsgBody msgBody) {

        int position = isExist(selfUserId + "_" + msgBody.groupId + "_" + GroupChatMessageType.GROUP_CHAT.name());

        MessageListBean bean = null;

        if (position > -1) {//存在

            bean = showMessageList.get(position);

            //更新原始时间，最后一条消息的date（用来排序），更新显示时间，红包数，最后一条消息的内容消息，未读消息数，
            setMessageInfo(bean, msgBody);
            if (TextUtils.equals(selfUserId, msgBody.sendUserId)
                    || TextUtils.equals(msgBody.groupId, mChatGroupId)) {//GroupChatActivity.sGroupId
                bean.setUnreadMessageCount(0);
            } else {
                bean.setUnreadMessageCount(showMessageList.get(position).getUnreadMessageCount() + 1);
            }
            myRealmHelper.add(bean);//推送过来的消息，已存在的，更新
            saveExistMessageList.add(bean);
            return false;

        } else {//新增的群聊
            bean = new MessageListBean();
            //groupId
            bean.setGroupId(msgBody.groupId);
            //聊天室名称
            bean.setRoomName(msgBody.groupName);
            //头像地址  http://mapi.xjq.net/groupLogo.resource?ownerId=%@&ownerType=GROUP&logoType=GROUP&sizeType=SMALL
            bean.setPhotoUrl(com.android.banana.commlib.http.AppParam.API_DOMAIN + "/groupLogo.resource?ownerId=" + msgBody.groupId + "&ownerType=GROUP&logoType=GROUP&sizeType=SMALL");
            //未读消息数
            if (!TextUtils.equals(selfUserId, msgBody.sendUserId)) {//不是自己发的
                bean.setUnreadMessageCount(1);
            }
            bean.setGroupChatMessageType(GroupChatMessageType.GROUP_CHAT.name());

            bean.setGroupIdAndType(selfUserId + "_" + msgBody.groupId + "_" + GroupChatMessageType.GROUP_CHAT.name());

            setMessageInfo(bean, msgBody);

            //匹配新增的是否被置顶
            if (allTopGroupList != null) {
                for (int i = 0; i < allTopGroupList.size(); i++) {
                    if (allTopGroupList.get(i).getPayloadId().equals(msgBody.groupId)) {
                        bean.setOrderValueByUser(allTopGroupList.get(i).getOrderValue());
                    }
                }
            }
            showMessageList.add(bean);
            myRealmHelper.add(bean);//推送过来的新增的，保存
            sortList();
            return true;
        }

    }


    //消息信息
    public void setMessageInfo(MessageListBean bean, ChatMsgBody msgBody) {
        //原本的发送时间
        bean.setOriginGmtCreate(msgBody.gmtCreate);
        //几分钟以前，，，，处理以后的时间显示
        bean.setGmtCreate(TimeUtils.formatMessageTime(nowDate, msgBody.gmtCreate));
        //最后一条消息创建时间，用来排序
        bean.setLastMessageCreateTime(TimeUtils.stringToDate(msgBody.gmtCreate, null));

        //最后一条消息内容
        bean.setLastMessageContent(new GroupChatMessageBean().getMessageContent(msgBody));
        //红包
        if (MessageType.COUPON_CREATE_SUCCESS_NOTICE_TEXT.equals(msgBody.typeCode)) {

            bean.setShowCoupon(true);
        }
        if (groupChatMessageBean != null && groupChatMessageBean.getGroupIdAndUserCountMap() != null
                && groupChatMessageBean.getGroupIdAndUserCountMap().containsKey(msgBody.groupId)) {
            bean.setMemberNum("(" + groupChatMessageBean.getGroupIdAndUserCountMap().get(msgBody.groupId) + ")");
        }
    }

    /**
     * 将所有的item侧滑栏置为关闭状态
     */
    public boolean setOpenStateFalseAndNotify() {
        if (showMessageList != null && showMessageList.size() > 0) {
            for (int i = 0; i < showMessageList.size(); i++) {
                showMessageList.get(i).setOpen(false);
            }
            return true;
        }
        return false;
    }

    public void unregister() {
        EventBus.getDefault().unregister(this);
    }

}
