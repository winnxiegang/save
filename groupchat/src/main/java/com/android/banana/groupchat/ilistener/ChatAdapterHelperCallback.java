package com.android.banana.groupchat.ilistener;

import com.android.banana.commlib.bean.GroupCouponInfoBean;
import com.android.httprequestlib.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/30.
 */

public interface ChatAdapterHelperCallback {

    /**
     * 抢红包 的回调
     *
     * @param adapterPos
     * @param jsonError
     * @throws JSONException 如果不为空 就是抢红包出错的json
     */
    void onQueryAvailableCouponResult(int adapterPos, GroupCouponInfoBean groupCouponInfo, JSONObject jsonError) throws JSONException;

    /**
     * 检查一个人是否被禁言
     *
     * @param request
     * @param forbidden
     * @param jsonError 错误状态下的jsonObject
     * @throws JSONException
     */
    void onCheckForbiddenResult(RequestContainer request, boolean forbidden, JSONObject jsonError) throws JSONException;

    /**
     * 取消一个人的禁言
     *
     * @param success   true 取消禁言成功,否则禁言取消失败
     * @param jsonError 错误状态下的jsonObject
     * @throws JSONException
     */
    void onCancelForbiddenResult(boolean success, JSONObject jsonError) throws JSONException;

    /**
     * 删除一条消息
     *
     * @param position  列表对应的位置
     * @param jsonError 错误状态下的jsonObject
     * @throws JSONException
     */
    void onDeleteMessageResult(int position, JSONObject jsonError) throws JSONException;


    /**
     * 删除群成员
     *
     * @param adapterPos 列表对应的位置
     * @param jsonError  错误状态下的jsonObject
     * @throws JSONException
     */
    void onRemoveGroupChatMemberResult(int adapterPos, JSONObject jsonError) throws JSONException;

}
