package com.android.xjq.activity.message;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.ListActivity;
import com.android.xjq.activity.message.adapter.MessageNotifyAdapter;
import com.android.xjq.bean.message.SystemNotifyBean;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageNotifyActivity extends ListActivity implements IHttpResponseListener {

    private ListView mListView;

    private MessageNotifyAdapter mAdapter;


    private List<SystemNotifyBean.NoticesBean> mList = new ArrayList<>();

    private Pagers mPagers;
    private WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);

    public static void startMessageNotifyActivity(Activity activity) {

        Intent intent = new Intent();

        intent.setClass(activity, MessageNotifyActivity.class);

        activity.startActivity(intent);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message_notify);

        ButterKnife.bind(this);
        // setTitleBar(true, "通知", getResources().getColor(R.color.main_red));
        setTitle("通知");
        initView();

        getData();

    }

    void setTitle(String title) {
        TextView tv = (TextView) findViewById(R.id.titleTv);
        tv.setText(title);
        findViewById(R.id.backIv).setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.backIv)
    void back() {
        finish();
    }

    private void initView() {

        mPagers = new Pagers();

        changePagers(mPagers);

        setRefreshLayout();

        mAdapter = new MessageNotifyAdapter(this, mList);

        mListView.setAdapter(mAdapter);

    }

    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        RefreshEmptyViewHelper.Builder builder = getRefreshHelperBuilder();

        builder.setEmptyDrawable(R.drawable.icon_no_content_about_chat_system_reply);

        builder.setEmptyTip("暂无消息");

        builder.setShowEmptyView(true);

        builder.setInitShowEmptyView(true);

        mRefreshHelper = new RefreshEmptyViewHelper(getWindow().getDecorView(), builder);

        mListView = mRefreshHelper.getListView();

        mListView.setDivider(new ColorDrawable(getResources().getColor(R.color.main_background)));

        mListView.setDividerHeight((int) getResources().getDimension(R.dimen.dp10));

    }

    private void getData() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.USER_MESSAGE_NOTICE, true);
        container.put("currentPage", mPagers.currentPages);
        container.setGenericClaz(SystemNotifyBean.class);
        httpHelper.startRequest(container, false);
    }


    @Override
    protected void loadDataFromNetwork() {
        getData();
    }


    @Override
    public void onSuccess(RequestContainer request, Object result) {
        XjqUrlEnum requestEnum = (XjqUrlEnum) request.getRequestEnum();
        if (requestEnum == XjqUrlEnum.USER_MESSAGE_NOTICE && result != null) {
            SystemNotifyBean bean = (SystemNotifyBean) result;
            mPagers.changeMaxPagers(bean.getPaginator());
            if (bean != null && bean.getNotices() != null) {
                bean.operatorData();
                if (mPagers.currentPages == 1) {
                    mList.clear();
                }
                mList.addAll(bean.getNotices());
            }
            mAdapter.notifyDataSetChanged();
        }
        closeRefresh();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeRefresh();
        operateErrorResponseMessage(jsonObject);
    }




}
