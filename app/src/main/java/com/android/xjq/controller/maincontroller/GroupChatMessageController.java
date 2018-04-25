package com.android.xjq.controller.maincontroller;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.event.GroupChatJoinExitEvent;
import com.android.banana.groupchat.bean.GroupChatMessageBean;
import com.android.banana.groupchat.bean.MessageListBean;
import com.android.banana.groupchat.chat.SimpleChatActivity;
import com.android.banana.groupchat.chatenum.GroupChatMessageType;
import com.android.banana.groupchat.groupchat.chat.GroupChatActivity;
import com.android.banana.groupchat.groupchat.groupcreat.FamilyVerifyCommandActivity;
import com.android.banana.groupchat.message.GroupChatNotificationActivity;
import com.android.banana.groupchat.msglist.GroupChatMessageHelper;
import com.android.banana.groupchat.msglist.realm.MyRealmHelper;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.view.BadgeView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.activity.MainActivity;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.im.ImGroupManager;
import com.jl.jczj.im.tim.TimMessageType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiaomu on 2017/12/4.
 * <p>
 * 首页消息列表
 */

public class GroupChatMessageController extends BaseController4JCZJ<MainActivity> implements View.OnClickListener, IMTimCallback, OnHttpResponseListener {
    private ImageView mAddGroupIv;
    private RecyclerView mRecyclerView;
    private ImageView mEmptyIv;
    private MsgAdapter mMsgAdapter;

    private MyRealmHelper myRealmHelper;
    private GroupChatMessageHelper groupChatMessageHelper;
    private HttpRequestHelper httpRequestHelper;

    private boolean pause = false;//是否执行过onPause
    private boolean start = false;//是否已经开始循环
    private boolean firstRequestFailed = false;
    private static final int delayTime = 3000;
    private static final int interval = 6000;
    private Handler mHandler = new Handler();
    private MessageChangedListener mMsgChangedListener;

    public GroupChatMessageController(MessageChangedListener messageChangedListener) {
        this.mMsgChangedListener = messageChangedListener;
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setNoAttachContentView(parent, R.layout.layout_home_header_msgrecyclerview);
        EventBus.getDefault().register(this);
        ImGroupManager.getInstance().addTimCallback(this);
    }

