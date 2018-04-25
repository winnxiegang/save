package com.android.xjq.activity.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.event.GroupChatJoinExitEvent;
import com.android.banana.groupchat.bean.GroupChatMessageBean;
import com.android.banana.groupchat.bean.MessageListBean;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.groupchat.chatenum.GroupChatMessageType;
import com.android.banana.groupchat.groupchat.chat.GroupChatActivity;
import com.android.banana.groupchat.ilistener.OnMyItemSlideClickListener;
import com.android.banana.groupchat.message.GroupChatNotificationActivity;
import com.android.banana.groupchat.message.adapter.GroupChatMessageAdapter;
import com.android.banana.groupchat.msglist.GroupChatMessageHelper;
import com.android.banana.groupchat.msglist.realm.MyRealmHelper;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.BadgeView;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.message.MessageNotifyActivity;
import com.android.xjq.activity.message.ReplyMeActivity;
import com.android.xjq.bean.message.NoticeMessageBean;
import com.google.gson.Gson;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.im.ImGroupManager;
import com.jl.jczj.im.tim.TimMessageType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * create by zaozao  2018-01-25
 */

public class MessageAndNotifyActivity extends BaseActivity implements OnHttpResponseListener, IMTimCallback {

    @BindView(R.id.refreshListView)
    ScrollListView refreshListView;
    @BindView(R.id.messageLayout)
    LinearLayout messageLayout;

    private HttpRequestHelper loopRequestHelper;

    private HttpRequestHelper httpRequestHelper;

    private int currentClickPosition = -1;

    private GroupChatMessageAdapter groupChatMessageAdapter;

    private MyRealmHelper myRealmHelper;

    private GroupChatMessageHelper groupChatMessageHelper;

    private boolean pause = false;//是否执行过onPause

    private boolean start = false;//是否已经开始循环

    private boolean firstRequestFailed = false;

    private Handler handler;

    private static final int delayTime = 3000;

    private View rootView;

