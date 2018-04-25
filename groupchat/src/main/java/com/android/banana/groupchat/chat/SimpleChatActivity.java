package com.android.banana.groupchat.chat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import com.android.banana.BuildConfig;
import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowSimpleMessageDialog;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.groupchat.bean.SimpleChatConfigBean;
import com.android.banana.groupchat.groupchat.RewardActivity;
import com.android.banana.groupchat.groupchat.chat.ChatAdapter;
import com.android.banana.groupchat.groupchat.chat.MessageSupport;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.recyclerview.PullRecycler;
import com.android.banana.pullrecycler.recyclerview.onRefreshListener;
import com.android.banana.utils.KeyboardHelper;
import com.android.banana.view.ImLayout;
import com.android.banana.view.MenuLayout;
import com.android.banana.view.TapLinearLayout;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.etiennelawlor.imagegallery.library.entity.StoreSelectBean;
import com.etiennelawlor.imagegallery.library.fullscreen.ImageGalleryActivity;
import com.jl.jczj.im.MessageType;
import com.jl.jczj.im.bean.ChatMsgBody;
import com.jl.jczj.im.bean.ChatMsgBodyList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by qiaomu on 2017/11/3.
 */

public class SimpleChatActivity extends BaseActivity4Jczj implements IChatCallback, MenuLayout.onMenuItemClickListener, onRefreshListener, View.OnClickListener {
    private static final String TAG = "SimpleChatActivity";
    private static final int MENU_CLICK_IMAGE = 0;//点击展开菜单的图片选项
    PullRecycler mPullRecycler;
    ImLayout mImlayout;
    TapLinearLayout mTapLayout;

    private String targetUserId, targetUserName, loginUserId, soleId;
    private ArrayList<ChatMsgBody> msgList = new ArrayList<>();

    private ChatAdapter mAdapter;
    private SimpleChatConfigBean mSimpleChatConfig;
    private SimpleChatHttpHelper mHttpHelper;
    private long mMessageSequence;

    private String groupId;
    private String sourceType;

    public static void startSimpleChatActivity(Context from, String targetUserId,
                                               String tartgetUserName, String soleId) {
        startSimpleChatActivity(from, targetUserId, tartgetUserName, soleId, null, "NORMAL");
    }

    public static void startSimpleChatActivity(Context from, String targetUserId,
                                               String tartgetUserName, String soleId,
                                               String groupId, String sourceType) {
        Intent intent = new Intent(from, SimpleChatActivity.class);
        intent.putExtra("targetUserId", targetUserId);
        intent.putExtra("tartgetUserName", tartgetUserName);
        intent.putExtra("soleId", soleId);
        intent.putExtra("groupId", groupId);
        intent.putExtra("sourceType", sourceType);
        from.startActivity(intent);
    }

    @Override
    protected void initEnv() {
        super.initEnv();
        loginUserId = LoginInfoHelper.getInstance().getUserId();
        targetUserId = getIntent().getStringExtra("targetUserId");
        targetUserName = getIntent().getStringExtra("tartgetUserName");
        soleId = getIntent().getStringExtra("soleId");
        groupId = getIntent().getStringExtra("groupId");//直播那边过来的为空
        sourceType = getIntent().getStringExtra("sourceType");

//        LogUtils.e("kk",groupId);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_simple_chat, targetUserName, -1, MODE_BACK);
    }

    @Override
    protected void setUpView() {
        mImlayout = findViewOfId(R.id.imlayout);
        mTapLayout = findViewOfId(R.id.rootLayout);
        mPullRecycler = findViewOfId(R.id.PullRecycler);

        MyLinearLayoutManager manager = new MyLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ChatAdapter(this, msgList, new MessageSupport(loginUserId));
        mPullRecycler.setLayoutManger(manager);
        mPullRecycler.setAdapter(mAdapter);
        mPullRecycler.setEnableLoadMore(false);
        mPullRecycler.setOnRefreshListener(this);
        mPullRecycler.setPostRefresh();

        //StatusBarCompat.fix4ImmersiveStatusBar(mTapLayout);
        KeyboardHelper.postDelayAvoidFlashingScreen4Input(this, mImlayout, mTapLayout, mPullRecycler);

        // mImlayout.enableTextChanged(false);
        mImlayout.setOnButtonClickListener(this);
        mImlayout.setonMenuClickListener(this);
        mImlayout.addGroupMenuOne();
    }

