package com.android.xjq.controller;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.LiveInfoJson;
import com.android.xjq.controller.live.LiveStateChangeController;
import com.android.xjq.model.MemberID;
import com.android.xjq.model.VideoPlayerTypeEnum;
import com.android.xjq.presenters.LiveHelper;
import com.android.xjq.presenters.viewinface.LiveView;
import com.android.xjq.service.FloatingView;
import com.tencent.av.TIMAvManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.ilivesdk.view.VideoListener;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayConfig;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2017/3/29.
 */

public class VideoPlayerController implements LiveView, ITXLivePlayListener {
    private final static String TAG = VideoPlayerController.class.getSimpleName();

    /**
     * 当前使用的播放类型
     */
    private VideoPlayerTypeEnum currentType;

    private LiveHelper mLiveHelper;

    private TXLivePlayer mTxlpPlayer;

    private ViewGroup mRootView;

    private Context context;

    private VideoPlayListener videoPlayListener;

    private boolean isFirstEnterRoom;

    private FloatingView floatingView;

    private LiveStateChangeController liveStateChangeController;

    private boolean mShowFloatingView = false;

    public VideoPlayerController(Context context, VideoPlayerTypeEnum currentType, VideoPlayListener videoPlayListener) {

        this.context = context;

        this.currentType = currentType;

        this.videoPlayListener = videoPlayListener;

        if (VideoPlayerTypeEnum.AVROOTVIEW == currentType) {

            mLiveHelper = new LiveHelper(this);

        } else if (VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW == currentType) {

            mTxlpPlayer = new TXLivePlayer(context);

            mTxlpPlayer.setConfig(new TXLivePlayConfig());

            mTxlpPlayer.setRenderMode(TXLiveConstants.RENDER_MODE_ADJUST_RESOLUTION);

            mTxlpPlayer.setPlayListener(this);
        }
    }

    public void reset() {
        isFirstEnterRoom = false;
    }

    public void startPlay(String channelId) {
        if (VideoPlayerTypeEnum.AVROOTVIEW == currentType) {
            if (videoPlayListener != null) {
                videoPlayListener.onPreparePlay();
            }
            if (isFirstEnterRoom) {
                mLiveHelper.startEnterRoom();
            } else {
                if (!floatingView.getChannelId().equals(channelId)) {
                    mLiveHelper.switchRoom();
                } else {
                    //进入房间成功
                    if (videoPlayListener != null) {
                        videoPlayListener.onPlaySuccessListener();

                        videoPlayListener.receiveFirstFrameListener();

                        liveStateChangeController.receiveFirstFrame();
                    }
                }
            }
        } else if (VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW == currentType) {

        }
        //重置
        isFirstEnterRoom = false;
    }

    public void switchRoom() {
        mLiveHelper.switchRoom();
    }

    @Override
    public void stopPlay() {
        if (VideoPlayerTypeEnum.AVROOTVIEW == currentType) {
            mLiveHelper.startExitRoom();
        } else if (VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW == currentType) {
            mTxlpPlayer.stopPlay(false);
        }
    }

    public void switchIMGroupId() {
        if (VideoPlayerTypeEnum.AVROOTVIEW == currentType) {
            mLiveHelper.switchRoom();
        } else if (VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW == currentType) {

        }
    }

    /**
     * 检查悬浮窗并给container添加videoview
     *
     * @param container
     */
    public boolean checkFloatWindow(FloatingView floatingView, ViewGroup container, int height, int width) {

        this.floatingView = floatingView;

        if (floatingView.getRootView() != null) {

            isFirstEnterRoom = false;

            mRootView = floatingView.removeRootView();

            floatingView.removeWindow();

        } else {

            isFirstEnterRoom = true;
            //初始化控件
            if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {

                mRootView = (ViewGroup) View.inflate(XjqApplication.getContext(), R.layout.view_portrait_live_state, null);

            } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {

                mRootView = new TXCloudVideoView(XjqApplication.getContext());
            }
        }

        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);

        mRootView.setLayoutParams(lps);

        //从悬浮窗打开的话,需要将悬浮窗的rootView重新调整大小
        if (!isFirstEnterRoom) {
            changeRootViewSize(true, height, width);
        }

        if (container.getChildCount() == 0) {
            container.addView(mRootView);
        }

