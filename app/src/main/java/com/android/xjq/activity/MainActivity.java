package com.android.xjq.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.eventBus.EventBusMessage;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.newmatch.MatchScheduleFragment2;
import com.android.xjq.activity.program.ProgramFragment;
import com.android.xjq.bean.TabBean;
import com.android.xjq.dialog.live.AchievementEffectDialog;
import com.android.xjq.fragment.CheerFragment;
import com.android.xjq.fragment.FragmentTabHost;
import com.android.xjq.fragment.HomeFragment2;
import com.android.xjq.fragment.MeFragment;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.presenters.LoginHelper2;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.utils.XjqUrlEnum;
import com.android.xjq.view.TabFrameLayout;
import com.android.xjq.view.TabPublishView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.utils.XjqUrlEnum.HOME_NAVIGATION_BAR_CONFIG;
import static com.android.xjq.utils.XjqUrlEnum.NEWLY_ACQUIRED_ACHIEVEMENT_MEDAL_QUERY;


public class MainActivity extends BaseActivity implements UserLoginView, IHttpResponseListener {

    @BindView(R.id.realTabContent)
    LinearLayout realTabContent;

    public FragmentTabHost mTabHost;

    private static final int PUBLISH_TAB_INDEX = -1;//发表TAB所在的 位置
    private static final int TAB_COUNT = 5;//导航栏按钮的个数

    //连续按两次返回键就退出
    private long firstTime;
    private LoginHelper2 loginHelper;
    private int[] textArray = {R.string.home_fragment, R.string.live, R.string.cheer_title, R.string.match_schedule, R.string.me};
    private String titleArray[] = {"home_fragment", "live", "", "match_schedule", "me"};
    private Class<?> fragmentArray[] = {HomeFragment2.class, ProgramFragment.class, CheerFragment.class, MatchScheduleFragment2.class, MeFragment.class};
    private TabPublishView mTabPublishView;//中间那个按钮 点击弹出动画
    private WrapperHttpHelper httpRequestHelper;
    private List<TabBean> mTabs = new ArrayList<>();
    private int[] iconArray = {
            R.drawable.selector_home_main_tab,
            R.drawable.selector_home_program_tab,
            R.drawable.selector_home_program_tab,//等一下替换为助威
            R.drawable.selector_home_schedule_tab,
            R.drawable.selector_home_mine_tab};

    public static void startMainActivity(Activity activity) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        loginHelper = new LoginHelper2(this);

        setContentView(R.layout.activity_main);

        compat(null, Color.TRANSPARENT);

        ButterKnife.bind(this);

        initView();

        httpRequestHelper = new WrapperHttpHelper(this);

        getTabDrawables();
        //有网络的情况下,如果广告页腾讯云登录失败,这在这里不断重连
        // checkIsLoginTim(inspectNet());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMessage msg) {
        if (msg != null && TextUtils.equals(msg.getMessage(), EventBusMessage.GO_MAIN_TAB)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("tabIndex", msg.getValue());
            startActivity(intent);
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        // super.onNewIntent(intent);
        int tab = intent.getIntExtra("tabIndex", 0);
        mTabHost.setCurrentTab(tab);
    }

    public void getTabDrawables() {
        ArrayList<RequestContainer> list = new ArrayList<>();
        list.add(new XjqRequestContainer(HOME_NAVIGATION_BAR_CONFIG, true));
        list.add(new XjqRequestContainer(NEWLY_ACQUIRED_ACHIEVEMENT_MEDAL_QUERY, true));
        httpRequestHelper.startRequestList(list, true);
    }

    private void initView() {
        int tab = getIntent().getIntExtra("tabIndex", 0);

        mTabHost = (FragmentTabHost) findViewById(R.id.tabHost);

        mTabHost.setup(this, getSupportFragmentManager(), R.id.realTabContent);

        mTabHost.getTabWidget().setDividerDrawable(null);
        //适配fragment

        for (int i = 0; i < TAB_COUNT; i++) {

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(titleArray[i]).setIndicator(getTabItemView(i));

            mTabHost.addTab(tabSpec, fragmentArray[i], null);
        }

        mTabHost.setCurrentTab(tab);
        /*mTabHost.getTabWidget().getChildTabViewAt(PUBLISH_TAB_INDEX).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTabPublishView == null)
                    mTabPublishView = new TabPublishView(MainActivity.this);

                mTabPublishView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WriteMySubjectActivity.WriteTypeEnum typeEnum = ((int) v.getTag()) == TabPublishView.CLICK_INDEX_SUBJECT ? WriteMySubjectActivity.WriteTypeEnum.WRITE_SUBJECT : WriteMySubjectActivity.WriteTypeEnum.WRITE_SPEAK;
                        WriteMySubjectActivity.startWriteMySubjectActivity(MainActivity.this, typeEnum);
                    }
                });
                mTabPublishView.showPublishView();
            }
        });*/
    }

    public void changeTabViewStatus(int currentStatus) {
        View tabViewAt = mTabHost.getTabWidget().getChildTabViewAt(0);
        if (tabViewAt instanceof TabFrameLayout) {
            TabFrameLayout tabItem = (TabFrameLayout) tabViewAt;
            tabItem.changeTabViewStatus(currentStatus);
        }
    }


