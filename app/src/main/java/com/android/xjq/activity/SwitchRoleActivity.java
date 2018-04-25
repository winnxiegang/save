package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.LoginSignTypeEnum;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.adapter.main.SwitchRoleAdapter;
import com.android.xjq.bean.live.main.UserRoleInfo;
import com.android.xjq.bean.live.main.UserRoleInfoBean;
import com.android.xjq.presenters.LoginHelper;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.utils.XjqUrlEnum.SET_DEFAULT_LOGIN_USER;
import static com.android.xjq.utils.XjqUrlEnum.USER_LIST_QUERY;

/**
 * Created by lingjiu on 2017/3/6 11:07.
 */
public class SwitchRoleActivity extends BaseActivity implements OnHttpResponseListener, UserLoginView {

    @BindView(R.id.listView)
    ListView listView;

    private SwitchRoleAdapter switchRoleAdapter;

    private List<UserRoleInfo> list = new ArrayList<>();

    private HttpRequestHelper httpRequestHelper;

    private boolean withoutDefaultRole;

    private LoginHelper loginHelper;

    public static void startSwitchRoleActivity(Activity activity) {

        activity.startActivity(new Intent(activity, SwitchRoleActivity.class));
    }

    public static void startSwitchRoleActivity(Activity activity, boolean withoutDefaultRole) {

        Intent intent = new Intent(activity, SwitchRoleActivity.class);

        intent.putExtra("withoutDefaultRole", withoutDefaultRole);

        activity.startActivityForResult(intent, 0);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.at_switch_role);

        setTitleBar(true, "切换账户",null);

        ButterKnife.bind(this);

        init();

        getAllRoleData();

        loginHelper = new LoginHelper(this);

    }

    @Override
    protected void onDestroy() {
        loginHelper.detachView();
        super.onDestroy();
    }

    private void init() {

        withoutDefaultRole = getIntent().getBooleanExtra("withoutDefaultRole", false);

        httpRequestHelper = new HttpRequestHelper(this, this);

        switchRoleAdapter = new SwitchRoleAdapter(this, list);

        listView.setAdapter(switchRoleAdapter);

        switchRoleAdapter.setOnClickListener(roleSelectorListener);

    }

    private void getAllRoleData() {

        XjqRequestContainer container = new XjqRequestContainer(USER_LIST_QUERY, true, LoginSignTypeEnum.ONE_AUTH_SIGN);

        httpRequestHelper.startRequest(container, true);

    }

    private void setDefaultRole(int position) {

        XjqRequestContainer container = new XjqRequestContainer(SET_DEFAULT_LOGIN_USER, true, LoginSignTypeEnum.ONE_AUTH_SIGN);

        container.put("defaultLoginUserId", list.get(position).getUserId());

        httpRequestHelper.startRequest(container, true);

    }

    private void selectorRole(int position) {
        //如果是当前角色,点击直接关闭页面即可,不用切换角色
        if (list.get(position).getUserId().equals(LoginInfoHelper.getInstance().getUserId())) {
            finish();
            return;
        }

        loginHelper.selectorRoleLogin(list.get(position).getUserId());

    }

    private SwitchRoleAdapter.OnClickListener roleSelectorListener = new SwitchRoleAdapter.OnClickListener() {
        @Override
        public void setDefaultListener(int position) {
            setDefaultRole(position);
        }

        @Override
        public void selectorRoleListener(int position) {
            selectorRole(position);
        }
    };

    private void responseSuccessUserList(JSONObject jsonObject) throws JSONException {
        if (jsonObject.has("userSelectInfo")) {
            JSONObject userSelectInfo = jsonObject.getJSONObject("userSelectInfo");

            UserRoleInfoBean userRoleInfoBean = new Gson().fromJson(userSelectInfo.toString(), UserRoleInfoBean.class);

            userRoleInfoBean.operatorData();

            list.addAll(userRoleInfoBean.getUserSimples());

            switchRoleAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case USER_LIST_QUERY:
                    responseSuccessUserList(jsonObject);
                    break;
                case SET_DEFAULT_LOGIN_USER:
                    responseSuccessSetDefaultRole(jsonObject);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessSetDefaultRole(JSONObject jsonObject) {
        ToastUtil.showLong(XjqApplication.getContext(),"设置默认角色成功");
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void authLoginSuccess(boolean userTagEnabled) {
        if (withoutDefaultRole) {
            setResult(RESULT_OK);
        } else {
            //ibAppUtil.showTip(SwitchRoleActivity.this, "切换角色成功");
        }
        EventBus.getDefault().post(new EventBusMessage(EventBusMessage.ROLE_SWITCH_SUCCESS));
        finish();
    }

    @Override
    public void anonymousLoginSuccess() {
    }

    @Override
    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
