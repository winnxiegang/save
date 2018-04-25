package com.android.xjq.presenters;

import android.hardware.Camera;
import android.os.Handler;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.library.Utils.LibAppUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.XjqApplication;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.NewLiveCommentBean;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.presenters.viewinface.LiveView;
import com.android.xjq.utils.live.Constants;
import com.google.gson.Gson;
import com.tencent.TIMCallBack;
import com.tencent.TIMCustomElem;
import com.tencent.TIMElem;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMMessage;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.av.sdk.AVVideoCtrl;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveMemStatusLisenter;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveRecordOption;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.core.ILiveRoomOption;
import com.tencent.ilivesdk.core.impl.ILVBLogin;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;


/**
 * 直播控制类
 */
public class LiveHelper extends Presenter implements ILiveRoomOption.onRoomDisconnectListener, Observer {

    private final String TAG = "LiveHelper";
    private LiveView mLiveView;
    private boolean bCameraOn = false;
    private boolean bMicOn = false;
    private boolean flashLgihtStatus = false;
    private Gson gson = new Gson();

    public LiveHelper(LiveView liveview) {
        mLiveView = liveview;
        if (MessageEvent.getInstance().countObservers() > 0) {
            MessageEvent.getInstance().deleteObservers();
        }
        MessageEvent.getInstance().addObserver(this);
    }

    @Override
    public void onDestory() {
        mLiveView = null;
        MessageEvent.getInstance().deleteObserver(this);
        ILVLiveManager.getInstance().quitRoom(null);
    }

    /**
     * 进入房间
     */
    public void startEnterRoom() {
        joinRoom();
    }

    public void switchRoom() {
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(null)
                .autoCamera(false)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BMUTE)
                .controlRole(Constants.NORMAL_MEMBER_ROLE)
                .exceptionListener(new ILiveRoomOption.onExceptionListener() {
                    @Override
                    public void onException(int exceptionId, int errCode, String errMsg) {
                        LogUtils.e("打开直播失败", errMsg);
                    }
                })
                .roomDisconnectListener(this)
                .imGroupId(CurLiveInfo.getAvChatRoomId())
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM |
                        AVRoomMulti.AUTH_BITS_RECV_AUDIO |
                        AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO |
                        AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .audioCategory(AVRoomMulti.AUDIO_CATEGORY_MEDIA_PLAYBACK)
                .highAudioQuality(true)
                .autoSpeaker(true)
                .autoMic(false);
        ILVLiveManager.getInstance().switchRoom(CurLiveInfo.pushStreamId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
//                LibAppUtil.showTip(XjqApplication.getContext(), "切换房间成功");
                ILiveLog.d(TAG, "ILVB-Suixinbo|switchRoom->join room sucess");
                LogUtils.e("LiveHelper", "切换房间成功-----------");
                mLiveView.enterRoomComplete(0, true);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("LiveActivity", "----------------------进入房间失败-----------------------");
                LogUtils.e("LiveHelper", "切换房间失败|errCode=" + errCode + " errMsg=" + errMsg);
                //切换到新房间失败，同时已退出原来的房间,这边再退一下,将部分标记值重置
                if (errCode == 2002) {
//                    joinRoom();
                    ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                        @Override
                        public void onSuccess(Object data) {
                            joinRoom();
                        }

                        @Override
                        public void onError(String module, int errCode, String errMsg) {
                            joinRoom();
                        }
                    });
                }
