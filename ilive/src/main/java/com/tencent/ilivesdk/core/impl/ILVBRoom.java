package com.tencent.ilivesdk.core.impl;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMValueCallBack;
import com.tencent.av.NetworkUtil;
import com.tencent.av.TIMAvManager;
import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.sdk.AVAudioCtrl;
import com.tencent.av.sdk.AVCallback;
import com.tencent.av.sdk.AVContext;
import com.tencent.av.sdk.AVError;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveFunc;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILivePushOption;
import com.tencent.ilivesdk.core.ILiveRecordOption;
import com.tencent.ilivesdk.core.ILiveRoomConfig;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.engines.EngineFactory;
import com.tencent.ilivesdk.tools.quality.ILiveQualityData;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.ilivesdk.view.GLVideoView;
import com.tencent.ilivesdk.view.ILiveRootView;
import com.tencent.imsdk.BaseConstants;
import com.tencent.imsdk.IMMsfCoreProxy;
import com.tencent.imsdk.IMMsfUserInfo;
import com.tencent.imsdk.av.MultiVideoTinyId;
import com.tencent.openqq.protocol.imsdk.gv_comm_operate;

import java.util.ArrayList;
import java.util.List;


/**
 * 房间管理类
 */
public class ILVBRoom extends ILiveRoomManager implements TIMMessageListener, AVRoomMulti.EventListener {
    private static String TAG = "ILVBRoom";

    private ILiveCallBack mEnterCallBack, mExitCallBack, mSwitchRoomCallBack;

    private AVRootView mRootView;           // 统一渲染控件
    private ILiveRootView mRootViewArr[];   // 分开渲染数组

    private AVRoomMulti mRoomMulti;         // 当前AVRoom
    private int mRoomId;                    // AV Room id

    private int curCameraId = ILiveConstants.NONE_CAMERA;                // 当前开启摄像头id
    private int curCtrlCameraId = ILiveConstants.NONE_CAMERA;           // 当前操作的摄像头id

    private float curWhite = 0;

    private boolean bRequsting = false;
    private boolean bChanged = false;
    private String chatRoomId;

    private boolean bSetCameraCb = false;   // 是否已设置摄像头回调
    private boolean bEnterRoom = false;     // 是否加入房间
    private boolean bCameraEnableUserBak = false;   // 保存摄像头用户意向状态(恢复时使用)
    private boolean bMicEnableUserBak = false;      // 保存Mic用户意向状态(恢复时使用)
    private List<String> mReqUserVideoListBak = new ArrayList<>();  // 保存要请求视频的用户(恢复时使用)
    private List<String> mReqUserScreenListBak = new ArrayList<>(); // 保存要请求屏幕的用户(恢复时使用)
    private SensorControl sensorControl;
    //private QualityReportHelper helper;
    private Boolean isSwitchRoomTag = false;
    private boolean bRoomLineLock = false;      // 进出房间线性锁(保证进出房间为线性操作)
    private boolean isHost = false;             // 区分createRoom和joinRoom

    private SurfaceView mSurfaceView = null;

    private AVVideoCtrl.CameraPreviewChangeCallback mCameraPreviewChangeCallback = new AVVideoCtrl.CameraPreviewChangeCallback() {
        @Override
        public void onCameraPreviewChangeCallback(int cameraId) {
            if (null == mOption) {
                ILiveLog.e(TAG, "ILVB-Room|CameraPreview->enter with option is empty!");
                return;
            }
            mOption.cameraId(cameraId);
            if (null != mRootView) {
                GLVideoView myView = mRootView.getUserAvVideoView(ILiveLoginManager.getInstance().getMyUserId(), AVView.VIDEO_SRC_TYPE_CAMERA);
                if (null != myView) {    // 若为前置摄像头，需镜像显示
                    myView.setMirror(cameraId == ILiveConstants.FRONT_CAMERA);
                }
            }
            ILiveLog.i(TAG, "ILVB-Room|mCameraPreviewChangeCallback id:" + cameraId);
            //if (mOption.isAutoFocus()) autoFocuse();//开启自动渲染
            if (null != mOption.getCameraListener()) {
                mOption.getCameraListener().onCameraPreviewChanged(cameraId);
            }
            if (mOption.isAutoFocus()) {
                if (null == sensorControl) {
                    sensorControl = new SensorControl(ILiveSDK.getInstance().getAppContext());
                    sensorControl.startListener((Camera) ILiveSDK.getInstance().getAvVideoCtrl().getCamera());
                } else {
                    sensorControl.updateCamera((Camera) ILiveSDK.getInstance().getAvVideoCtrl().getCamera());
                }
            }
            super.onCameraPreviewChangeCallback(cameraId);
        }
    };

    private AVVideoCtrl.EnableCameraCompleteCallback mEnableCameraCallback = new AVVideoCtrl.EnableCameraCompleteCallback() {
        @Override
        protected void onComplete(boolean bEnable, int result) {
            if (AVError.AV_OK == result) {
                int cameraId = curCtrlCameraId;
                curCtrlCameraId = ILiveConstants.NONE_CAMERA;
                ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|enable camera id:" + cameraId + "/" + bEnable);
                if (null != mOption && null != mOption.getCameraListener()) {
                    if (bEnable) {
                        curCameraId = cameraId;
                        mOption.getCameraListener().onCameraEnable(cameraId);
                        renderUserVideo(ILiveLoginManager.getInstance().getMyUserId(), AVView.VIDEO_SRC_TYPE_CAMERA);
                    } else {
                        curCameraId = ILiveConstants.NONE_CAMERA;
                        mOption.getCameraListener().onCameraDisable(cameraId);
                    }
                }
            } else {
                notifyException(ILiveConstants.EXCEPTION_ENABLE_CAMERA_FAILED, result, "enable camera failed");
            }
            super.onComplete(bEnable, result);
        }
    };

