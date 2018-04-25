package com.android.xjq.controller;

import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.bean.live.LiveInfoJson;
import com.android.xjq.model.MemberID;
import com.android.xjq.presenters.LiveHelper;
import com.android.xjq.presenters.viewinface.LiveView;
import com.tencent.av.TIMAvManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.ilivesdk.view.VideoListener;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/1/29.
 */

public class AVLiveRoomManager implements ILiveBaseManager, LiveView {

    private LiveHelper mLiveHelper;

    private ViewGroup mRootView;

    private ILiveRoomCallBack iLiveRoomCallBack;

    public AVLiveRoomManager() {
        mLiveHelper = new LiveHelper(this);
    }

    @Override
    public void setView(boolean isFirstEnterRoom, ViewGroup rootView) {
        //设置渲染层
        mRootView = rootView;
        getAvRootView().setAutoOrientation(false);//防止视频播放自动旋转
        ILVLiveManager.getInstance().setAvVideoView(getAvRootView());
    }


    @Override
    public void startPlay(String channelId) {
        if (iLiveRoomCallBack != null) iLiveRoomCallBack.onPreparePlay();
        mLiveHelper.startEnterRoom();
    }

    @Override
    public void switchRoom() {
        mLiveHelper.switchRoom();
    }

    @Override
    public void stopPlay() {
        mLiveHelper.startExitRoom();
    }

    @Override
    public void onResume() {
        ILiveRoomManager.getInstance().onResume();
    }

    //pause状态下设置后台模式(静默模式/支持后台模式等),详情见IliveConstans的定义
    @Override
    public void onPause() {
        ILiveRoomManager.getInstance().onPause();
    }


    @Override
    public void onDestroy() {
        ILiveRoomManager.getInstance().onDestory();
        mLiveHelper.onDestory();
        iLiveRoomCallBack = null;
        mRootView = null;
    }

    @Override
    public void changeViewSize(boolean isLandscape, ViewGroup container, int height, int width) {
        //要改变容器的宽高大小
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) container.getLayoutParams();
        params.width = width;
        params.height = height;
        params.topMargin = isLandscape ? 0 : XjqApplication.getContext().getResources().getDimensionPixelOffset(R.dimen.live_portrait_guest_header_height);
        container.setLayoutParams(params);
        //要改变RootView的宽高大小
        ViewGroup.LayoutParams lps = mRootView.getLayoutParams();
        lps.height = height;
        lps.width = width;
        mRootView.setLayoutParams(lps);
        getAvRootView().getViewByIndex(0).setPosWidth(width);
        getAvRootView().getViewByIndex(0).setPosHeight(height);
        if (isLandscape) {
            getAvRootView().getViewByIndex(0).setRotation(180);
        } else {
            getAvRootView().getViewByIndex(0).setRotation(0);
        }
        getAvRootView().getViewByIndex(0).autoLayout();
    }

    @Override
    public void cancelAllView() {
        /* if (ILiveRoomManager.getInstance().getAvRoom() == null) return;
        if (currentType == VideoPlayerTypeEnum.AVROOTVIEW) {
            if (ILiveRoomManager.getInstance().getAvRoom() == null) return;
            ILiveRoomManager.getInstance().getAvRoom().cancelAllView(new AVCallback() {
                @Override
                public void onComplete(int i, String s) {
                    mRootView.setVisibility(View.GONE);
                    LibAppUtil.showTip(context, "关闭图像成功");
                }
            });
        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {
        }*/
    }

    @Override
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
                    LibAppUtil.showTip(context, s);
                }
            });

        } else if (currentType == VideoPlayerTypeEnum.TXCLOUDVIDEOVIEW) {

        }*/
    }

    @Override
    public void addLiveRoomCallback(ILiveRoomCallBack iLiveRoomCallBack) {
        this.iLiveRoomCallBack = iLiveRoomCallBack;
    }


    private AVRootView getAvRootView() {
        return (AVRootView) mRootView.findViewById(R.id.rootView);
    }

    /*******以下是LiveView的回调*******/
    @Override
    public void enterRoomComplete(int id_status, boolean succ) {

        if (getAvRootView().getViewByIndex(0) == null) return;
        //设置界面自适应屏幕
        getAvRootView().getViewByIndex(0).setSameDirectionRenderMode(AVVideoView.ILiveRenderMode.BLACK_TO_FILL);

        getAvRootView().getViewByIndex(0).setRotate(false);

        getAvRootView().getViewByIndex(0).setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                if (iLiveRoomCallBack != null) iLiveRoomCallBack.receiveFirstFrameListener();
            }

            @Override
            public void onHasVideo(int srcType) {
                if (iLiveRoomCallBack != null) iLiveRoomCallBack.onHasVideo();
            }

            @Override
            public void onNoVideo(int srcType) {
                if (iLiveRoomCallBack != null) iLiveRoomCallBack.onNoVideo();
            }
        });

        getAvRootView().getViewByIndex(0).autoLayout();

       /* getAvRootView().getViewByIndex(0).setRecvFirstFrameListener(new AVVideoView.RecvFirstFrameListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                //liveStateChangeController.receiveFirstFrame();

                if (iLiveRoomCallBack != null) iLiveRoomCallBack.receiveFirstFrameListener();
            }
        });*/

        if (iLiveRoomCallBack != null) iLiveRoomCallBack.onPlaySuccessListener();
    }

    @Override
    public void quiteRoomComplete(int id_status, boolean succ, LiveInfoJson liveinfo) {
        getAvRootView().clearUserView();

        if (iLiveRoomCallBack != null) iLiveRoomCallBack.onExitComplete();
       /* if (mShowFloatingView) {
            onDestroy();
        }*/

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
        iLiveRoomCallBack.receiveMessage(liveCommentBean);
    }


}
