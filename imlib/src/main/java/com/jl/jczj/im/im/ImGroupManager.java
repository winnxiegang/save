package com.jl.jczj.im.im;

import com.jl.jczj.im.IBaseMessageManager;
import com.jl.jczj.im.callback.IMComCallback;
import com.jl.jczj.im.callback.IMTimCallback;
import com.jl.jczj.im.service.ImChannelEnum;
import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.loop.LoopMessageManager;
import com.jl.jczj.im.tim.TencentMessageManager;

/**
 * Created by zhouyi on 2017/5/27.
 */

public class ImGroupManager {
    private static ImGroupManager manager;
    private IBaseMessageManager mMessageManager;
    private ImChannelEnum mImChannelEnum;


    public synchronized static ImGroupManager getInstance() {
        if (manager == null) {
            synchronized (ImGroupManager.class) {
                if (manager == null)
                    manager = new ImGroupManager();
            }
        }
        return manager;
    }

    public void init(ImChannelEnum channelEnum, IMCallback imCallback) {
        if (mMessageManager != null && mImChannelEnum != channelEnum) {
            addImCallback(imCallback);
            return;
        }
        switch (channelEnum) {
            case TIM:
                mMessageManager = new TencentMessageManager();
                break;
            case LOOP:
                mMessageManager = new LoopMessageManager();
                break;
        }
        mImChannelEnum = channelEnum;
        addImCallback(imCallback);
    }

    public void addImCallback(IMCallback imCallback) {
        mMessageManager.addImCallback(imCallback);
    }

    public void addTimCallback(IMTimCallback imCallback) {
        if (mMessageManager == null)
            mMessageManager = new TencentMessageManager();
        mMessageManager.addTimCallback(imCallback);
    }


    public void joinGroup() {
        if (mMessageManager != null) mMessageManager.joinGroup();
    }

    /**
     * 暂时离开
     *
     * @param groupId
     */
    public void leaveGroup(String groupId) {
        if (mMessageManager != null) mMessageManager.leaveGroup(groupId);
    }

    /**
     * 发送消息
     *
     * @param message
     * @param position
     */
    public void sendMessage(String mode, String message, int position) {
        if (mMessageManager != null) mMessageManager.sendMessage(mode, message, position);
    }

    /**
     * @param loadMore
     * @param messageSequence
     * @return true 参数初始化成功,否则直接返回false，接着可以处理业务界面
     */
    public boolean getMessageList(boolean loadMore, long messageSequence) {
        if (mMessageManager != null) {
            mMessageManager.getMessageList(loadMore, messageSequence);
            return true;
        }
        return false;
    }

    public void exitGroup(IMComCallback callback) {
        if (mMessageManager != null) mMessageManager.exitGroup(callback);
    }

    public void removeImCallback(IMCallback imCallback) {
        if (mMessageManager != null) mMessageManager.removeImCallback(imCallback);
    }

    public void removeTimCallback(IMTimCallback timCallback) {
        if (mMessageManager != null) mMessageManager.removeTimCallback(timCallback);
    }
}
