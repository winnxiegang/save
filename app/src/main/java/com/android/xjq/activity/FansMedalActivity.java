package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.R;
import com.android.xjq.adapter.main.MedalAdapter;
import com.android.xjq.bean.medal.UserFansMedalInfoEntity;
import com.android.xjq.bean.medal.UserMedalBean;

import com.android.banana.commlib.dialog.ShowMessageDialog;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.model.medal.MedalOperateActionTypeEnum;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.expandtv.CustomMedalDrawable;
import com.android.banana.commlib.loadmore.LoadMoreListView;
import com.android.banana.commlib.loadmore.OnLoadMoreListener;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ajiao on 2017/9/27 0027.
 */

public class FansMedalActivity extends BaseActivity implements OnHttpResponseListener {
    @BindView(R.id.medal_list)
    LoadMoreListView mMedalList;
    @BindView(R.id.emptyLayout)
    LinearLayout mEmptyLay;

    private List<UserMedalBean> mData = null;
    private MedalAdapter mAdapter = null;
    private HttpRequestHelper mHttpRequestHelper;
    private int currentPage = 1;
    private int maxPages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_medal);
        setTitleBar(true, getString(R.string.fans_medal));
        ButterKnife.bind(this);
        initView();
        requestData();
    }

    private void initView() {
        setEmptyView();
        mHttpRequestHelper = new HttpRequestHelper(this, this);
        mData = new ArrayList<>();
        mAdapter = new MedalAdapter(FansMedalActivity.this, mData);
        mAdapter.setOnOperateMedalClick(new MedalAdapter.IonOperateMedalClick() {
            @Override
            public void onOperateMedal(boolean isAdorned, String userMedalDetailId) {
                String actionType = isAdorned ? MedalOperateActionTypeEnum.CANCEL.name() : MedalOperateActionTypeEnum.ADORN.name();
                operateMedal(actionType, userMedalDetailId);
            }
        });

        mMedalList.initBottomView(true);
        mMedalList.setAdapter(mAdapter);
        mMedalList.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (currentPage == maxPages) {
                    mMedalList.getFooterView().showEnd();
                    return;
                }
                currentPage++;
                requestData();
            }
        });
        mMedalList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                showDeleteMedalDialog(i);
                return false;
            }
        });

    }

    private void setEmptyView() {
        ((ImageView) mEmptyLay.findViewById(R.id.emptyIv)).setImageResource(R.drawable.icon_no_content_about_match_schedule_detail);
        ((TextView) mEmptyLay.findViewById(R.id.emptyTv)).setText("暂无粉丝勋章");
    }

    private void showDeleteMedalDialog(final int pos) {
        ShowMessageDialog.Builder builder = new ShowMessageDialog.Builder();
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString ss = new SpannableString("勋章删除后，该勋章亲密度也会清零，只能重新获得和升级，确定要删除勋章");
        CustomMedalDrawable drawable = mAdapter.getCustomMedalDrawable(pos);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString ssp = new SpannableString("图片");
        ssp.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), 0, ssp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb.append(ss);
        ssb.append(ssp);
        ssb.append("吗?");
        builder.setMessage(ssb);
        builder.setPositiveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateMedal(MedalOperateActionTypeEnum.DELETE.name(), mData.get(pos).getId());
            }
        });
        builder.setTitle("提示");
        builder.setPositiveMessage("确定");
        builder.setNegativeMessage("取消");
        builder.setShowMessageMiddle(true);
        ShowMessageDialog dialog = new ShowMessageDialog(this, builder);
    }

    private void operateMedal(String actionType, String userMedalDetailId) {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.USER_MEDAL_OPERATE, true);
        container.put("userMedalDetailId", userMedalDetailId);
        container.put("actionType", actionType);
        container.put("medalType", "FAN_MEDAL");
        mHttpRequestHelper.startRequest(container, true);
    }

    private void requestData() {
        XjqRequestContainer container = new XjqRequestContainer(XjqUrlEnum.USER_FANS_MEDAL_PAGE_QUERY, true);
        container.put("currentPage", currentPage);
        container.put("medalType", "FAN_MEDAL");
        //container.put("channelId", CurLiveInfo.getRoomNum() + "");
        mHttpRequestHelper.startRequest(container, true);
    }

    public static void startFansMedalActivity(Activity activity) {
        activity.startActivity(new Intent(activity, FansMedalActivity.class));
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case USER_FANS_MEDAL_PAGE_QUERY:
                responseGetMedalDataSuccess(jsonObject);
                break;
            case USER_MEDAL_OPERATE:
                responseOperateMedalSuccess();
                break;
        }
    }

    private void responseOperateMedalSuccess() {
        currentPage = 0;
        mData.clear();
        requestData();
        mAdapter.notifyDataSetChanged();
    }

    private void responseGetMedalDataSuccess(JSONObject jsonObject) {
        UserFansMedalInfoEntity userFansMedalInfoEntity = new Gson().fromJson(jsonObject.toString(), UserFansMedalInfoEntity.class);
        maxPages = userFansMedalInfoEntity.getPaginator().getPages();
        List<UserMedalBean> userMedalList = userFansMedalInfoEntity.getUserMedalDetailList();
        if (userMedalList != null && userMedalList.size() > 0) {
            userFansMedalInfoEntity.operatorData(FansMedalActivity.this, null);
            userMedalList = userFansMedalInfoEntity.getUserMedalDetailList();
            if (userMedalList != null && userMedalList.size() > 0) {
                mData.addAll(userMedalList);
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mEmptyLay.setVisibility(View.VISIBLE);
        }
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
}
