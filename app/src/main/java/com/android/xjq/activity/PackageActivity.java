package com.android.xjq.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.Money;
import com.android.banana.commlib.utils.StatusBarCompat;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.fragment.PackageListFragment;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by qiaomu on 2018/3/8.
 *
 * 包裹 道具宝箱
 */

public class PackageActivity extends BaseActivity4Jczj implements IHttpResponseListener {
    @BindView(R.id.package_gold_tv)
    TextView mPackageGoldTv;
    @BindView(R.id.package_banna_tv)
    TextView mPackageBannaTv;
    @BindView(R.id.package_dollar_tv)
    TextView mPackageDollarTv;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.root_view)
    RelativeLayout rootView;

    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private PackageListFragment mTreasureFragment, mPropFragment;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PackageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_package);
        ButterKnife.bind(this);
        StatusBarCompat.compat(this, ContextCompat.getColor(this, R.color.half_transparent_4statusbar));
        StatusBarCompat.fitsSystemWindows(rootView);
    }

    @Override
    protected void setUpView() {
        mTreasureFragment = PackageListFragment.newInstance(true);
        mPropFragment = PackageListFragment.newInstance(false);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                hideShowFragment(checkedId == R.id.btn_box ? 0 : 1);
            }
        });
    }

    private void hideShowFragment(int index) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (index == 0) {
            if (!mTreasureFragment.isAdded()) {
                ft.add(R.id.container, mTreasureFragment);
            }
            ft.hide(mPropFragment).show(mTreasureFragment);
        } else {
            if (!mPropFragment.isAdded()) {
                ft.add(R.id.container, mPropFragment);
            }
            ft.hide(mTreasureFragment).show(mPropFragment);
        }

        ft.commit();
    }


    @Override
    protected void setUpData() {

    }


    @OnClick(R.id.package_close)
    public void onViewClicked() {
        onBackPressed();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        hideShowFragment(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.MY_FUND_ACCOUNT_QUERY, true);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        JSONObject jsonObject = (JSONObject) o;
        double goldCoinAmount = 0;
        if (jsonObject.has("goldCoinAmount")) {
            goldCoinAmount = jsonObject.optDouble("goldCoinAmount");
        }
        double pointCoinAmount = 0;
        if (jsonObject.has("pointCoinAmount")) {
            pointCoinAmount = jsonObject.optDouble("pointCoinAmount");
        }
        double giftCoinAmount = 0;
        if (jsonObject.has("giftCoinAmount")) {
            giftCoinAmount = jsonObject.optDouble("giftCoinAmount");
        }
        mPackageGoldTv.setText(new Money(goldCoinAmount).toSimpleString());
        mPackageBannaTv.setText(new Money(pointCoinAmount).toSimpleString());
        mPackageDollarTv.setText(new Money(giftCoinAmount).toSimpleString());
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        operateErrorResponseMessage(jsonObject);
    }
}
