package com.android.banana.groupchat.groupchat.forbidden;

import android.text.TextUtils;
import android.view.View;

import com.android.banana.R;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseListActivity;
import com.android.banana.groupchat.bean.ForbiddenList;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.httprequestlib.RequestContainer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by qiaomu on 2017/6/1.
 */

public class ForbiddenListActivity extends BaseListActivity<ForbiddenList.UserBean> implements IHttpResponseListener<ForbiddenList> {
    private WrapperHttpHelper httpHelper;
    private String groupId;
    private String mUserId;

    @Override
    protected void setUpData() {
        setUpToolbar(getString(R.string.gag_manager), -1, MODE_BACK);

        mPullRecycler.addItemDecoration(new DividerItemDecoration(this, R.drawable.base_divider_list));

        httpHelper = new WrapperHttpHelper(this);

        groupId = getIntent().getStringExtra("groupId");

        mUserId = LoginInfoHelper.getInstance().getUserId();

    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.activity_gag_list_item_1;
    }

    @Override
    public void onBindHolder(ViewHolder holder, ForbiddenList.UserBean item, final int position) {
        holder.setText(R.id.userName, item.getLoginName());
        holder.setImageByUrl(this, R.id.avartIv, item.getUserLogoUrl(), R.drawable.user_default_logo);
        holder.setOnClickListener(R.id.cancel_forbidden, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestFormBody container = new RequestFormBody(JczjURLEnum.USER_FORBBIDEN_ACTION_CANCEL, true);
                container.put("id", mDatas.get(position).getId());
                container.put("position", position);
                container.put("groupId", groupId);
                container.put("userId", mDatas.get(position).getUserId());
                showProgressDialog();
                httpHelper.startRequest(container, false);
            }
        });
    }

    @Override
    public void onRefresh(boolean refresh) {
        if (refresh) {
            mCurPage = 1;
            mDatas.clear();
        }
        RequestFormBody container = new RequestFormBody(JczjURLEnum.USER_FORBIDDEN_QUERY, true);
        container.put("currentPage", String.valueOf(mCurPage));
        container.put("groupId", groupId);
        httpHelper.startRequest(container, false);
    }

    @Override
    public void onSuccess(RequestContainer request, ForbiddenList forbiddenList) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_FORBIDDEN_QUERY:
                loadCompleted(forbiddenList.userForbiddenActionList);
                break;
            case USER_FORBBIDEN_ACTION_CANCEL:
                closeProgressDialog();
                int position = request.getInt("position");
                String id = request.getString("userId");
                mDatas.remove(position);
                loadCompleted(null);

                if (TextUtils.equals(id, mUserId))
                    EventBus.getDefault().post(new ForbiddenCancelEvent());
                break;
        }
    }


    @Override
    public void onFailed(RequestContainer request, JSONObject object, boolean netFailed) {
        JczjURLEnum requestEnum = (JczjURLEnum) request.getRequestEnum();
        switch (requestEnum) {
            case USER_FORBIDDEN_QUERY:
                loadCompleted(null);
                break;
            case USER_FORBBIDEN_ACTION_CANCEL:
                closeProgressDialog();
                try {
                    operateErrorResponseMessage(object, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public static class ForbiddenCancelEvent {

    }
}
