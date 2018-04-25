package com.android.xjq.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.banana.commlib.coupon.SendCouponSuccessDialog;
import com.android.banana.commlib.coupon.SendCouponSuccessListener;
import com.android.banana.commlib.coupon.couponenum.CouponEnum;
import com.android.banana.commlib.coupon.couponenum.CouponPlatformType;
import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.utils.HhsUtils;
import com.android.banana.commlib.utils.NetworkUtils;
import com.android.banana.commlib.utils.PermissionUtil;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.DialogActivity.AccountQuitDialogActivity;
import com.android.xjq.activity.DialogActivity.LiveTransferNoticeActivity;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.bean.draw.DrawIssueEntity;
import com.android.xjq.bean.live.EnterRoomBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.main.PrizeCoreInfoBean;
import com.android.xjq.bean.live.main.gift.SendGiftResultBean;
import com.android.xjq.controller.ILiveRoomCallBack;
import com.android.xjq.controller.LiveRoomManager;
import com.android.xjq.controller.live.FetchCouponController;
import com.android.xjq.controller.live.GiftController;
import com.android.xjq.controller.live.InputCommentController;
import com.android.xjq.controller.live.LiveHeaderController;
import com.android.xjq.controller.live.LiveHttpRequestController;
import com.android.xjq.controller.live.LiveLandscapeController;
import com.android.xjq.controller.live.LivePortraitController;
import com.android.xjq.controller.live.LivePushMessageController;
import com.android.xjq.dialog.CurrentLiveListDialog;
import com.android.xjq.dialog.GeneralDialog;
import com.android.xjq.dialog.GiftDialog;
import com.android.xjq.dialog.LiveCouponDialog;
import com.android.xjq.dialog.base.BaseDialog;
import com.android.xjq.dialog.base.DialogBase;
import com.android.xjq.dialog.base.ViewConvertListener;
import com.android.xjq.dialog.base.ViewHolder;
import com.android.xjq.dialog.live.Config;
import com.android.xjq.dialog.live.LiveCheerDialog;
import com.android.xjq.dialog.live.LiveMoreFunctionDialog;
import com.android.xjq.dialog.live.LivePkDialog;
import com.android.xjq.dialog.live.LuckyDrawDialog;
import com.android.xjq.dialog.live.RocketFlyPop;
import com.android.xjq.dialog.popupwindow.FullScreenAnimPop;
import com.android.xjq.listener.live.RocketFlyListener;
import com.android.xjq.model.VideoPlayerTypeEnum;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.model.live.LiveCommentMessageTypeEnum;
import com.android.xjq.service.FloatingView;
import com.android.xjq.utils.MyTimerTask;
import com.android.xjq.utils.SharePopUpWindowHelper;
import com.android.xjq.view.CouponFrameLayout;
import com.qiniu.pili.droid.shortvideo.PLErrorCode;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLScreenRecordStateListener;
import com.qiniu.pili.droid.shortvideo.PLScreenRecorder;
import com.qiniu.pili.droid.shortvideo.PLScreenRecorderSetting;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
import static com.android.banana.commlib.eventBus.EventBusMessage.LIVE_ROOM_REFRESH;
import static com.android.banana.commlib.utils.NetworkUtils.NETWORK_MOBILE;


/**
 * Live直播类
 */
public class LiveActivity extends BaseActivity implements ILiveRoomCallBack, PLScreenRecordStateListener {

    public static final String TAG = LiveActivity.class.getSimpleName();

    private static final int UPDATE_CHANNEL_USER_COUNT_TASK = 1;

    private static final int GET_RECENTLY_PRIZE_RECORD_TASK = 2;

    private static final int CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY = 3;

    private final int REQUEST_PHONE_PERMISSIONS = 0;

    public static final int OVERLAY_PERMISSION_REQ_CODE = 1;

    public static final int LIVE_TRANSFER_NOTICE_REQ_CODE = 2;

    private boolean bInAvRoom = false;

    private LivePortraitController mPortraitController;

    private LiveLandscapeController mLandscapeController;

    /**
     * 是否显示浮窗
     */
    private boolean mShowFloatWindow = false;

    private InputCommentController mInputCommentController;

    private GiftController mGiftController;

    private FrameLayout container;

    private LiveRoomManager liveRoomManager;

    private LiveHttpRequestController mHttpController;

    private LivePushMessageController mPushMessageController;

    private FetchCouponController mFetchCouponController;

    private LiveHeaderController mLiveHeaderController;

    private String channelId;

    private boolean bHasLiveStream = false;

    //当前是否是横屏
    private boolean isLandscape = false;

    //心跳,与在线人数task
    private TimerTask mOnlineUserCountTask = null;

    private Timer onlineUserTimer;

    private HeartHandler mHandler;

    private MyOnKeyBoardStateChangeListener onKeyBoardStateChangeListener;

    //爆奖池最近中奖情况
    private TimerTask mRecentlyPrizeTask = null;

    private Timer recentlyPrizeTimer;

    private boolean mDestroy = false;

    //当前是否推流
    private boolean mPushStreamStatus;

