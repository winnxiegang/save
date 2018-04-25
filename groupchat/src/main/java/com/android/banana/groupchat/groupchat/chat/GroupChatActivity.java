package com.android.banana.groupchat.groupchat.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.banana.BuildConfig;
import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.coupon.SendCouponPop;
import com.android.banana.commlib.coupon.SendCouponSuccessDialog;
import com.android.banana.commlib.coupon.SendCouponSuccessListener;
import com.android.banana.commlib.utils.StatusBarCompat;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.PageIndicatorView;
import com.android.banana.event.JczjMessageEvent;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.ChatConfigBean;
import com.android.banana.groupchat.bean.GroupChatSimpleInfo;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.banana.groupchat.chatenum.AuthorityStateQueryType;
import com.android.banana.groupchat.groupchat.GroupChatHttpHelper;
import com.android.banana.groupchat.groupchat.setInfo.GroupInfoActivity;
import com.android.banana.groupchat.ilistener.IMGroupChatCallback;
import com.android.banana.groupchat.view.baselist.TopicRecyclerView3;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.TimelineDecoration;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ImLayout;
import com.android.banana.view.MarqueeText;
import com.android.banana.view.MenuLayout;
import com.android.banana.view.TapLinearLayout;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.fullscreen.ImageGalleryActivity;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.bean.ChatMsgBodyList;
import com.jl.jczj.im.bean.IMParams;
import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.im.ImGroupManager;
import com.jl.jczj.im.im.ImManager;
import com.jl.jczj.im.tim.TimMessageType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by qiaomu on 2017/5/26.
 */

public class GroupChatActivity extends BaseActivity4Jczj implements IMGroupChatCallback, MenuLayout.onMenuItemClickListener, IMCallback, onRefreshListener, View.OnClickListener, IMTimCallback, SendCouponSuccessListener {
    private static final int MENU_CLICK_IMAGE = 0;//点击展开菜单的图片选项
    private static final int MENU_CLICK_COUPON = 1;//点击展开菜单中的红包选项

    private static final int POST_DELAY_4MATCH_TOPIC = 100;//获取 头部主题
    private static final int POST_DELAY_4DIMISS_COUPON = 101;//3s 隐藏红包提示
    private static final int POST_DELAY_4TIM_MESSAGE = 104;//轮询取推送过来本地集合中的消息，展示到界面列表中

    public static final int ACTIVITY_SEND_COUPON = 102;//发红包返回本界面
    public static final int ACTIVITY_RESULT_GROUP_SETTING = 103;//修改公告

    private static long TIM_INTERVAL = 350L;//从推送来的集合中取数据时间间隔

    TapLinearLayout mTapParent;
    MarqueeText mAdvertiseTv;
    TextView mCouponTipTv;
    PullRecycler mPullRecycler;
    ImLayout mImLayout;
    TopicRecyclerView3 mTopicRecyclerView;
    ImageView mFloatIv;
    FrameLayout mTopicLayout;
    LinearLayout mFolderLayout;
    TextView mFolderScoreTv;
    TextView mFolderNameTv;
    ImageView mFolderIv;
    FrameLayout mTitleLayout;

    ImageView mTitleBackIv, mTitleZoneIv, mTitleMoreIv;
    TextView mTitleTv;

    private MyLinearLayoutManager manager;
    private ArrayList<ChatMsgBody> mDatas = new ArrayList<>();
    private ArrayList<ChatMsgBody> tempList = new ArrayList<>();
    private ChatAdapter mChatAdapter;
    private GroupChatHttpHelper httpHelper;

    private ChatConfigBean mChatConfig;
    private GroupChatSimpleInfo mGroupInfo;
    private long messageSequence;
    private String groupId;
    private String loginKey, loginUserId, chatRoomId, chatRoomName, chatLogo;
    private LoopHandler loopHandler;
    private PageIndicatorView indicatorView;

    static class LoopHandler extends Handler {
        private WeakReference<GroupChatActivity> mReference = null;

        public LoopHandler(GroupChatActivity reActivity) {
            mReference = new WeakReference(reActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mReference == null || mReference.get() == null)
                return;
            GroupChatActivity referenceActivity = this.mReference.get();
            switch (msg.what) {
                case POST_DELAY_4DIMISS_COUPON:
                    referenceActivity.mCouponTipTv.animate().translationX(referenceActivity.mCouponTipTv.getMeasuredWidth()).start();
                    break;
                case POST_DELAY_4MATCH_TOPIC:
                    referenceActivity.httpHelper.queryChatRoomTopic(referenceActivity.mGroupInfo.groupChatId);
                    break;
                case POST_DELAY_4TIM_MESSAGE:
                    referenceActivity.popTimNewMessages();
                    break;
            }
        }
    }

