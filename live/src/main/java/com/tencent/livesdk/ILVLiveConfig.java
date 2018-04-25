package com.tencent.livesdk;

import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.core.ILiveRoomConfig;


/**
 * Live业务配置
 */
public class ILVLiveConfig extends ILiveRoomConfig<ILVLiveConfig> {
    private boolean isCustomProtocol = false;

    /**
     * 直播类消息事件回调(用户实现)
     */
    public interface ILVLiveMsgListener {

        /**
         * 文本消息
         *
         * @param text        内容
         * @param SenderId    发送者id
         * @param userProfile 托管在IM的用户资料
         */
        void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile);


        /**
         * 自定义消息
         *
         * @param cmd         命令字
         * @param id          发送者id
         * @param userProfile 托管在IM的用户资料
         */
        void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile);


        /**
         * 其他IM消息(不属于上面几类的消息)
         *
         * @param message 消息
         */
        void onNewOtherMsg(TIMMessage message);
    }


    private ILVLiveMsgListener msgLiveListener = null;


    public ILVLiveMsgListener getLiveMsgListener() {
        return msgLiveListener;
    }

    /**
     * 设置消息回调
     */
    public ILVLiveConfig setLiveMsgListener(ILVLiveMsgListener msgListener) {
        this.msgLiveListener = msgListener;
        return this;
    }

    // 禁用部分父类消息

    /**
     * 该接口已废弃，请使用setLiveMsgListener，坚持使用，需要自行处理内部信令
     */
    @Deprecated
    public ILVLiveConfig messageListener(TIMMessageListener listener) {
        return super.messageListener(listener);
    }


    /**
     * 获取用户自定义协议
     * @return true 代表走自己的定义的信令通道
     */
    public boolean isCustomProtocol() {
        return isCustomProtocol;
    }

    /**
     * 配置是否自定义信令 默认为false。如果设置成true 则认为用户有自己的信令，将不再解析LiveSDK自定义信令。
     * @param customProtocol
     */
        public void setCustomProtocol(boolean customProtocol) {
        isCustomProtocol = customProtocol;
    }
}
