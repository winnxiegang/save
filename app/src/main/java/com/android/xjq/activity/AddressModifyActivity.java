package com.android.xjq.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.AddressListBean;
import com.android.xjq.utils.AddressSelectUtils;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.utils.AddressSelectUtils.ADDR_CITY;
import static com.android.xjq.utils.AddressSelectUtils.ADDR_COUNTY;
import static com.android.xjq.utils.AddressSelectUtils.ADDR_PRO;
import static com.android.xjq.utils.AddressSelectUtils.ADDR_TOWN;
import static com.android.xjq.utils.XjqUrlEnum.USER_DEFAULT_RECEIVE_ADDRESS_QUERY;

/**
 * Created by qiaomu on 2018/3/5.
 */

public class AddressModifyActivity extends BaseActivity4Jczj implements IHttpResponseListener {
    /*------------------------------*/
    /*修改收货地址的*/
    @BindView(R.id.address_tv)
    TextView addressTv;
    @BindView(R.id.address_user)
    TextView addressUser;
    @BindView(R.id.address_modify)
    ImageView addressModify;
    @BindView(R.id.address_layout_simpleInfo)
    RelativeLayout addressSimpleInfoLayout;
    @BindView(R.id.address_add_layout)
    RelativeLayout addressAddLayout;

    /*------------------------------*/
    /*修改收货地址的2*/
    @BindView(R.id.address_layout_info)
    LinearLayout addressInputInfoLayout;
    @BindView(R.id.address_name_input)
    EditText addressNameInput;
    @BindView(R.id.address_mobile_input)
    EditText addressMobileInput;
    @BindView(R.id.address_address_detail)
    EditText addressDetailInput;

    /*省市区 三个tv*/
    @BindView(R.id.address_province)
    TextView addressProvince;
    @BindView(R.id.address_district)
    TextView addressDistrict;
    @BindView(R.id.address_county)
    TextView addressCounty;
    @BindView(R.id.address_town)
    TextView addressTown;
    @BindView(R.id.address_save)
    Button addressSave;

