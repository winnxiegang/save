package com.android.xjq.dialog;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.xjq.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.xjq.adapter.main.InfoPagerAdapter;
import com.android.xjq.bean.live.ChannelUserEntity;
import com.android.xjq.controller.AudienceListController;
import com.android.xjq.dialog.base.DialogBase;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.banana.commlib.LoginInfoHelper;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Admin on 2017/3/7.
 */

public class CurrentAudienceDialog extends DialogBase implements OnHttpResponseListener {
    //关注
    public static int AUDIENCE_TAB = 0;
    //麦序
    public static int MIC_TAB = 1;
    private int currentTab;

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.audienceRb)
    RadioButton audienceRb;
    @BindView(R.id.micRb)
    RadioButton micRb;
    @BindView(R.id.rg)
    RadioGroup rg;
    private HttpRequestHelper httpRequestHelper;

    private String channelId;
    private AudienceListController audienceListController;

    public CurrentAudienceDialog(Context context, int orientation, String channelId) {
        super(context, R.layout.dialog_current_audience, R.style.dialog_base, orientation);

        this.channelId = channelId;

        init();

        requestData();
    }


    private void requestData() {
        channelUserQuery();
    }

    private void channelUserQuery() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_USER_QUERY_BY_CHANNEL_ID, false);

        map.put("channelAreaId", channelId);

        map.put("queryChannelMicCount", "1");

        map.put("queryChannelUserCount", "1");

        if (LoginInfoHelper.getInstance().getUserId() != null) {
            map.put("authedUserId", LoginInfoHelper.getInstance().getUserId());
        }

        httpRequestHelper.startRequest(map, false);
    }

    private void channelMicQuery() {
        XjqRequestContainer map = new XjqRequestContainer(XjqUrlEnum.CHANNEL_MIC_LIST_QUERY_BY_CHANNEL_ID, true);

        map.put("channelAreaId", channelId);

        httpRequestHelper.startRequest(map, false);
    }

    private void init() {
        ButterKnife.bind(this, rootView);

        httpRequestHelper = new HttpRequestHelper(context, this);

        setRadioButtonDrawable();

        audienceListController = new AudienceListController(context);

        viewPager.setAdapter(new InfoPagerAdapter(audienceListController.getView()));

        audienceRb.setOnCheckedChangeListener(checkedListener);

        micRb.setOnCheckedChangeListener(checkedListener);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    currentTab = 0;
                    audienceRb.setChecked(true);
                    micRb.setChecked(false);
                    channelUserQuery();
                } else {
                    currentTab = 1;
                    audienceRb.setChecked(false);
                    micRb.setChecked(true);
                    channelMicQuery();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private CompoundButton.OnCheckedChangeListener checkedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                if (buttonView.getId() == R.id.audienceRb) {
                    currentTab = AUDIENCE_TAB;
                    micRb.setChecked(false);
                    viewPager.setCurrentItem(0);
                } else if (buttonView.getId() == R.id.micRb) {
                    currentTab = MIC_TAB;
                    audienceRb.setChecked(false);
                    viewPager.setCurrentItem(1);
                }
            }
        }
    };

    private void setRadioButtonDrawable() {
        Drawable audienceDrawable = context.getResources().getDrawable(R.drawable.selector_audience_rb);

        audienceDrawable.setBounds(0, 0, audienceDrawable.getIntrinsicWidth() / 2, audienceDrawable.getIntrinsicHeight() / 2);

        audienceRb.setCompoundDrawables(audienceDrawable, null, null, null);

        Drawable micDrawable = context.getResources().getDrawable(R.drawable.selector_mic_rb);

        micDrawable.setBounds(0, 0, micDrawable.getIntrinsicWidth() / 2 + 10, micDrawable.getIntrinsicHeight() / 2 + 10);

        micRb.setCompoundDrawables(micDrawable, null, null, null);
    }

    private void responseSuccessChannelMicList(JSONObject jsonObject) throws JSONException {
       /* if (jsonObject.has("channelUserInfoList")) {
            JSONArray channelUserInfoList = jsonObject.getJSONArray("channelUserInfoList");

            List<ChannelUserEntity.ChannelUserBean> channelUserBeanList = new Gson().fromJson(channelUserInfoList.toString(), new TypeToken<List<ChannelUserEntity.ChannelUserBean>>() {
            }.getType());

            audienceListController.setChannelMicList(channelUserBeanList);

        }*/

        ChannelUserEntity channelUserEntity = new Gson().fromJson(jsonObject.toString(), ChannelUserEntity.class);

        channelUserEntity.operatorData();

        audienceListController.setChannelMicList(channelUserEntity.getChannelUserInfoList());

    }

    private void responseSuccessChannelUserList(JSONObject jsonObject) throws JSONException {
        ChannelUserEntity channelUserEntity = new Gson().fromJson(jsonObject.toString(), ChannelUserEntity.class);

        audienceRb.setText("观众(" + channelUserEntity.getUserCount() + ")");

        micRb.setText("麦序(" + channelUserEntity.getMicCount() + ")");

        channelUserEntity.operatorData();

        audienceListController.setChannelUserList(channelUserEntity.getChannelUserInfoList());
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case CHANNEL_USER_QUERY_BY_CHANNEL_ID:
                    responseSuccessChannelUserList(jsonObject);
                    break;
                case CHANNEL_MIC_LIST_QUERY_BY_CHANNEL_ID:
                    responseSuccessChannelMicList(jsonObject);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ((BaseActivity) context).operateErrorResponseMessage(jsonObject);
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