    private String kickOutReason;
    private PLScreenRecorder mScreenRecorder;
    private boolean permissionStorageEnable;
    private CouponFrameLayout couponFrameLayout;
    private boolean bOnPreparePlay = false;
    private boolean mIsCloseVideo;

    public static void startLiveActivity(Activity activity, int channelId) {

        Intent intent = new Intent(activity, LiveActivity.class);

        intent.putExtra("channelId", String.valueOf(channelId));

        activity.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏

        setContentView(R.layout.activity_live);

        checkPermission();

        channelId = getIntent().getStringExtra("channelId");

        onKeyBoardStateChangeListener = new MyOnKeyBoardStateChangeListener(this);

        mHandler = new HeartHandler(this);

        initView();

        // liveRoomManager = new VideoPlayerController(this, VideoPlayerTypeEnum.AVROOTVIEW, this);

        liveRoomManager = LiveRoomManager.getInstance();

        liveRoomManager.init(VideoPlayerTypeEnum.AVROOTVIEW, this);

        bInAvRoom = !LiveRoomManager.getInstance().checkFloatWindow(FloatingView.getInstance(liveRoomManager),
                container,
                (int) getResources().getDimension(R.dimen.live_video_height),
                getWindowManager().getDefaultDisplay().getWidth());

        /*bInAvRoom = !liveRoomManager.checkFloatWindow(FloatingView.getInstance(liveRoomManager),
                container,
                (int) getResources().getDimension(R.dimen.live_video_height),
                getWindowManager().getDefaultDisplay().getWidth());*/

        //进入直播间网络状态判断,如果是移动网络给个提示
        initNetJudge();
//        mHttpController.startRequest(true);
        full(false);

        getFetchCouponController().setCouponView(null);

        //获取爆奖信息
        //recentlyPrizeTimer = new Timer(true);
        //recentlyPrizeTimer.schedule(mRecentlyPrizeTask, 100, 5 * 1000);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtils.e("LiveHelper", "onNewIntent");
        String toChannelId = intent.getStringExtra("channelId");
        switchChannel(toChannelId);
    }

