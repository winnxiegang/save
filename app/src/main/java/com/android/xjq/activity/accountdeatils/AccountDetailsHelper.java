package com.android.xjq.activity.accountdeatils;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.xjq.R;
import com.android.xjq.bean.accountdetails.AccountBean;
import com.android.xjq.model.AccountDetailsEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zaozao on 2017/11/3.
 */

public class AccountDetailsHelper {
    private View.OnClickListener mSelectedListener;

    public List<AccountDetailsOperateBean> beanList = new ArrayList<>();

    private Context context;

    private LinearLayout dropDownView;

    private String[] name;
    private String[] code;

    public AccountDetailsHelper( Context context) {
        this.context = context;
    }
    //有popuWindow的时候用这个
    public AccountDetailsHelper( Context context, View.OnClickListener mSelectedListener,LinearLayout dropDownView) {
        this.mSelectedListener = mSelectedListener;
        this.context = context;
        this.dropDownView = dropDownView;
    }

//    public void showPopu(int currentSelect){
//        getBean(currentSelect).popupWindowHelper
//                .showPopupWindow(getBean(currentSelect).selectFilterType);
//    }

    //判断切换的时候是否需要请求，如果没有点击过，并且list数据为空
    public boolean doRequest(int currentSelect){
//        return !getBean(currentSelect).tabClicked&&getBean(currentSelect).list.size()==0;
        return !getBean(currentSelect).tabClicked;
    }


    public boolean setTabClicked(int currentSelect){
        return getBean(currentSelect).tabClicked = true;
    }


    public void operatorData() {

        for (int i = 0; i < 3; i++) {
            AccountDetailsOperateBean<AccountBean.AccountLogListBean> bean = new AccountDetailsOperateBean<>();
//            messageParamBean.typeList.addAll(initTypeDataList(i));
//            messageParamBean.popupWindowHelper = new PopupWindowHelper(context,initTypeDataList(i),
//                    mSelectedListener, dropDownView, KjggIssueSelectorAdapter.SELECT_LOTTERY_TYPE);
//            messageParamBean.selectFilterType="";
            bean.currentPage =1;
            bean.maxPages=100;
            bean.endDate = "";
            bean.startDate = "";
            if(i==0){
                bean.tabClicked = true;
            }else{
                bean.tabClicked = false;
            }
            switch (i){
                case 0:
                    bean.accountType = AccountDetailsEnum.GOLDCOIN.name();
                    break;
                case 1:
                    bean.accountType = AccountDetailsEnum.GIFTCOIN.name();
                    break;
                case 2:
                    bean.accountType = AccountDetailsEnum.POINTCOIN.name();
                    break;
            }
            beanList.add(bean);
        }
    }

    public AccountDetailsOperateBean getBean(int currentSelect){
        return beanList.get(currentSelect);
    }

    //验证时间
    public  boolean validate(String startDate,String endDate,int currentSelect) {

        if (context.getResources().getString(R.string.endDate).equals(endDate) ||
                context.getResources().getString(R.string.startDate).equals(startDate)) {

            ToastUtil.showLong(context.getApplicationContext(),"请选择相应的日期");
            return false;
        }
        if (TimeUtils.isAfterNowNoTime(endDate, startDate)) {
            ToastUtil.showLong(context.getApplicationContext(),"结束日期不能小于开始日期");
            return false;
        }
        getBean(currentSelect).startDate = startDate;
        getBean(currentSelect).endDate = endDate;

        return true;
    }

//    /**
//     * 冻结明细类型
//     * @return
//     */
//    private ArrayList<LtTypeData> initTypeDataList(int index) {
//        initNameCode(index);
//
//        ArrayList<LtTypeData> list = new ArrayList<LtTypeData>();
//
//        for (int i = 0; i <name.length ; i++) {
//            LtTypeData data = new LtTypeData();
//            data.setCode(code[i]);
//            data.setName(name[i]);
//            list.add(data);
//        }
//        list.get(0).setSelected(true);
//
//        return list;
//    }
//
//    private void initNameCode(int i){
//        switch (i){
//            case 0:
//                name =new String[] {"","1","2","3"};
//                code =new String[] {"1","2","3","4"};
//                break;
//            case 1:
//                name =new String[] {"","12","22","32","1111","12","22","32"};
//                code =new String[] {"31","233","333","34","1111","12","22","32"};
//                break;
//            case 2:
//                name =new String[] {"","14","24","34","24","34"};
//                code =new String[] {"144","244","344","444","24","34"};
//                break;
//        }
//    }



    public class AccountDetailsOperateBean<T>{
        public int currentPage;
        public int maxPages;
        public boolean tabClicked;//对应的tab有没有被点击过，请求过，请求过了再点回来就不用在请求了（前提是筛选条件没有变）
        public String accountType = ""; // 金币，礼金，点券
        public String startDate;
        public String endDate;
        //popuWindow需要的数据
//        public String selectFilterType;
//        public ArrayList<LtTypeData> typeList = new ArrayList<LtTypeData>();
//        public PopupWindowHelper popupWindowHelper;
        public List<T> list = new ArrayList<>();

    }



}