    private View getTabItemView(int index) {
        if (index == PUBLISH_TAB_INDEX) {
            ImageView tabPublishIv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -2);
            params.topMargin = LibAppUtil.dip2px(this, 5);
            params.bottomMargin = params.topMargin;
            tabPublishIv.setLayoutParams(params);
            tabPublishIv.setBackgroundColor(Color.WHITE);
            tabPublishIv.setImageResource(iconArray[index]);
            return tabPublishIv;
        }

        //int realIndex = index > PUBLISH_TAB_INDEX ? index - 1 : index;
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        TabFrameLayout tabItem = (TabFrameLayout) layoutInflater.inflate(R.layout.tab_bottom_nav, null);
        tabItem.setId(index);
        tabItem.setImageResource(iconArray[index]);
        tabItem.setText(getString(textArray[index]));
        /*if (realIndex == 0)
            tabItem.setTabExtraEnable(true);*/
        return tabItem;
    }


    private void responseGetTabDrawables(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("bottomNavigationBarList");
            if (jsonArray != null && jsonArray.length() > 0) {
                mTabs.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    TabBean tabBean = new TabBean();
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    tabBean.setName(obj.optString("name"));
                    JSONArray columnConfigList = obj.getJSONArray("columnConfigList");
                    JSONObject normal = (JSONObject) columnConfigList.get(0);
                    JSONObject pressed = (JSONObject) columnConfigList.get(1);
                    tabBean.setNormalUrl(normal.optString("logoUrl"));
                    tabBean.setPressedUrl(pressed.optString("logoUrl"));
                    mTabs.add(tabBean);
                }
                //invalidateMainTabs(mTabs);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void responseSuccessAchievementQuery(JSONObject jsonObject) {
        try {
            List<String> medalCodeList = null;
            if (jsonObject.has("medalCodeList")) {
                JSONArray medalCodeListJson = jsonObject.getJSONArray("medalCodeList");
                medalCodeList = new Gson().fromJson(medalCodeListJson.toString(), new TypeToken<List<String>>() {
                }.getType());
            }
            if (medalCodeList != null && medalCodeList.size() > 0) {
                for (String medalCode : medalCodeList) {
                    AchievementEffectDialog.newInstance(medalCode).show(getSupportFragmentManager());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void invalidateMainTabs(List<TabBean> beans) {
        int childCount = mTabHost.getTabWidget().getChildCount();
        int realIndex = 0;
        for (int i = 0; i < childCount; i++) {
            View tabViewAt = mTabHost.getTabWidget().getChildTabViewAt(i);
            if (tabViewAt instanceof TabFrameLayout) {
                TabFrameLayout tabItem = (TabFrameLayout) tabViewAt;
                tabItem.setUrl(beans.get(realIndex).getNormalUrl(), beans.get(realIndex).getPressedUrl());
                realIndex++;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginHelper.detachView();
    }

    @Override
    protected void onNetChange(boolean isNetConnected) {
        checkIsLoginTim(isNetConnected);
    }

    private void checkIsLoginTim(boolean isNetConnected) {
        if (InitBusinessHelper.isLoginSuccess() || !isNetConnected || InitBusinessHelper.isLoginning()) {
            return;
        }
        //没有登录成功,网络连接正常,且当前没有处于登录状态,则尝试重新登录腾讯云
        if (!InitBusinessHelper.isHasInitApp()) {
            loginHelper.autonmLogin();
        } else {
            InitBusinessHelper.iLiveLogin(LoginInfoHelper.getInstance().getUserIdentifier(),
                    LoginInfoHelper.getInstance().getUserAppUserSign(), null);
        }
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - firstTime < 2000) {
            finish();
            System.exit(0);
        } else {
            firstTime = System.currentTimeMillis();
            Toast.makeText(MainActivity.this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void authLoginSuccess(boolean userTagEnabled) {
    }

    @Override
    public void anonymousLoginSuccess() {
    }

    @Override
    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject) {
    }

    //底部小红点显示隐藏根据 右上角邮箱和群聊消息
    private int mNotifyCount;
    private int mMessageCount;

    public void onMsgCountChanged(int notifyCount, int messageCount) {
        if (notifyCount != -1) {
            mNotifyCount = notifyCount;
        }
        if (messageCount != -1) {
            mMessageCount = messageCount;
        }
        boolean visible = (mMessageCount + mNotifyCount) > 0;
        TabFrameLayout tabItem = (TabFrameLayout) mTabHost.getTabWidget().getChildTabViewAt(0);
        tabItem.setNoticeVisible(visible);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        switch (((XjqUrlEnum) request.getRequestEnum())) {
            case HOME_NAVIGATION_BAR_CONFIG:
                responseGetTabDrawables(((JSONObject) o));
                break;
            case NEWLY_ACQUIRED_ACHIEVEMENT_MEDAL_QUERY:
                responseSuccessAchievementQuery(((JSONObject) o));
                break;
        }

    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }
}
