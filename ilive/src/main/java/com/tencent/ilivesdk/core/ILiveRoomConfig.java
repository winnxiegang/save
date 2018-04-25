package com.tencent.ilivesdk.core;

import com.tencent.TIMMessageListener;

/**
 * 房间配置参数(初始化时设置)
 */
public class ILiveRoomConfig<Self extends ILiveRoomConfig<Self>> {
    public interface GenerateFunc {
        String generateImGroupId(int roomid);
    }

    private TIMMessageListener mMessageListener = null;                 // 用户IM消息回调

    private GenerateFunc mGenFunc = new GenerateFunc() {
        @Override
        public String generateImGroupId(int roomid) {
            return "" + roomid;
        }
    };              // 默认生成方式(通过av roomid生成im roomid)

    /**
     * 设置IM群组id生成方法
     */
    public Self generateFunc(GenerateFunc func) {
        this.mGenFunc = func;
        return (Self)this;
    }

    /**
     * 设置IM消息回调
     *
     * @param listener 消息回调
     * @return
     */
    public Self messageListener(TIMMessageListener listener) {
        this.mMessageListener = listener;
        return (Self)this;
    }

    // 获取函数
    public GenerateFunc getGenFunc() {
        return mGenFunc;
    }

    public TIMMessageListener getRoomMessageListener() {
        return mMessageListener;
    }
}