        //设置直播状态改变控制
        liveStateChangeController = new LiveStateChangeController(mRootView);

        //设置渲染层
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            getAvRootView().setAutoOrientation(false);//防止视频播放自动旋转
            ILVLiveManager.getInstance().setAvVideoView(getAvRootView());
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
            mTxlpPlayer.setPlayerView(getTXCloudVideoView());
        }

        return isFirstEnterRoom;
    }

    public void startLoadingAnim() {
        liveStateChangeController.startOrStopAnim(true);
    }

    private AVRootView getAvRootView() {
        return (AVRootView) mRootView.findViewById(R.id.rootView);
    }

    private TXCloudVideoView getTXCloudVideoView() {
        return (TXCloudVideoView) mRootView.findViewById(R.id.rootView);
    }

    /**
     * 显示悬浮窗
     *
     * @param container
     * @param channelId
     */
    public void showFloatWindow(ViewGroup container, String channelId) {

        mShowFloatingView = true;

        container.removeView(mRootView);
        //先改变rootView的大小
        int width = context.getResources().getDimensionPixelOffset(R.dimen.floating_windows_width);

        int height = context.getResources().getDimensionPixelOffset(R.dimen.floating_windows_height);

        changeRootViewSize(true, height, width);

        floatingView.setAvRootView(mRootView);

        try {
            //在部分魅族/小米手机上,检测不到悬浮窗权限,则会抛出异常,这里try catch掉,不显示悬浮框
            floatingView.showWindow(channelId);
        } catch (Exception e) {
            //回收资源,并关闭悬浮窗
            floatingView.closeWindow();
        }
    }

    public void changeWindowSize(boolean isPortrait, ViewGroup container, int height, int width) {

        //要改变容器的宽高大小
        ViewGroup.LayoutParams params = container.getLayoutParams();

        params.width = width;

        params.height = height;

        container.setLayoutParams(params);

        changeRootViewSize(isPortrait, height, width);

    }

    private void changeRootViewSize(boolean isPortrait, int height, int width) {

        ViewGroup.LayoutParams lps = mRootView.getLayoutParams();

        lps.height = height;

        lps.width = width;

        mRootView.setLayoutParams(lps);

        if (VideoPlayerTypeEnum.AVROOTVIEW == currentType) {

            getAvRootView().getViewByIndex(0).setPosWidth(width);

            getAvRootView().getViewByIndex(0).setPosHeight(height);

            if (isPortrait) {
                getAvRootView().getViewByIndex(0).setRotation(0);
            } else {
                getAvRootView().getViewByIndex(0).setRotation(180);
            }

            getAvRootView().getViewByIndex(0).autoLayout();

        } else if (VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW == currentType) {
            getTXCloudVideoView().adjustVideoSize();
        }
    }

    //去掉视频，只显示音频
    public void cancelAllView() {
        /*if (ILiveRoomManager.getInstance().getAvRoom() == null) return;
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            if (ILiveRoomManager.getInstance().getAvRoom() == null) return;
            ILiveRoomManager.getInstance().getAvRoom().cancelAllView(new AVCallback() {
                @Override
                public void onComplete(int i, String s) {
                    mRootView.setVisibility(View.GONE);
//                    LibAppUtil.showTip(context, "关闭图像成功");
                }
            });
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
        }*/
    }

    //显示视频
    public void showView() {
        /*if (ILiveRoomManager.getInstance().getAvRoom() == null) return;
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            mRootView.setVisibility(View.VISIBLE);
            switchRoom();
            AVView avView = new AVView();
            avView.videoSrcType = getAvRootView().getViewByIndex(0).getVideoSrcType();
            avView.viewSizeType = AVView.VIEW_SIZE_TYPE_SMALL;
            ILiveRoomManager.getInstance().getAvRoom().requestViewList(new String[]{getAvRootView().getViewByIndex(0).getIdentifier()}, new AVView[]{avView}, 1, new AVRoomMulti.RequestViewListCompleteCallback() {
                @Override
                public void OnComplete(String[] strings, AVView[] avViews, int i, int i1, String s) {
                    mRootView.setVisibility(View.VISIBLE);
//                    LibAppUtil.showTip(context, s);
                }
            });

        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {

        }*/
    }


    public void onResume() {
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            ILiveRoomManager.getInstance().onResume();
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
            mTxlpPlayer.resume();
        }
    }

    public void onPause() {
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            ILiveRoomManager.getInstance().onPause();
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
            mTxlpPlayer.pause();
        }

    }

    public void onDestroy() {
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            ILiveRoomManager.getInstance().onDestory();
            mLiveHelper.onDestory();
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
            getTXCloudVideoView().onDestroy();
        }
        floatingView.onDestroy();
        liveStateChangeController.onDestroy();
        videoPlayListener = null;
        context = null;
    }

    @Override
    public void enterRoomComplete(int id_status, boolean succ) {
        /*if (id_status == 1) {
            //切换房间成功,不会有视频首帧回调,所以要提前在进入房间成功时,就将加载动画消失掉
            liveStateChangeController.receiveFirstFrame();
        }*/
        if (getAvRootView().getViewByIndex(0) == null) return;

        //设置界面自适应屏幕
        getAvRootView().getViewByIndex(0).setSameDirectionRenderMode(AVVideoView.ILiveRenderMode.BLACK_TO_FILL);

        getAvRootView().getViewByIndex(0).setRotate(false);

        getAvRootView().getViewByIndex(0).autoLayout();

        getAvRootView().getViewByIndex(0).setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                liveStateChangeController.receiveFirstFrame();
                if (videoPlayListener != null) {
                    videoPlayListener.receiveFirstFrameListener();
                }
            }

            @Override
            public void onHasVideo(int srcType) {
                liveStateChangeController.liveStateChange(true);
                if (videoPlayListener != null)
                    videoPlayListener.onHasVideo();
            }

            @Override
            public void onNoVideo(int srcType) {
                liveStateChangeController.liveStateChange(false);
                if (videoPlayListener != null)
                    videoPlayListener.onNoVideo();
            }
        });

        if (videoPlayListener != null) {
            videoPlayListener.onPlaySuccessListener();
        }


    }

    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {
        //LibAppUtil.showTip(XjqApplication.getContext(), "退出房间成功");
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            clearOldData();
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
        }
        if (videoPlayListener != null) {
            videoPlayListener.onExitComplete();
        }
        if (mShowFloatingView) {
            onDestroy();
        }
    }

    //清除老房间数据
    private void clearOldData() {
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            getAvRootView().clearUserView();
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {

        }
    }

    @Override
    public void showInviteDialog() {

    }

    @Override
    public void refreshText(String text, String name) {

    }

    @Override
    public void refreshThumbUp() {

    }

    @Override
    public void refreshUI(String id) {

    }

    @Override
    public boolean showInviteView(String id) {
        return false;
    }

    @Override
    public void cancelInviteView(String id) {

    }

    @Override
    public void cancelMemberView(String id) {

    }

    @Override
    public void memberJoin(String id, String name) {

    }

    @Override
    public void hideInviteDialog() {

    }

    @Override
    public void pushStreamSucc(TIMAvManager.StreamRes streamRes) {

    }

    @Override
    public void stopStreamSucc() {

    }

    @Override
    public void startRecordCallback(boolean isSucc) {

    }

    @Override
    public void stopRecordCallback(boolean isSucc, List<String> files) {

    }

    @Override
    public void hostLeave(String id, String name) {

    }

    @Override
    public void hostBack(String id, String name) {

    }

    @Override
    public void refreshMember(ArrayList<MemberID> memlist) {

    }

    @Override
    public void showMessage(LiveCommentBean liveCommentBean) {
        if (context == null) {
            return;
        }
        if (((LiveActivity) context).getPushMessageController() != null) {
            ((LiveActivity) context).getPushMessageController().addMessage(liveCommentBean);
        }
    }


    /**
     * TXCloudVideoView的回调
     **/
    @Override
    public void onPlayEvent(int event, Bundle param) {
        //开始播放
        if (TXLiveConstants.PLAY_EVT_PLAY_BEGIN == event) {
            if (videoPlayListener != null) {
                videoPlayListener.onPlaySuccessListener();
            }
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    public void clearContext() {
        context = null;
        videoPlayListener = null;
    }

    public void liveStateChange(boolean pushStreamStatus) {

        liveStateChangeController.liveStateChange(pushStreamStatus);
    }
}
