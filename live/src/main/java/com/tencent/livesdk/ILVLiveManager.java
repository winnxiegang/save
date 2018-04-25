package com.tencent.livesdk;

import com.tencent.TIMMessage;
import com.tencent.TIMMessagePriority;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.view.AVRootView;

import java.util.ArrayList;
import java.util.List;

/**
 * 直播接口类
 */
public abstract class ILVLiveManager {
    protected String version = "1.1.1";
    protected List<String> mLinkedUserId = new ArrayList<>();     // 保存当前跨房连麦的用户

    /* 内部私有静态实例，目的是实现延迟加载 */
    private static class ILVBLiveHolder {
        private static ILVLiveManager instance = new LiveMgr();
    }

    /**
     * 获取直播管理类静态实例
     *
     * @return
     */
    public static ILVLiveManager getInstance() {
        return ILVBLiveHolder.instance;
    }

    /**
     * 初始化
     * @param liveConfig
     */
    public abstract int init(ILVLiveConfig liveConfig) ;


    /**
     * 创建房间
     *
     * @param roomId   房间id
     * @param hostOption  房间配置项
     * @param callBack 回调
     * @return 结果，成功返回NO_ERR
     */
    public abstract int createRoom(int roomId,ILVLiveRoomOption hostOption, ILiveCallBack callBack);


    /**
     * 加入房间
     * @param roomId  房间ID
     * @param hostOption  房间配置项
     * @param callback  回调
     * @return 结果，成功返回NO_ERR
     */
    public abstract int joinRoom(int roomId, ILVLiveRoomOption hostOption, ILiveCallBack callback);


    /**
     * 切换房间
     * @param roomId  房间ID
     * @param hostOption 房间配置项
     * @param callback  回调
     * @return  结果，成功返回NO_ERR
     */
    public abstract int switchRoom(int roomId, ILVLiveRoomOption hostOption, ILiveCallBack callback);

    /**
     * 退出房间
     *
     * @return 结果，成功返回NO_ERR
     */
    public abstract int quitRoom(ILiveCallBack callback);

    /**
     * 设置界面渲染
     *
     * @param view
     * @return
     */
    public abstract int setAvVideoView(AVRootView view);


    public abstract void onPause();


    public abstract void onResume();


    public abstract void onDestory();

    /**
     * 发送房间群文本消息
     *
     * @param msg      消息体
     * @param callBack 回调
     * @return 结果，成功返回NO_ERR
     */
    @Deprecated
    public abstract int sendGroupTextMsg(String msg, ILiveCallBack callBack);


    /**
     * 发送群自定义消息
     * @param context
     * @param level
     * @param callback
     */
    @Deprecated
    public abstract void sendGroupCustomMessage(String context, TIMMessagePriority level, ILiveCallBack callback);


    /**
     * 发送C2C自定义消息
     * @param context
     * @param sendId
     * @param level
     * @param callBack
     */
    @Deprecated
    public abstract void sendC2CCustomMessage(String context, String sendId, TIMMessagePriority level, ILiveCallBack<TIMMessage> callBack);


    /**
     * 发送文本消息
     * @param text
     * @param callback
     * @return
     */
    public abstract int sendText(ILVText text, ILiveCallBack callback);

    /**
     * 发送在线文本消息(目标上线才会收到)
     * @param text 文本消息
     * @param callBack 回调
     * @return
     */
    public abstract int sendOnlineText(ILVText text, ILiveCallBack callBack);


    /**
     * 发送自定义信令
     * @param cmd
     * @param callback
     * @return
     */
    public abstract int sendCustomCmd(ILVCustomCmd cmd, ILiveCallBack callback);

    /**
     * 发送在线自定义消息(目标上线才会收到)
     * @param cmd
     * @param callBack
     * @return
     */
    public abstract int sendOnlineCustomCmd(ILVCustomCmd cmd, ILiveCallBack callBack);

    /**
     * 发送其它消息
     * @param destUserId 目标用户(如为空，则发送到当前群组)
     * @param message
     * @param callBack
     * @return
     */
    public abstract int sendOtherMessage(String destUserId, TIMMessage message, ILiveCallBack callBack);

    /**
     * 发送在线其它消息(目标上线才会收到)
     * @param destUserId 目标用户(如为空，则发送到当前群组)
     * @param message
     * @param callBack
     * @return
     */
    public abstract int sendOnlineOtherMessage(String destUserId, TIMMessage message, ILiveCallBack callBack);


    /**
     * 自己上麦
     * @param role  角色(请确保在腾讯云SPEAR上已配置该角色)
     * @param isOpenCam 是否打开摄像头
     * @param  isOpenMic 是否打开Mic
     * @param callBack 回调
     */
    public abstract void upToVideoMember(String role, boolean isOpenCam, boolean isOpenMic, ILiveCallBack<ILVChangeRoleRes> callBack);


    /**
     * 自己下麦
     * @param role  角色(请确保在腾讯云SPEAR上已配置该角色)
     * @param callBack 回调
     */
    public abstract void downToNorMember(String role,ILiveCallBack<ILVChangeRoleRes> callBack);

    /**
     * 邀请跨房连麦
     * @param toId  目标id
     * @param callBack 回调
     */
    public abstract int linkRoomRequest(String toId, ILiveCallBack callBack);

    /**
     * 接受跨房连麦
     * @param toId 邀请者id
     * @param callBack 回调
     */
    public abstract int acceptLinkRoom(String toId, ILiveCallBack callBack);

    /**
     * 拒绝跨房连麦
     * @param toId  邀请者id
     * @param callBack 回调
     */
    public abstract int refuseLinkRoom(String toId, ILiveCallBack callBack);

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
     * 获取所有跨房连麦的用户
     * @return
     */
    public List<String> getCurrentLinkedUserArray(){
        return mLinkedUserId;
    }
	

    /**
     * 资源释放
     */
    public abstract void shutdown();

    /**
     * 获取LiveSDK版本号
     */
    public abstract String getVersion();

}
