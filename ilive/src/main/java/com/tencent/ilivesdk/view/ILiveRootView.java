package com.tencent.ilivesdk.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.opengl.ui.GLRootView;
import com.tencent.av.opengl.ui.GLView;
import com.tencent.av.sdk.AVView;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;

/**
 * 视频显示控件[分开显示]
 */
public class ILiveRootView extends GLRootView {
    private final static String TAG = "ILVB-ILiveRootView";

    private INGroupView groupView;
    private INVideoView videoView;
    private boolean isRendering = false;
    private boolean bInited = false;

    public ILiveRootView(Context context) {
        super(context);
        groupView = new INGroupView();
        videoView = new INVideoView(getContext(), GraphicRendererMgr.getInstance());
    }

    public ILiveRootView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        groupView = new INGroupView();
        videoView = new INVideoView(getContext(), GraphicRendererMgr.getInstance());
    }

    private void initSubView(){
        videoView.layout(0, 0, getWidth(), getHeight());
        groupView.addView(videoView);
    }

    /**
     * 初始化View(设置到房间时由房间内部调用)
     */
    public void initViews(){
        ILiveLog.d(TAG, "initViews->entered: "+bInited);
        if (!bInited) {
            if (0 == getWidth()) {
                addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                    @Override
                    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                        initSubView();
                    }
                });
            } else {
                initSubView();
            }

            setContentPane(groupView);
            bInited = true;
        }
    }

    /**
     * 当前是否在渲染
     * @return
     */
    public boolean isRendering() {
        return isRendering;
    }

    public void render(String id, int srcType){
        ILiveLog.v(TAG, "render->id: "+id+", srcType: "+srcType);
        if (id.equals(ILiveLoginManager.getInstance().getMyUserId())) { //直播设置ID
            GraphicRendererMgr.getInstance().setSelfId(ILiveLoginManager.getInstance().getMyUserId() + "_" + AVView.VIDEO_SRC_TYPE_CAMERA);
        }
        isRendering = true;
        videoView.setRender(id, srcType);
        videoView.setIsPC(false);
        videoView.enableLoading(false);
        videoView.setVisibility(GLView.VISIBLE);
    }

    /**
     * 关闭当前渲染
     */
    public void closeVideo(){
        ILiveLog.v(TAG, "closeVideo->id: "+videoView.getIdentifier());
        isRendering = false;

        videoView.setVisibility(GLView.INVISIBLE);
        videoView.setNeedRenderVideo(true);
        videoView.enableLoading(false);
        videoView.setIsPC(false);
        videoView.clearRender();
    }

    /**
     * 设置Gesture事件
     * @param gestureListener
     */
    public void setGestureListener(GestureDetector.SimpleOnGestureListener gestureListener) {
        groupView.setGestureListener(getContext(), gestureListener);
    }

    /**
     * 设置视频回调
     * @param listener
     */
    public void setVideoListener(VideoListener listener){
        videoView.setVideoListener(listener);
    }

    /**
     * 获取当前渲染的用户id
     */
    public String getIdentifier(){
        return videoView.getIdentifier();
    }

    /**
     * 获取渲染数据类型
     */
    public int getVideoSrcType(){
        return videoView.getVideoSrcType();
    }

    /**
     * 获取视频回调
     */
    public VideoListener getVideoListener(){
        return videoView.getVideoListener();
    }

    /**
     * 设置是否旋转画面(与view长宽比一致)
     * @param rotate
     */
    public void setRotate(boolean rotate){
        videoView.setRotate(rotate);
    }

    /**
     * 设置方向一致(画面与view)的渲染模式
     * @param mode 渲染模式
     */
    public void setSameDirectionRenderMode(BaseVideoView.BaseRenderMode mode){
        videoView.setSameDirectionRenderMode(mode);
    }

    /**
     * 设置方向一致(画面与view)的渲染模式
     * @param mode 渲染模式
     */
    public void setDiffDirectionRenderMode(BaseVideoView.BaseRenderMode mode){
        videoView.setDiffDirectionRenderMode(mode);
    }
}