    /**
     * 画面请求回调
     */
    private AVRoomMulti.RequestViewListCompleteCallback mRequestViewListCompleteCallback = new AVRoomMulti.RequestViewListCompleteCallback() {
        public void OnComplete(String identifierList[], AVView viewList[], int count, int result, String errMsg) {
            if (AVError.AV_OK == result) {
                for (int i = 0; i < identifierList.length; i++) {
                    if (!renderUserVideo(identifierList[i], viewList[i].videoSrcType)) {
                        notifyException(ILiveConstants.EXCEPTION_RENDER_USER_FAILED, ILiveConstants.ERR_SDK_FAILED, identifierList.toString());
                    }
                }
            } else {
                notifyException(ILiveConstants.EXCEPTION_REQUEST_VIDEO_FAILED, result, errMsg);
            }
            // TODO
            bRequsting = false;
            if (bChanged) {
                ILiveSDK.getInstance().runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        requestRemoteVideo();
                    }
                }, 0);
            }
            ILiveLog.d(TAG, "RequestViewListCompleteCallback.OnComplete result: " + result);
        }
    };

    /**
     * surfaceHolder listener
     */
    private SurfaceHolder.Callback mSurfaceHolderListener = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (null != ILiveSDK.getInstance().getAVContext()) {
                // 初始化渲染view
                ILiveSDK.getInstance().getAVContext().setRenderMgrAndHolder(GraphicRendererMgr.getInstance(), holder);//获取SurfaceView holder;
            }
            if (null != mRootView && null != mRootView.getmSCUserListner()) {
                mRootView.getmSCUserListner().onSurfaceCreated();
            }

            ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|onSurfaceCreated");
            if (null != mOption && mOption.isAutoCamera()) {    // 自动打开摄像头
                int ret = enableCamera(mOption.getCameraId(), true);
                if (ILiveConstants.NO_ERR != ret) {
                    notifyException(ILiveConstants.EXCEPTION_ENABLE_CAMERA_FAILED, ret, "open camera failed!");
                }
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (holder.getSurface() == null) {
                return;
            }
            holder.setFixedSize(width, height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
        }
    };

    // 生成IMGroupId
    private void generateIMGroupId() {
        if (null != mOption && !TextUtils.isEmpty(mOption.getIMGroupId())) {  // 优先采用用户传入的群组id
            chatRoomId = mOption.getIMGroupId();
        } else if (!TextUtils.isEmpty(chatRoomId)) {          // 其次采用之前绑定的群组
            if (null != mOption) {
                mOption.imGroupId(chatRoomId);
            }
        } else {     // 最后自动生成
            chatRoomId = mConfig.getGenFunc().generateImGroupId(mRoomId);
            if (null != mOption) {
                mOption.imGroupId(chatRoomId);
            }
        }
    }

    // 通知房间异常事件
    private void notifyException(int exceptionId, int errCode, String errMsg) {
        ILiveLog.d(TAG, "notifyException->id:" + exceptionId + "|" + errCode + "|" + errMsg);
        if (null != mOption && null != mOption.getExceptionListener()) {
            mOption.getExceptionListener().onException(exceptionId, errCode, errMsg);
        }
    }

    private int enterAVRoom(ILiveCallBack callBack) {
        int ret = ILiveConstants.NO_ERR;
//        ILiveLog.d(TAG, ILiveConstants.LOG_KEY_PR+"|ILVB-Room|enter start enter avroom ");
        if (null == mOption) {   // 房间已退出
            ILiveLog.d(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|enterAVRoom with option empty!");
            return ILiveConstants.ERR_WRONG_STATE;
        }

        if (!mOption.isAVSupport()) {    // 不用创建AV房间
            ILiveFunc.notifySuccess(mEnterCallBack, 0);
            return ILiveConstants.NO_ERR;
        }

        // 重置美颜美白
        curWhite = 0;

        // 重置要请求的用户
        mReqUserVideoListBak.clear();
        mReqUserScreenListBak.clear();

        AVContext avContext = ILiveSDK.getInstance().getAVContext();

        // 进入房间前开启音频服务
        if (null != avContext && null != avContext.getAudioCtrl())
            avContext.getAudioCtrl().startTRAEService();

        AVRoomMulti.EnterParam.Builder builder = new AVRoomMulti.EnterParam.Builder(mRoomId)
                .auth(mOption.getAuthBits(), mOption.getAuthBuffer())
                .avControlRole(mOption.getAvControlRole())
                .audioCategory(mOption.getAudioCategory())
                .autoCreateRoom(true)
                .isEnableHwEnc(mOption.isEnableHwEnc())
                .isEnableHwDec(mOption.isEnableHwDec())
                .isEnableHdAudio(mOption.isHighAudioQuality())
                .isEnableMic(mOption.isAutoMic())
                .isEnableSpeaker(mOption.isAutoSpeaker())
                .videoRecvMode(mOption.getVideoRecvMode());

        if (avContext != null) {
            if (isSwitchRoomTag) {
                avContext.switchRoom(mRoomId);
            } else {
                avContext.enterRoom(this, builder.build());
            }
        } else {
            isSwitchRoomTag = false;
            bRoomLineLock = false;
            ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, ret, "enterAVRoom failed");
            quitIMRoom();
        }

        return ret;
    }

    private void quitIMRoom() {
        if (mOption.isIMSupport()) {
            if (isHost) {
                TIMGroupManager.getInstance().deleteGroup(getIMGroupId(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
                TIMManager.getInstance().deleteConversation(TIMConversationType.Group, getIMGroupId());
            } else {
                //成员退出群
                TIMGroupManager.getInstance().quitGroup(getIMGroupId(), new TIMCallBack() {
                    @Override
                    public void onError(int i, String s) {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
            }
        }
    }

    private void requestRemoteVideo() {
        if (bRequsting) {
            bChanged = true;
            ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Endpoint | requestRemoteVideo set Change whilie requesting");
            return;
        } else {
            bChanged = false;
        }

        int viewindex = 0;
        int len = mReqUserVideoListBak.size() + mReqUserScreenListBak.size();
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Endpoint | requestRemoteVideo enter length: " + len);
        if (len > ILiveConstants.MAX_AV_VIDEO_NUM) {
            len = ILiveConstants.MAX_AV_VIDEO_NUM;
        }
        AVView mRequestViewList[] = new AVView[len];
        String mRequestIdentifierList[] = new String[len];
        for (String id : mReqUserVideoListBak) {
            if (viewindex >= len)
                break;
            AVView view = new AVView();
            view.videoSrcType = AVView.VIDEO_SRC_TYPE_CAMERA;
            view.viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
            mRequestViewList[viewindex] = view;
            mRequestIdentifierList[viewindex] = id;
            viewindex++;
        }

        for (String id : mReqUserScreenListBak) {
            if (viewindex >= len)
                break;
            AVView view = new AVView();
            view.videoSrcType = AVView.VIDEO_SRC_TYPE_SCREEN;
            view.viewSizeType = AVView.VIEW_SIZE_TYPE_BIG;
            mRequestViewList[viewindex] = view;
            mRequestIdentifierList[viewindex] = id;
            viewindex++;
        }

        bRequsting = true;
        mRoomMulti.requestViewList(mRequestIdentifierList, mRequestViewList, viewindex, mRequestViewListCompleteCallback);
        ILiveLog.i(TAG, "showVideo->requestViewList enter");
    }

    // 切换权限(以及角色) 废弃方法
    @Deprecated
    private int innerChangeAuthAndRole(long authBits, byte[] authBuf, final String role, final ILiveCallBack callBack) {
        if (ILiveSDK.getInstance().getAVContext() == null) return -1;
        final AVRoomMulti roomMulti = ILiveSDK.getInstance().getAVContext().getRoom();
        if (null != roomMulti) {
            int length = (null != authBuf) ? authBuf.length : 0;
            roomMulti.changeAuthority(authBits, authBuf, length, new AVCallback() {
                @Override
                public void onComplete(int i, String s) {
                    if (AVError.AV_OK != i) {
                        ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, i, "change auth failed");
                    }

                    if (!TextUtils.isEmpty(role)) {
                        roomMulti.changeAVControlRole(role, new AVCallback() {
                            @Override
                            public void onComplete(int i, String s) {
                                if (AVError.AV_OK != i) {
                                    ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, i, "change role failed");
                                } else {
                                    ILiveFunc.notifySuccess(callBack, 0);
                                }
                            }
                        });
                    } else {
                        ILiveFunc.notifySuccess(callBack, 0);
                    }
                }
            });
        } else {
            return ILiveConstants.ERR_NOT_FOUND;
        }

        return ILiveConstants.NO_ERR;
    }

    // 对外接口
    @Override
    public int init(ILiveRoomConfig config) {
        //helper = new QualityReportHelper();
        ILiveLog.d(TAG, "ILVB-Room|init entered");
        this.mConfig = config;

        clearRoomRes();

        // 添加IM消息回调
        TIMManager.getInstance().addMessageListener(this);
        return ILiveConstants.NO_ERR;
    }

    @Override
    public void shutdown() {
        // 移除IM消息回调
        TIMManager.getInstance().removeMessageListener(this);
        mConfig = null;
        ILiveLog.d(TAG, "ILVB-Room|shutdown");
    }

    @Override
    public int getRoomId() {
        return mRoomId;
    }

    @Override
    public AVRoomMulti getAvRoom() {
        return mRoomMulti;
    }

    @Override
    public String getIMGroupId() {
        return chatRoomId;
    }

    @Override
    public String getHostId() {
        return mOption.getStrHostId();
    }

    /**
     * 获取质量数据，仅限在主线程使用
     */
    @Override
    public ILiveQualityData getQualityData() {
        if (null == ILiveSDK.getInstance().getAVContext() || null == ILiveSDK.getInstance().getAVContext().getRoom()) {
            ILiveLog.w(TAG, "getQualityData->enter with null ptr");
            return null;
        }
        String strTips = ILiveSDK.getInstance().getAVContext().getRoom().getQualityParam();
        if (null == strTips) {   // 空字符串检测
            return null;
        }
        String[] tips = strTips.split(",");
        long loss_rate_recv = 0, loss_rate_send = 0, loss_rate_recv_udt = 0, tick_count_end = 0, cpu_rate_app = 0, cpu_rate_sys = 0, loss_rate_send_udt = 0, tick_count_begin = 0,
                kbps_send = 0, kbps_recv = 0, qos_big_fps = 0;
        for (String tip : tips) {
            if (tip.contains("loss_rate_recv")) {
                loss_rate_recv = getQuality(tip);
            }
            if (tip.contains("loss_rate_send")) {
                loss_rate_send = getQuality(tip);
            }
            if (tip.contains("loss_rate_recv_udt")) {
                loss_rate_recv_udt = getQuality(tip);
            }
            if (tip.contains("loss_rate_send_udt")) {
                loss_rate_send_udt = getQuality(tip);
            }
            if (tip.contains("tick_count_begin")) {
                tick_count_begin = getQuality(tip);
            }
            if (tip.contains("tick_count_end")) {
                tick_count_end = getQuality(tip);
            }
            if (tip.contains("cpu_rate_app")) {
                cpu_rate_app = getQuality(tip);
            }
            if (tip.contains("cpu_rate_sys")) {
                cpu_rate_sys = getQuality(tip);
            }
            if (tip.contains("kbps_send")) {
                kbps_send = getQuality(tip);
            }
            if (tip.contains("kbps_recv")) {
                kbps_recv = getQuality(tip);
            }
            if (tip.contains("qos_big_fps")) {
                qos_big_fps = getQuality(tip);
            }
        }
        ILiveQualityData qd = new ILiveQualityData(tick_count_begin, tick_count_end,
                loss_rate_send, loss_rate_recv,
                cpu_rate_app, cpu_rate_sys,
                kbps_send, kbps_recv, qos_big_fps);
        return qd;

    }

    private long getQuality(String str) {
        long res = 0;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                res = res * 10 + (c - '0');
            }
        }
        return res;
    }

    @Override
    public int createRoom(int roomId, ILiveRoomOption option, final ILiveCallBack callBack) {
        if (bEnterRoom) {
            ILiveLog.w(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|create room failed|last room not quit:" + roomId + "/" + mRoomId);
            return ILiveConstants.ERR_ALREADY_IN_ROOM;
        }
        if (null == option || 0 == (option.getAuthBits() & AVRoomMulti.AUTH_BITS_CREATE_ROOM)) {
            ILiveLog.w(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|create room option is empty or no create room permission!");
            return ILiveConstants.ERR_INVALID_PARAM;
        }

        isHost = true;
        mRoomId = roomId;
        mOption = option;
        mEnterCallBack = callBack;

        bMicEnableUserBak = mOption.isAutoMic();
        bCameraEnableUserBak = mOption.isAutoCamera();

        // 默认添加房间创建者视频
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|start create room:" + roomId + " enter with im:" + mOption.isIMSupport() + "|video:" + mOption.isAutoCamera());
        if (mOption.isIMSupport()) {
            generateIMGroupId();
            TIMGroupManager.getInstance().createGroup(mOption.getGroupType(), new ArrayList<String>(), mOption.getGroupType(), getIMGroupId(), new TIMValueCallBack<String>() {
                @Override
                public void onError(int i, String s) {
                    if (i == 10025) {   //已在房间中,重复进入房间
                        ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|createRoom->im room exist!");
                        notifyException(ILiveConstants.EXCEPTION_IMROOM_EXIST, i, "room exist");
                        enterAVRoom(mEnterCallBack);
                    } else {
                        //helper.init(ILiveConstants.EVENT_CREATE_AVCHATROOM, i, s);
                        //helper.report();
                        ILiveLog.d(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|create im room error:" + i + "|" + s);
                        ILiveFunc.notifyError(mEnterCallBack, ILiveConstants.Module_IMSDK, i, s);
                        //helper.init(ILiveConstants.EVENT_ILIVE_CREATEROOM, i, s);
                        //helper.report();//数据采集
                    }
                }

                @Override
                public void onSuccess(String s) {
                    //数据采集
                    //helper.init(ILiveConstants.EVENT_CREATE_AVCHATROOM, 0, "");
                    //helper.report();
                    //创建AV房间
                    ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|createRoom->im room ok:" + getIMGroupId());
                    enterAVRoom(mEnterCallBack);
                }
            });
        } else {
            enterAVRoom(mEnterCallBack);
        }
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int joinRoom(final int roomId, ILiveRoomOption option, ILiveCallBack callBack) {
        //如果第一次进入房间失败,将一些标志位重置
        if (!bEnterRoom) {
            bRoomLineLock = false;
            isSwitchRoomTag = false;
        }

        if ((bEnterRoom && !isSwitchRoomTag) || bRoomLineLock) {
            ILiveLog.w(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|join room failed|last room not quit:" + roomId + "/" + mRoomId);
            bEnterRoom = false;
            return ILiveConstants.ERR_ALREADY_IN_ROOM;
        }
        if (null == option) {
            ILiveLog.w(TAG, "ILVB-Room|create room option can not be empty!");
            isSwitchRoomTag = false;
            return ILiveConstants.ERR_INVALID_PARAM;
        }
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|joinRoom->id: " + roomId + " isIMsupport: " + option.isIMSupport() + " switch:" + isSwitchRoomTag);
        mRoomId = roomId;
        mOption = option;
        mEnterCallBack = callBack;
        if (mOption.isAVSupport())
            bRoomLineLock = true;

        bMicEnableUserBak = mOption.isAutoMic();
        bCameraEnableUserBak = mOption.isAutoCamera();

        generateIMGroupId();
        if (mOption.isIMSupport() && !mOption.getGroupType().equals("Private")) {   // 非讨论组的群组
            TIMGroupManager.getInstance().applyJoinGroup(getIMGroupId(), "request to join" + getIMGroupId(), new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    //已经在是成员了
                    if (i == ILiveConstants.IS_ALREADY_MEMBER) {
                        ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|joinLiveRoom joinIMChatRoom callback onError  is already member");
                        notifyException(ILiveConstants.EXCEPTION_ALREADY_MEMBER, i, s);
                        enterAVRoom(mEnterCallBack);
                    } else {
                        //helper.init(ILiveConstants.EVENT_JOIN_AVCHATROOM, i, s);
                        //helper.report();
                        ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|join IM room fail " + s + " " + i);
                        if (isSwitchRoomTag) {
                            ILiveFunc.notifyError(mSwitchRoomCallBack, ILiveConstants.Module_IMSDK, i, s);
                            isSwitchRoomTag = false;
                        } else
                            ILiveFunc.notifyError(mEnterCallBack, ILiveConstants.Module_IMSDK, i, s);
                        //helper.init(ILiveConstants.EVENT_ILIVE_JOINROOM, i, s);
                        //helper.report();//数据采集
                    }
                    bRoomLineLock = false;
                }

                @Override
                public void onSuccess() {
                    //helper.init(ILiveConstants.EVENT_JOIN_AVCHATROOM, 0, "");
                    //helper.report();
                    ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|joinLiveRoom joinIMChatRoom callback succ ");
                    if (isSwitchRoomTag)
                        enterAVRoom(mSwitchRoomCallBack);
                    else
                        enterAVRoom(mEnterCallBack);
                }
            });
        } else {
            enterAVRoom(callBack);
        }
        return 0;
    }


    @Override
    public int switchRoom(final int roomId, final ILiveRoomOption option, final ILiveCallBack callBack) {
        mSwitchRoomCallBack = callBack;
        isSwitchRoomTag = true;
        if (mOption != null && mOption.isIMSupport()) {
            //先退出群
            TIMGroupManager.getInstance().quitGroup(getIMGroupId(), new TIMCallBack() {
                @Override
                public void onError(int i, String s) {
                    ILiveFunc.notifyError(mSwitchRoomCallBack, ILiveConstants.Module_IMSDK, i, s);
                    isSwitchRoomTag = false;
                }

                @Override
                public void onSuccess() {
                    //再加入房间
                    if (null != mRootView) {
                        mRootView.clearUserView();
                    }
                    joinRoom(roomId, option, callBack);
                }
            });
        } else {
            //再加入房间
            if (null != mRootView) {
                mRootView.clearUserView();
            }
            joinRoom(roomId, option, callBack);
        }
        return 0;
    }

    @Override
    public int quitRoom(ILiveCallBack callBack) {
        int ret = 0;
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|quitRoom->entered with status:" + bEnterRoom + "/" + bRoomLineLock);
        mExitCallBack = callBack;
        if (bEnterRoom) {
            // 退出房间前关闭服务
            AVContext avContext = ILiveSDK.getInstance().getAVContext();
            if (null != avContext && null != avContext.getAudioCtrl()) {
                avContext.getAudioCtrl().stopTRAEService();
                ret = avContext.exitRoom();
            }
            bEnterRoom = false;
            ILiveLog.w(TAG, "bEnterRoom true quitRoom->exit Room entered!");
        } else {
            if (mOption != null && mOption.isIMSupport()) {
                quitIMRoom();
            }

            if (bRoomLineLock) {
                ILiveFunc.notifyError(mExitCallBack, ILiveConstants.Module_ILIVESDK, ILiveConstants.ERR_BUSY_HERE, "Entering room");
            } else {
                ILiveFunc.notifySuccess(mExitCallBack, 0);
            }
        }
        if (null != mRootView) {
            mRootView.clearUserView();
        }

        return ret;
    }

    @Override
    public int linkRoom(int roomId, final String accountId, String sign, final ILiveCallBack callBack) {
        if (null == mRoomMulti) {
            ILiveLog.w(TAG, "linkRoom->no in av room");
            return ILiveConstants.ERR_NO_ROOM;
        }
        ILiveLog.d(TAG, "linkRoom->room:" + roomId + ", account:" + accountId + ", sign:" + sign);
        mRoomMulti.linkRoom(roomId, accountId, sign, new AVCallback() {
            @Override
            public void onComplete(int code, String errMsg) {
                if (AVError.AV_OK != code) {
                    ILiveLog.w(TAG, "linkRoom->failed:" + code + "|" + errMsg);
                    ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, code, errMsg);
                } else {
                    ILiveFunc.notifySuccess(callBack, 0);
                }
            }
        });

        return ILiveConstants.NO_ERR;
    }

    @Override
    public int unlinkRoom(final ILiveCallBack callBack) {
        if (null == mRoomMulti) {
            ILiveLog.w(TAG, "linkRoom->no in av room");
            return ILiveConstants.ERR_NO_ROOM;
        }
        ILiveLog.d(TAG, "unlinkRoom->enter");
        mRoomMulti.unlinkRoom(new AVCallback() {
            @Override
            public void onComplete(int code, String errMsg) {
                if (AVError.AV_OK != code) {
                    ILiveLog.w(TAG, "unlinkRoom->failed:" + code + "|" + errMsg);
                    ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, code, errMsg);
                } else {
                    ILiveFunc.notifySuccess(callBack, 0);
                }
            }
        });
        return 0;
    }

    @Override
    public boolean isEnterRoom() {
        return bEnterRoom;
    }

    @Override
    public int initAvRootView(AVRootView view) {
        if (null == mRootView || view != mRootView) {
            ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|init root view");
            if (isEnterRoom() && null == mSurfaceView) { // 房间已进入未创建悬浮窗
                createSurfaceView(view.getContext());
            }
            mRootView = view;
            mRootView.initView();
            mRootView.getVideoGroup().initAvRootView(mRootView.getContext(), view);
        }
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int initRootViewArr(ILiveRootView[] views) {
        if (null != views && 0 != views.length) {
            ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|init ilive root view array");
            mRootViewArr = views;
            if (isEnterRoom() && null == mSurfaceView) { // 房间已进入且未创建悬浮窗
                createSurfaceView(mRootViewArr[0].getContext());
            }
            for (ILiveRootView aMRootViewArr : mRootViewArr) {
                aMRootViewArr.initViews();
            }
        }
        return ILiveConstants.NO_ERR;
    }

    @Override
    public AVRootView getRoomView() {
        return mRootView;
    }

    @Override
    public void onPause() {
        if (null == ILiveSDK.getInstance().getAvVideoCtrl()) {
            // SDK尚未初始化
            return;
        }

        if (null != mOption) {
            switch (mOption.getVideoMode()) {
                case ILiveConstants.VIDEOMODE_BSUPPORT:
                    // 允许后台模式下什么也不做
                    break;
                case ILiveConstants.VIDEOMODE_NORMAL:
                    pauseCameraAndMic();
                    break;
                case ILiveConstants.VIDEOMODE_BMUTE:
                    pauseBgStream();
                    break;
                default:
                    break;
            }
        }

        if (null != mRootView) {
            mRootView.onPause();
        }
    }

    @Override
    public void onResume() {
        // 恢复摄像头与Mic状态
        ILiveLog.d(TAG, "ILVB-Room|onResume->resume: camera:" + bCameraEnableUserBak + ", mic:" + bMicEnableUserBak + "/" + bSetCameraCb);

        if (null != mRootView) {
            mRootView.onResume();
        }

        if (!bSetCameraCb || null == ILiveSDK.getInstance().getAVContext()) { // 尚未初始化摄像头回调(未进入房间)，则不处理
            return;
        }

        if (null != mOption) {
            switch (mOption.getVideoMode()) {
                case ILiveConstants.VIDEOMODE_BSUPPORT:
                    // 允许后台模式下什么也不做
                    break;
                case ILiveConstants.VIDEOMODE_NORMAL:
                    resumeCameraAndMic();
                    break;
                case ILiveConstants.VIDEOMODE_BMUTE:
                    resumeBgStream();
                    break;
            }
        }
    }

    @Override
    public void onDestory() {
        ILiveLog.d(TAG, "ILVB-Room|onDestory->enter status:" + bEnterRoom);
        if (bEnterRoom) {
            quitRoom(mExitCallBack);
        }
        if (null != mRootView) {
            mRootView.onDestory();
            mRootView = null;
        }
        if (null != mRootViewArr) {
            mRootViewArr = null;
        }

        mEnterCallBack = null;
        mExitCallBack = null;
        mSwitchRoomCallBack = null;
    }

    @Override
    public int changeAuthAndRole(final long authBits, byte[] authBuf, final String role, final ILiveCallBack callBack) {
        ILiveLog.d(TAG, "ILVB-Room|changeAuthAndRole->" + role);
        return innerChangeAuthAndRole(authBits, authBuf, role, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                // 更新当前权限和角色
                mOption.authBits(authBits);
                if (!TextUtils.isEmpty(role)) {
                    mOption.controlRole(role);
                }
                ILiveFunc.notifySuccess(callBack, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveFunc.notifyError(callBack, module, errCode, errMsg);
            }
        });
    }


    @Override
    public void changeRole(final String role, final ILiveCallBack callBack) {
        ILiveLog.d(TAG, "ILVB-Room|changeRole->" + role);
        final AVRoomMulti roomMulti = ILiveSDK.getInstance().getAVContext().getRoom();
        if (roomMulti != null)
            roomMulti.changeAVControlRole(role, new AVCallback() {
                @Override
                public void onComplete(int i, String s) {
                    if (AVError.AV_OK != i) {
                        ILiveFunc.notifyError(callBack, ILiveConstants.Module_AVSDK, i, "change role failed");
                    } else {
                        ILiveFunc.notifySuccess(callBack, 0);
                    }
                }
            });
    }

    @Override
    public int startPushStream(ILivePushOption option, final ILiveCallBack<ILivePushRes> callBack) {
        PushUseCase usecase = new PushUseCase(EngineFactory.provideCommunicationEngine());
        usecase.start(ILiveLoginManager.getInstance().getMyUserId(), mRoomId, option, callBack);
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int stopPushStream(long channelId, ILiveCallBack callBack) {
        List<Long> ids = new ArrayList<>();
        ids.add(channelId);
        return stopPushStreams(ids, callBack);
    }

    @Override
    public int stopPushStreams(List<Long> ids, final ILiveCallBack callBack) {
        PushUseCase usecase = new PushUseCase(EngineFactory.provideCommunicationEngine());
        usecase.stop(ILiveLoginManager.getInstance().getMyUserId(), mRoomId, ids, callBack);

        return ILiveConstants.NO_ERR;
    }

    @Override
    public int startRecordVideo(ILiveRecordOption option, final ILiveCallBack callBack) {
        if (null == TIMAvManager.getInstance()) {
            ILiveLog.e(TAG, "ILVB-Room|find TIMAvManager failed for record: " + ILiveLoginManager.getInstance().getMyUserId());
            return ILiveConstants.ERR_NOT_FOUND;
        }
        TIMAvManager.RoomInfo roomInfo = TIMAvManager.getInstance().new RoomInfo();
        roomInfo.setRoomId(mRoomId);
        roomInfo.setRelationId(mRoomId);

        requestMultiVideoRecorderStart(roomInfo, option.getParam(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess() {
                ILiveFunc.notifySuccess(callBack, 0);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int stopRecordVideo(final ILiveCallBack<List<String>> callBack) {
        if (null == TIMAvManager.getInstance()) {
            ILiveLog.e(TAG, "ILVB-Room|find TIMAvManager failed for stop record: " + ILiveLoginManager.getInstance().getMyUserId());
            return ILiveConstants.ERR_NOT_FOUND;
        }
        TIMAvManager.RoomInfo roomInfo = TIMAvManager.getInstance().new RoomInfo();
        roomInfo.setRoomId(mRoomId);
        roomInfo.setRelationId(mRoomId);
        TIMAvManager.getInstance().requestMultiVideoRecorderStop(roomInfo, new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int i, String s) {
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(List<String> files) {
                ILiveFunc.notifySuccess(callBack, files);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int enableMic(boolean bEnable) {
        boolean bRet = ILiveSDK.getInstance().getAvAudioCtrl().enableMic(bEnable, new AVAudioCtrl.EnableMicCompleteCallback() {
            @Override
            protected void onComplete(boolean bRet, int iError) {
                ILiveLog.d(TAG, "ILVB-Room|enableMic->onComplete" + bRet + ", result:" + iError);
                super.onComplete(bRet, iError);
            }
        });
        if (bRet) {
            bMicEnableUserBak = bEnable;     // 更新备份状态(允许后台修改)
            ILiveLog.d(TAG, "ILVB-Room|enableMic " + bEnable + ", result:" + bRet);
        } else {
            ILiveLog.e(TAG, "ILVB-Room|enableMic " + bEnable + ", result:" + bRet);
        }
        return bRet ? ILiveConstants.NO_ERR : ILiveConstants.ERR_SDK_FAILED;
    }

    @Override
    public int enableSpeaker(boolean bEnable) {
        boolean bRet = ILiveSDK.getInstance().getAvAudioCtrl().enableSpeaker(bEnable, new AVAudioCtrl.EnableSpeakerCompleteCallback() {
            protected void onComplete(boolean bRet, int iError) {
                ILiveLog.d(TAG, "ILVB-Room|enableSpeaker->onComplete" + bRet + ", result:" + iError);
                super.onComplete(bRet, iError);
            }
        });
        if (bRet) {
            ILiveLog.d(TAG, "ILVB-Room|enableSpeaker " + bEnable + ", result:" + bRet);
        } else {
            ILiveLog.e(TAG, "ILVB-Room|enableSpeaker " + bEnable + ", result:" + bRet);
        }
        return bRet ? ILiveConstants.NO_ERR : ILiveConstants.ERR_SDK_FAILED;
    }

    @Override
    public int enableCamera(int cameraId, boolean bEnable) {
        if (ILiveConstants.NONE_CAMERA != curCtrlCameraId) {
            ILiveLog.w(TAG, "ILVB-Room|enableCamera->warning last operation not completed");
        }

        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|strart enableCamera:" + cameraId + ", " + bEnable);
        if (null == ILiveSDK.getInstance().getAvVideoCtrl()) {
            ILiveLog.w(TAG, "ILVB-Room|enableCamera->no AvVideoCtrl valid!");
            return ILiveConstants.ERR_AV_NOT_READY;
        }
        curCtrlCameraId = cameraId;
        int bRet = ILiveSDK.getInstance().getAvVideoCtrl().enableCamera(cameraId, bEnable, mEnableCameraCallback);
        if (ILiveConstants.NO_ERR == bRet) {
            bCameraEnableUserBak = bEnable;     // 更新备份状态(允许后台修改)
            if (true == bEnable && null != mRootView && null != mOption) {
                renderUserVideo(ILiveLoginManager.getInstance().getMyUserId(), AVView.VIDEO_SRC_TYPE_CAMERA);
            }
            ILiveLog.d(TAG, "ILVB-Room|enableCamera id:" + cameraId + "/" + bEnable + ", result: " + bRet);
        } else {
            curCtrlCameraId = ILiveConstants.NONE_CAMERA;
            ILiveLog.e(TAG, "ILVB-Room|enableCamera id:" + cameraId + "/" + bEnable + ", result: " + bRet);
        }
        return bRet;
    }

    @Override
    public int getCurCameraId() {
        if (null != mOption) {
            return mOption.getCameraId();
        }
        return ILiveConstants.FRONT_CAMERA;
    }

    @Override
    public int switchCamera(int cameraId) {
        int bRet = ILiveSDK.getInstance().getAvVideoCtrl().switchCamera(cameraId, new AVVideoCtrl.SwitchCameraCompleteCallback() {
            @Override
            protected void onComplete(int cameraId, int result) {
                curCameraId = cameraId;
                mOption.cameraId(cameraId);
                super.onComplete(cameraId, result);
            }
        });
        return bRet;
    }

    @Override
    public int getActiveCameraId() {
        return curCameraId;
    }

    @Override
    public int enableBeauty(float value) {
        ILiveSDK.getInstance().getAvVideoCtrl().inputBeautyParam(value);
        // 恢复美白
        enableWhite(curWhite);
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int enableWhite(float value) {
        ILiveSDK.getInstance().getAvVideoCtrl().inputWhiteningParam(value);
        curWhite = value;
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int bindIMGroupId(String groupId) {
        ILiveLog.d(TAG, "bindIMGroupId->groupId: " + groupId);
        chatRoomId = groupId;
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int unBindIMGroupId() {
        ILiveLog.d(TAG, "unBindIMGroupId->enter");
        chatRoomId = null;
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int sendC2CMessage(String dstUser, TIMMessage message, final ILiveCallBack<TIMMessage> callBack) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, dstUser);
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|sendC2CMessage->id:" + dstUser);
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //helper.init(ILiveConstants.EVENT_SEND_C2C_CUSTOM_MSG, i, s);
                //helper.report();//数据采集
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                //helper.init(ILiveConstants.EVENT_SEND_C2C_CUSTOM_MSG, 0, "");
                //helper.report();//数据采集
                ILiveFunc.notifySuccess(callBack, message);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int sendGroupMessage(TIMMessage message, final ILiveCallBack<TIMMessage> callBack) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, getIMGroupId());
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|sendGroupMessage->id:" + getIMGroupId());
        conversation.sendMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //helper.init(ILiveConstants.EVENT_SEND_GROUP_TEXT_MSG, i, s);
                //helper.report();//数据采集
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                //helper.init(ILiveConstants.EVENT_SEND_GROUP_TEXT_MSG, 0, "");
                //helper.report();//数据采集
                ILiveFunc.notifySuccess(callBack, message);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int sendC2COnlineMessage(String dstUser, TIMMessage message, final ILiveCallBack<TIMMessage> callBack) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, dstUser);
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|sendC2COnlineMessage->id:" + dstUser);
        conversation.sendOnlineMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                //helper.init(ILiveConstants.EVENT_SEND_GROUP_TEXT_MSG, i, s);
                //helper.report();//数据采集
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                //helper.init(ILiveConstants.EVENT_SEND_GROUP_TEXT_MSG, 0, "");
                //helper.report();//数据采集
                ILiveFunc.notifySuccess(callBack, message);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public int sendGroupOnlineMessage(TIMMessage message, final ILiveCallBack<TIMMessage> callBack) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, getIMGroupId());
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|sendGroupOnlineMessage->id:" + getIMGroupId());
        conversation.sendOnlineMessage(message, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                ILiveFunc.notifyError(callBack, ILiveConstants.Module_IMSDK, i, s);
            }

            @Override
            public void onSuccess(TIMMessage message) {
                ILiveFunc.notifySuccess(callBack, message);
            }
        });
        return ILiveConstants.NO_ERR;
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> list) {
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|onNewMessage->size:" + list.size());
        if (null != mConfig.getRoomMessageListener()) {
            return mConfig.getRoomMessageListener().onNewMessages(list);
        }
        return false;
    }

    @Override
    public void onEnterRoomComplete(int result, String s) {
        ILiveLog.w(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|enter av room complete result: " + result);
        if (result == 0) {
            //helper.init(isHost ? ILiveConstants.EVENT_ILIVE_CREATEROOM : ILiveConstants.EVENT_ILIVE_JOINROOM, 0, "");
            //helper.report();//数据采集
            // 获取房间
            mRoomMulti = ILiveSDK.getInstance().getAVContext().getRoom();

            if (!bSetCameraCb) {
                ILiveLog.d(TAG, "ILVB-Room|onEnterRoomComplete->setCameraPreviewCallback enter");
                ILiveSDK.getInstance().getAvVideoCtrl().setCameraPreviewChangeCallback(mCameraPreviewChangeCallback);
                bSetCameraCb = true;
            }

            bEnterRoom = true;
            bRequsting = false;

            // 创建surfaceView
            if (null != mRootView) {
                createSurfaceView(mRootView.getContext());
            } else if (null != mRootViewArr && null != mRootViewArr[0]) {
                createSurfaceView(mRootViewArr[0].getContext());
            } else {
                notifyException(ILiveConstants.EXCEPTION_NO_ROOT_VIEW, ILiveConstants.ERR_SDK_FAILED, "no root view found");
            }
            // 通知上层进入房间成功
            ILiveFunc.notifySuccess(mEnterCallBack, 0);
        } else {
            //helper.init(isHost ? ILiveConstants.EVENT_ILIVE_CREATEROOM : ILiveConstants.EVENT_ILIVE_JOINROOM, result, s);
            //helper.report();//数据采集
            ILiveFunc.notifyError(mEnterCallBack, ILiveConstants.Module_AVSDK, result, "Enter AV Room failed");
            if (result == AVError.AV_ERR_HAS_IN_THE_STATE) {
                bEnterRoom = true;
            }
        }
        bRoomLineLock = false;
    }

    @Override
    public void onRoomDisconnect(int i, String s) {
        ILiveLog.e(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|onRoomDisconnect->enter: " + i);
        if (null != mOption.getRoomDisconnectListener()) {
            mOption.getRoomDisconnectListener().onRoomDisconnect(i, "room disconnected");
        }
        clearRoomRes();
    }

    @Override
    public void onSwitchRoomComplete(int i, String s) {
        if (i == 0) {
            ILiveFunc.notifySuccess(mSwitchRoomCallBack, 0);
            isSwitchRoomTag = false;
        } else {
            ILiveFunc.notifyError(mSwitchRoomCallBack, ILiveConstants.Module_AVSDK, i, s);
            isSwitchRoomTag = false;
        }
        bRoomLineLock = false;
    }


    @Override
    public void onDisableAudioIssue() {

    }

    @Override
    public void onHwStateChangeNotify(boolean b, boolean b1, boolean b2, String s) {

    }

    @Override
    public void onExitRoomComplete() {
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|exit avroom  Complete with cb:" + mExitCallBack);
        if (null != mOption && mOption.isIMSupport()) { // 退出IM房间
            quitIMRoom();
        }

        if (null != mRootView) {
            removeSurfaceView(mRootView.getContext());
            mRootView.clearUserView();
        } else if (null != mRootViewArr && null != mRootViewArr[0]) {
            removeSurfaceView(mRootViewArr[0].getContext());
        }

        clearRoomRes();
        ILiveFunc.notifySuccess(mExitCallBack, 0);
    }

    @Override
    public void onEndpointsUpdateInfo(int eventid, String[] updateList) {
        //设置了外部回调
        if (mOption == null) return;

        if (mOption.getMemberStatusLisenter() != null) {
            if (mOption.getMemberStatusLisenter().onEndpointsUpdateInfo(eventid, updateList) == true) {
                ILiveLog.d(TAG, "ILVB-Endpoint| custom handle eventid = " + eventid + "/" + updateList.toString());
                return;
            }
        }

        switch (eventid) {
            case ILiveConstants.TYPE_MEMBER_CHANGE_IN:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|member in/" + ILiveFunc.getArrStr(updateList));
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_HAS_CAMERA_VIDEO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|has camera/" + ILiveFunc.getArrStr(updateList));
                if (null == updateList) return;
                //如果有自己直接渲染
                for (String id : updateList) {
                    if (id.equals(ILiveLoginManager.getInstance().getMyUserId())) {
                        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|onEndpointsUpdateInfo myself id has camera " + ILiveLoginManager.getInstance().getMyUserId());
                        renderUserVideo(id, AVView.VIDEO_SRC_TYPE_CAMERA);
                        return;
                    } else if (!mReqUserVideoListBak.contains(id)) {
                        mReqUserVideoListBak.add(id);
                    }
                    notifyVideoVideoEvent(id, AVView.VIDEO_SRC_TYPE_CAMERA, true);
                }
                //其他人全量请求渲染
                if (mOption.isAutoRender()) {
                    requestRemoteVideo();
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_HAS_SCREEN_VIDEO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|has screen/" + ILiveFunc.getArrStr(updateList));
                if (null == updateList) return;
                //如果有自己直接渲染
                for (String id : updateList) {
                    if (id.equals(ILiveLoginManager.getInstance().getMyUserId())) {
                        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|onEndpointsUpdateInfo myself id has screen " + ILiveLoginManager.getInstance().getMyUserId());
                        renderUserVideo(id, AVView.VIDEO_SRC_TYPE_SCREEN);
                        return;
                    } else if (!mReqUserScreenListBak.contains(id)) {
                        mReqUserScreenListBak.add(id);
                    }
                    notifyVideoVideoEvent(id, AVView.VIDEO_SRC_TYPE_SCREEN, true);
                }
                //其他人全量请求渲染
                if (mOption.isAutoRender()) {
                    requestRemoteVideo();
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_NO_CAMERA_VIDEO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|no video/" + ILiveFunc.getArrStr(updateList));
                if (null == updateList) return;
                for (String id : updateList) {
                    ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-Room|onEndpointsUpdateInfo no camera id " + id);
                    closeUserVideo(id, AVView.VIDEO_SRC_TYPE_CAMERA);
                    // 移除备份信息
                    mReqUserVideoListBak.remove(id);
                    notifyVideoVideoEvent(id, AVView.VIDEO_SRC_TYPE_CAMERA, false);
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_NO_SCREEN_VIDEO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|no screen/" + ILiveFunc.getArrStr(updateList));
                if (null == updateList) return;
                for (String id : updateList) {
                    closeUserVideo(id, AVView.VIDEO_SRC_TYPE_SCREEN);
                    // 移除备份信息
                    mReqUserScreenListBak.remove(id);
                    notifyVideoVideoEvent(id, AVView.VIDEO_SRC_TYPE_SCREEN, false);
                }
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_HAS_AUDIO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|has audio/" + ILiveFunc.getArrStr(updateList));
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_NO_AUDIO:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|no audio/" + ILiveFunc.getArrStr(updateList));
                break;
            case ILiveConstants.TYPE_MEMBER_CHANGE_OUT:
                ILiveLog.d(TAG, "ILVB-Endpoint| eventid = " + eventid + "|member out/" + ILiveFunc.getArrStr(updateList));
                break;
            default:
                ILiveLog.d(TAG, "ILVB-Endpoint| ignore eventid = " + eventid + "/" + ILiveFunc.getArrStr(updateList));
                break;
        }
    }

    @Override
    public void onPrivilegeDiffNotify(int i) {

    }

    @Override
    public void onSemiAutoRecvCameraVideo(String[] strings) {
        ILiveLog.d(TAG, "onSemiAutoRecvCameraVideo->" + ILiveFunc.getArrStr(strings));
        for (String id : strings) {
            renderUserVideo(id, AVView.VIDEO_SRC_TYPE_CAMERA);
        }
    }

    @Override
    public void onSemiAutoRecvScreenVideo(String[] strings) {
        ILiveLog.d(TAG, "onSemiAutoRecvScreenVideo->" + ILiveFunc.getArrStr(strings));
        for (String id : strings) {
            renderUserVideo(id, AVView.VIDEO_SRC_TYPE_SCREEN);
        }
    }

    @Override
    public void onSemiAutoRecvMediaFileVideo(String[] strings) {

    }

    @Override
    public void onCameraSettingNotify(int i, int i1, int i2) {

    }

    @Override
    public void onRoomEvent(int i, int i1, Object o) {

    }

    private void clearRoomRes() {
        curCameraId = ILiveConstants.NONE_CAMERA;
        bEnterRoom = false;
        bRoomLineLock = false;
        bSetCameraCb = false;
        bCameraEnableUserBak = false;
        bMicEnableUserBak = false;
        mOption = null;
        chatRoomId = null;
        isHost = false;
        if (null != sensorControl) {
            sensorControl.stopListener();
            sensorControl = null;
        }
        // 重置房间质量信息
        ILiveQualityData.clearLive();
        mRoomId = ILiveConstants.INVALID_INTETER_VALUE;
    }

    private void pauseCameraAndMic() {
        ILiveLog.d(TAG, "ILVB-Room|bakCameraAndMic->bak: camera:" + bCameraEnableUserBak + ", mic:" + bMicEnableUserBak);
        if (bCameraEnableUserBak) {
            enableCamera(mOption.getCameraId(), false);
            bCameraEnableUserBak = true;   // 恢复修改
        }
        if (bMicEnableUserBak) {
            enableMic(false);
            bMicEnableUserBak = true;   // 恢复修改
        }
    }

    private void resumeCameraAndMic() {
        ILiveLog.d(TAG, "ILVB-Room|resumeCameraAndMic->resume: camera:" + bCameraEnableUserBak + ", mic:" + bMicEnableUserBak);
        if (bCameraEnableUserBak) {
            enableCamera(mOption.getCameraId(), true);
        }
        if (bMicEnableUserBak) {
            enableMic(true);
        }
    }

    private void pauseBgStream() {
        pauseCameraAndMic();
        if (null != mRoomMulti) {
            mRoomMulti.cancelAllView(new AVCallback() {
                @Override
                public void onComplete(int ret, String message) {
                    ILiveLog.d(TAG, "ILVB-Room|cancelAllView complete:" + ret + "|" + message);
                }
            });

            // 切换权限(关闭后台音频)
            innerChangeAuthAndRole(AVRoomMulti.AUTH_BITS_JOIN_ROOM, mOption.getAuthBuffer(), null, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    ILiveLog.d(TAG, "ILVB-Room|change bg auth complete");
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ILiveLog.d(TAG, "ILVB-Room|change bg auth failed:" + errCode + "|" + errMsg);
                }
            });
        }
    }

    private void resumeBgStream() {
        // 恢复权限(恢复后台音频)
        innerChangeAuthAndRole(mOption.getAuthBits(), mOption.getAuthBuffer(), mOption.getAvControlRole(), new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                ILiveLog.d(TAG, "ILVB-Room|change ft auth complete");
                resumeCameraAndMic();
                requestRemoteVideo();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ILiveLog.d(TAG, "ILVB-Room|change ft auth failed:" + errCode + "|" + errMsg);
            }
        });
    }

    /**
     * 视频渲染
     */
    private boolean renderUserVideo(String id, int srcType) {
        if (null != mRootView) {
            if (id.equals(mOption.getStrHostId())) {
                int otherType = (AVView.VIDEO_SRC_TYPE_CAMERA == srcType ? AVView.VIDEO_SRC_TYPE_SCREEN : AVView.VIDEO_SRC_TYPE_CAMERA);
                int idx = mRootView.findUserViewIndex(id, otherType);
                if (ILiveConstants.INVALID_INTETER_VALUE != idx && mRootView.getViewByIndex(idx) != null && !mRootView.getViewByIndex(idx).isRendering()) {    // 关闭主播其它的无效视频
                    mRootView.closeUserView(id, otherType, true);
                    ILiveLog.d(TAG, "ILVB-Room|close host zambie video index:" + idx);
                }
            }
            return mRootView.renderVideoView(true, id, srcType, mOption.isAutoRender());
        } else if (null != mRootViewArr) {
            for (ILiveRootView aMRootViewArr1 : mRootViewArr) {
                if (id.equals(aMRootViewArr1.getIdentifier()) && srcType == aMRootViewArr1.getVideoSrcType()) {
                    aMRootViewArr1.render(id, srcType);
                    return true;
                }
            }
            if (null != mOption && mOption.isAutoRender()) {    // 开启自动渲染去查找空闲view
                for (ILiveRootView aMRootViewArr : mRootViewArr) {
                    if (!aMRootViewArr.isRendering()) {
                        aMRootViewArr.render(id, srcType);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void closeUserVideo(String id, int srcType) {
        if (null != mOption && mOption.isAutoRender()) {
            if (null != mRootView) {
                if (id.equals(mOption.getStrHostId())) {
                    int otherType = (AVView.VIDEO_SRC_TYPE_CAMERA == srcType ? AVView.VIDEO_SRC_TYPE_SCREEN : AVView.VIDEO_SRC_TYPE_CAMERA);
                    if (ILiveConstants.INVALID_INTETER_VALUE == mRootView.findUserViewIndex(id, otherType)) {    // 是主播仅有的一路视频
                        int idx = mRootView.findUserViewIndex(id, srcType);
                        if (ILiveConstants.INVALID_INTETER_VALUE != idx) {
                            mRootView.getViewByIndex(idx).setRendering(false);
                        }
                        ILiveLog.d(TAG, "ILVB-Room|do not close host only video idx: " + idx);
                        return;
                    }
                }
                mRootView.closeUserView(id, srcType, true);
            } else if (null != mRootViewArr) {
                for (ILiveRootView aMRootViewArr : mRootViewArr) {
                    if (id.equals(aMRootViewArr.getIdentifier())) {
                        aMRootViewArr.closeVideo();
                        return;
                    }
                }
            }
        }
    }

    // 删除SurfaceView
    private void removeSurfaceView(Context context) {
        if (null != mSurfaceView) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            try {
                windowManager.removeView(mSurfaceView);
                mSurfaceView = null;
            } catch (Exception e) {
                ILiveLog.w(TAG, "removeSurfaceView->failed: " + e.toString());
            }
        }
    }

    // 创建悬浮窗，用于打开摄像头
    private void createSurfaceView(Context context) {
        removeSurfaceView(context);    // 若之前有创建，先删除

        WindowManager windowManager = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = 1;
        layoutParams.height = 1;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // layoutParams.flags |= LayoutParams.FLAG_NOT_TOUCHABLE;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.windowAnimations = 0;// android.R.style.Animation_Toast;
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //layoutParams.setTitle("Toast");
        try {
            mSurfaceView = new SurfaceView(context);
            SurfaceHolder holder = mSurfaceView.getHolder();
            holder.addCallback(mSurfaceHolderListener);
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 3.0以下必须在初始化时调用，否则不能启动预览
            mSurfaceView.setZOrderMediaOverlay(true);
            windowManager.addView(mSurfaceView, layoutParams);
        } catch (IllegalStateException e) {
            windowManager.updateViewLayout(mSurfaceView, layoutParams);
            ILiveLog.w(TAG, "add camera surface view fail: IllegalStateException." + e);
        } catch (Exception e) {
            ILiveLog.w(TAG, "add camera surface view fail." + e);
        }
        ILiveLog.v(TAG, "createSurfaceView->enter");
    }

    /**
     * 发起开始录制请求
     *
     * @param roomInfo 录制房间相关信息，详见{@see RoomInfo}
     * @param param    录制参数，详见{@see RecordParam}
     * @param cb       回调
     */
    private void requestMultiVideoRecorderStart(TIMAvManager.RoomInfo roomInfo, TIMAvManager.RecordParam param, final TIMCallBack cb) {
        if (ILiveLoginManager.getInstance().getMyUserId() == null) return;
        IMMsfUserInfo msfUserInfo = IMMsfCoreProxy.get().getMsfUserInfo(ILiveLoginManager.getInstance().getMyUserId());
        if (null == msfUserInfo || !msfUserInfo.isLoggedIn()) {
            cb.onError(BaseConstants.ERR_SDK_NOT_LOGGED_IN, "current user not login. id: " + ILiveLoginManager.getInstance().getMyUserId());
            return;
        }

        StreamerRecorderContext context = new StreamerRecorderContext();
        context.busiType = 7;
        context.authType = 6;
        context.authKey = mRoomId;
        context.roomId = mRoomId;
        context.sdkAppId = IMMsfCoreProxy.get().getSdkAppId();
        context.uin = msfUserInfo.getTinyid();
        context.recordParam = param;
        context.operation = 1;
        context.subcmd = 0x142;

        requestMultiVideoRecorderRelay(context, new TIMValueCallBack<List<String>>() {
            @Override
            public void onError(int code, String desc) {
                cb.onError(code, desc);
            }

            @Override
            public void onSuccess(List<String> t) {
                cb.onSuccess();
            }
        });
    }


    private void requestMultiVideoRecorderRelay(StreamerRecorderContext context, final TIMValueCallBack<List<String>> cb) {

        if (context.sig != null && context.sig.getBytes().length > 256) {
            cb.onError(-1, "Invalid signature, length is limited to 256 bytes");
        }

        gv_comm_operate.GVCommOprHead head = new gv_comm_operate.GVCommOprHead();
        head.uint32_buss_type.set(context.busiType);        //opensdk
        head.uint32_auth_type.set(context.authType);        //opensdk
        head.uint32_auth_key.set(context.authKey);
        head.uint64_uin.set(context.uin);
        head.uint32_sdk_appid.set(context.sdkAppId);

        gv_comm_operate.ReqBody reqbody = new gv_comm_operate.ReqBody();

        reqbody.req_0x5.setHasFlag(true);
        reqbody.req_0x5.uint32_oper.set(context.operation);
        reqbody.req_0x5.uint32_seq.set(IMMsfCoreProxy.get().random.nextInt());
        if (context.recordParam != null) {
            if (context.recordParam.filename() != null) {
                reqbody.req_0x5.string_file_name.set(context.recordParam.filename());
            }

            reqbody.req_0x5.uint32_classid.set(context.recordParam.classId());
            reqbody.req_0x5.uint32_IsTransCode.set(context.recordParam.isTransCode() ? 1 : 0);
            reqbody.req_0x5.uint32_IsScreenShot.set(context.recordParam.isScreenShot() ? 1 : 0);
            reqbody.req_0x5.uint32_IsWaterMark.set(context.recordParam.isWaterMark() ? 1 : 0);
            for (String tag : context.recordParam.tags()) {
                reqbody.req_0x5.string_tags.add(tag);
            }

            reqbody.req_0x5.uint32_sdk_type.set(0x01);

        }

        byte[] busibuf = NetworkUtil.formReq(ILiveLoginManager.getInstance().getMyUserId(), context.subcmd, context.roomId, context.sig,
                head.toByteArray(), reqbody.toByteArray());

        //do the request
        MultiVideoTinyId.get().requestMultiVideoInfo(busibuf, new TIMValueCallBack<byte[]>() {
            @Override
            public void onError(int code, String desc) {
                cb.onError(code, desc);
            }

            @Override
            public void onSuccess(byte[] rspbody) {
                gv_comm_operate.RspBody rsp = new gv_comm_operate.RspBody();

                byte[] buff = NetworkUtil.parseRsp(rspbody);
                if (buff == null) {
                    cb.onError(BaseConstants.ERR_PARSE_RESPONSE_FAILED, "parse recorder rsp failed");
                    return;
                }

                try {
                    rsp.mergeFrom(buff);
                } catch (Throwable e) {
                    cb.onError(BaseConstants.ERR_PARSE_RESPONSE_FAILED, "parse recorder rsp failed");
                    return;
                }

                if (rsp.rsp_0x5.uint32_result.get() != 0) {

                    cb.onError(rsp.rsp_0x5.uint32_result.get(), rsp.rsp_0x5.str_errorinfo.get());
                    return;
                }

                cb.onSuccess(rsp.rsp_0x5.str_fileID.get());
            }
        });
    }

    private void notifyVideoVideoEvent(String id, int srcType, boolean bHasVideo) {
        if (null != mRootView) {
            AVVideoView videoView = mRootView.getUserAvVideoView(id, srcType);
            if (null != videoView && null != videoView.getVideoListener()) {
                if (bHasVideo)
                    videoView.getVideoListener().onHasVideo(srcType);
                else
                    videoView.getVideoListener().onNoVideo(srcType);
            }
        } else if (null != mRootViewArr) {
            for (ILiveRootView aMRootViewArr : mRootViewArr) {
                if (id.equals(aMRootViewArr) && srcType == aMRootViewArr.getVideoSrcType()) {
                    if (null != aMRootViewArr.getVideoListener()) {
                        if (bHasVideo)
                            aMRootViewArr.getVideoListener().onHasVideo(srcType);
                        else
                            aMRootViewArr.getVideoListener().onNoVideo(srcType);
                    }
                    return;
                }
            }
        }
    }
}


