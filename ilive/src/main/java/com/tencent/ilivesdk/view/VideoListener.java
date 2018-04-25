package com.tencent.ilivesdk.view;

/**
 * 视频控件事件回调
 */
public interface VideoListener
{
    /**
     * 首帧到达
     * @param width  宽度
     * @param height 高度
     * @param angle 角度
     * @param identifier 用户id
     */
    void onFirstFrameRecved(int width, int height, int angle, String identifier);

    /**
     * 有视频数据
     * @param srcType 视频数据类型
     */
    void onHasVideo(int srcType);

    /**
     * 无视频数据
     * @param srcType 视频数据类型
     */
    void onNoVideo(int srcType);
}