//                LibAppUtil.showTip(XjqApplication.getContext(), "切换房间失败");
              /*  LibAppUtil.showTip(XjqApplication.getContext(), "切换房间失败");
                if (ILiveConstants.Module_ILIVESDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "LIVE sdk初始化失败");
                } else if (ILiveConstants.Module_AVSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "AV sdk初始化失败");
                } else if (ILiveConstants.Module_IMSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "IM sdk初始化失败");
                } else if (ILiveConstants.Module_TLSSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "TLS sdk初始化失败");
                }*/
            }
        });

    }


    public void startExitRoom() {
        ILVLiveManager.getInstance().quitRoom(new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("LiveHelper", "退出房间成功-----------");
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(0, true, null);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("LiveHelper", "退出房间失败-----------");
                if (null != mLiveView) {
                    mLiveView.quiteRoomComplete(0, true, null);
                }
            }
        });

    }

    /**
     * 发送信令
     */
    public int sendGroupCmd(int cmd, String param) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        return sendCmd(customCmd);
    }

    public int sendC2CCmd(final int cmd, String param, String destId) {
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setDestId(destId);
        customCmd.setCmd(cmd);
        customCmd.setParam(param);
        customCmd.setType(ILVText.ILVTextType.eC2CMsg);
        return sendCmd(customCmd);
    }

    /**
     * 打开闪光灯
     */
    public void toggleFlashLight() {
        AVVideoCtrl videoCtrl = ILiveSDK.getInstance().getAvVideoCtrl();
        if (null == videoCtrl) {
            return;
        }

        final Object cam = videoCtrl.getCamera();
        if ((cam == null) || (!(cam instanceof Camera))) {
            return;
        }
        final Camera.Parameters camParam = ((Camera) cam).getParameters();
        if (null == camParam) {
            return;
        }

        Object camHandler = videoCtrl.getCameraHandler();
        if ((camHandler == null) || (!(camHandler instanceof Handler))) {
            return;
        }

        //对摄像头的操作放在摄像头线程
        if (flashLgihtStatus == false) {
            ((Handler) camHandler).post(new Runnable() {
                public void run() {
                    try {
                        camParam.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        ((Camera) cam).setParameters(camParam);
                        flashLgihtStatus = true;
                    } catch (RuntimeException e) {

                    }
                }
            });
        } else {
            ((Handler) camHandler).post(new Runnable() {
                public void run() {
                    try {
                        camParam.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        ((Camera) cam).setParameters(camParam);
                        flashLgihtStatus = false;
                    } catch (RuntimeException e) {

                    }

                }
            });
        }
    }

    public void startRecord(ILiveRecordOption option) {
        ILiveRoomManager.getInstance().startRecordVideo(option, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

                mLiveView.startRecordCallback(true);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                mLiveView.startRecordCallback(false);
            }
        });
    }

    public void stopRecord() {
        ILiveRoomManager.getInstance().stopRecordVideo(new ILiveCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {

                for (String url : data) {

                }
                mLiveView.stopRecordCallback(true, data);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

                mLiveView.stopRecordCallback(false, null);
            }
        });
    }

    public void stopPush() {
        ILiveRoomManager.getInstance().stopPushStream(0, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

                mLiveView.stopStreamSucc();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }


    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        if (null != mLiveView) {
            mLiveView.quiteRoomComplete(0, true, null);
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        List<TIMMessage> list = (List<TIMMessage>) o;
        parseIMMessage(list);
    }

    public void toggleCamera() {
        bCameraOn = !bCameraOn;

        ILiveRoomManager.getInstance().enableCamera(ILiveRoomManager.getInstance().getCurCameraId(), bCameraOn);
    }

    public void toggleMic() {
        bMicOn = !bMicOn;

        ILiveRoomManager.getInstance().enableMic(bMicOn);
    }

    public boolean isMicOn() {
        return bMicOn;
    }


    public void downMemberVideo() {
        if (!ILiveRoomManager.getInstance().isEnterRoom()) {

        }
        ILVLiveManager.getInstance().downToNorMember(Constants.NORMAL_MEMBER_ROLE, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                bMicOn = false;
                bCameraOn = false;

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {

            }
        });
    }


    public void switchIMGroupId() {

        TIMGroupManager.getInstance().applyJoinGroup(CurLiveInfo.getAvChatRoomId(), "request to join" + CurLiveInfo.getAvChatRoomId(), new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                //已经在是成员了
                if (i == ILiveConstants.IS_ALREADY_MEMBER) {
                } else {
                }
            }

            @Override
            public void onSuccess() {
                //String identifier = LoginInfoHelper.getInstance().getIdentifier();
                String identifier1 = TIMGroupManager.getInstance().getIdentifier();
                ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|joinLiveRoom joinIMChatRoom callback succ ");
            }
        });
    }


    private void joinRoom() {
        final String identifier = getIdentifier();
        //之前登录失败,如果一进来由于网络原因登录失败
        if (StringUtils.isBlank(ILVBLogin.getInstance().getMyUserId())) {
            String appUserSign = null;
            if (LoginInfoHelper.getInstance().getUserId() != null) {
                appUserSign = LoginInfoHelper.getInstance().getUserAppUserSign();
            } else {
                appUserSign = LoginInfoHelper.getInstance().getGuestAppUserSign();
            }
            InitBusinessHelper.reLoginLive(identifier, appUserSign, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    startAvContext(identifier);
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    LibAppUtil.showTip(XjqApplication.getContext(), errMsg);
                }
            });
        } else {
            startAvContext(identifier);
        }
    }

    private void startAvContext(String identifier) {
        if (ILVBLogin.getInstance().isAvContextStarted()) {
            startPlay();
        } else {
            ILVBLogin.getInstance().startContext(identifier, new ILiveCallBack() {
                @Override
                public void onSuccess(Object data) {
                    if (mLiveView == null) return;
                    startPlay();
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ToastUtil.showLong(XjqApplication.getContext(), "播放失败");
                }
            });
        }
    }

    private String getIdentifier() {
        String identifier = null;
        if (LoginInfoHelper.getInstance().getUserId() != null) {
            identifier = LoginInfoHelper.getInstance().getUserIdentifier();
        } else {
            identifier = LoginInfoHelper.getInstance().getGuestIdentifier();
        }
        return identifier;
    }

    private void startPlay() {
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(LoginInfoHelper.getInstance().getAppId())
                .autoCamera(false)
                .imsupport(true)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BMUTE)
                .controlRole(Constants.NORMAL_MEMBER_ROLE)
                .exceptionListener(new ILiveRoomOption.onExceptionListener() {
                    @Override
                    public void onException(int exceptionId, int errCode, String errMsg) {
                        LogUtils.e("打开直播失败", errMsg);
                    }
                })
                .setRoomMemberStatusLisenter(new ILiveMemStatusLisenter() {
                    @Override
                    public boolean onEndpointsUpdateInfo(int eventid, String[] updateList) {

                        return false;
                    }
                })
                .imGroupId(CurLiveInfo.getAvChatRoomId())
                .authBits(AVRoomMulti.AUTH_BITS_CREATE_ROOM |
                        AVRoomMulti.AUTH_BITS_JOIN_ROOM |
                        AVRoomMulti.AUTH_BITS_RECV_AUDIO |
                        AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO |
                        AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .audioCategory(AVRoomMulti.AUDIO_CATEGORY_MEDIA_PLAYBACK)
                .highAudioQuality(true)
                .autoSpeaker(true)
                .autoMic(false);

        ILVLiveManager.getInstance().joinRoom(CurLiveInfo.pushStreamId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                LogUtils.e("LiveActivity", "----------------------进入房间成功-----------------------");
                LogUtils.e(TAG, "加入房间成功");
                mLiveView.enterRoomComplete(0, true);
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                LogUtils.e("LiveActivity", "----------------------进入房间失败-----------------------");
                LogUtils.e(TAG, "加入房间失败|errCode=" + errCode + " errMsg=" + errMsg);
               /* if (ILiveConstants.Module_ILIVESDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "LIVE sdk初始化失败" + errMsg);
                } else if (ILiveConstants.Module_AVSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "AV sdk初始化失败" + errMsg);
                } else if (ILiveConstants.Module_IMSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "IM sdk初始化失败" + errMsg);
                } else if (ILiveConstants.Module_TLSSDK.equals(module)) {
                    LibAppUtil.showTip(XjqApplication.getContext(), "TLS sdk初始化失败" + errMsg);
                }*/

            }
        });
    }

    private int sendCmd(final ILVCustomCmd cmd) {
        return ILVLiveManager.getInstance().sendCustomCmd(cmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
//                Toast.makeText(mContext, "sendCmd->failed:" + module + "|" + errCode + "|" + errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 解析消息回调
     *
     * @param list 消息列表
     */
    private void parseIMMessage(List<TIMMessage> list) {
        List<TIMMessage> tlist = list;
        if (tlist == null) return;
        for (int i = tlist.size() - 1; i >= 0; i--) {
            TIMMessage currMsg = tlist.get(i);
            if (currMsg == null) continue;
            for (int j = 0; j < currMsg.getElementCount(); j++) {
                if (currMsg.getElement(j) == null)
                    continue;
                TIMElem elem = currMsg.getElement(j);
                TIMElemType type = elem.getType();
                //定制消息
                if (type == TIMElemType.Custom) {
                    handleCustomMsg(elem);
                    continue;
                }
            }
        }
    }

    /**
     * 处理定制消息 赞 关注 取消关注
     */
    private void handleCustomMsg(TIMElem elem) {
        try {
            if (null == mLiveView) {
                return;
            }
            String customText = new String(((TIMCustomElem) elem).getData(), "UTF-8");

            LogUtils.e(TAG, "customText=" + customText);

            NewLiveCommentBean newLiveCommentBean = gson.fromJson(customText, NewLiveCommentBean.class);
            if (!CurLiveInfo.getGroupId().equals(newLiveCommentBean.getGroupId())) return;
            LiveCommentBean bean = new LiveCommentBean();
            bean.setSenderId(newLiveCommentBean.getSendUserId());
            bean.setType(newLiveCommentBean.getTypeCode());
            bean.setSenderName(newLiveCommentBean.getSendUserLoginName());
            bean.setProperties(newLiveCommentBean.getProperties());
            LiveCommentBean.ContentBean contentBean = new LiveCommentBean.ContentBean();
            if (newLiveCommentBean.getBodies() != null && newLiveCommentBean.getBodies().size() > 0) {
                NewLiveCommentBean.BodiesBean body = newLiveCommentBean.getBodies().get(0);
                contentBean.setBody(body.getParameters());
                contentBean.setProperties(body.getProperties());
                contentBean.setText(body.getContent());
                contentBean.setFontColor(body.getFontColor());
            }
            bean.setContent(contentBean);

            mLiveView.showMessage(bean);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void switchCamera() {
        if (ILiveRoomManager.getInstance().getCurCameraId() == ILiveConstants.NONE_CAMERA) return;
        if (ILiveRoomManager.getInstance().getCurCameraId() == ILiveConstants.FRONT_CAMERA) {
            ILiveRoomManager.getInstance().switchCamera(ILiveConstants.BACK_CAMERA);
        } else {
            ILiveRoomManager.getInstance().switchCamera(ILiveConstants.FRONT_CAMERA);
        }

    }
}