    @Override
    protected void initEnv() {
        loginUserId = LoginInfoHelper.getInstance().getUserId();
        loginKey = LoginInfoHelper.getInstance().getUserLoginKey();
        groupId = getIntent().getStringExtra("groupId");
        chatRoomName = getIntent().getStringExtra("chatRoomName");
        chatLogo = getIntent().getStringExtra("logo");
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_group_chat_new_1);
        indicatorView = findViewOfId(R.id.indicatorView);
        mTapParent = findViewOfId(R.id.rootLayout);
        mAdvertiseTv = findViewOfId(R.id.advertiseTv);
        mCouponTipTv = findViewOfId(R.id.couponRecordShowTv);
        mPullRecycler = findViewOfId(R.id.PullRecycler);
        View decorView = getWindow().getDecorView();
        mTopicRecyclerView = findViewOfId(R.id.topicRecyclerView);
        mTopicLayout = findViewOfId(R.id.topic_layout);
        mFolderLayout = findViewOfId(R.id.topic_folder_layout);
        mFolderScoreTv = findViewOfId(R.id.topic_score);
        mFolderNameTv = findViewOfId(R.id.topic_name);
        mFolderIv = findViewOfId(R.id.topic_expand);

        mTitleBackIv = findViewOfId(R.id.title_back);
        mTitleZoneIv = findViewOfId(R.id.title_zone);
        mTitleMoreIv = findViewOfId(R.id.title_more);
        mTitleTv = findViewOfId(R.id.title_title);
        mTitleLayout = findViewOfId(R.id.title_layout);

        mImLayout = findViewOfId(R.id.imlayout);
        mFloatIv = findViewOfId(R.id.float_iv);

