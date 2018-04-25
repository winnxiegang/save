package com.android.banana.groupchat.message;

import android.os.Bundle;
import android.widget.ListView;

import com.android.banana.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.groupchat.bean.GroupChatNotificationBean;
import com.android.banana.groupchat.groupchat.chat.GroupChatActivity;
import com.android.banana.groupchat.message.adapter.GroupChatNotificationAdapter;
import com.android.banana.http.JczjURLEnum;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zaozao on 2016/5/27.
 * 群聊通知
 */
public class GroupChatNotificationActivity extends BaseActivity implements OnHttpResponseListener {

    private ListView notificationListView;

    private ArrayList<GroupChatNotificationBean.NoticesBean> notificationList = new ArrayList<>();

    private GroupChatNotificationAdapter adapter;

    private HttpRequestHelper requestHelper ;

    private GroupChatNotificationBean.NoticesBean clickBean ;

    private String applyType ="";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_chat_notification);

        setTitleBar(true, "群聊通知",null);

        setRefreshLayout();

        adapter = new GroupChatNotificationAdapter(this, notificationList,noticeClickListener);

        notificationListView.setAdapter(adapter);

        requestHelper = new HttpRequestHelper(this,this);

        getNotifications(true);
    }

    /**
     *
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(this, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mRequestType = REFRESH;

                currentPage = DEFAULT_PAGE;

                getNotifications(false);
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;

                if (currentPage >= maxPages) {
                    ToastUtil.showLong(GroupChatNotificationActivity.this.getApplicationContext(),"已经到最后一页了!!");
                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    getNotifications(false);
                }
            }

            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(R.drawable.icon_no_content_about_chat_system_reply), "暂无通知");

        notificationListView = (ListView) findViewById(R.id.refreshListView);

        notificationListView.setDivider(null);

    }

    private void handleNoticesrequestSuccess( JSONObject jo){
        GroupChatNotificationBean bean = new Gson().fromJson(jo.toString(),GroupChatNotificationBean.class);

        bean.operatorData();

        if(bean.getPaginator()!=null){
            maxPages = bean.getPaginator().getPages();
        }

        if(mRequestType == REFRESH&&notificationList.size()>0){

            notificationList.clear();
        }
        if(bean.getNotices()!=null){
            notificationList.addAll(bean.getNotices());
        }

        mRefreshEmptyViewHelper.closeRefresh();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void executorSuccess(RequestContainer request, JSONObject jo) {
        JczjURLEnum jczjURLEnum = (JczjURLEnum) request.getRequestEnum();
        switch (jczjURLEnum){
            case GROUP_MESSAGE_NOTICE:
                handleNoticesrequestSuccess(jo);
                break;
            case USER_MANAGE_GROUP_JOINED_OPERATION:
                clickBean.setShowButton(false);
                clickBean.setShowResult(true);
                if("AGREEED".equals(applyType)){
                    clickBean.getMessageParamBean().setADUIT_STATUS("AGREEED");
                    clickBean.setApplyResult("已同意");
                }else{
                    clickBean.getMessageParamBean().setADUIT_STATUS("REFUSED");
                    clickBean.setApplyResult("已拒绝");
                }
                adapter.notifyDataSetChanged();
                break;
        }

    }

    @Override
    public void executorFalse(RequestContainer request, JSONObject jo) {
        mRefreshEmptyViewHelper.closeRefresh();
        try {
            operateErrorResponseMessage(jo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer request) {
        mRefreshEmptyViewHelper.closeRefresh();
    }

    @Override
    public void executorFinish() {

    }

    /**
     * 进入页面获取通知列表
     */
    private void getNotifications(boolean showDialog) {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.GROUP_MESSAGE_NOTICE,true);

        map.put("currentPage",currentPage+"");

        requestHelper.startRequest(map,showDialog);
    }

    /**
     * 拒绝，同意请求
     */
    private void agreeRequest() {

        RequestFormBody map = new RequestFormBody(JczjURLEnum.USER_MANAGE_GROUP_JOINED_OPERATION,true);

        map.put("groupId",clickBean.getMessageParamBean().getGROUP_ID());

        map.put("applyId",clickBean.getMessageParamBean().getAPPLY_ID());

        map.put("aduitStatus",applyType);

        requestHelper.startRequest(map,true);
    }

    NoticeClickListener noticeClickListener =new NoticeClickListener() {
        @Override
        public void onclick(int position,boolean toGroupChat, boolean agreeClick) {
            clickBean = notificationList.get(position);
            if(toGroupChat){
                GroupChatActivity.start2GroupChatActivity(GroupChatNotificationActivity.this,clickBean.getMessageParamBean().getGROUP_ID(),
                        clickBean.getMessageParamBean().getGROUP_CHAT_NAME(),clickBean.getMessageParamBean().getGROUP_LOGO_URL());
            }else{
                if(agreeClick){
                    applyType = "AGREEED";
                    //同意请求
                    agreeRequest();
                }else{
                    applyType = "REFUSED";
                    //拒绝请求
                    agreeRequest();
                }
            }
        }
    };

    public interface NoticeClickListener{
        void onclick(int position,boolean toGroupChat,boolean agreeClick);//去群聊页或者点击同意还是拒绝
    }

}
