package com.android.xjq.activity.accountdeatils;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.AppParam;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.utils.RefreshEmptyViewHelper;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.swipyrefreshlayout.VpSwipeRefreshLayout;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.view.ScrollListView;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.jjx.sdk.listener.OnMyClickListener;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.adapter.main.AccountDetailsAdapter;
import com.android.xjq.bean.accountdetails.AccountBean;
import com.android.xjq.bean.userInfo.DateModel;
import com.android.xjq.dialog.AccountDetailFilterDialog;
import com.android.xjq.utils.TimeAlertDialogUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * zaozao 2017-11-03
 * 继承群聊的BaseActivity
 * popWindow这次没有，代码不删了，以后可能会有
 */

public class AccountDetailsActivity extends BaseActivity implements OnHttpResponseListener{
    public static int GOLD_DETAIL = 0;
    public static int GIFT_DETAIL = 1;
    public static int COUPON_DETAIL = 2;

    @BindView(R.id.filterIv)
    ImageView filterIv;
    @BindView(R.id.goldCoinTv)
    TextView goldCoinTv;

    @BindView(R.id.leftTriangleIv)
    ImageView leftTriangleIv;
    @BindView(R.id.giftCoinTv)
    TextView giftCoinTv;
    //    @BindView(R.id.leftArrowIv)
//    ImageView leftArrowIv;
//    @BindView(R.id.middleArrowIv)
//    ImageView middleArrowIv;
//    @BindView(R.id.rightArrowIv)
//    ImageView rightArrowIv;
    @BindView(R.id.middleTriangleIv)
    ImageView middleTriangleIv;
    @BindView(R.id.couponTv)
    TextView couponTv;

    @BindView(R.id.rightTriangleIv)
    ImageView rightTriangleIv;
    @BindView(R.id.startDateTv)
    TextView startDateTv;
    @BindView(R.id.endDateTv)
    TextView endDateTv;
    @BindView(R.id.searchBtn1)
    TextView searchBtn1;
    @BindView(R.id.refreshListView)
    ScrollListView refreshListView;
    @BindView(R.id.refreshLayout)
    VpSwipeRefreshLayout refreshLayout;
    @BindView(R.id.dropDownView)
    LinearLayout dropDownView;

    private int currentSelect = GOLD_DETAIL;

    private AccountDetailsHelper accountDetailsHelper;

    private HttpRequestHelper httpRequestHelper;

    private List<AccountBean.AccountLogListBean> list= new ArrayList<>();

    private AccountDetailsAdapter adapter;

    public String selectAccountType = "ALL";//全部，收入，支出

    @OnClick(R.id.filterIv)
    public void filterInOut(){
        new AccountDetailFilterDialog(AccountDetailsActivity.this,filterOnClickListener,selectAccountType);
    }

    @OnClick(R.id.goldCoinTabLayout)
    public void onGoldCoinTagLayoutClick() {
//        if (currentSelect == GOLD_DETAIL) {
//            accountDetailsHelper.showPopu(GOLD_DETAIL);
//        }else{
        currentSelect = GOLD_DETAIL;
        changeTabView();
//        }
    }

    @OnClick(R.id.couponTabLayout)
    public void onCouponTabLayoutClick() {

//        if (currentSelect == COUPON_DETAIL) {
//            accountDetailsHelper.showPopu(COUPON_DETAIL);
//        }else{
        currentSelect = COUPON_DETAIL;
        changeTabView();
//        }
    }
    @OnClick(R.id.giftCoinTabLayout)
    public void onGiftCoinTabLayoutClick() {
//        if (currentSelect == GIFT_DETAIL) {
//            accountDetailsHelper.showPopu(GIFT_DETAIL);
//        }else{
        currentSelect = GIFT_DETAIL;
        changeTabView();
//        }

    }



