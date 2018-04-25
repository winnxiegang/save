package com.android.xjq.fragment.input;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.activity.FansMedalActivity;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.BottomMedalAdapter;
import com.android.xjq.bean.medal.UserFansMedalInfoEntity;
import com.android.xjq.bean.medal.UserMedalBean;
import com.android.xjq.fragment.BaseFragment;
import com.android.xjq.utils.live.SpannableStringHelper;
import com.android.xjq.view.CustomProgressView;
import com.android.xjq.view.TableGirdView;
import com.android.xjq.view.expandtv.CustomMedalDrawable;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by lingjiu on 2017/9/27.
 */

public class MedalFragment extends BaseFragment {


    @BindView(R.id.myMedalTv)
    TextView myMedalTv;
    @BindView(R.id.progressView)
    CustomProgressView progressView;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.circles)
    CirclePageIndicator circles;
    @BindView(R.id.hostNameTv)
    TextView hostNameTv;
    @BindView(R.id.operateTv)
    TextView operateTv;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;

    @BindView(R.id.withoutMedalLayout)
    LinearLayout withoutMedalLayout;
    @BindView(R.id.medalDescTv)
    TextView medalDescTv;
    @BindView(R.id.medalDetailLayout)
    LinearLayout medalDetailLayout;
    private InputCallback callback;
    private List<List<UserMedalBean>> mList = new ArrayList<>();
    private UserMedalBean currentUserMedalBean;
    private ArrayList<BottomMedalAdapter> adapterArray = new ArrayList<>();

    public void setCallback(InputCallback callback) {
        this.callback = callback;
    }

    @OnClick(R.id.operateTv)
    public void operateMedal() {
        if (currentUserMedalBean == null) return;
        ((LiveActivity) activity).getHttpController().operateMedal(currentUserMedalBean.getId());
    }

    @OnClick(R.id.managerTv)
    public void managerMedal() {
        FansMedalActivity.startFansMedalActivity(activity);
    }

    @OnClick({R.id.descTv, R.id.knowMedalTv})
    public void instruction() {
        ((LiveActivity) activity).getHttpController().fansMedalIntroduction();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragemnt_medal, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    protected void initData() {
    }

    public void setCurrentMedalInfo(UserMedalBean userMedalDetail) {
        if (mList != null && mList.size() > 0) {
            for (List<UserMedalBean> userMedalBeen : mList) {
                for (UserMedalBean userMedalBean : userMedalBeen) {
                    userMedalBean.setAdorned(false);
                    if (userMedalBean.getId() != null && userMedalDetail != null &&
                            userMedalBean.getId().equals(userMedalDetail.getId())) {
                        userMedalBean.setAdorned(true);
                    }
                }
            }
            notifyArrayDataSetChanged();
        }
    }

    public void setUserMedalData(UserFansMedalInfoEntity userFansMedalInfoEntity) {
        withoutMedalLayout.setVisibility(View.GONE);
        medalDetailLayout.setVisibility(View.GONE);
        if (userFansMedalInfoEntity.getAwardNum() == 0) {
            setWithoutMedalTextDesc(userFansMedalInfoEntity);
            return;
        }
        medalDetailLayout.setVisibility(View.VISIBLE);
        setMyMedalTv(userFansMedalInfoEntity);
        List<UserMedalBean> userMedalDetailList = userFansMedalInfoEntity.getUserMedalDetailList();
        if (userMedalDetailList != null && userMedalDetailList.size() > 0) {
            userMedalDetailList.add(0, new UserMedalBean());
        }
        ArrayList<View> list = new ArrayList<>();
        if (userMedalDetailList != null && userMedalDetailList.size() > 0) {
            mList.clear();
            int pageCount = (int) Math.ceil((float) userMedalDetailList.size() / 6);
            for (int i = 0; i < pageCount; i++) {
                List<UserMedalBean> userPerPageList = userMedalDetailList.subList(i * 6, (i + 1) * 6 > userMedalDetailList.size() ? userMedalDetailList.size() : (i + 1) * 6);
                GridView girdView = createGridView(userPerPageList);
                list.add(girdView);
            }
            viewPager.setAdapter(new MyPagerAdapter(list));
            circles.setViewPager(viewPager);
        }
    }

    private void setMyMedalTv(UserFansMedalInfoEntity userFansMedalInfoEntity) {
        SpannableStringBuilder ssb = new SpannableStringBuilder("我的粉丝勋章");
        int medalTotal = userFansMedalInfoEntity.getUserMedalDetailList() == null ? 0 : userFansMedalInfoEntity.getUserMedalDetailList().size();
        ssb.append(SpannableStringHelper.changeTextColor(
                "(" + medalTotal + ")", Color.parseColor("#f7b80f")));
        myMedalTv.setText(ssb);
        List<UserMedalBean> userMedalDetailList = userFansMedalInfoEntity.getUserMedalDetailList();
        if (userMedalDetailList != null && userMedalDetailList.size() > 0) {
            for (UserMedalBean userMedalBean : userMedalDetailList) {
                if (userMedalBean.isAdorned()) {
                    hostNameTv.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.VISIBLE);
                    progressView.setProgressValue(userMedalBean.getCurrentValue(), userMedalBean.getMaxMedalLevelValue(), true);
                    hostNameTv.setText(userMedalBean.getHostName() == null ? "" :
                            String.format(getContext().getString(R.string.host_name), userMedalBean.getHostName()));
                    break;
                }
            }
        }
    }

    private void setWithoutMedalTextDesc(UserFansMedalInfoEntity userFansMedalInfoEntity) {
        withoutMedalLayout.setVisibility(View.VISIBLE);
        SpannableStringBuilder ssb = new SpannableStringBuilder("空空如也，您还没有粉丝勋章可佩带;\n");
        if (userFansMedalInfoEntity.isAnchorApply()) {
            String str = String.format(getString(R.string.fans_medal_min_gold),
                    userFansMedalInfoEntity.getPointcoinAmount() + "",userFansMedalInfoEntity.getGoldcoinCoinAmount() + "");
            ssb.append(SpannableStringHelper.changeTextColor(str, Color.parseColor("#ff5443")));
            ssb.append("\n即可成为TA的粉丝，获得 ");
            SpannableString ss = new SpannableString("图片");
            String content = userFansMedalInfoEntity.getMedalLabelConfigList().size() > 0 ? userFansMedalInfoEntity.getMedalLabelConfigList().get(0).getContent() : "";
            Drawable d = new CustomMedalDrawable(content, 0, activity.getResources());
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            ss.setSpan(new ImageSpan(d, ImageSpan.ALIGN_BASELINE), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
            ssb.append(" 粉丝勋章!");
        } else {
            ssb.append(SpannableStringHelper.changeTextColor("该主播暂未开通粉丝勋章哦!", 3, 7, Color.parseColor("#ff5443")));
        }
        medalDescTv.setText(ssb);
    }

    private GridView createGridView(final List<UserMedalBean> userPerPageList) {
        mList.add(userPerPageList);
        TableGirdView gridView = new TableGirdView(getContext());
        gridView.setNumColumns(3);
        gridView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        BottomMedalAdapter medalAdapter = new BottomMedalAdapter(getContext(), userPerPageList);
        adapterArray.add(medalAdapter);
        gridView.setAdapter(medalAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (List<UserMedalBean> userMedalBeen : mList) {
                    for (UserMedalBean userMedalBean : userMedalBeen) {
                        userMedalBean.setSelected(false);
                    }
                }
                UserMedalBean userMedalBean = userPerPageList.get(position);
                userMedalBean.setSelected(true);
                currentUserMedalBean = userMedalBean;
                notifyArrayDataSetChanged();
                progressView.setProgressValue(userMedalBean.getCurrentValue(), userMedalBean.getMaxMedalLevelValue(), true);
                if (userMedalBean.getHostName() == null) {
                    hostNameTv.setVisibility(View.INVISIBLE);
                    progressLayout.setVisibility(View.INVISIBLE);
                } else {
                    hostNameTv.setVisibility(View.VISIBLE);
                    progressLayout.setVisibility(View.VISIBLE);
                    hostNameTv.setText(String.format(getContext().getString(R.string.host_name), userMedalBean.getHostName()));
                }


            }
        });
        return gridView;
    }

    private void notifyArrayDataSetChanged() {
        for (BottomMedalAdapter adapter : adapterArray) {
            adapter.notifyDataSetChanged();
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private ArrayList<View> list;

        public MyPagerAdapter(ArrayList<View> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }
}
