package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.xjq.adapter.main.BindThirdAdapter;
import com.android.xjq.bean.ThirdCertifyBean;

import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/7/28.
 */

public class BindThirdCertifyActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.listView)
    ListView listView;
    private HttpRequestHelper httpRequestHelper;
    private List<ThirdCertifyBean.ThirdUserMappingConfigsBean> mList = new ArrayList<>();
    private BindThirdAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_bind_third);
        setTitleBar(true, "绑定第三方",null);
        ButterKnife.bind(this);
        httpRequestHelper = new HttpRequestHelper(this, this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestData();
    }

    public static void startBindThirdCertifyActivity(Activity activity) {
        Intent intent = new Intent(activity, BindThirdCertifyActivity.class);

        activity.startActivity(intent);
    }

    private void requestData() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.THIRD_CHANNEL_CERTIFY_INFO_QUERY, true);

        httpRequestHelper.startRequest(map, true);
    }

    private void initView() {
        mAdapter = new BindThirdAdapter(this, mList);
        listView.setAdapter(mAdapter);
    }

    private void responseSuccessThirdCertify(JSONObject jsonObject) {
        ThirdCertifyBean thirdCertifyBean = new Gson().fromJson(jsonObject.toString(), ThirdCertifyBean.class);
        thirdCertifyBean.operatorData();
        if (thirdCertifyBean.getThirdUserMappingConfigs() != null) {
            mList.clear();
            mList.addAll(thirdCertifyBean.getThirdUserMappingConfigs());
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        responseSuccessThirdCertify(jsonObject);
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }
}
