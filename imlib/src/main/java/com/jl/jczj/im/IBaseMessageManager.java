package com.jl.jczj.im;

import com.android.banana.commlib.bean.ErrorBean;
import com.jl.jczj.im.callback.IMCallback;
import com.jl.jczj.im.callback.IMComCallback;
import com.jl.jczj.im.callback.IMTimCallback;

/**
 * Created by zhouyi on 2017/5/27.
 */

public interface IBaseMessageManager {

    void receiveMessage(String message, ErrorBean errorBean);

    void sendMessage(String mode, String message, int position);

    void joinGroup();

    void leaveGroup(String groupId);

    void getMessageList(boolean loadMore, long messageSequence);

    void exitGroup(IMComCallback callback);

    void removeImCallback(IMCallback imCallback);

    void removeTimCallback(IMTimCallback timCallBack);

    void addImCallback(IMCallback imCallback);

    void addTimCallback(IMTimCallback imCallback);
}