        mFloatIv.setOnClickListener(this);
        mFolderIv.setOnClickListener(this);
        mTitleBackIv.setOnClickListener(this);
        mTitleZoneIv.setOnClickListener(this);
        mTitleMoreIv.setOnClickListener(this);
    }

    //初始化view视图
    @Override
    protected void setUpView() {
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.half_transparent_4statusbar));
        StatusBarCompat.fitsSystemWindows(mTitleLayout);
        StatusBarCompat.fix4ImmersiveStatusBar(mTapParent);
        /*避免弹起软键盘时闪屏*/
        KeyboardHelper.postDelayAvoidFlashingScreen4Input(this, mImLayout, mTapParent, mPullRecycler);
        mTitleTv.setText(chatRoomName);

        mImLayout.setOnButtonClickListener(this);
        mImLayout.setonMenuClickListener(this);
        mImLayout.addGroupMenu();

        manager = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPullRecycler.setLayoutManger(manager);
        mChatAdapter = new ChatAdapter(this, mDatas, new MessageSupport(loginUserId));
        mPullRecycler.setAdapter(mChatAdapter);
        mPullRecycler.addItemDecoration(new TimelineDecoration(this, mChatAdapter));
        mPullRecycler.setEnableRefresh(true);
        mPullRecycler.setEnableLoadMore(false);
        mPullRecycler.setOnRefreshListener(this);
        mTopicRecyclerView.attachToRecyclerView(mPullRecycler.getRecyclerView());
        mTopicRecyclerView.attachToIndicatorView(indicatorView);
        loopHandler = new LoopHandler(this);

    }

    //创建网络请求工具类
    @Override
    protected void setUpData() {
        httpHelper = new GroupChatHttpHelper(this, groupId);
        httpHelper.enterRoomOperate();//jczj
    }

    @Override
    public void onRefresh(boolean refresh) {
        ImGroupManager.getInstance().getMessageList(refresh, messageSequence);
    }

    //发送按钮 表情按钮的点击
    @Override
    public void onClick(View v) {
        if (mGroupInfo == null)
            return;
        int id = v.getId();
        if (id == R.id.sendMsg) {

            httpHelper.checkChatRoomConfig(R.id.sendMsg, mGroupInfo.groupChatId, AuthorityStateQueryType.ActionType.RELATION_GROUP_POST_TEXT);

        } else if (id == R.id.emoji || id == R.id.more) {

            KeyboardHelper.postDelayAvoidFlashingScreen4Menu(this, mImLayout, id, mPullRecycler);

        } else if (id == R.id.float_iv) {
            Intent intent = new Intent("com.android.xjq.betting_game");
            startActivity(intent);
        } else if (id == R.id.title_back) {
            onBackPressed();
        } else if (id == R.id.title_more) {
            GroupInfoActivity.startGroupInfoActivity(this, mGroupInfo.groupId, mGroupInfo.groupChatId);
        } else if (id == R.id.title_zone) {
            Intent intent = new Intent("com.android.banana.zone");
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", chatRoomName);
            intent.putExtra("groupMemo", mGroupInfo.memo);
            intent.putExtra("groupLogoUrl", chatLogo);
            startActivity(intent);
        } else if (id == R.id.topic_expand) {
            mTopicRecyclerView.startAnimation(mTopicLayout, mFolderLayout, mFolderScoreTv, mFolderNameTv, mFolderIv);
        }

    }

    //展开菜单按钮
    @Override
    public void onMenuMoreItemClick(int position) {
        switch (position) {
            case MENU_CLICK_IMAGE:
                httpHelper.checkChatRoomConfig(R.id.im_image, mGroupInfo.groupChatId, AuthorityStateQueryType.ActionType.RELATION_GROUP_POST_PICTURE);
                break;
            case MENU_CLICK_COUPON:
                httpHelper.checkChatRoomConfig(R.id.im_coupon, mGroupInfo.groupChatId, AuthorityStateQueryType.ActionType.RELATION_GROUP_POST_COUPON);
                break;
        }
    }

    //进入房间获取配置,error不为空说明出错了
    @Override
    public void onEnterChatRoomOperateResult(ChatConfigBean chatConfig, ErrorBean error, JSONObject object) {
        if (error != null && error.getError() != null) {
            if("USER_NOT_JOINED_GROUP".equals(error.getError().getName())){
                showConfirmDialog(error.getError().getMessage(), error.isJumpLogin());
            }else{
                ToastUtil.show(this,error.getError().getMessage(),2000);
            }
            return;
        }
        //初始化聊天室参数
        this.mGroupInfo = chatConfig.groupInfo;
        this.mChatConfig = chatConfig;
        mChatAdapter.setChatAdapterHelper(new ChatAdapterHelper(mChatConfig.userRoleCode, ChatAdapterHelper.ChatType.GROUP_CHAT));
        mChatAdapter.setUserIdAndRoleCodeMap(mChatConfig.userIdAndRoleCodeMap);
        mChatAdapter.setGroupChatId(mGroupInfo.groupChatId);
        mChatAdapter.setUserNickName(mGroupInfo.nickName);
        if (chatConfig.groupMessageSendAuthority != null) {
            //说明不能发言了
            mImLayout.setInputLayoutEnable(chatConfig.groupMessageSendAuthority.getMessage(), false, Gravity.CENTER);
        } else {
            //根据权限设置房间状态
            mImLayout.changeGroupChatStatus(mChatConfig);
        }

        //跑马灯公告
        mAdvertiseTv.setVisibility(TextUtils.isEmpty(mGroupInfo.notice) ? View.INVISIBLE : View.VISIBLE);
        mAdvertiseTv.setMarqueeText(mGroupInfo.notice);

        //是否显示右侧有红包标签
        if (mChatConfig.showCoupon) {
            mCouponTipTv.setVisibility(View.VISIBLE);
            mCouponTipTv.setX(LibAppUtil.getScreenWidth(this));
            mCouponTipTv.animate().translationX(0).setDuration(1500).start();
            //3s隐藏有红包提示
            loopHandler.sendEmptyMessageDelayed(POST_DELAY_4DIMISS_COUPON, 3000);
        }

        httpHelper.checkIMLogin(mGroupInfo.groupId);//im
        if (mGroupInfo.openThemeMode) {
            httpHelper.queryChatRoomTopic(mGroupInfo.groupChatId);//jczj
        }
        popTimNewMessages();
        EventBus.getDefault().post(new JczjMessageEvent(groupId));
    }

    //用户进入房间
    @Override
    public void onUserAuthorityLoginQueryResult(ChatConfigBean loginConfig) {
        mPullRecycler.setRefreshing(false);
        mImLayout.enableTouchEvent(true);
        IMParams.initParams(loginConfig.id, loginConfig.authKey, mGroupInfo.groupId, loginUserId, loginKey, loginConfig.signKey, loginConfig.identifyId);
        ImManager.getInstance().init(this);
        ImGroupManager.getInstance().addTimCallback(this);
        onRefresh(false);
    }

    //头部主题查询
    @Override
    public void onChatTopicQueryResult(GroupChatTopic1 chatTopic, JSONObject errorObj) throws JSONException {
        operateErrorResponseMessage(errorObj, true);
        if (errorObj == null && chatTopic != null) {
            mTopicLayout.setVisibility((chatTopic.gameBoardList == null || chatTopic.gameBoardList.size() <= 0) ? View.GONE : View.VISIBLE);
            mTopicRecyclerView.setTopic(chatTopic);
            boolean gameValidate = mTopicRecyclerView.isGameValidate();
            if (gameValidate) {
                loopHandler.removeMessages(POST_DELAY_4MATCH_TOPIC, null);
                loopHandler.sendEmptyMessageDelayed(POST_DELAY_4MATCH_TOPIC, 3000);
            }
        }
    }

    //发言发图片权限查询
    @Override
    public void onUserAuthorityQueryResult(RequestContainer request, ChatConfigBean newConfig, JSONObject jsonError) throws JSONException {
        if (mChatConfig == null) {
            if (BuildConfig.DEBUG)
                showToast(getString(R.string.debug_user_authority_query_falied));
            return;
        }
        //因为发红包,发消息之前都要先调用 权限检测接口,
        //回调到这里,那么为了区分是点击了哪一个,这里用clickViewId来判断
        int clickViewId = request.getInt("clickViewId");
        mChatConfig.groupChatOpen = newConfig.groupChatOpen;

        //error 不为空，说明权限检测有错,可能的情况很多,比如禁止发言,禁言，等级限制
        if (jsonError != null) {
            boolean consume = mImLayout.changeGroupChatStatus(newConfig);
            if (!consume) operateErrorResponseMessage(jsonError);
            KeyboardHelper.dispatchViewTouch(0, 0, mImLayout);
            return;
        }

        if (!mChatConfig.groupChatOpen || newConfig.error != null)
            return;

        if (clickViewId == R.id.im_coupon) {

            showCouponPop();

        } else if (clickViewId == R.id.sendMsg) {

            sendTxtMessage();

        } else if (clickViewId == R.id.im_image) {

            start2ImageGalleryActivity();
        }
    }

    //发送普通文本消息
    private void sendTxtMessage() {
        String newMsg = mImLayout.getNewMsg();
        ChatMsgBody newMsgBody = mChatAdapter.addNormalMessage(newMsg, mGroupInfo.groupId);
        mDatas.add(newMsgBody);

        final int position = mChatAdapter.getItemCount() - 1;
        mPullRecycler.scrollToPosition(false);
        mPullRecycler.scrollToTopIfPossible(position, false, false);
        httpHelper.sendMessage(MessageType.TXT, newMsg, position);
        //ImGroupManager.getInstance().sendMessage(MessageType.Type.TXT, newMsg, position);
    }

    //文本发送结果
    @Override
    public void onMessageSendResult(String msgId, long messageSequence, int position, JSONObject errorJson) throws JSONException {
        if (errorJson != null) {
            operateErrorResponseMessage(errorJson);
            return;
        }
        if (position >= 0 && !TextUtils.isEmpty(msgId))
            mChatAdapter.updateMessageId(position, msgId);

        if (this.messageSequence == 0 && messageSequence > 0)
            this.messageSequence = messageSequence;
    }

    //弹出发红包的框框
    private void showCouponPop() {
        SendCouponPop mCouponPop = new SendCouponPop(this, 1, "GROUP_CHAT", mGroupInfo.groupChatId);
        mCouponPop.setSendCouponSuccessListener(this);
        KeyboardHelper.updateSoftInputMode(GroupChatActivity.this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mImLayout.clearFocus();
        mImLayout.emojiEditv.setFocusable(false);
        mImLayout.emojiEditv.setFocusableInTouchMode(true);
        mCouponPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                loopHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        KeyboardHelper.updateSoftInputMode(GroupChatActivity.this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    }
                }, 200);
            }
        });
        mCouponPop.show();
    }

    //红包发送结果
    @Override
    public void sendCouponSuccess(String couponType, String couponId, String couponTitle) {
        ChatMsgBody chatMsgBody = mChatAdapter.addCouponMessage(couponTitle, couponId, mGroupInfo.groupId);
        mDatas.add(chatMsgBody);
        mPullRecycler.scrollToPosition(false);

        SendCouponSuccessDialog successDialog = new SendCouponSuccessDialog(this);
        successDialog.show(mTitleLayout);
    }

    //图片选择完成后，发送图片文件消息,这个消息是从ImageGalleryActivity  post过来的
    @Subscribe(priority = 1)
    public void onImageSelectResult(StoreSelectBean bean) {
        List<Photo> list = (List<Photo>) bean.getList();
        if (list == null || list.size() == 0)
            return;
        String filePath = list.get(0).getPath();
        File file = new File(filePath);
        if (TextUtils.isEmpty(filePath) || !file.exists()) {
            showToast(getString(R.string.file_not_found));
            return;
        }
        compress(filePath);
    }

    private void compress(final String filePath) {
        String targetDir = getExternalCacheDir().getAbsolutePath() + "/compress/";
        Luban.with(this).load(filePath).setTargetDir(null).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(File file) {
                Log.e(TAG, "onSuccess: " + file.length() / 1024);
                sendImageMessage(file.getAbsolutePath());
            }

            @Override
            public void onError(Throwable e) {
                showToast("图片压缩失败");
            }
        }).launch();
    }




    void sendImageMessage(String targetPath) {
        ChatMsgBody newMsgBody = mChatAdapter.addImageMessage(targetPath, groupId);
        mDatas.add(newMsgBody);
        int position = mChatAdapter.getItemCount() - 1;
        mPullRecycler.scrollToPosition(false);
        mPullRecycler.scrollToTopIfPossible(position, false, false);
        httpHelper.sendImageMessage(MessageType.IMAGE, targetPath, position);
    }

    //拉取到历史消息
    @Override
    public void onHistoryMessages(ChatMsgBodyList msgList) {
        if (messageSequence == 0 && msgList != null && messageSequence != msgList.lastReadMessageSequence && mDatas.size() == 0) {
            messageSequence = msgList.lastReadMessageSequence + 1;
            mPullRecycler.setRefreshing(true);
            onRefresh(true);
            return;
        }

        if (msgList == null || msgList.messages == null || msgList.messages.size() == 0) {
            mPullRecycler.setRefreshing(false);
            return;
        }
        List<ChatMsgBody> deleteList = new ArrayList<>();
        for (ChatMsgBody chatMsgBody : msgList.messages) {

            chatMsgBody.parseMedalInfos();
            chatMsgBody.parsePatronMedalInfos();

            String typeCode = chatMsgBody.typeCode;
            if (TextUtils.equals(typeCode, MessageType.IMAGE_VIEWABLE))
                deleteList.add(chatMsgBody);

        }
        msgList.messages.removeAll(deleteList);
        if (msgList.messages.size() <= 0)
            return;

        messageSequence = msgList.messages.get(0).messageSequence;
        int itemCount = mChatAdapter.getItemCount();
        boolean scrollBottom = mDatas.size() == 0;
        mDatas.addAll(0, msgList.messages);
        mPullRecycler.refreshCompleted();
        mPullRecycler.keepPosition(true, itemCount, scrollBottom, KeyboardHelper.isActive(this));
    }

    //推送过来的系统类型新消息
    @Override
    public void onTimSystemMessage(String groupId, String messageType) {
        if (!TextUtils.equals(groupId, groupId))
            return;
        if (TextUtils.equals(TimMessageType.USER_QUIT_GROUP_TEXT, messageType)) {
            showConfirmDialog(getString(R.string.user_quit_group_text), false);
            loopHandler.removeCallbacksAndMessages(null);
            return;
        }
    }

    //推送过来的新消息
    @Override
    public synchronized void onTimNewMessage(final ChatMsgBody msgBody) {
        if (msgBody == null || !TextUtils.equals(msgBody.groupId, this.groupId))
            return;
        msgBody.parseMedalInfos();
        boolean isSendByMe = TextUtils.equals(msgBody.sendUserId, loginUserId);

        if (isSendByMe) {
            mChatAdapter.updateMessage(msgBody, true);
            return;
        }

        //如果是图片消息需要更新对应的实体对象字段
        if (TextUtils.equals(msgBody.typeCode, MessageType.IMAGE_VIEWABLE)) {
            if (isSendByMe) {
                mChatAdapter.updateMessage(msgBody, false);
            } else {
                boolean needUpdateAdapter = true;
                for (ChatMsgBody chatMsgBody : tempList) {
                    if (TextUtils.equals(chatMsgBody.id, msgBody.bodies.get(0).parameters.imageMessageId)) {
                        chatMsgBody.bodies.get(0).parameters = msgBody.bodies.get(0).parameters;
                        needUpdateAdapter = false;
                        break;
                    }
                }

                if (needUpdateAdapter)
                    mChatAdapter.updateMessage(msgBody, false);

            }
            return;
        }

        //自己发送消息过滤掉
        if (isSendByMe || mDatas.contains(msgBody) || tempList.contains(msgBody))
            return;

        tempList.add(msgBody);
    }

    //从缓存中取消息展示在界面上
    private void popTimNewMessages() {
        if (isActivityFinished())
            return;
        loopHandler.sendEmptyMessageDelayed(POST_DELAY_4TIM_MESSAGE, TIM_INTERVAL);
        if (tempList == null || tempList.size() <= 0)
            return;

        final boolean scrollVertical = mPullRecycler.getRecyclerView().canScrollVertically(1);
        final boolean isBottomVisible = mPullRecycler.getLastVisibleItemPosition() >= mChatAdapter.getItemCount() - 1;
        int timMsgCount = tempList.size() > 5 ? 3 : 1;
        TIM_INTERVAL = tempList.size() > 5 ? 500L : 300L;

        for (int i = 0; i < timMsgCount; i++) {
            if (tempList == null || tempList.size() <= 0)
                break;
            ChatMsgBody remove = tempList.remove(0);
            mChatAdapter.addNewMsg(remove);
        }

        mPullRecycler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!scrollVertical || isBottomVisible) {
                    int itemCount = mChatAdapter.getItemCount();
                    mPullRecycler.scrollToPosition(false);
                    mPullRecycler.scrollToTopIfPossible(itemCount - 1, false, false);
                }
            }
        }, 300);
    }

    //任何接口的失败
    @Override
    public void onFailed(RequestContainer request, JSONObject error) throws JSONException {
        operateErrorResponseMessage(error);
        mPullRecycler.setRefreshing(false);
    }

    public static void start2GroupChatActivity(Activity from, String groupChatId, String chatRoomName, String logo) {
        Intent intent = new Intent(from, GroupChatActivity.class);
        intent.putExtra("groupId", groupChatId);
        intent.putExtra("chatRoomName", chatRoomName);
        intent.putExtra("logo", logo);
        from.startActivity(intent);
    }

    //去选择图片
    private void start2ImageGalleryActivity() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra("maxInsert", 1);
        intent.putExtra("operation", 1);
        startActivity(intent);
    }

    //修改主题
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == ACTIVITY_RESULT_GROUP_SETTING) {
            if (data == null) { //退出了聊天室
                this.finish();
            } else {
                boolean theme_query = data.getBooleanExtra("theme_query", false);
                boolean theme_enable = data.getBooleanExtra("theme_enable", mGroupInfo.openThemeMode);
                if (theme_query) {//修改了公告 ，主题模式
                    String notice = data.getStringExtra("notice");
                    mAdvertiseTv.setVisibility(TextUtils.isEmpty(notice) ? View.GONE : View.VISIBLE);
                    mAdvertiseTv.setMarqueeText(notice);
                    if (theme_enable) {
                        httpHelper.queryChatRoomTopic(mGroupInfo.groupChatId);
                        loopHandler.sendEmptyMessage(POST_DELAY_4MATCH_TOPIC);
                    } else {
                        mTopicRecyclerView.clear();
                        loopHandler.removeMessages(POST_DELAY_4MATCH_TOPIC, null);
                    }
                    mTopicRecyclerView.setVisibility(theme_enable ? View.VISIBLE : View.GONE);
                    mGroupInfo.openThemeMode = theme_enable;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mImLayout.onBackPressed())
            return;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImGroupManager.getInstance().removeImCallback(this);
        ImGroupManager.getInstance().removeTimCallback(this);
        loopHandler.removeCallbacksAndMessages(null);
        tempList.clear();
        tempList = null;
        EventBus.getDefault().post(new JczjMessageEvent(""));
        httpHelper.onDestroy();
    }
}