    @Override
    public void onSetUpView() {
        myRealmHelper = new MyRealmHelper();
        groupChatMessageHelper = new GroupChatMessageHelper(myRealmHelper);

        mAddGroupIv = findViewOfId(R.id.group_add);
        mRecyclerView = findViewOfId(R.id.msg_recyclerview);
        mMsgAdapter = new MsgAdapter(getContext(), groupChatMessageHelper.showMessageList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        mRecyclerView.setAdapter(mMsgAdapter);
        //LinearSnapHelper helper = new LinearSnapHelper();
      //  helper.attachToRecyclerView(mRecyclerView);

        mEmptyIv = findViewOfId(R.id.empty_iv);
        mAddGroupIv.setOnClickListener(this);
        setBottomRedPointTip();
    }

    @Override
    public void onSetUpData() {
        httpRequestHelper = new HttpRequestHelper(getActivity(), this);
        showEmptyIvViewIfNeed();

        getAllGroupId();
        getSecretAndSystemMessage();//轮循私聊和通知数据
        mHandler.postDelayed(newMessageNotifyTask, delayTime);
    }

    //我加入的群聊和置顶信息
    private void getAllGroupId() {
        RequestFormBody requestFormBody = new RequestFormBody(JczjURLEnum.USER_JOINED_GROUP_QUERY, true);
        httpRequestHelper.addRequest(requestFormBody);
    }

    //请求私聊数据
    private void getSecretAndSystemMessage() {
        RequestFormBody requestFormBody = new RequestFormBody(JczjURLEnum.SOLE_MESSAGE_COUNT_QUERY, true);
        httpRequestHelper.addRequest(requestFormBody);
    }

    /**
     * 进入界面第一次请求结束后，开始轮循
     */
    private void startMessageLoop() {
        if (!start && !pause) {//没有开启轮循并且没有暂停过
            start = true;
            mHandler.postDelayed(simpleChatLoopRunnable, interval);
        }
    }

    private Runnable newMessageNotifyTask = new Runnable() {
        @Override
        public void run() {
            if (groupChatMessageHelper.saveExistMessageList != null && groupChatMessageHelper.saveExistMessageList.size() > 0) {
                notifyAndClearData();
            }
            mHandler.postDelayed(this, delayTime);
        }
    };

    private Runnable simpleChatLoopRunnable = new Runnable() {
        @Override
        public void run() {
            getSecretAndSystemMessage();
            mHandler.postDelayed(this, interval);
        }
    };

    private void notifyAndClearData() {
        if (groupChatMessageHelper.saveExistMessageList != null) {
            groupChatMessageHelper.saveExistMessageList.clear();
        }

        mMsgAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getActivity(), FamilyVerifyCommandActivity.class));
    }

    @Override
    public void onTimNewMessage(ChatMsgBody msgBody) {
        if (TextUtils.equals(TimMessageType.USER_QUIT_GROUP_TEXT, msgBody.typeCode)) {
            return;
        } else {
            if (groupChatMessageHelper.addNewGroupMessage(msgBody)) {
                showEmptyIvViewIfNeed();
                notifyAndClearData();
                setBottomRedPointTip();
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

    @Subscribe
    public void onGroupChatJoinExitEvent(GroupChatJoinExitEvent event) {
        if (event.exit) {
            for (int i = 0; i < groupChatMessageHelper.showMessageList.size(); i++) {
                String groupIdAndType = LoginInfoHelper.getInstance().getUserId() + "_" + event.groupId + "_" + GroupChatMessageType.GROUP_CHAT.name();
                if (groupIdAndType.equals(groupChatMessageHelper.showMessageList.get(i).getGroupIdAndType())) {
                    myRealmHelper.delete(groupIdAndType);
                    groupChatMessageHelper.showMessageList.remove(i);
                    notifyAndClearData();
                    setBottomRedPointTip();
                    return;
                }
            }
        }
    }

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
                myRealmHelper.add(bean);//保存
                groupChatMessageHelper.sortList();
            }
            notifyAndClearData();
        }
    }

    //    更新本地数据库
    private void updateLocalData(MessageListBean bean) {
        myRealmHelper.update(bean);
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        JczjURLEnum urlEnum = (JczjURLEnum) requestContainer.getRequestEnum();
        switch (urlEnum) {
            case PAYLOAD_ORDER_BY_USER_SET:
            case PAYLOAD_ORDER_BY_USER_DISABLED:
            case MODIFY_MESSAGE_LAST_READ_TO_NEWEST:
                break;
            case SOLE_MESSAGE_COUNT_QUERY:
                startMessageLoop();
                if (groupChatMessageHelper.requestSuccessMessageListRequest(jsonObject)) {
                    notifyAndClearData();
                    setBottomRedPointTip();
                    showEmptyIvViewIfNeed();
                }
                break;
            case USER_JOINED_GROUP_QUERY:
                groupChatMessageHelper.responseSuccessMessageCount(jsonObject);
                break;
        }
    }

    private void showEmptyIvViewIfNeed() {
        if (groupChatMessageHelper.showMessageList != null && groupChatMessageHelper.showMessageList.size() > 0) {
            mEmptyIv.setVisibility(View.GONE);
        } else {
            mEmptyIv.setVisibility(View.VISIBLE);
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

    public void errTipShow(JSONObject jo) {
        try {
            ((BaseActivity) getActivity()).operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            if (groupChatMessageHelper.setOpenStateFalseAndNotify()) {
                notifyAndClearData();
            }
        }
    }


    public void onResume() {
        super.onResume();
        if (!start && pause) {//第一次onResume不执行（所以判断一下是否暂停过）
            pause = false;
            myRealmHelper.initRealm();
            if (groupChatMessageHelper.setOpenStateFalseAndNotify()) {
                notifyAndClearData();
            }
            getSecretAndSystemMessage();
        }
    }

    @Override
    public void onDestroy() {
        ImGroupManager.getInstance().removeTimCallback(this);
        myRealmHelper.close();
        groupChatMessageHelper.unregister();
        mHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        start = false;
        pause = true;//暂停过要重新请求
        firstRequestFailed = false;
        mHandler.removeCallbacks(simpleChatLoopRunnable);
    }

    class MsgAdapter extends RecyclerView.Adapter<ViewHolder> {
        private Context mContext;
        private List<MessageListBean> mMsgList;

        public MsgAdapter(Context context, List<MessageListBean> msgList) {
            mContext = context;
            mMsgList = msgList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_msg_recyclerview, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final MessageListBean msgBean = mMsgList.get(position);
            if (msgBean.isSystemMessage()) {
                holder.setImageDrawable(R.id.item_msg_iv, ContextCompat.getDrawable(mContext, R.drawable.icon_chat_room_system_notify));
            } else {
                holder.setImageByUrl(mContext, R.id.item_msg_iv, msgBean.getPhotoUrl(), R.drawable.user_default_logo);
            }

            holder.setText(R.id.item_msg_tv, msgBean.getRoomName());
            BadgeView badgeView = holder.getView(R.id.badgeView);
            badgeView.setBadgeNum(msgBean.getUnreadMessageCount());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (GroupChatMessageType.safeValueOf(msgBean.getGroupChatMessageType())) {
                        case GROUP_CHAT:
                            GroupChatActivity.start2GroupChatActivity(getActivity(), msgBean.getGroupId(), msgBean.getRoomName(),msgBean.getPhotoUrl());
                            break;
                        case SECRET_CHAT:
                            SimpleChatActivity.startSimpleChatActivity(getActivity(), msgBean.getSendUserId(), msgBean.getSendUserLoginName(), msgBean.getSoleId());
                            break;
                        case SYSTEM_MESSAGE:
                            startActivity(new Intent().setClass(getActivity(), GroupChatNotificationActivity.class));
                            break;
                    }
                    msgBean.setUnreadMessageCount(0);
                    updateLocalData(msgBean);
                    notifyAndClearData();
                    setBottomRedPointTip();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMsgList == null ? 0 : mMsgList.size();
        }

    }

    private void setBottomRedPointTip() {
        int messageCount = 0;
        for (int i = 0; i < groupChatMessageHelper.showMessageList.size(); i++) {
            messageCount = messageCount + groupChatMessageHelper.showMessageList.get(i).getUnreadMessageCount();
        }
        if (mMsgChangedListener != null)
            mMsgChangedListener.onMsgCountChanged(messageCount);
    }

    public interface MessageChangedListener {
        void onMsgCountChanged(int count);
    }
}
