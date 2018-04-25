package com.android.banana.groupchat.ilistener;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.groupchat.bean.ChatConfigBean;
import com.android.banana.groupchat.bean.GroupChatTopic1;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/7.
 */

public interface IMGroupChatCallback {

    /**
     * 进入群聊 获取配置信息
     *
     * @param groupChatConfigBean
     * @param errorBean
     * @param error
     */
    void onEnterChatRoomOperateResult(ChatConfigBean groupChatConfigBean, ErrorBean errorBean, JSONObject error);

    /**
     * 主题查询
     *
     * @param chatTopic
     * @param jsonObject
     * @throws JSONException 请求出现错误时的返回结果
     */
    void onChatTopicQueryResult(GroupChatTopic1 chatTopic, JSONObject jsonObject) throws JSONException;

    /**
     * 查询群聊房间是否进入成功
     *
     * @param chatConfigBean
     */
    void onUserAuthorityLoginQueryResult(ChatConfigBean chatConfigBean);

    /**
     * 发消息 发红包之前的权限检测
     *
     * @param request
     * @param newConfig
     * @param error     接口请求错误状态下的返回结果
     * @throws JSONException
     */
    void onUserAuthorityQueryResult(RequestContainer request, ChatConfigBean newConfig, JSONObject error) throws JSONException;


    /**
     * 群聊消息发送 的回调
     *
     * @param newMsgId        如果发送成功 那么它就是该条消息的id，  如果发送失败返回-1
     * @param messageSequence
     * @param adapterPos      这条消息对应的适配器中的位置
     * @param errorJson         消息发送失败的错误
     */
    void onMessageSendResult(String newMsgId, long messageSequence, int adapterPos, JSONObject errorJson) throws JSONException;

    /**
     * 所有接口的请求错误都会回调到这里
     *
     * @param request
     * @param error
     */
    void onFailed(RequestContainer request, JSONObject error) throws JSONException;

}