    private void initNetJudge() {
        if (NetworkUtils.getNetworkState(this) == NETWORK_MOBILE && !NetworkUtils.isUseMobileNetWork) {
            ShowMessageDialog dialog = new ShowMessageDialog(this, "继续观看", "退出房间", new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    mHttpController.startRequest(true);

                    NetworkUtils.isUseMobileNetWork = true;

                    full(false);
                }
            }, new OnMyClickListener() {
                @Override
                public void onClick(View v) {
                    quitLiveByPurpose();
                }
            }, "您正在使用移动网络观看直播");

        } else {
            mHttpController.startRequest(true);

            full(false);
        }
    }

    public void startPlay() {
        LogUtils.e("LiveActivity", "-----------开始进入房间--------" + "channelId=" + channelId + "  streamId=" + CurLiveInfo.pushStreamId);
        liveRoomManager.startPlay(channelId);
    }


    //去掉视频，只显示音频
    public void cancelAllView() {
        liveRoomManager.cancelAllView();
    }

    //显示视频
    public void showView() {
        liveRoomManager.showView();
    }


    /**
     * 初始化界面
     */
    private void initView() {

        container = (FrameLayout) findViewById(R.id.contentView);

        mInputCommentController = new InputCommentController(this);
        mInputCommentController.init(findViewById(R.id.commentView));
        mInputCommentController.setOnKeyBoardStateChangeListener(onKeyBoardStateChangeListener);

        mLandscapeController = new LiveLandscapeController(this);
        mLandscapeController.init(findViewById(R.id.landscapeView));

        mPortraitController = new LivePortraitController(this);
        mPortraitController.init(findViewById(R.id.portraitView));

        mGiftController = new GiftController(this);
        mGiftController.init(findViewById(R.id.giftShowLayout));

        mLiveHeaderController = new LiveHeaderController(this);
        mLiveHeaderController.init(container);
        mLiveHeaderController.setShowSingleCheer(true);

        mHttpController = new LiveHttpRequestController(this, channelId, mInputCommentController, rocketFlyListener);
        mHttpController.setDeviceOrientation(isLandscape);
        mHttpController.init(null);
        mPushMessageController = new LivePushMessageController(this);
        mPushMessageController.init(null);
        couponFrameLayout = (CouponFrameLayout) findViewById(R.id.couponFl);
        mFetchCouponController = new FetchCouponController(this, couponFrameLayout);
    }


    RocketFlyListener rocketFlyListener = new RocketFlyListener() {
        @Override
        public void rocketFly(String title, boolean up) {
            new RocketFlyPop(LiveActivity.this).show(findViewById(R.id.closeIv), up, title);
        }
    };

    public void prizeInfoQueryResponse(PrizeCoreInfoBean prizeCoreInfoBean) {
        getLivePortraitController().getMainController().prizeInfoQuery(prizeCoreInfoBean);
    }


    //修改轮询时间
    public void fixQueryTime(int intervalTime) {
        if (mRecentlyPrizeTask != null) {
            ((MyTimerTask) mRecentlyPrizeTask).setPeriod(intervalTime * 1000);
        }
    }

    @Override
    public void onReady() {
        Log.e(TAG, "录屏初始化成功！");
    }

    @Override
    public void onError(int code) {
        Log.e(TAG, "onError: code = " + code);
        if (code == PLErrorCode.ERROR_UNSUPPORTED_ANDROID_VERSION) {
            final String tip = "录屏只支持 Android 5.0 以上系统";
        }
    }

    @Override
    public void onRecordStarted() {
        Log.e("RecordScreen", "正在录屏……");
    }

    @Override
    public void onRecordStopped() {
        Log.e("RecordScreen", "已经停止录屏！");
        getLivePortraitController().controlRecordProgress(false, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, new PermissionUtil.PermissionChecker() {
            @Override
            public void onGrant(String grantPermission, int index) {
                if (grantPermission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissionStorageEnable = true;
                }
            }

            @Override
            public void onDenied(String deniedPermission, int index) {
                if (deniedPermission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    showToast(getString(com.android.banana.R.string.storage_permission_denied));
            }
        });
    }


    /**
     * 开始录屏
     */
    public void applyScreenRecordPermission() {
        permissionStorageEnable = PermissionUtil.isPermissionEnable(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (!permissionStorageEnable) {
            //PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            showToast(getString(com.android.banana.R.string.storage_permission_denied));
            getLivePortraitController().controlRecordProgress(false, false);
            return;
        }
        readyScreenRecord();
    }

    private void readyScreenRecord() {
        if (mScreenRecorder == null) {
            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;
            int dpi = metrics.densityDpi;

            Log.e(TAG, "width=" + width + "     height = " + height + "    dpi=" + dpi);
            mScreenRecorder = new PLScreenRecorder(this);
            mScreenRecorder.setRecordStateListener(this);

            PLScreenRecorderSetting screenSetting = new PLScreenRecorderSetting();
            screenSetting.setRecordFile(Config.SCREEN_RECORD_FILE_PATH)
                    .setInputAudioEnabled(false)
                    .setSize(width, height)
                    .setDpi(dpi);
            PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();
            mScreenRecorder.prepare(screenSetting, microphoneSetting);
        }
        mScreenRecorder.requestScreenRecord();
    }

    //主播在频道内,准备开始直播
    public void anchorInChannelPrepareLive() {
        setLiveRoomResume();
        liveStateChange(true);
        getLivePortraitController().receiveFirstFrame();
        mLiveHeaderController.controlPollMatchLive(false);
        mPortraitController.controlLiveFunctionHide(false);
        liveRoomManager.getRootView().setVisibility(View.VISIBLE);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private class OnlineUserCountTask extends TimerTask {

        @Override
        public void run() {
            if (bOnPreparePlay) {
                mHandler.sendEmptyMessage(UPDATE_CHANNEL_USER_COUNT_TASK);
            }
            if (bInAvRoom) {
                mHandler.sendEmptyMessage(CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY);
            } else {
                mHandler.removeMessages(CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY);
            }

        }
    }

    private class RecentlyPrizeRecordTask extends MyTimerTask {
        @Override
        public void run() {
            mHandler.sendEmptyMessage(GET_RECENTLY_PRIZE_RECORD_TASK);
        }
    }

    class HeartHandler extends Handler {

        WeakReference<LiveActivity> softReference;

        public HeartHandler(LiveActivity activity) {
            softReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            LiveActivity activity = softReference.get();
            if (activity != null) {
                if (activity.mHttpController == null) {
                    return;
                }
                switch (msg.what) {
                    case UPDATE_CHANNEL_USER_COUNT_TASK:
                        activity.mHttpController.startHeartBeatAndGetUserCount();
                        break;
                    case GET_RECENTLY_PRIZE_RECORD_TASK:
                        activity.mHttpController.startGetPrizeRecord();
                        break;
                    case CHANNEL_AREA_TOP_PK_GAME_BOARD_QUERY:
                        activity.mHttpController.startGetPkProgressAndTreasureBox();
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void full(boolean enable) {
        /*if (enable) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else {
            WindowManager.LayoutParams attr = getWindow().getAttributes();
            attr.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(attr);
            //去掉底部虚拟导航栏，否则会引起键盘高度的测量问题
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
//            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        liveRoomManager.onResume();
        if (isLandscape) {
            mLandscapeController.onResume();
        } else {
            mPortraitController.onResume();
        }
        getLivePortraitController().getMainController().onKeyBoardStateChange(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mInputCommentController.closeInputLayout();
        if (!mShowFloatWindow) {
            liveRoomManager.onPause();
        }
        if (isLandscape) {
            mLandscapeController.onPause();
        } else {
            mPortraitController.onPause();
        }
//        cancelHeartBeat();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.e(TAG, "onDestroy");

        sendCouponSuccessListener = null;
        onKeyBoardStateChangeListener = null;
        onDialogStateListener = null;
        sendGiftListener = null;
        rocketFlyListener = null;

        liveRoomManager.onDestroy();
        /*if (!mShowFloatWindow) {
            liveRoomManager.onDestroy();
        } else {
            liveRoomManager.clearContext();
        }*/
        cancelHeartBeat();
        //如果关闭该界面需要将一些数据进行回收
        mPushMessageController.onDestroy();
        mPortraitController.onDestroy();
        mGiftController.onDestroy();
        mInputCommentController.onDestroy();
        mLandscapeController.onDestroy();
        mFetchCouponController.onDestroy();
        mHttpController.onDestroy();
        mLiveHeaderController.onDestroy();
        liveRoomManager = null;
        mHttpController = null;
        mPortraitController = null;
        mGiftController = null;
        mInputCommentController = null;
        mLandscapeController = null;
        mDestroy = true;
        setContentView(new View(this));
        UMShareAPI.get(this).release();
    }


    /**
     * 关闭当前界面,并且显示视屏浮窗
     *
     * @param isShow 是否显示浮窗
     */
    public void showFloatWindow(boolean isShow) {
        quitLiveByPurpose();
       /* if (bInAvRoom) {
            if (isShow && mPushStreamStatus) {
                mShowFloatWindow = true;
                askForPermission();
            } else {
                quitLiveByPurpose();
            }
        } else {
            finish();
        }*/
    }

    public void liveStateChange(boolean pushStreamStatus) {
        mPushStreamStatus = pushStreamStatus;
        liveRoomManager.liveStateChange(pushStreamStatus);
    }

    /**
     * 直播间传送
     *
     * @param liveCommentBean
     */
    public void showTransferNotice(LiveCommentBean liveCommentBean) {
        LiveCommentBean.ContentBean.BodyBean body = liveCommentBean.getContent().getBody();
        String fromChannelId = body.getFromChannelAreaId();
        String toChannelId = body.getToChannelAreaId();
        //属于当前直播间的通知
        if (channelId.equals(fromChannelId)) {
            LiveTransferNoticeActivity.startLiveTransferNoticeActivity(
                    LiveActivity.this, body.getToChannelTitle(), body.getToChannelAreaAnchorName(), toChannelId);
        }
    }

    /**
     * 点击礼物显示礼物对话框
     *
     * @param isLandScape
     */
    public void showGift(boolean isLandScape) {
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            if (isLandScape) {
                GiftDialog giftDialog = new GiftDialog(this, SCREEN_ORIENTATION_LANDSCAPE, channelId);
                giftDialog.setSendGiftListener(sendGiftListener);
                giftDialog.show();
            } else {
                GiftDialog giftDialog = new GiftDialog(this, SCREEN_ORIENTATION_PORTRAIT, channelId);
                giftDialog.setOnDialogStateListener(onDialogStateListener);
                giftDialog.setSendGiftListener(sendGiftListener);
                giftDialog.show();
            }
        } else {
            LoginActivity.startLoginActivity(this, false);
        }

    }


   /* @Override
    public String getPackageName() {
        return "com.qiniu.pili.droid.shortvideo.demo";
    }*/

    public void showMedalAwardAnim(LiveCommentBean liveCommentBean) {
        if (liveCommentBean.getSenderId().equals(LoginInfoHelper.getInstance().getUserId())) {
            if (!liveCommentBean.getContent().getBody().isFristAward()) {
                String content = liveCommentBean.getContent().getBody().getMedalContent();
                String msg = "恭喜您获得粉丝勋章【" + content + "】,\n送礼物会提升等级哦";
                HhsUtils.showToast(Toast.makeText(this, msg, Toast.LENGTH_LONG), 5 * 1000);
            } else {
                getHttpController().queryCurrentMedalInfo();
                FullScreenAnimPop pop = new FullScreenAnimPop(this, liveCommentBean.getContent().getBody().getMedalLevelCode(),
                        liveCommentBean.getContent().getBody().getMedalContent(), isLandscape);
                pop.show(findViewById(R.id.backIv));
            }
            getInputCommentController().getNewFansMedal(liveCommentBean.getContent().getBody().getUserMedalDetailId());
        }
    }

    public void showVoteView(String userVotedContent, final EnterRoomBean enterRoomBean) {
        if (TextUtils.isEmpty(userVotedContent)) {
            final boolean isFootball = enterRoomBean.getRaceDataSimple() != null && enterRoomBean.getRaceDataSimple().isFootball();
            final GeneralDialog dialog = GeneralDialog.init(this);
            final View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mHttpController.voteTeam(enterRoomBean.getVoteIdBySequence(Integer.valueOf((String) v.getTag())));
                    dialog.dismiss();
                }
            };
            dialog.setLayoutId(R.layout.dialog_layout_support_team)
                    .setConvertListener(new ViewConvertListener() {
                        @Override
                        public void convertView(ViewHolder holder, final BaseDialog dialog) {
                            holder.setBackgroundResource(R.id.contentView, isFootball ?
                                    R.drawable.icon_support_team_ft_bg : R.drawable.icon_support_team_bt_bg);
                            holder.getView(R.id.closeIv).setOnClickListener(listener);
                            holder.getView(R.id.supportHomeTv).setOnClickListener(listener);
                            holder.getView(R.id.supportGuestTv).setOnClickListener(listener);
                        }
                    })
                    .setMargin(10)
                    .setOutCancel(false)
                    .show();
        }
    }


    /**
     * 发红包
     */
    public void showSendCouponView(View anchorView) {

        LiveCouponDialog sendCouponPop;
        if (isLandscape) {
            sendCouponPop = new LiveCouponDialog(this, SCREEN_ORIENTATION_LANDSCAPE,
                    CouponPlatformType.CHANNEL_AREA.name(), channelId);
        } else {
            sendCouponPop = new LiveCouponDialog(this, SCREEN_ORIENTATION_PORTRAIT,
                    CouponPlatformType.CHANNEL_AREA.name(), channelId);
        }
        sendCouponPop.setSendCouponSuccessListener(sendCouponSuccessListener);
        sendCouponPop.show();
        // getHttpController().queryCouponAllowFetchCount();
    }

    private SendCouponSuccessListener sendCouponSuccessListener = new SendCouponSuccessListener() {

        @Override
        public void sendCouponSuccess(final String couponType, String couponId, String couponTitle) {
            //发红包成功效果
            SendCouponSuccessDialog dialog = new SendCouponSuccessDialog(LiveActivity.this);
            dialog.setOnDismissListener(new SendCouponSuccessDialog.OnDismissListener() {
                @Override
                public void onDismissListener() {
                    //公屏显示
                    getPushMessageController().listViewShow(createCouponMessage(couponType));
                    //查一遍红包数
                    getHttpController().queryCouponAllowFetchCount();
                }
            });
            dialog.show(findViewById(R.id.closeIv));
        }
    };

    private LiveCommentBean createCouponMessage(String couponType) {
        LiveCommentBean liveCommentBean = new LiveCommentBean();
        liveCommentBean.setType(LiveCommentMessageTypeEnum.COUPON_CREATE_SUCCESS_NOTICE_TEXT.name());
        liveCommentBean.setSenderName(LoginInfoHelper.getInstance().getNickName());
        liveCommentBean.setSenderId(LoginInfoHelper.getInstance().getUserId());
        LiveCommentBean.ContentBean contentBean = new LiveCommentBean.ContentBean();
        LiveCommentBean.ContentBean.BodyBean body = new LiveCommentBean.ContentBean.BodyBean();
        if (CouponEnum.LUCKY_GROUP_COUPON.name().equals(couponType)) {
            body.setCouponType("拼手气红包");
        } else {
            body.setCouponType("普通红包");
        }
        contentBean.setBody(body);
        liveCommentBean.setContent(contentBean);
        return liveCommentBean;
    }

    public void showGroupCoupon() {
        /*int orientation;
        if (isLandscape) {
            orientation = SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            orientation = SCREEN_ORIENTATION_PORTRAIT;
        }
        GroupCouponPop groupCouponPop = new GroupCouponPop(this, orientation, CouponPlatformType.CHANNEL_AREA.name(), channelId);
        groupCouponPop.show();*/
        showSendCouponView(null);

        getHttpController().queryCouponAllowFetchCount();


    }

    //这边切换的是整个节目
    public void switchChannel(String toChannelId) {
        if (!channelId.equals(toChannelId)) {
            channelId = toChannelId;
            //取消上一个心跳,关闭当前所有弹窗,界面数据更新,切换房间,如果是横屏切换成竖屏
            cancelHeartBeat();
            EventBus.getDefault().post(new EventBusMessage(LIVE_ROOM_REFRESH));
            setContentView(R.layout.activity_live);
            initView();
            bInAvRoom = !liveRoomManager.checkFloatWindow(FloatingView.getInstance(liveRoomManager),
                    container,
                    (int) getResources().getDimension(R.dimen.live_video_height),
                    getWindowManager().getDefaultDisplay().getWidth());
            //liveRoomManager.reset();
            mHttpController.setChannelId(channelId);
            mHttpController.startRequest(true);
            LogUtils.e("LiveHelper", "开始切换房间 toChannelId" + toChannelId);
            if (isLandscape) {
                setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
            }
        }
    }

    //设置后台模式
    public void setLiveRoomPause() {
        mIsCloseVideo = true;
        liveRoomManager.onPause();
        mLiveHeaderController.controlPollMatchLive(true);
        mPortraitController.controlLiveFunctionHide(true);
    }

    public void setLiveRoomResume() {
        if (mIsCloseVideo)
            liveRoomManager.onResume();
        mIsCloseVideo = false;
    }

    //抽奖view显示控制
    public void setDrawViewShow(DrawIssueEntity.IssueSimpleBean issueSimpleBean, String nowDate, boolean isShow) {
        couponFrameLayout.setDrawViewStatus(issueSimpleBean, nowDate, isShow);
    }


    /**
     * 当前观众对话框
     *
     * @param isLandScape
     */
    public void showAudience(boolean isLandScape) {
        /*if (isLandScape) {
            CurrentAudienceDialog audienceDialog = new CurrentAudienceDialog(this, SCREEN_ORIENTATION_LANDSCAPE, channelId);
            audienceDialog.show();
        } else {
            CurrentAudienceDialog audienceDialog = new CurrentAudienceDialog(this, SCREEN_ORIENTATION_PORTRAIT, channelId);
            audienceDialog.show();
        }*/
    }

    //分享
    public void showShare() {
        new SharePopUpWindowHelper.Builder(this)
                .setLandscape(isLandscape)
                .setShareUrl(CurLiveInfo.getWebUrl())
                .setHostImageUrl(CurLiveInfo.hostAvator)
                .setHostName(CurLiveInfo.hostName)
                .setTitle(CurLiveInfo.getTitle())
                .builder()
                .show();
    }

    public void showMoreFunctionView() {
        if (isLandscape) {
            new LiveMoreFunctionDialog(this, SCREEN_ORIENTATION_LANDSCAPE).show();
        } else {
            new LiveMoreFunctionDialog(this, SCREEN_ORIENTATION_PORTRAIT).show();
        }
    }

    public void showLuckyDrawView() {
        if (isLandscape) {
            new LuckyDrawDialog(this, SCREEN_ORIENTATION_LANDSCAPE).show();
        } else {
            new LuckyDrawDialog(this, SCREEN_ORIENTATION_PORTRAIT).show();
        }
    }

    public void showPKView() {
        if (isLandscape) {
            new LivePkDialog(this, SCREEN_ORIENTATION_LANDSCAPE).show();
        } else {
            new LivePkDialog(this, SCREEN_ORIENTATION_PORTRAIT).show();
        }
    }

    public void showCheerView() {
        if (CurLiveInfo.jczqDataBean == null) return;
        if (isLandscape) {
            changeScreenOrientation();
        }
        container.postDelayed(new Runnable() {
            @Override
            public void run() {
                new LiveCheerDialog(LiveActivity.this, CurLiveInfo.jczqDataBean).show();
            }
        }, isLandscape ? 3 * 1000 : 0);


    }

    public void changeScreenOrientation() {
        setRequestedOrientation(
                getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ?
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }


    /**
     * 当前子频道列表对话框
     */
    public void showCurrentLive() {
        CurrentLiveListDialog dialog = new CurrentLiveListDialog(this);
        dialog.show();
    }

    //改变当前人数
    public void changePeopleCount(long userCount) {
        mLandscapeController.changePeopleCountTv(userCount);
        mPortraitController.changePeopleCountTv(userCount);
    }

    //设置主播信息
    public void setAnchorView(EnterRoomBean enterRoomBean) {
        mPortraitController.setAnchorView(enterRoomBean);
        mLandscapeController.setAnchorView(enterRoomBean);
    }

    /**
     * 礼物发送成功回调
     */
    private GiftDialog.OnSendGiftListener sendGiftListener = new GiftDialog.OnSendGiftListener() {

        @Override
        public void sendGiftSuccess(SendGiftResultBean.ResultListBean resultListBean) {
            getPushMessageController().listViewShow(createGiftMessage(resultListBean));
            //getGiftController().addSendGiftSuccessShow(resultListBean);
        }
    };

    private LiveCommentBean createGiftMessage(SendGiftResultBean.ResultListBean resultList) {
        LiveCommentBean liveCommentBean = new LiveCommentBean();
        liveCommentBean.setType(LiveCommentMessageTypeEnum.GIFTCORE_GIFT_GIVE_TEXT.name());
        liveCommentBean.setSenderName(LoginInfoHelper.getInstance().getNickName());
        liveCommentBean.setSenderId(LoginInfoHelper.getInstance().getUserId());
        LiveCommentBean.ContentBean contentBean = new LiveCommentBean.ContentBean();
        LiveCommentBean.ContentBean.BodyBean body = new LiveCommentBean.ContentBean.BodyBean();
        body.setTotalCount(String.valueOf(resultList.getTotalCount()));
        body.setGiftConfigName(resultList.getGiftName());
        body.setGiftImageUrl(resultList.getGiftShowUrl());
        body.setGiftConfigId(String.valueOf(resultList.getGiftConfigId()));
        body.setOwnGift(true);
        contentBean.setBody(body);
        liveCommentBean.setContent(contentBean);
        return liveCommentBean;
    }

    /**
     * dailog显示状态回调
     */
    private DialogBase.OnDialogStateListener onDialogStateListener = new DialogBase.OnDialogStateListener() {

        @Override
        public void dialogStateChangeListener(boolean isShow) {
            mGiftController.showKeyBoard(isShow);
            mInputCommentController.setChatListShow(isShow);
            if (isShow) {
                full(true);
            }
        }
    };

    @Override
    public void showSoftKeyboard(View focusView) {
        super.showSoftKeyboard(focusView);
    }

    /**
     * 键盘弹出状态监听
     */

    class MyOnKeyBoardStateChangeListener implements InputCommentController.OnKeyBoardStateChangeListener {

        private WeakReference<LiveActivity> softReference;

        public MyOnKeyBoardStateChangeListener(LiveActivity activity) {
            softReference = new WeakReference<>(activity);
        }

        @Override
        public void onKeyBoardStateChange(boolean keyBoardIsShow) {
            LiveActivity activity = softReference.get();
            if (activity != null) {
                activity.mGiftController.showKeyBoard(keyBoardIsShow);
                activity.getLivePortraitController().getMainController().onKeyBoardStateChange(keyBoardIsShow);
                activity.getFetchCouponController().isHideCoupon(keyBoardIsShow);

                if (keyBoardIsShow) {
                    activity.full(true);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Object event) {
        if (mDestroy) {
            return;
        }

        if (event instanceof GroupCouponInfoBean) {
            getHttpController().queryCouponAllowFetchCount();
            return;
        }
        if (event instanceof EventBusMessage) {
            EventBusMessage message = (EventBusMessage) event;
            if (message.isReLoginSuccess()) {
                mHttpController.changeToUserLogin();
                mHttpController.startRequest(false);
            } else if (message.isKickOut()) {
                kickOutReason = message.getMessage();
                quitLiveByPurpose();
            }/* else if (message.isRefreshLiveRoom()) {
            switchRoom();
        }*/
        }

    }

    public String getChannelId() {
        return channelId;
    }

    public LiveHttpRequestController getLiveHttpRequestController() {
        return mHttpController;
    }

    public LivePortraitController getLivePortraitController() {
        return mPortraitController;
    }

    public LiveLandscapeController getLiveLandscapeController() {
        return mLandscapeController;
    }

    public LiveHttpRequestController getHttpController() {
        return mHttpController;
    }

    public LivePushMessageController getPushMessageController() {
        return mPushMessageController;
    }

    public LiveHeaderController getLiveHeaderController() {
        return mLiveHeaderController;
    }

    public InputCommentController getInputCommentController() {
        return mInputCommentController;
    }

    public GiftController getGiftController() {
        return mGiftController;
    }

    public FetchCouponController getFetchCouponController() {
        return mFetchCouponController;
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if ((checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.READ_PHONE_STATE);
            if ((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                        REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    public void cancelHeartBeat() {
//        mOnlineUserCountTask.cancel();
//        mHandler.removeMessages(UPDATE_CHANNEL_USER_COUNT_TASK);
        if (null != onlineUserTimer) {
            mOnlineUserCountTask.cancel();
            mOnlineUserCountTask = null;
            onlineUserTimer.cancel();
            onlineUserTimer.purge();
            onlineUserTimer = null;
        }
        if (null != recentlyPrizeTimer) {
            mRecentlyPrizeTask.cancel();
            mRecentlyPrizeTask = null;
            recentlyPrizeTimer.cancel();
            recentlyPrizeTimer.purge();
            recentlyPrizeTimer = null;
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    public void setPrizedShowView(boolean isShow) {
        couponFrameLayout.setIsShowPrizedView(isShow);
    }

    /**
     * 点击Back键
     */
    @Override
    public void onBackPressed() {

        //如果是横屏就改为竖屏
        if (getRequestedOrientation() == SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(SCREEN_ORIENTATION_PORTRAIT);
            return;
        }

        showFloatWindow(true);
    }

    public boolean isLandscape() {
        return isLandscape;
    }


    /**
     * 主动退出直播
     */
    public void quitLiveByPurpose() {
        if (bInAvRoom) {
            liveRoomManager.stopPlay();
        } else {
            finish();
        }
    }

    public void showComment() {
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            // getHttpController().queryCurrentMedalInfo();
            mInputCommentController.showComment();
        } else {
            LoginActivity.startLoginActivity(this, false);
        }
    }

    //根据横竖屏，设置界面
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == LiveActivity.this.getResources().getConfiguration().ORIENTATION_PORTRAIT) {
            isLandscape = false;
            mHttpController.setDeviceOrientation(isLandscape);

            int liveVideoHeight = (int) getResources().getDimension(R.dimen.live_video_height);

            int width = getWindowManager().getDefaultDisplay().getWidth();

            liveRoomManager.changeViewSize(false, container, liveVideoHeight, width);

            mLandscapeController.hide();

            mPortraitController.show();

            mPortraitController.changePortrait();

            mGiftController.changeOrientation(true);

            mInputCommentController.changeOrientation(false);

            mFetchCouponController.changeOrientation(isLandscape);

        } else if (newConfig.orientation == LiveActivity.this.getResources().getConfiguration().ORIENTATION_LANDSCAPE) {

            isLandscape = true;
            mHttpController.setDeviceOrientation(isLandscape);
            int width = getWindowManager().getDefaultDisplay().getWidth();

            int height = getWindowManager().getDefaultDisplay().getHeight();

            liveRoomManager.changeViewSize(true, container, height, width);

            mPortraitController.hide();

            mLandscapeController.show();

            mGiftController.changeOrientation(false);

            mInputCommentController.changeOrientation(true);

            mFetchCouponController.changeOrientation(isLandscape);

            full(false);

        }

    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            //无权限
            if (!Settings.canDrawOverlays(this)) {
                applyOverlayPermission();
            } else {
                liveRoomManager.showFloatWindow(container, channelId);
                finish();
            }
        } else {
            liveRoomManager.showFloatWindow(container, channelId);
            finish();
        }

    }

    private void applyOverlayPermission() {
        if (!XjqApplication.isShowFloatingWindow) {
            quitLiveByPurpose();
            return;
        }
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        builder.setTitle("开启悬浮窗播放功能");
        builder.setMessage("请到系统设置中开启系统权限,点取消后关闭悬浮窗");
        builder.setPositiveMessage("去设置");
        builder.setNegativeMessage("取消");
        builder.setShowTitle(true);
        builder.setShowMessageMiddle(true);
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                //在联想的自由客 Z2121、Z2131手机上,定制的Rom中该界面被移除了
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } else {
                    ToastUtil.showLong(XjqApplication.getContext(), "权限授予失败,可在设置页面-->可出现在顶部的应用程序中设置");
                    quitLiveByPurpose();
                }

            }
        });
        builder.setCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XjqApplication.isShowFloatingWindow = false;
                quitLiveByPurpose();
            }
        });
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    //开始录屏
    public void controlRecord() {
        if (mScreenRecorder == null) return;
        if (mScreenRecorder.isRecording()) {
            mScreenRecorder.stop();
        } else {
            mScreenRecorder.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
                if (!Settings.canDrawOverlays(this)) {
                    ToastUtil.showLong(XjqApplication.getContext(), "权限授予失败，无法开启悬浮窗");
                    quitLiveByPurpose();
                } else {
                    //启动悬浮窗
                    liveRoomManager.showFloatWindow(container, channelId);
                    finish();
                }
            } else if (requestCode == PLScreenRecorder.REQUEST_CODE) {
                if (data == null) {
                    String tip = "录屏申请启动失败！";
                    Toast.makeText(this, tip, Toast.LENGTH_SHORT).show();
                    mScreenRecorder.stop();
                    mScreenRecorder = null;
                    return;
                }
                boolean isReady = mScreenRecorder.onActivityResult(requestCode, resultCode, data);
                if (isReady) {
                    getLivePortraitController().readyRecord();
                    //moveTaskToBack(true);
                    //mScreenRecorder.start();
                }
            }
        } catch (Exception e) {
        }
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPreparePlay() {
        //每5秒拉取一次人数,以及上报心跳
        LogUtils.e(TAG, "----------------------onPreparePlay-----------------------");
        bOnPreparePlay = true;
       /* //获取爆奖信息
        recentlyPrizeTimer = new Timer(true);
        mRecentlyPrizeTask = new RecentlyPrizeRecordTask();
        recentlyPrizeTimer.schedule(mRecentlyPrizeTask, 100, 5 * 1000);*/

        onlineUserTimer = new Timer(true);
        mOnlineUserCountTask = new OnlineUserCountTask();
        onlineUserTimer.schedule(mOnlineUserCountTask, 100, 5 * 1000);
    }

    @Override
    public void onPlaySuccessListener() {
        bInAvRoom = true;
        FloatingView.getInstance().setChannelId(channelId);
    }

    @Override
    public void receiveFirstFrameListener() {
        LogUtils.e(TAG, "----------------------receiveFirstFrameListener-----------------------");
        //先查询主播是否在房间内
        mHttpController.queryAnchorIsInChannel();
        //liveStateChange(true);
    }

    @Override
    public void onPlayFailedListener() {

    }

    @Override
    public void onExitComplete() {
        bInAvRoom = false;
        finish();
        if (EventBusMessage.CHANNEL_USER_ENTER_OTHER_CHANNEL.equals(kickOutReason)) {
            AccountQuitDialogActivity.startAccountQuitDialogActivity(false,
                    getString(R.string.have_open_room_in_other));
        } else if (EventBusMessage.USER_ALREADY_LOGIN_OTHER.equals(kickOutReason)) {
            AccountQuitDialogActivity.startAccountQuitDialogActivity(true,
                    getString(R.string.have_login_in_other));
        } else if (EventBusMessage.USER_KICK_OUT_FROM_CHANNEL.equals(kickOutReason)) {
            AccountQuitDialogActivity.startAccountQuitDialogActivity(false,
                    getString(R.string.user_kick_out_from_channel));
        }

    }

    @Override
    public void receiveMessage(LiveCommentBean liveCommentBean) {
        getPushMessageController().addMessage(liveCommentBean);
    }

    @Override
    public void onHasVideo() {
        LogUtils.e(TAG, "----------------------onHasVideo-----------------------");
        bHasLiveStream = true;
        //liveStateChange(true);
        //mHttpController.queryAnchorIsInChannel();
       /* mLiveHeaderController.controlPollMatchLive(false);
        mPortraitController.controlLiveFunctionHide(false);
        liveRoomManager.getRootView().setVisibility(View.VISIBLE);*/
    }

    @Override
    public void onNoVideo() {
        LogUtils.e(TAG, "----------------------onNoVideo-----------------------");
        bHasLiveStream = false;
        liveStateChange(false);
        mLiveHeaderController.controlPollMatchLive(true);
        mPortraitController.controlLiveFunctionHide(true);
    }


}