    @Override //1.创建私聊聊天室
    protected void setUpData() {
        mHttpHelper = new SimpleChatHttpHelper(this, targetUserId, loginUserId, soleId);
        mHttpHelper.initUserSole(groupId, sourceType);
    }

    @Override
    public void onRefresh(boolean refresh) {
        mHttpHelper.queryUnreadMsg(true, mMessageSequence);
    }

    @Override
    public void onInitSuccess(SimpleChatConfigBean simpleChatConfig, JSONObject jsonError) throws JSONException {
        if (jsonError != null || simpleChatConfig == null) {
            operateErrorResponseMessage(jsonError);
            mPullRecycler.setRefreshing(false);
            return;
        }
        this.soleId = simpleChatConfig.soleId;
        mSimpleChatConfig = simpleChatConfig;
        mImlayout.enableTouchEvent(true);
        mHttpHelper.queryUnreadMsg(false, 0);
        mHttpHelper.startLoop();

        //修改 因为KeyboardHelper封装,点击 R.id.more 时 会弹起展开菜单 ,而私聊里需要跳转到打赏,所以改id就好。
        RadioButton button = (RadioButton) mImlayout.findViewById(R.id.more);
        if (mSimpleChatConfig.canCoinReward) {
            button.setId(R.id.im_coin);
            button.setBackgroundResource(R.drawable.ic_reward_shang);
        }
//        else {
//            mImlayout.enableTextChanged(false);
//        }

    }

    @Override
    public void onNewMessages(ChatMsgBodyList bodyList) {
        if (bodyList == null) {
            mPullRecycler.setRefreshing(false);
            return;
        }
        int countBeforeUpdate = mAdapter.getItemCount();
        boolean scrollDown = mPullRecycler.getLastVisibleItemPosition() == countBeforeUpdate - 1;
        boolean refresh = mPullRecycler.isRefreshing();
        boolean isNewListNotEmpty = msgList != null && bodyList.messages != null && bodyList.messages.size() > 0;
        if (isNewListNotEmpty) {
            if (refresh || msgList.size() == 0) {//手动下拉或者第一次进来
                mMessageSequence = bodyList.messages.get(0).messageSequence;
                msgList.addAll(0, bodyList.messages);
            } else {//轮循过来的
                int size = bodyList.messages.size();
                ArrayList<ChatMsgBody> deleteIfNeeded = new ArrayList<>();
                deleteIfNeeded.clear();
                for (int i = 0; i < size; i++) {
                    ChatMsgBody chatMsgBody = bodyList.messages.get(i);
                    if (TextUtils.equals(chatMsgBody.sendUserId, loginUserId)
                            && !TextUtils.equals(chatMsgBody.typeCode, MessageType.COIN_REWARD_NOTICE))
                        deleteIfNeeded.add(chatMsgBody);
                }
                bodyList.messages.removeAll(deleteIfNeeded);
                msgList.addAll(bodyList.messages);
            }

            //首次进入如果列表为空，自动调用下拉加载更多 ，仅此一次
        } else if (mMessageSequence == 0 && msgList != null && mMessageSequence != bodyList.lastReadMessageSequence && msgList.size() == 0) {
            mMessageSequence = bodyList.lastReadMessageSequence + 1;
            mPullRecycler.setRefreshing(true);
            onRefresh(true);
            return;
        }
        mPullRecycler.setEnableRefresh(true);
        mPullRecycler.setRefreshing(false);
        isNewListNotEmpty = msgList != null && bodyList.messages != null && bodyList.messages.size() > 0;
        if (!isNewListNotEmpty)
            return;
        mAdapter.notifyDataSetChanged();
        boolean active = KeyboardHelper.isActive(this);
        mPullRecycler.keepPosition(refresh, countBeforeUpdate, scrollDown, active);
    }

