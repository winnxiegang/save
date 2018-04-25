package com.tencent.ilivesdk.core;

/**
 * Created by tencent on 2016/9/21.
 */
public interface ILiveCameraListener {
    /**
     *  摄像头打开事件
     *  @param cameraId 摄像头id
     */
    void onCameraEnable(int cameraId);

    /**
     *  摄像头关闭事件
     *  @param cameraId 摄像头id
     */
    void onCameraDisable(int cameraId);

    /**
     *  摄像头数据切换事件
     *  @param cameraId 摄像头id
     */
    void onCameraPreviewChanged(int cameraId);
}
