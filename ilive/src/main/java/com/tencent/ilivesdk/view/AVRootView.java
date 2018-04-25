package com.tencent.ilivesdk.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.OrientationEventListener;
import android.view.View;

import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.opengl.utils.Utils;
import com.tencent.av.sdk.AVView;
import com.tencent.av.utils.PhoneStatusTools;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.tools.quality.ILiveQualityData;


/**
 * 视频显示根控件[统一显示]
 */
public class AVRootView extends GLRootView {
    private static String TAG = "AVRootView";

    /**
     * 左侧对齐
     */
    public final static int LAYOUT_GRAVITY_LEFT = 0;
    /**
     * 上侧对齐
     */
    public final static int LAYOUT_GRAVITY_TOP = 1;
    /**
     * 右侧对齐
     */
    public final static int LAYOUT_GRAVITY_RIGHT = 2;
    /**
     * 下侧对齐
     */
    public final static int LAYOUT_GRAVITY_BOTTOM = 3;

    private class BindInfo {
        String id = null;                               // 绑定的id
        int type = AVView.VIDEO_SRC_TYPE_CAMERA;        // 视频类型
        boolean bWeak = true;                           // 是否强绑定(用户绑定为强绑定，自动分配为弱绑定)
    }

    private GraphicRendererMgr mGraphicRenderMgr = null;
    private AVVideoView[] mVideoArr = new AVVideoView[ILiveConstants.MAX_AV_VIDEO_NUM];
    //private String[] mIdMap = new String[ILiveConstants.MAX_AV_VIDEO_NUM];
    private BindInfo[] mBindMap = new BindInfo[ILiveConstants.MAX_AV_VIDEO_NUM];
    private AVVideoGroup mVideoGroup;
    private boolean mAutoOrientation = true;       // 自动旋转功能
    private VideoOrientationEventListener mOrientationEventListener;
    private boolean mInitLandScape = false; // 初始化是否为横屏

    // 小屏布局
    private int mGravity = LAYOUT_GRAVITY_LEFT;
    private int mMarginX = 50;
    private int mMarginY = 50;
    private int mPadding = 30;
    private int mWidth = 0;         // 小屏宽度(0为屏幕的1/4)
    private int mHeight = 0;        // 小屏高度(0为屏幕的1/4)

    public interface onSurfaceCreatedListener {
        void onSurfaceCreated();
    }

    /**
     * 小屏初始化回调
     */
    public interface onSubViewCreatedListener {
        void onSubViewCreated();
    }

    private onSurfaceCreatedListener mSCUserListner = null;     // SurfaceView创建回调
    private onSubViewCreatedListener mSubCreateListner;

    class VideoOrientationEventListener extends OrientationEventListener {
        boolean mIsTablet = false;
        int mRotationAngle = 0;
        int mLastOrientation = -25;

        public VideoOrientationEventListener(Context context, int rate) {
            super(context, rate);
            mIsTablet = PhoneStatusTools.isTablet(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                mLastOrientation = orientation;
                return;
            }

            if (mLastOrientation < 0) {
                mLastOrientation = 0;
            }

            if (((orientation - mLastOrientation) < 20)
                    && ((orientation - mLastOrientation) > -20)) {
                return;
            }

            if (mIsTablet) {
                orientation -= 90;
                if (orientation < 0) {
                    orientation += 360;
                }
            }
            mLastOrientation = orientation;

            if (orientation > 314 || orientation < 45) {
                mRotationAngle = 0;
            } else if (orientation > 44 && orientation < 135) {
                mRotationAngle = 90;
            } else if (orientation > 134 && orientation < 225) {
                mRotationAngle = 180;
            } else {
                mRotationAngle = 270;
            }

            // 角度纠正
            int fixAngle = 0;
            if (mInitLandScape) {
                fixAngle = 270;
                if (ILiveConstants.BACK_CAMERA == ILiveRoomManager.getInstance().getCurCameraId())
                    fixAngle -= 180;
                mRotationAngle = (mRotationAngle + fixAngle) % 360;
                ILiveLog.i(TAG, "set angle:" + mRotationAngle + "/" + fixAngle + "|" + mInitLandScape + "," + ILiveRoomManager.getInstance().getCurCameraId());
            }

            if (null != ILiveSDK.getInstance().getAvVideoCtrl()) {
                ILiveSDK.getInstance().getAvVideoCtrl().setRotation(mRotationAngle);
            }
            for (AVVideoView aMVideoArr : mVideoArr) {
                if (null != aMVideoArr) {
                    aMVideoArr.setRotation(mRotationAngle);
                }
            }
        }
    }