    public static final int REQUEST_CODE = 1000;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);

    //修改地址intent字段
    private ArrayList<AddressListBean.ProvinceCityCounty> addressProvinceList, addressCityList, addressCountyList, addressTownList;
    private String addressType = ADDR_PRO, provinceId, cityId, countyId, townId;
    private boolean hasDefaultAddress = false;

    //修改用户收货地址调这个
    public static void startAddressModifyActivity4Result(Activity from) {
        Intent intent = new Intent(from, AddressModifyActivity.class);
        from.startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_address_modify, R.string.title_edit_address, -1, MODE_BACK);
        ButterKnife.bind(this);
    }

    @Override
    protected void setUpView() {
        mToolbar.setBackgroundColor(Color.WHITE);
        mToolbarTitle.setTextColor(Color.BLACK);
        mToolbar.setNavigationIcon(R.drawable.ic_back_black);

        addressAddLayout.setVisibility(View.VISIBLE);
        addressAddLayout.setOnClickListener(new AddressClick());
        addressModify.setOnClickListener(new AddressClick());
        addressCounty.setOnClickListener(new AddressClick());
        addressProvince.setOnClickListener(new AddressClick());
        addressDistrict.setOnClickListener(new AddressClick());
        addressTown.setOnClickListener(new AddressClick());
        addressSave.setOnClickListener(new AddressClick());

        //同一个形状的图片 只是颜色不同可以用此方法渲染 有效节省内存 和安装包体积
//        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.icon_title_write).mutate();
//        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.gray_text_color));
//        addressModify.setImageDrawable(drawable);
    }

    @Override
    protected void setUpData() {
        RequestFormBody formBody = new RequestFormBody(USER_DEFAULT_RECEIVE_ADDRESS_QUERY, true);
        mHttpHelper.startRequest(formBody, true);
    }

    private void setUpDefaultAddressInfo(String address, String name, String mobile) {
        if (TextUtils.isEmpty(address) && TextUtils.isEmpty(name) && TextUtils.isEmpty(mobile))
            return;

        hasDefaultAddress = true;
        addressAddLayout.setVisibility(View.GONE);
        addressInputInfoLayout.setVisibility(View.GONE);
        addressSimpleInfoLayout.setVisibility(View.VISIBLE);
        addressTv.setText(address);
        addressUser.setText(name + "  " + mobile);
    }

    private class AddressClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.address_add_layout:
                case R.id.address_modify:
                    addressAddLayout.setVisibility(View.GONE);
                    addressSimpleInfoLayout.setVisibility(View.GONE);
                    addressInputInfoLayout.setVisibility(View.VISIBLE);
                    break;
                case R.id.address_province:
                    addressType = ADDR_PRO;
                    getAddressProvinceCityCounty();
                    break;
                case R.id.address_district:
                    //如果没有选择省份就妄图选择市  直接不响应，下同
                    if (provinceId == null)
                        return;
                    addressType = ADDR_CITY;
                    getAddressProvinceCityCounty();
                    break;
                case R.id.address_county:
                    if (cityId == null) {
                        return;
                    }
                    addressType = ADDR_COUNTY;
                    getAddressProvinceCityCounty();
                    break;
                case R.id.address_town:
                    if (countyId == null) {
                        return;
                    }
                    addressType = ADDR_TOWN;
                    getAddressProvinceCityCounty();
                    break;
                case R.id.address_save:
                    if (TextUtils.isEmpty(addressNameInput.getText().toString().trim())
                            || TextUtils.isEmpty(addressMobileInput.getText().toString().trim())
                            || TextUtils.isEmpty(addressDetailInput.getText().toString().trim())
                            || TextUtils.isEmpty(provinceId)
                            || TextUtils.isEmpty(cityId)
                            || TextUtils.isEmpty(countyId)) {
                        showToast("请把信息填写完整");
                        return;
                    }

                    showProgressDialog();
                    RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.USER_RECEIVE_ADDRESS_CREATE, true);
                    formBody.put("receiverName", addressNameInput.getText().toString());
                    formBody.put("receiverCell", addressMobileInput.getText().toString());
                    formBody.put("streetInfo", addressDetailInput.getText().toString());
                    formBody.put("provinceId", provinceId);
                    formBody.put("cityId", cityId);
                    formBody.put("countyId", countyId);
                    formBody.put("townId", townId);
                    mHttpHelper.startRequest(formBody);
                    break;

            }
        }
    }

    //获取省市区数据
    private void getAddressProvinceCityCounty() {
        switch (addressType) {
            case ADDR_PRO:
                if (addressProvinceList != null && addressProvinceList.size() > 0) {
                    AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressProvinceList, addressType, new onAddressSelectListener());
                    return;
                }
                break;
            case ADDR_CITY:
                if (addressCityList != null && addressCityList.size() > 0) {
                    AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressCityList, addressType, new onAddressSelectListener());
                    return;
                }
                break;
            case ADDR_COUNTY:
                if (addressCountyList != null && addressCountyList.size() > 0) {
                    AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressCountyList, addressType, new onAddressSelectListener());
                    return;
                }
                break;
            case ADDR_TOWN:
                if (addressTownList != null && addressTownList.size() > 0) {
                    AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressTownList, addressType, new onAddressSelectListener());
                    return;
                }
                break;
        }
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.PROVINCES_CITIES_COUNTS_TOWNS_QUERY, true);
        formBody.put("type", addressType);
        formBody.put("provinceId", provinceId);
        formBody.put("cityId", cityId);
        formBody.put("countyId", countyId);
        formBody.setGenericClaz(AddressListBean.class);
        mHttpHelper.startRequest(formBody, true);

    }

    private class onAddressSelectListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (addressType) {
                case ADDR_PRO:
                    AddressListBean.ProvinceCityCounty province = addressProvinceList.get(which);
                    addressProvince.setText(province.name);
                    //如果这次选择的省份与上次不同，那么清空市区的缓存列表
                    if (province.id != provinceId) {
                        cityId = null;
                        countyId = null;
                        townId = null;

                        addressDistrict.setText(getString(R.string.address_title_city));
                        addressCounty.setText(getString(R.string.address_title_county));
                        addressTown.setText(R.string.address_title_town);
                        addressTown.setVisibility(View.GONE);
                        addressCityList = null;
                        addressCountyList = null;
                        addressTownList = null;
                    }
                    provinceId = province.id;
                    break;
                case ADDR_CITY:
                    AddressListBean.ProvinceCityCounty city = addressCityList.get(which);
                    addressDistrict.setText(city.name);
                    if (city.id != cityId) {

                        countyId = null;
                        townId = null;
                        addressCounty.setText(getString(R.string.address_title_county));
                        addressTown.setText(R.string.address_title_town);

                        addressCountyList = null;
                        addressTownList = null;
                    }
                    cityId = city.id;
                    break;
                case ADDR_COUNTY:
                    AddressListBean.ProvinceCityCounty county = addressCountyList.get(which);
                    addressCounty.setText(county.name);
                    if (county.id != countyId) {
                        townId = null;
                        addressTown.setText(R.string.address_title_town);
                        addressTownList = null;
                    }
                    countyId = county.id;
                    //选择完区后 自动去请求镇的数据
                    addressTown.performClick();
                    break;
                case ADDR_TOWN:
                    AddressListBean.ProvinceCityCounty town = addressTownList.get(which);
                    addressTown.setText(town.name);
                    townId = town.id;
                    break;
            }
        }
    }

    @Override
    public void onSuccess(RequestContainer request, Object o) {
        closeProgressDialog();
        XjqUrlEnum anEnum = (XjqUrlEnum) request.getRequestEnum();
        switch (anEnum) {
            case USER_DEFAULT_RECEIVE_ADDRESS_QUERY://默认地址查询
                JSONObject object = ((JSONObject) o);
                setUpDefaultAddressInfo(object.optString("userReceiveAddresss"), object.optString("receiverName"), object.optString("receiverCell"));
                break;
            case PROVINCES_CITIES_COUNTS_TOWNS_QUERY://省市区查询
                switch (addressType) {
                    case ADDR_PRO:
                        addressProvinceList = ((AddressListBean) o).areaProvinceList;
                        AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressProvinceList, addressType, new onAddressSelectListener());
                        break;
                    case ADDR_CITY:
                        addressCityList = ((AddressListBean) o).areaCityList;
                        AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressCityList, addressType, new onAddressSelectListener());
                        break;
                    case ADDR_COUNTY:
                        addressCountyList = ((AddressListBean) o).areaCountList;
                        AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressCountyList, addressType, new onAddressSelectListener());
                        break;
                    case ADDR_TOWN:
                        addressTownList = ((AddressListBean) o).areaTownList;
                        boolean isTownEmpty = addressTownList == null || addressTownList.size() <= 0;
                        addressTown.setVisibility(isTownEmpty ? View.GONE : View.VISIBLE);
                        //AddressSelectUtils.onProvinceCityCountyQueryResult(this, addressTownList, addressType, new onAddressSelectListener());
                        break;
                }
                break;
            case USER_RECEIVE_ADDRESS_CREATE://创建收货地址
                String mTownAddress = addressTown.getText().toString();
                if (TextUtils.equals(mTownAddress, getString(R.string.address_title_town))) {
                    mTownAddress = "";
                }
                String address = addressDistrict.getText().toString() + addressCounty.getText().toString() + mTownAddress + addressDetailInput.getText().toString();
                setUpDefaultAddressInfo(address, addressNameInput.getText().toString(), addressMobileInput.getText().toString());
                break;
            default:
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        closeProgressDialog();
        operateErrorResponseMessage(jsonObject);
    }

    @Override
    public void onNavigationBtnClicked() {
        if (addressInputInfoLayout.getVisibility() == View.VISIBLE) {
            addressSimpleInfoLayout.setVisibility(hasDefaultAddress ? View.VISIBLE : View.GONE);
            addressAddLayout.setVisibility(hasDefaultAddress ? View.GONE : View.VISIBLE);
            addressInputInfoLayout.setVisibility(View.GONE);
        } else {
            this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        onNavigationBtnClicked();
    }
}