    private HeaderViewHolder headerViewHolder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_and_notify);
        ButterKnife.bind(this);
        initView();
    }


    protected void initView() {

        setTitleBar(true, "消息", null);
        ImGroupManager.getInstance().addTimCallback(this);
        refreshListView.setSwipe2Dimiss(true);

        refreshListView.setDivider(null);

        refreshListView.addHeaderView(getHeaderView());

        httpRequestHelper = new HttpRequestHelper(this, this);

        loopRequestHelper = new HttpRequestHelper(this, this);

        myRealmHelper = new MyRealmHelper();

        groupChatMessageHelper = new GroupChatMessageHelper(myRealmHelper);

        groupChatMessageAdapter = new GroupChatMessageAdapter(getApplicationContext(), groupChatMessageHelper.showMessageList);

        refreshListView.setAdapter(groupChatMessageAdapter);

        if (handler == null) {
            handler = new Handler();
        }
        groupChatMessageAdapter.setOnMyItemSlideClickListener(OnMyItemSlideClickListener);

        handler.postDelayed(newMessageNotifyTask, delayTime);

        getInitData();

        getSecretAndSystemMessage();//轮循私聊和通知数据
    }

    private View getHeaderView() {
        //评论头部
        View headerView = getLayoutInflater().inflate(R.layout.layout_system_message_reply, null);

        headerViewHolder = new HeaderViewHolder(headerView);
        headerViewHolder.toReplyMeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerViewHolder.replyMeCount.setBadgeNum(0);
                ReplyMeActivity.startReplyMeActivity(MessageAndNotifyActivity.this);
            }
        });
        headerViewHolder.toNotifyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                headerViewHolder.notifyCount.setBadgeNum(0);
                MessageNotifyActivity.startMessageNotifyActivity(MessageAndNotifyActivity.this);

            }
        });
        return headerView;
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        JczjURLEnum urlEnum = (JczjURLEnum) requestContainer.getRequestEnum();
        switch (urlEnum) {
            case PAYLOAD_ORDER_BY_USER_SET:
                toTopSuccess(jsonObject);
                break;
            case PAYLOAD_ORDER_BY_USER_DISABLED:
                cancelToTopSuccess();
                break;
            case MODIFY_MESSAGE_LAST_READ_TO_NEWEST:
                deleteSuccess();
                break;
            case SOLE_MESSAGE_COUNT_QUERY:
                startMessageLoop();
                if (groupChatMessageHelper.requestSuccessMessageListRequest(jsonObject)) {
                    notifyAndClearData();//轮训回来，先排序
                }
                break;
            case USER_JOINED_GROUP_QUERY:
                groupChatMessageHelper.responseSuccessMessageCount(jsonObject);
                break;
            case MESSAGE_NOTICE_QUERY:
                NoticeMessageBean bean = new Gson().fromJson(jsonObject.toString(), NoticeMessageBean.class);
                headerViewHolder.replyMeCount.setBadgeNum(bean.getAccessCodeAndNewCount().getREPLY());
                headerViewHolder.notifyCount.setBadgeNum(bean.getAccessCodeAndNewCount().getUSER_MESSAGE_NOTICE());
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        JczjURLEnum urlEnum = (JczjURLEnum) requestContainer.getRequestEnum();
        switch (urlEnum) {
            case PAYLOAD_ORDER_BY_USER_SET:
            case PAYLOAD_ORDER_BY_USER_DISABLED:
            case MODIFY_MESSAGE_LAST_READ_TO_NEWEST:
            case USER_JOINED_GROUP_QUERY:
            case MESSAGE_NOTICE_QUERY:
                errTipShow(jsonObject);
                break;
            case SOLE_MESSAGE_COUNT_QUERY:
                if (!firstRequestFailed) {//只有第一次请求失败才会提示
                    errTipShow(jsonObject);
                    firstRequestFailed = true;
                }
                startMessageLoop();
                break;
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {
    }


    //删除成功
    private void deleteSuccess() {
        //删除
        myRealmHelper.delete(groupChatMessageHelper.showMessageList.get(currentClickPosition).getGroupIdAndType());
        groupChatMessageHelper.showMessageList.remove(currentClickPosition);
        notifyAndClearData();//删除成功后
//        setBottomRedPointTip(false);

    }

    //置顶成功
    private void toTopSuccess(JSONObject jo) {

        try {
            long value = jo.getLong("orderValue");

            groupChatMessageHelper.showMessageList.get(currentClickPosition).setOrderValueByUser(value);

            updateLocalData(groupChatMessageHelper.showMessageList.get(currentClickPosition));

            groupChatMessageHelper.sortList();

            notifyAndClearData();//置顶成功，排序后

        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    //取消置顶成功
    private void cancelToTopSuccess() {

        groupChatMessageHelper.showMessageList.get(currentClickPosition).setOrderValueByUser(0);

        updateLocalData(groupChatMessageHelper.showMessageList.get(currentClickPosition));

        groupChatMessageHelper.sortList();

        notifyAndClearData();//取消置顶成功  排序后
    }


    public void errTipShow(JSONObject jo) {
        try {
            operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 进入界面第一次请求结束后，开始轮循
     */
    private void startMessageLoop() {

        if (!start && !pause) {//没有开启轮循并且没有暂停过
            start = true;
            handler.postDelayed(getMessageTask, 10000);
        }
    }

    private Runnable getMessageTask = new Runnable() {
        @Override
        public void run() {
            getSecretAndSystemMessage();
            handler.postDelayed(this, 10000);
        }
    };


    /**
     * 群聊页加入聊天室
     * 群聊设置页退出聊天室
     */
    @Subscribe
    public void onGroupChatJoinExitEvent(GroupChatJoinExitEvent event) {

        if (event.exit) {
            for (int i = 0; i < groupChatMessageHelper.showMessageList.size(); i++) {
                String groupIdAndType = LoginInfoHelper.getInstance().getUserId() + "_" + event.groupId + "_" + GroupChatMessageType.GROUP_CHAT.name();
                if (groupIdAndType.equals(groupChatMessageHelper.showMessageList.get(i).getGroupIdAndType())) {
                    myRealmHelper.delete(groupIdAndType);
                    groupChatMessageHelper.showMessageList.remove(i);
                    notifyAndClearData();//退出聊天室
                    return;
                }
            }
        }
    }

    /**
     * 接收到私聊消息event
     */
    @Subscribe
    public void onSimpleChatMessageEvent(ChatMsgBody msgBody) {
        if (msgBody != null) {
            String groupIdAndType = LoginInfoHelper.getInstance().getUserId() + "_" + msgBody.getSoleId() + "_" + GroupChatMessageType.SECRET_CHAT.name();
            int position = groupChatMessageHelper.isExist(groupIdAndType);
            if (position > -1) {
                groupChatMessageHelper.setMessageInfo(groupChatMessageHelper.showMessageList.get(position), msgBody);
                groupChatMessageHelper.showMessageList.get(position).setUnreadMessageCount(0);
                myRealmHelper.update(groupChatMessageHelper.showMessageList.get(position));
            } else {
                MessageListBean bean = new MessageListBean();
                bean.setRoomName(msgBody.getTargetUserName());
                bean.setGroupChatMessageType(GroupChatMessageType.SECRET_CHAT.name());
                bean.setGroupIdAndType(LoginInfoHelper.getInstance().getUserId() + "_" + msgBody.getSoleId() + "_" + GroupChatMessageType.SECRET_CHAT.name());
                bean.setLastMessageCreateTime(TimeUtils.stringToDate(msgBody.gmtCreate, null));
                bean.setOriginGmtCreate(msgBody.gmtCreate);
                bean.setSoleId(msgBody.getSoleId());
                bean.setSendUserId(msgBody.getTargetUserId());
                bean.setSendUserLoginName(msgBody.getTargetUserName());
                bean.setGmtCreate(TimeUtils.formatMessageTime(groupChatMessageHelper.nowDate, msgBody.gmtCreate));
                bean.setLastMessageContent(new GroupChatMessageBean().getMessageContent(msgBody));
                //"http://mapi.xjq.net/userLogoUrl.htm?userId=8201711068675884&mt=1509972179000"
                bean.setPhotoUrl(AppParam.API_DOMAIN + AppParam.USER_LOGO_URL + "userId=" + msgBody.getTargetUserId());
                groupChatMessageHelper.showMessageList.add(bean);
                myRealmHelper.add(bean);//私聊玩的，发过来的eventBus 保存
                groupChatMessageHelper.sortList();
            }
            notifyAndClearData();//私聊消息回来后
        }
    }

    //    更新本地数据库
    private void updateLocalData(MessageListBean bean) {
        myRealmHelper.update(bean);
    }

    OnMyItemSlideClickListener OnMyItemSlideClickListener = new OnMyItemSlideClickListener() {
        @Override
        public void onItemClick(int position, boolean itemClick, boolean delete, boolean toTop) {

            currentClickPosition = position;

            MessageListBean listBean = groupChatMessageHelper.showMessageList.get(position);

            if (itemClick) {

                groupChatMessageHelper.showMessageList.get(position).setUnreadMessageCount(0);

                notifyAndClearData();//item点击之后

                updateLocalData(groupChatMessageHelper.showMessageList.get(position));

                if (listBean.isShowCoupon()) {
                    listBean.setShowCoupon(false);
                }

//                setBottomRedPointTip(true);

                switch (GroupChatMessageType.safeValueOf(listBean.getGroupChatMessageType())) {
                    case GROUP_CHAT:
                        GroupChatActivity.start2GroupChatActivity(MessageAndNotifyActivity.this, listBean.getGroupId(), listBean.getRoomName(), listBean.getPhotoUrl());
                        break;
                    case SECRET_CHAT:
                        SimpleChatActivity.startSimpleChatActivity(MessageAndNotifyActivity.this, listBean.getSendUserId(), listBean.getSendUserLoginName(), listBean.getSoleId());
                        break;
                    case SYSTEM_MESSAGE:
                        startActivity(new Intent().setClass(MessageAndNotifyActivity.this, GroupChatNotificationActivity.class));
                        break;
                }
            } else if (delete) {
                if (!GroupChatMessageType.GROUP_CHAT.name().equals(listBean.getGroupChatMessageType())) {
                    deleteMessage();
                } else {
                    deleteSuccess();
                }

            } else if (toTop) {

                if (TextUtils.equals("0", groupChatMessageHelper.showMessageList.get(position).getOrderValueByUser() + "")) {
                    //置顶
                    toTopRequest();
                } else {
                    //取消
                    cancelToTopRequest();
                }
            } else {
                return;
            }
        }
    };

    //请求私聊数据
    private void getSecretAndSystemMessage() {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.SOLE_MESSAGE_COUNT_QUERY, true);

        loopRequestHelper.startRequest(map, false);
    }


    private void getInitData() {
        List<RequestContainer> list = new ArrayList<>();
        list.add(getAllGroupId());
        list.add(getMessageCount());
        httpRequestHelper.startRequest(list);
    }

    //我加入的群聊和置顶信息
    private RequestFormBody getAllGroupId() {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.USER_JOINED_GROUP_QUERY, true);

//        httpRequestHelper.startRequest(map, false);
        return map;
    }

    private RequestFormBody getMessageCount() {
        RequestFormBody container = new RequestFormBody(JczjURLEnum.MESSAGE_NOTICE_QUERY, true);
        container.setGenericClaz(NoticeMessageBean.class);
//        httpRequestHelper.startRequest(container, false);
        return container;
    }

    /**
     * 置顶
     */
    private void toTopRequest() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.PAYLOAD_ORDER_BY_USER_SET, true);

        map.put("payloadId", getGroupIdOrSoleId());

        map.put("payloadType", getChatType());

        httpRequestHelper.startRequest(map, true);
    }

    /**
     * 取消置顶
     */
    private void cancelToTopRequest() {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.PAYLOAD_ORDER_BY_USER_DISABLED, true);
        map.put("payloadId", getGroupIdOrSoleId());
        map.put("payloadType", getChatType());
        httpRequestHelper.startRequest(map, true);
    }

    private String getGroupIdOrSoleId() {
        if (GroupChatMessageType.SECRET_CHAT.name().
                equals(groupChatMessageHelper.showMessageList.get(currentClickPosition).getGroupChatMessageType())) {
            return groupChatMessageHelper.showMessageList.get(currentClickPosition).getSoleId();
        } else {
            return groupChatMessageHelper.showMessageList.get(currentClickPosition).getGroupId();
        }
    }

    private String getChatType() {
        String s = "";
        switch (GroupChatMessageType.safeValueOf(groupChatMessageHelper.showMessageList.get(currentClickPosition).getGroupChatMessageType())) {
            case GROUP_CHAT:
                s = "GROUP";
                break;
            case SECRET_CHAT:
                s = "SOLE";
                break;
            case SYSTEM_MESSAGE:
                s = "SYSTEM";
                break;
        }
        return s;
    }


    /**
     * 删除消息
     */
    private void deleteMessage() {
        RequestFormBody map = new RequestFormBody(JczjURLEnum.MODIFY_MESSAGE_LAST_READ_TO_NEWEST, true);

        map.put("objectId", getGroupIdOrSoleId());

        map.put("objectType", getChatType());

        httpRequestHelper.startRequest(map, true);
    }


//    /**
//     * 小红点
//     */
//    private void setBottomRedPointTip(boolean isClick) {
//
//        int messageCount = 0;
//
//        for (int i = 0; i < groupChatMessageHelper.showMessageList.size(); i++) {
//
//            messageCount = messageCount + groupChatMessageHelper.showMessageList.get(i).getUnreadMessageCount();
//        }
//
//        if (messageCount > 0) {
//            (((MainActivity) getActivity()).mTabHost.getTabWidget().getChildAt(0))
//                    .findViewById(R.id.redPointTip).setVisibility(View.VISIBLE);
//        } else {
//            (((MainActivity) getActivity()).mTabHost.getTabWidget().getChildAt(0))
//                    .findViewById(R.id.redPointTip).setVisibility(View.GONE);
//        }
//    }

    //推送消息回来
    @Override
    public void onTimNewMessage(ChatMsgBody msgBody) {
        LogUtils.e("kk","推送消息回来--------------------------------------------");
        if (TextUtils.equals(TimMessageType.USER_QUIT_GROUP_TEXT, msgBody.typeCode)) {
            return;
        } else {
            if (groupChatMessageHelper.addNewGroupMessage(msgBody)) {
                notifyAndClearData();//推送消息过来后
//                setBottomRedPointTip(false);
            } else {
                return;
            }
        }
    }

    @Override
    public void onTimSystemMessage(String groupId, String messageType) {
        if (TextUtils.equals(TimMessageType.USER_QUIT_GROUP_TEXT, messageType)) {
            onGroupChatJoinExitEvent(new GroupChatJoinExitEvent(groupId, true));
        }
    }


    //定时从保存的推送新消息列表中去除数据显示并刷新
    private Runnable newMessageNotifyTask = new Runnable() {
        @Override
        public void run() {
            if (groupChatMessageHelper.saveExistMessageList != null && groupChatMessageHelper.saveExistMessageList.size() > 0) {
                notifyAndClearData();//定时从保存的推送新消息列表中去除数据显示并刷新
            }
            handler.postDelayed(this, delayTime);
        }
    };

    private void notifyAndClearData() {
        if (groupChatMessageHelper.saveExistMessageList != null) {
            groupChatMessageHelper.saveExistMessageList.clear();
        }
        groupChatMessageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!start && pause) {//第一次onResume不执行（所以判断一下是否暂停过）
            pause = false;
            myRealmHelper.initRealm();
            if (groupChatMessageHelper.setOpenStateFalseAndNotify()) {
                notifyAndClearData();//onResume--- if (!start && pause) --if (groupChatMessageHelper.setOpenStateFalseAndNotify()) {
            }
            getSecretAndSystemMessage();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ImGroupManager.getInstance().removeTimCallback(this);
        myRealmHelper.close();
        groupChatMessageHelper.unregister();
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onPause() {
        super.onPause();
        start = false;
        pause = true;//暂停过要重新请求
        firstRequestFailed = false;
        handler.removeCallbacks(getMessageTask);
    }


    static class HeaderViewHolder {
        @BindView(R.id.replyMeCount)
        BadgeView replyMeCount;
        @BindView(R.id.toReplyMeLayout)
        LinearLayout toReplyMeLayout;
        @BindView(R.id.notifyCount)
        BadgeView notifyCount;
        @BindView(R.id.toNotifyLayout)
        LinearLayout toNotifyLayout;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