    private void changeTabView(){
        leftTriangleIv.setVisibility(View.GONE);
        rightTriangleIv.setVisibility(View.GONE);
        middleTriangleIv.setVisibility(View.GONE);
        goldCoinTv.setTextColor(getResources().getColor(R.color.alphaWhite));
        giftCoinTv.setTextColor(getResources().getColor(R.color.alphaWhite));
        couponTv.setTextColor(getResources().getColor(R.color.alphaWhite));
//        leftArrowIv.setImageResource(R.drawable.icon_arrow_white);
//        middleArrowIv.setImageResource(R.drawable.icon_arrow_white);
//        rightArrowIv.setImageResource(R.drawable.icon_arrow_white);
        switch (currentSelect){
            case 0:
                leftTriangleIv.setVisibility(View.VISIBLE);
                goldCoinTv.setTextColor(getResources().getColor(R.color.white));
//                leftArrowIv.setImageResource(R.drawable.icon_arrow_pink);
                break;
            case 1:
                middleTriangleIv.setVisibility(View.VISIBLE);
                giftCoinTv.setTextColor(getResources().getColor(R.color.white));
//                middleArrowIv.setImageResource(R.drawable.icon_arrow_pink);
                break;
            case 2:
                rightTriangleIv.setVisibility(View.VISIBLE);
                couponTv.setTextColor(getResources().getColor(R.color.white));
//                rightArrowIv.setImageResource(R.drawable.icon_arrow_pink);
                break;
        }

        if(accountDetailsHelper.getBean(currentSelect).startDate!=null
                &&!TextUtils.equals("",accountDetailsHelper.getBean(currentSelect).startDate)){

            startDateTv.setText(accountDetailsHelper.getBean(currentSelect).startDate);

            endDateTv.setText(accountDetailsHelper.getBean(currentSelect).endDate);
        }else{
            startDateTv.setText("开始日期");

            endDateTv.setText("结束日期");
        }
        currentPage = accountDetailsHelper.getBean(currentSelect).currentPage;
        maxPages = accountDetailsHelper.getBean(currentSelect).maxPages;

        if(accountDetailsHelper.doRequest(currentSelect)){
            mRequestType = REFRESH;
            //第一次切换过来，请求数据
            doRequest();
            accountDetailsHelper.setTabClicked(currentSelect);
        }else{
            // 适配之前的数据
            list.clear();
            list.addAll(accountDetailsHelper.getBean(currentSelect).list);
            adapter.setCurrentSelect(currentSelect);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.searchBtn1)
    public void search() {
        String  startDate = startDateTv.getText().toString();

        String endDate = endDateTv.getText().toString();

        initPage();//搜索条件改变，页码需要重置

        if (accountDetailsHelper.validate(startDate,endDate,currentSelect)) {
            // 网络请求
            doRequest();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account_details);

        ButterKnife.bind(this);

        setTitleBar(true, "账户明细",null);

        filterIv.setVisibility(View.VISIBLE);

        setRefreshLayout();

//      accountDetailsHelper = new AccountDetailsHelper(this,mSelectedListener,dropDownView);

        accountDetailsHelper = new AccountDetailsHelper(this);

        httpRequestHelper = new HttpRequestHelper(this,this);

        adapter = new AccountDetailsAdapter(this,list);

        refreshListView.setDivider(null);

        refreshListView.setAdapter(adapter);

        accountDetailsHelper.operatorData();

        startDateTv.setOnClickListener(dateTouchListener);

        endDateTv.setOnClickListener(dateTouchListener);
        doRequest();
    }

    //刷新，页面重置
    private void initPage(){
        currentPage =1;
        maxPages = 100;
        mRequestType = REFRESH;

        AccountDetailsHelper.AccountDetailsOperateBean bean= accountDetailsHelper.getBean(currentSelect);
        bean.currentPage =1;
        bean.maxPages=100;
    }
    /**
     * 刷新时：日期重置，
     */
    private void initData(){
        initPage();//下拉刷新的时候
        startDateTv.setText("开始日期");
        endDateTv.setText("结束日期");

        AccountDetailsHelper.AccountDetailsOperateBean bean= accountDetailsHelper.getBean(currentSelect);
        bean.endDate = "";
        bean.startDate = "";

    }


    OnMyClickListener myClickListener = new OnMyClickListener() {
        @Override
        public void onClick(View v) {
            DateModel dateModel = (DateModel) v.getTag();
            if (dateModel.getTag().equals(AppParam.startDate)) {

                startDateTv.setText(dateModel.getDateStr());

            } else if (dateModel.getTag().equals(AppParam.endDate)) {

                endDateTv.setText(dateModel.getDateStr());
            }
        }
    };

    //请求数据
    private void doRequest(){
        RequestFormBody requestContainer = null;

        requestContainer = new RequestFormBody(JczjURLEnum.ACCOUNT_LOG_QUERY, true);

        requestContainer.put("currentPage", accountDetailsHelper.getBean(currentSelect).currentPage);

        requestContainer.put("startDate",accountDetailsHelper.getBean(currentSelect).startDate);

        requestContainer.put("accountLogQueryAppCode",accountDetailsHelper.getBean(currentSelect).accountType);//POINTCOIN   GIFTCOIN    GOLDCOIN

        requestContainer.put("timestamp", System.currentTimeMillis());

        if(!"ALL".equals(selectAccountType)){
            requestContainer.put("transDirection",selectAccountType);//不传：全部   O 支出，I 收入
        }

        requestContainer.put("endDate", accountDetailsHelper.getBean(currentSelect).endDate);

        httpRequestHelper.startRequest(requestContainer, true);

    }




    private View.OnClickListener dateTouchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.startDateTv) {
                TimeAlertDialogUtil alertDialogUtil = new TimeAlertDialogUtil(AccountDetailsActivity.this, "选择开始时间",
                        myClickListener, true, AppParam.startDate, startDateTv.getText().toString());

            } else if (v.getId() == R.id.endDateTv) {
                TimeAlertDialogUtil alertDialogUtil = new TimeAlertDialogUtil(AccountDetailsActivity.this, "选择结束时间",
                        myClickListener, true, AppParam.endDate, endDateTv.getText().toString());
            }
        }

    };

    SelectFilterOnClickListener filterOnClickListener = new SelectFilterOnClickListener() {
        @Override
        public void select(String selectType) {
            if(!selectAccountType.equals(selectType)){

                selectAccountType = selectType;

                //筛选条件改变时，切换到其他，都要重新请求,之前的数据要清空
                for (int i = 0; i <3 ; i++) {
                    if(i!=currentSelect){
                        accountDetailsHelper.getBean(i).tabClicked = false;
                        //在这里清空数据是为了作为判断是否需要请求的一个条件.相当于第一次进入页面
                        // accountDetailsHelper.getBean(i).list.clear();
                        LogUtils.e("kk",i+"数据暂时没有清空");
                    }
                }
                initPage();//收入支出切换的时候需要重置
                doRequest();
            }
        }
    };

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {

        AccountBean accountBean = new Gson().fromJson(jsonObject.toString(),AccountBean.class);

        maxPages = accountBean.getPaginator().getPages();

        accountDetailsHelper.getBean(currentSelect).maxPages = maxPages;

        if(mRequestType==REFRESH){

            if(list.size()>0){
                list.clear();
            }
            if(accountDetailsHelper.getBean(currentSelect).list.size()>0){
                accountDetailsHelper.getBean(currentSelect).list.clear();
            }
        }

        list.addAll(accountBean.getAccountLogList());

        accountDetailsHelper.getBean(currentSelect).list.addAll(accountBean.getAccountLogList());

        adapter.setCurrentSelect(currentSelect);

        adapter.notifyDataSetChanged();

        mRefreshEmptyViewHelper.closeRefresh();
    }

    /**
     * 设置刷新布局
     */
    private void setRefreshLayout() {

        mRefreshEmptyViewHelper = new RefreshEmptyViewHelper(this, new RefreshEmptyViewHelper.OnRefreshListener() {
            @Override
            public void onRefresh() {

                initData();

                doRequest();
            }

            @Override
            public void onLoadMore() {

                mRequestType = LOAD_MORE;
                if (currentPage >= maxPages) {
                    ToastUtil.showLong(AccountDetailsActivity.this.getApplicationContext(),"已经到最后一页了!!");
                    mRefreshEmptyViewHelper.closeRefresh();

                } else {

                    currentPage++;

                    accountDetailsHelper.getBean(currentSelect).currentPage = currentPage;

                    doRequest();
                }
            }
            @Override
            public void onEmptyRefresh() {

            }
        }, getResources().getDrawable(com.android.banana.R.drawable.icon_no_details), "暂无记录");
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

    public interface SelectFilterOnClickListener{
        void  select(String selectType);
    }


    /**
     * 参见91的账户明细页面效果popuwindow，这次没用到
     */
//    private View.OnClickListener mSelectedListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            int pos = (int) v.getTag();
//            LtTypeData ltTypeData = null;
//            ltTypeData = (LtTypeData) (accountDetailsHelper.getBean(currentSelect).typeList).get(pos);
//            accountDetailsHelper.getBean(currentSelect).selectFilterType = ltTypeData.getCode();
//            switch (currentSelect){
//                case 0:
//
//                    if("".equals(accountDetailsHelper.getBean(currentSelect).selectFilterType)){
//                        goldCoinTv.setText("金币明细");
//                    }else{
//                        goldCoinTv.setText( ltTypeData.getName());
//                    }
//                    break;
//                case 1:
//                    if("".equals(accountDetailsHelper.getBean(currentSelect).selectFilterType)){
//                        giftCoinTv.setText("礼金明细");
//                    }else{
//                        giftCoinTv.setText( ltTypeData.getName());
//                    }
//                    break;
//                case 2:
//                    if("".equals(accountDetailsHelper.getBean(currentSelect).selectFilterType)){
//                        couponTv.setText("点券明细");
//                    }else{
//                        couponTv.setText( ltTypeData.getName());
//                    }
//                    break;
//            }
//            accountDetailsHelper.getBean(currentSelect).popupWindowHelper.dismiss();
//
//            initData();
//
//            doRequest();
//
//        }
//    };
}