    private void init() {
        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            mBindMap[i] = new BindInfo();
        }
    }

    public AVRootView(Context context) {
        super(context);
        init();
    }

    public AVRootView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    /**
     * 设置SurfaceView创建成功回调(用户在该回调触发后手动打开摄像头)
     *
     * @param listener
     */
    public void setSurfaceCreateListener(onSurfaceCreatedListener listener) {
        mSCUserListner = listener;
    }

    public onSurfaceCreatedListener getmSCUserListner() {
        return mSCUserListner;
    }

    /**
     * 查找用户对应的view的索引
     *
     * @param id   用户id
     * @param type 视频类型
     * @return
     * @see AVView#VIDEO_SRC_TYPE_CAMERA
     * @see AVView#VIDEO_SRC_TYPE_SCREEN
     */
    public int findUserViewIndex(String id, int type) {
        if (null == id) {
            return ILiveConstants.INVALID_INTETER_VALUE;
        }

        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null != mBindMap[i]
                    && null != mBindMap[i].id
                    && mBindMap[i].type == type
                    && mBindMap[i].id.equals(id)) {
                return i;
            }
        }

        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null != mVideoArr[i]
                    && mVideoArr[i].getVideoSrcType() == type
                    && id.equals(mVideoArr[i].getIdentifier())) {
                return i;
            }
        }

        return ILiveConstants.INVALID_INTETER_VALUE;
    }

    /**
     * 查找空闲索引
     *
     * @return
     */
    public int findValidViewIndex() {
        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null == mBindMap[i].id) {
                return i;
            }
        }
        return ILiveConstants.INVALID_INTETER_VALUE;
    }


    // 获取AVVideoGroup
    public AVVideoGroup getVideoGroup() {
        if (null == mVideoGroup) {
            mVideoGroup = new AVVideoGroup();
        }
        return mVideoGroup;
    }

    // 判断是否横屏
    private boolean isLandScape() {
        return ILiveSDK.getInstance().getAppContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    // 初始化View
    public void initView() {
        mGraphicRenderMgr = GraphicRendererMgr.getInstance();

        mOrientationEventListener = new VideoOrientationEventListener(getContext().getApplicationContext(), SensorManager.SENSOR_DELAY_UI);
        if (mAutoOrientation) {
            mInitLandScape = isLandScape();
            mOrientationEventListener.enable();
        }
    }

    // 通知小屏初始化完毕
    public void notifySubViewCreated() {
        if (null != mSubCreateListner) {
            mSubCreateListner.onSubViewCreated();
        }
    }

    public void layoutVideo(boolean virtical) {//
        int width = getWidth();
        int height = getHeight();

        ILiveLog.v(TAG, "layoutVideo->width:" + width + ", height:" + height);

        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null != mVideoArr[i]) {
                mVideoArr[i].autoLayout();
                mVideoArr[i].setBackgroundColor(Color.BLACK);
            }
        }

        ILiveSDK.getInstance().runOnMainThread(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        }, 0);
    }

    // 初始化用户视频
    public AVVideoView[] initVideoGroup() {
        int topPos = 0, topLeft = 0;

        ILiveLog.d(TAG, "ILVB-AVRootView|initVideoGroup->enter");

        if (0 == mWidth) {   // 小屏默认宽度为主屏的1/4
            mWidth = getWidth() / 4;
        }
        if (0 == mHeight) {  // 小屏默认高度为主屏的1/4
            mHeight = getHeight() / 4;
        }

        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            mVideoArr[i] = new AVVideoView(getContext(), mGraphicRenderMgr);

            if (0 == i) {    // 主屏占满控件
                mVideoArr[i].setPosLeft(0);
                mVideoArr[i].setPosTop(0);
                mVideoArr[i].setPosWidth(getWidth());
                mVideoArr[i].setPosHeight(getHeight());
            } else {
                switch (mGravity) {
                    case LAYOUT_GRAVITY_TOP:    // 小屏在上侧
                        mVideoArr[i].setPosLeft(mMarginX + topLeft);
                        mVideoArr[i].setPosTop(mMarginY);
                        topLeft += (mWidth + mPadding);
                        break;
                    case LAYOUT_GRAVITY_RIGHT:  // 小屏在右侧
                        mVideoArr[i].setPosLeft(getWidth() - mWidth - mMarginX);
                        mVideoArr[i].setPosTop(mMarginY + topPos);
                        topPos += (mHeight + mPadding);
                        break;
                    case LAYOUT_GRAVITY_BOTTOM: // 小屏在底侧
                        mVideoArr[i].setPosLeft(mMarginX + topLeft);
                        mVideoArr[i].setPosTop(getHeight() - mHeight - mMarginY);
                        topLeft += (mWidth + mPadding);
                        break;
                    case LAYOUT_GRAVITY_LEFT:   // 小屏在左侧
                    default:
                        mVideoArr[i].setPosLeft(mMarginX);
                        mVideoArr[i].setPosTop(mMarginY + topPos);
                        topPos += (mHeight + mPadding);
                        break;
                }
                mVideoArr[i].setPosWidth(mWidth);
                mVideoArr[i].setPosHeight(mHeight);
            }
            mVideoArr[i].setVisibility(GLView.INVISIBLE);
        }

        return mVideoArr;
    }

    /**
     * 渲染视频
     *
     * @param bHasVideo    是否已有视频
     * @param id           要渲染的用户id
     * @param videoSrcType 渲染的视频类型
     * @param bAutoRender  是否自动渲染(自动查找空闲view)
     * @return
     * @see AVView#VIDEO_SRC_TYPE_CAMERA
     */
    public boolean renderVideoView(boolean bHasVideo, String id, int videoSrcType, boolean bAutoRender) {
        if (Utils.getGLVersion(getContext()) == 1) {
            return false;
        }
        if (id.equals(ILiveLoginManager.getInstance().getMyUserId())) { //直播设置ID
            mGraphicRenderMgr.setSelfId(ILiveLoginManager.getInstance().getMyUserId() + "_" + AVView.VIDEO_SRC_TYPE_CAMERA);
        }

        int index = findUserViewIndex(id, videoSrcType);
//        ILiveLog.v(TAG, ILiveConstants.LOG_KEY_PR+"|ILVB-AVRootView|renderVideoView->enter id:" + id + "/" + ILiveLoginManager.getInstance().getMyUserId() + "|index:" + index);
        if (ILiveConstants.INVALID_INTETER_VALUE == index) {
            if (bAutoRender) {
                index = findValidViewIndex();   // 获取空闲view
            }
            if (ILiveConstants.INVALID_INTETER_VALUE == index) {
                ILiveLog.w(TAG, "ILVB-AVRootView|renderVideoView->video view not found: " + id);
                return false;
            }
        }
        AVVideoView videoView = mVideoArr[index];
        if (null == videoView) {
            ILiveLog.w(TAG, "ILVB-AVRootView|renderVideoView->no sub view found:" + index);
            return false;
        }
        videoView.resetCache();
        ILiveLog.i(TAG, ILiveConstants.LOG_KEY_PR + "|ILVB-AVRootView|renderVideoView->enter index:" + index + ":" + id + "|" + videoView.getPosLeft() + "," + videoView.getPosTop() + "," + videoView.getPosWidth() + "," + videoView.getPosHeight());
        mBindMap[index].id = id; // 绑定用户
        mBindMap[index].type = videoSrcType;    // 配置视频类型
        if (bHasVideo) {// 打开摄像头
            videoView.setRender(id, videoSrcType);
            videoView.setIsPC(false);
            videoView.enableLoading(false);
            videoView.setVisibility(GLView.VISIBLE);
            videoView.setRendering(true);
        } else {// 关闭摄像头
            videoView.setVisibility(GLView.INVISIBLE);
            videoView.setNeedRenderVideo(true);
            videoView.enableLoading(false);
            videoView.setIsPC(false);
            videoView.clearRender();

            layoutVideo(false);
        }

        return true;
    }

    /**
     * 交换两路视频
     *
     * @param src 源view索引
     * @param dst 目标view索引
     * @return
     */
    public int swapVideoView(int src, int dst) {
        if (src >= ILiveConstants.MAX_AV_VIDEO_NUM || dst >= ILiveConstants.MAX_AV_VIDEO_NUM) {
            return ILiveConstants.ERR_NOT_FOUND;
        }
        AVVideoView srcView = mVideoArr[src];
        AVVideoView dstView = mVideoArr[dst];

        if (null == srcView || null == dstView) {
            ILiveLog.w(TAG, "swapVideoView->find view failed:" + src + ", " + dst);
            return ILiveConstants.ERR_NOT_FOUND;
        }

        String tmpId = srcView.getIdentifier();
        int tmpSrcType = srcView.getVideoSrcType();
        boolean tmpIsPC = srcView.isPC();
        boolean tmpIsMirror = srcView.isMirror();
        boolean tmpIsLoading = srcView.isLoading();
        boolean tmpIsRendering = srcView.isRendering();
        int tmpVisible = srcView.getVisibility();

        srcView.setIsPC(dstView.isPC());
        srcView.setMirror(dstView.isMirror());
        srcView.enableLoading(dstView.isLoading());
        srcView.setRendering(dstView.isRendering());
        srcView.setRender(dstView.getIdentifier(), dstView.getVideoSrcType());
        srcView.setVisibility(dstView.getVisibility());

        dstView.setIsPC(tmpIsPC);
        dstView.setMirror(tmpIsMirror);
        dstView.enableLoading(tmpIsLoading);
        dstView.setRendering(tmpIsRendering);
        dstView.setRender(tmpId, tmpSrcType);
        dstView.setVisibility(tmpVisible);

        // 交换绑定关系
        BindInfo tmpBindInfo = mBindMap[src];
        mBindMap[src] = mBindMap[dst];
        mBindMap[dst] = tmpBindInfo;
        ILiveLog.d(TAG, "swapVideoView->swap view:" + srcView.getIdentifier() + "," + dstView.getIdentifier());

        return ILiveConstants.NO_ERR;
    }

    /**
     * 通过索引获取AVVideoView
     *
     * @param index 索引
     * @return view
     */
    public AVVideoView getViewByIndex(int index) {
        if (index >= ILiveConstants.MAX_AV_VIDEO_NUM) {
            return null;
        }

        return mVideoArr[index];
    }

    /**
     * 关闭用户视频
     *
     * @param id      用户id
     * @param type    视频类型
     * @param bRemove 是否移除该路视频(索引在后面的视频会向前顺移)
     */
    public void closeUserView(String id, int type, boolean bRemove) {
        int index = findUserViewIndex(id, type);
        if (ILiveConstants.INVALID_INTETER_VALUE == index) {
            ILiveLog.w(TAG, "AVRootView|closeUserView not found:" + id);
            return;
        }


        AVVideoView avVideoView = mVideoArr[index];
        avVideoView.flush();
        avVideoView.clearRender();
        avVideoView.setRendering(false);
        avVideoView.setVisibility(GLView.INVISIBLE);

        ILiveLog.d(TAG, "ILVB-AVRootView|closeUserView: " + id + "-" + type + "/" + index + "|remove:" + bRemove);
        if (bRemove && mBindMap[index].bWeak) {
            int curIdx = index;
            mBindMap[index].id = null;  //解除当前绑定
            mBindMap[index].type = AVView.VIDEO_SRC_TYPE_CAMERA;
            // 前移弱绑定的view
            for (int i = index + 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                if (!mVideoArr[i].isRendering() || !mBindMap[i].bWeak) {    // 跳过正在渲染的及强绑定的
                    continue;
                }
                swapVideoView(i, curIdx);
                curIdx = i;
            }
        }
        ILiveQualityData.removeLive(id);
    }

    /**
     * 清空用户View并重置绑定关系
     */
    public void clearUserView() {
        ILiveLog.v(TAG, "ILVB-View|clearUserView");
        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null == mVideoArr[i])
                continue;
            mVideoArr[i].flush();
            mVideoArr[i].clearRender();
            mVideoArr[i].setRendering(false);
            mVideoArr[i].setVisibility(View.INVISIBLE);
            mBindMap[i].id = null;   // 重置绑定关系
            mBindMap[i].type = AVView.VIDEO_SRC_TYPE_CAMERA;
            mBindMap[i].bWeak = true;
        }
    }


    public void onDestory() {
        if (null != mOrientationEventListener && mAutoOrientation) {
            mOrientationEventListener.disable();
        }
        if (null != mVideoGroup) {
            mVideoGroup.onDestroy();
        }
        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (null != mVideoArr[i]) {
                mVideoArr[i].flush();
                mVideoArr[i].clearRender();
            }
        }
    }

    /**
     * 绑定view索引与用户id(id为null时解绑)
     *
     * @param index view索引
     * @param type  视频类型
     * @param id    用户id
     * @return 成功返回
     * @see AVView#VIDEO_SRC_TYPE_CAMERA
     * @see AVView#VIDEO_SRC_TYPE_SCREEN
     */
    public int bindIdAndView(int index, int type, String id) {
        ILiveLog.d(TAG, "ILVB-AVRootView|bind id to view:" + id + "-" + type + "/" + index);
        if (index >= ILiveConstants.MAX_AV_VIDEO_NUM) {
            return ILiveConstants.ERR_NOT_FOUND;
        }

        mBindMap[index].id = id;
        mBindMap[index].type = type;
        mBindMap[index].bWeak = (null == id);
        return ILiveConstants.NO_ERR;
    }

    /**
     * 通过用户id查找view
     *
     * @param id   用户id
     * @param type 视频类型
     * @return
     * @see AVView#VIDEO_SRC_TYPE_CAMERA
     * @see AVView#VIDEO_SRC_TYPE_SCREEN
     */
    public AVVideoView getUserAvVideoView(String id, int type) {
        int index = findUserViewIndex(id, type);
        if (ILiveConstants.INVALID_INTETER_VALUE == index) {
            return mVideoArr[0];
            //return null;此处已做修改
        }
        return mVideoArr[index];
    }

    /**
     * 设置小屏初始位置
     *
     * @param gravity 小屏位置
     * @see #LAYOUT_GRAVITY_LEFT
     * @see #LAYOUT_GRAVITY_RIGHT
     * @see #LAYOUT_GRAVITY_TOP
     * @see #LAYOUT_GRAVITY_BOTTOM
     */
    public void setGravity(int gravity) {
        this.mGravity = gravity;
    }

    /**
     * 设置小屏初始x轴边距
     *
     * @param margin 边距
     */
    public void setSubMarginX(int margin) {
        this.mMarginX = margin;
    }

    /**
     * 设置小屏初始y轴边距
     *
     * @param margin 边距
     */
    public void setSubMarginY(int margin) {
        this.mMarginY = margin;
    }

    /**
     * 设置小屏初始间距
     *
     * @param padding 间距
     */
    public void setSubPadding(int padding) {
        this.mPadding = padding;
    }

    /**
     * 设置小屏初始宽度(默认为0表示为主屏的1/4)
     *
     * @param width 宽度
     */
    public void setSubWidth(int width) {
        this.mWidth = width;
    }

    /**
     * 设置小屏初始高度(默认为0表示为主屏的1/4)
     *
     * @param height 高度
     */
    public void setSubHeight(int height) {
        this.mHeight = height;
    }

    /**
     * 设置小屏初始化回调(可用于设置小屏初始位置等)
     *
     * @param listner 回调
     */
    public void setSubCreatedListener(onSubViewCreatedListener listner) {
        this.mSubCreateListner = listner;
    }

    /**
     * 设置是否开启自动旋转
     *
     * @param bEnable
     */
    public void setAutoOrientation(boolean bEnable) {
        // 判断是否需要更新
        if (null != mOrientationEventListener && mAutoOrientation != bEnable) {
            if (bEnable)
                mOrientationEventListener.enable();
            else
                mOrientationEventListener.disable();
        }
        mAutoOrientation = bEnable;
    }

    public int getRenderFuncPtr() {
        return mGraphicRenderMgr.getRecvDecoderFrameFunctionptr();
    }

    public void debugViews() {
        for (int i = 0; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
            if (mVideoArr[i].isRendering()) {
                ILiveLog.d(TAG, "ILVB-DBG|" + i + ":" + mVideoArr[i].getIdentifier() + ", " + mBindMap[i].bWeak);
            }
        }
    }

}
