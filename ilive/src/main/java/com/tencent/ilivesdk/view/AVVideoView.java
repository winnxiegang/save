package com.tencent.ilivesdk.view;

import android.content.Context;
import android.view.GestureDetector;

import com.tencent.av.opengl.GraphicRendererMgr;

/**
 * 视频显示GL控件[统一显示]
 */
public class AVVideoView extends BaseVideoView {
    private static String TAG = "AVVideoView";

    public enum ILiveRenderMode {
        SCALE_TO_FIT,
        BLACK_TO_FILL,
    }

    private int posLeft;
    private int posTop;
    private int posWidth;
    private int posHeight;

    private boolean bDragable = false;      // 能否拖动
    private GestureDetector.SimpleOnGestureListener gestureListener;

    /**
     * 首帧到达回调
     */
    public interface RecvFirstFrameListener{
        /**
         * 首帧到达
         * @param width  宽度
         * @param height 高度
         * @param angle 角度
         * @param identifier 用户id
         */
        void onFirstFrameRecved(int width, int height, int angle, String identifier);
    }

    public AVVideoView() {
    }

    public AVVideoView(Context context, GraphicRendererMgr graphicRenderMgr) {
        super(context, graphicRenderMgr);
    }

    public boolean isDragable() {
        return bDragable;
    }

    /**
     * 设置可拖动
     * @param enable
     */
    public void setDragable(boolean enable) {
        this.bDragable = enable;
    }

    /**
     * 设置X
     * @param posLeft
     */
    public void setPosLeft(int posLeft) {
        this.posLeft = posLeft;
    }

    /**
     * 设置Y
     * @param posTop
     */
    public void setPosTop(int posTop) {
        this.posTop = posTop;
    }

    /**
     * 设置宽度
     * @param posWidth
     */
    public void setPosWidth(int posWidth) {
        this.posWidth = posWidth;
    }

    /**
     * 设置高度
     * @param posHeight
     */
    public void setPosHeight(int posHeight) {
        this.posHeight = posHeight;
    }

    /**
     * 设置Gesture事件
     * @param gestureListener
     */
    public void setGestureListener(GestureDetector.SimpleOnGestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    /**
     * 设置首帧到达回调
     * @param recvFirstFrameListener
     * @see BaseVideoView#setVideoListener(VideoListener)
     */
    @Deprecated
    public void setRecvFirstFrameListener(final RecvFirstFrameListener recvFirstFrameListener) {
        setVideoListener(new VideoListener() {
            @Override
            public void onFirstFrameRecved(int width, int height, int angle, String identifier) {
                recvFirstFrameListener.onFirstFrameRecved(width, height, angle, identifier);
            }

            @Override
            public void onHasVideo(int srcType) {

            }

            @Override
            public void onNoVideo(int srcType) {

            }
        });
    }

    /**
     * 设置方向一致时的渲染模式
     * @param sameDirectionRenderMode 渲染模式
     * @see BaseVideoView#setSameDirectionRenderMode(BaseRenderMode)
     */
    @Deprecated
    public void setSameDirectionRenderMode(ILiveRenderMode sameDirectionRenderMode) {
        if (ILiveRenderMode.SCALE_TO_FIT == sameDirectionRenderMode){
            setSameDirectionRenderMode(BaseRenderMode.SCALE_TO_FIT);
        }else{
            setSameDirectionRenderMode(BaseRenderMode.BLACK_TO_FILL);
        }
    }

    /**
     * 设置方向一致时的渲染模式
     * @param diffDirectionRenderMode 渲染模式
     * @see BaseVideoView#setDiffDirectionRenderMode(BaseRenderMode)
     */
    @Deprecated
    public void setDiffDirectionRenderMode(ILiveRenderMode diffDirectionRenderMode) {
        if (ILiveRenderMode.SCALE_TO_FIT == diffDirectionRenderMode){
            setDiffDirectionRenderMode(BaseRenderMode.SCALE_TO_FIT);
        }else{
            setDiffDirectionRenderMode(BaseRenderMode.BLACK_TO_FILL);
        }
    }

    /**
     * 刷新显示(修改位置信息后请调用本方法刷新显示)
     */
    public void autoLayout(){
        super.layout(posLeft, posTop, posLeft+posWidth, posTop+posHeight);
    }

    public int getPosLeft() {
        return posLeft;
    }

    public int getPosTop() {
        return posTop;
    }

    public int getPosWidth() {
        return posWidth;
    }

    public int getPosHeight() {
        return posHeight;
    }

    public GestureDetector.SimpleOnGestureListener getGestureListener() {
        return gestureListener;
    }


     //  视频相关数据获取

    /**
     * 获取视频宽度
     * @return
     */
    public int getImageWidth(){
        return mImageWidth;
    }

    /**
     * 获取视频高度
     * @return
     */
    public int getImageHeight(){
        return mImageHeight;
    }

    /**
     * 获取视频旋转角度
     * @return
     */
    public int getImageAngle(){
        return mImageAngle;
    }
}
