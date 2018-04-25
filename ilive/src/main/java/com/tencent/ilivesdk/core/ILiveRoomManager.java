package com.tencent.ilivesdk.core;

import com.tencent.TIMMessage;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.impl.ILVBRoom;
import com.tencent.ilivesdk.data.ILivePushRes;
import com.tencent.ilivesdk.tools.quality.ILiveQualityData;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.ILiveRootView;

import java.util.List;

/**
 * 房间抽象类（单实例）
 */
public abstract class ILiveRoomManager {
    private static ILiveRoomManager instance;
    protected ILiveRoomConfig mConfig = null;
    protected ILiveRoomOption mOption;

    /**
     * 获取房间实例
     * @return
     */
    public static ILiveRoomManager getInstance() {
        if(instance == null) {
            synchronized (ILiveRoomManager.class) {
                if (instance == null) {
                    instance = new ILVBRoom();
                }
            }
        }

        return instance;
    }

    /**
     * 获取加入房间参数
     * @return
     */
    public ILiveRoomOption getOption(){
        return mOption;
    }

    /**
     * 初始化函数
     * @param config 初始化配置
     * @see ILiveRoomConfig
     * @return
     */
    public abstract int init(ILiveRoomConfig config);


    public abstract void shutdown();


    /**
     * 创建房间
     * @param roomId 房间id
     * @param option 加入房间选项
     * @param callBack
     * @see ILiveRoomOption
     * @see ILiveCallBack
     * @return
     */
    public abstract int createRoom(int roomId, ILiveRoomOption option, ILiveCallBack callBack);

    /**
     * 加入房间
     * @param roomId 房间id
     * @param option 加入房间选项
     * @param callBack
     * @see ILiveRoomOption
     * @see ILiveCallBack
     * @return
     */
    public abstract int joinRoom(int roomId, ILiveRoomOption option, ILiveCallBack callBack);




    /**
     * 切换房间
     * @param roomId 房间id
     * @param option 加入房间选项
     * @param callBack
     * @see ILiveRoomOption
     * @see ILiveCallBack
     * @return
     */
    public abstract int switchRoom(int roomId, ILiveRoomOption option, ILiveCallBack callBack);

    /**
     * 退出房间
     *
     * @param callBack
     * @see ILiveCallBack
     * @return
     */
    public abstract int quitRoom(ILiveCallBack callBack);

    /**
     * 跨房连麦
     * @param roomId    目标房间id
     * @param accountId 目标用户id
     * @param sign      签名
     * @param callBack  回调
     * @return
     */
    public abstract int linkRoom(int roomId, String accountId, String sign, ILiveCallBack callBack);

    /**
     * 取消(所有)跨房连麦
     * @param callBack  回调
     * @return
     */
    public abstract int unlinkRoom(ILiveCallBack callBack);

    /**
     * 判断当前是否在房间中
     * @return
     */
    public abstract boolean isEnterRoom();

    /**
     * 初始化AVRootView[统一渲染]
     * @param view
     * @see AVRootView
     */
    public abstract int initAvRootView(AVRootView view);
    public abstract AVRootView getRoomView();

    /**
     * 初始化ILiveRootView[分开渲染]
     * @param views 渲染控件数组
     * @return
     */
    public abstract int initRootViewArr(ILiveRootView views[]);

    /**
     * IM消息相关
     */
    public abstract int bindIMGroupId(String groupId);  // 绑定IM群组id
    public abstract int unBindIMGroupId();  // 解绑定IM群组id
    public abstract int sendC2CMessage(String dstUser, TIMMessage message, ILiveCallBack<TIMMessage> callBack);
    public abstract int sendGroupMessage(TIMMessage message, ILiveCallBack<TIMMessage> callBack);
    public abstract int sendC2COnlineMessage(String dstUser, TIMMessage message, ILiveCallBack<TIMMessage> callBack);
    public abstract int sendGroupOnlineMessage(TIMMessage message, ILiveCallBack<TIMMessage> callBack);

    /**
     * 控制接口
     */
    public abstract int enableMic(boolean bEnable);
    /** 开启或关闭扬声器/耳机 */
    public abstract int enableSpeaker(boolean bEnable);
    public abstract int enableCamera(int cameraId, boolean bEnable);
    public abstract int getCurCameraId();
    public abstract int switchCamera(int cameraId);
    /** 获取当前开启的摄像头id */
    public abstract int getActiveCameraId();

    /**
     * 美颜接口
     */
    public abstract int enableBeauty(float value);  // 美颜
    public abstract int enableWhite(float value);   // 美白

    /**
     * 资源相关方法(在Activity对应事件中调用)
     */
    public abstract void onPause();
    public abstract void onResume();
    public abstract void onDestory();

    /**
     * 修改房间权限及角色
     * @param authBits  权限字段
     * @param authBuf 加密串
     * @param role 角色(请确保在腾讯云SPEAR上已配置该角色)
     * @param callBack 回调
     * @return
     */
    @Deprecated
    public abstract int changeAuthAndRole(long authBits, byte[] authBuf, String role, ILiveCallBack callBack);


    /**
     * 修改房间角色
     * @param role      角色(请确保在腾讯云SPEAR上已配置该角色)
     * @param calllback  回调
     */
    public abstract void changeRole(String role , ILiveCallBack calllback);

    /**
     * 推流接口
     */
    public abstract int startPushStream(ILivePushOption option, ILiveCallBack<ILivePushRes> callBack);
    public abstract int stopPushStream(long channelId, ILiveCallBack callBack);
    public abstract int stopPushStreams(List<Long> ids, ILiveCallBack callBack);

    /**
     * 录制接口
     */
    public abstract int startRecordVideo(ILiveRecordOption option, ILiveCallBack callBack);
    public abstract int stopRecordVideo(ILiveCallBack<List<String>> callBack);

    /**
     * 获取AVRoom
     */
    public abstract AVRoomMulti getAvRoom();

    /**
     * 获取房间id
     * @return
     */
    public abstract int getRoomId();

    /**
     * 获取IM聊天室id
     * @return
     */
    public abstract String getIMGroupId();


    /**
     * 获取房间创建者id
     * @return
     */
    public abstract String getHostId();

    /**
     * 获取质量数据，仅限在主线程使用
     * @return
     */
    public abstract ILiveQualityData getQualityData();


}