    @Override
    public void onMsgSendResult(String newMsgId, int adapterPos, JSONObject errorJson, long messageSequence) throws JSONException {
        if (errorJson != null)
            operateErrorResponseMessage(errorJson);

        if (!TextUtils.equals(newMsgId, "-1"))
            mAdapter.updateMessageId(adapterPos, newMsgId);

        if (this.mMessageSequence == 0 && messageSequence > 0)
            this.mMessageSequence = messageSequence;
    }

    @Override
    public void onClick(View v) {
        if (mSimpleChatConfig == null) {
            if (BuildConfig.DEBUG)
                showToast(getString(R.string.debug_user_authority_query_falied));
            return;
        }
        if (v.getId() == R.id.sendMsg) {
            if (checkChatConfig())
                return;
            String newMsg = mImlayout.getNewMsg();
            ChatMsgBody newMsgBody = mAdapter.addNormalMessage(newMsg, null);
            msgList.add(newMsgBody);
            int position = mAdapter.getItemCount() - 1;
            mPullRecycler.scrollToPosition(true);

            mHttpHelper.sendMsg(MessageType.TXT, newMsg, position, null);

        } else if (v.getId() == R.id.emoji || v.getId() == R.id.more) {
            KeyboardHelper.postDelayAvoidFlashingScreen4Menu(SimpleChatActivity.this, mImlayout, v.getId(), mPullRecycler);
        } else if (v.getId() == R.id.im_coin) {
            RewardActivity.startRewardActivity(this, targetUserId, mSimpleChatConfig.identifyId);

        }
    }

    private void start2ImageGalleryActivity() {
        Intent intent = new Intent(this, ImageGalleryActivity.class);
        intent.putExtra("maxInsert", 1);
        intent.putExtra("operation", 1);
        startActivity(intent);
    }

    private boolean checkChatConfig() {
        if (TextUtils.isEmpty(mSimpleChatConfig.identifyId)) {
            ShowSimpleMessageDialog dialog = new ShowSimpleMessageDialog(this, String.format(getString(R.string.simple_chat_indentifyed_null), mSimpleChatConfig.identifyId), new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    mHttpHelper.onDestroy();
                    SimpleChatActivity.this.finish();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (msgList != null && msgList.size() > 0 && !TextUtils.isEmpty(soleId)) {
            ChatMsgBody chatMsgBody = msgList.get(msgList.size() - 1);
            chatMsgBody.setTargetUserIdName(targetUserId, targetUserName);
            chatMsgBody.setSoleId(soleId);
            EventBus.getDefault().post(chatMsgBody);
        }
        mHttpHelper.onDestroy();
    }

    //图片选择完成后，发送图片文件消息,这个消息是从ImageGalleryActivity  post过来的
    @Subscribe(priority = 99)
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

        EventBus.getDefault().cancelEventDelivery(bean);
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
//                showToast("图片压缩失败"+(e==null?"空":e.toString())+e.getMessage());
            }
        }).launch();
    }

    void sendImageMessage(String targetPath) {
        ChatMsgBody newMsgBody = mAdapter.addImageMessage(targetPath, groupId);
        msgList.add(newMsgBody);
        int position = mAdapter.getItemCount() - 1;
        mPullRecycler.scrollToPosition(false);
        mPullRecycler.scrollToTopIfPossible(position, false, false);
        mHttpHelper.sendMsg(MessageType.IMAGE, "", position, targetPath);
    }

    /*展开菜单*/
    @Override
    public void onMenuMoreItemClick(int position) {
        switch (position) {
            case MENU_CLICK_IMAGE://点击发图片，进行权限检测
                start2ImageGalleryActivity();
                break;
        }
    }

}
