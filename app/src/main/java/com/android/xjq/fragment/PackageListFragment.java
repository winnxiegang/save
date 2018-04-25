package com.android.xjq.fragment;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.pullrecycler.ilayoutmanager.ILayoutManager;
import com.android.banana.pullrecycler.ilayoutmanager.MyGridLayoutManager;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.GridSpacingItemDecoration;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.TreasureList;
import com.android.xjq.bean.TreasureOpenListBean;
import com.android.xjq.liveanim.TreasureBoxOpenDialog1;
import com.android.xjq.liveanim.TreasureBoxOpenDialog2;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/8.
 */

public class PackageListFragment extends BaseListFragment<TreasureList.Treasure> implements IHttpResponseListener<TreasureList> {
    private static final String TREASURE_TYPE_TREASURE = "TREASURE_CHEST";//宝箱
    private static final String TREASURE_TYPE_GIFT = "GIFT";//道具

    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private String treasureType = TREASURE_TYPE_TREASURE;
    private boolean isTreasure;
    private String gmtNow;

    public static PackageListFragment newInstance(boolean isTreasure) {

        Bundle args = new Bundle();
        args.putBoolean("isTreasure", isTreasure);
        PackageListFragment fragment = new PackageListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initData() {
        isTreasure = getArguments().getBoolean("isTreasure");
        treasureType = isTreasure ? TREASURE_TYPE_TREASURE : TREASURE_TYPE_GIFT;
    }

    @Override
    protected ILayoutManager getLayoutManager() {
        MyGridLayoutManager manager = new MyGridLayoutManager(getContext(), 3);
        return manager;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        int space = LibAppUtil.dip2px(getContext(), 10);
        return new GridSpacingItemDecoration(3, space, false);
    }

    @Override
    protected void onSetUpView() {
        setLoadMoreEnable(true);
        setRefreshEnable(true);
        SpannableString string = SpannableStringHelper.changeTextColor(getString(R.string.package_empty_tip), ContextCompat.getColor(getContext(), R.color.package_empty_tip_color));
        mRecycler.setEmptyView(R.drawable.ic_package_empty, string, "");
    }

    @Override
    public int getItemLayoutRes() {
        return R.layout.fragment_package_list_item;
    }

    @Override
    public void onRefresh(boolean refresh) {
        RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.MY_PACKAGE_QUERY, true);
        formBody.put("currentPage", mCurPage);
        formBody.put("type", treasureType);
        mHttpHelper.startRequest(formBody);
    }

    @Override
    public void onBindHolder(ViewHolder holder, TreasureList.Treasure treasure, int position) {
        holder.setBackgroundRes(R.id.package_item, isTreasure ? R.drawable.bg_package_enable : R.drawable.bg_package_normal);

        holder.setText(R.id.package_count, "X " + (int) treasure.currentTotalCount)
                .setText(R.id.package_name, isTreasure ? treasure.title : treasure.giftName)
                .setTextColor(R.id.package_name, ContextCompat.getColor(getContext(), isTreasure ? R.color.package_enable : R.color.white))
                .setImageByUrl(getContext(), R.id.package_iv, isTreasure ? treasure.imageUrl : treasure.giftImageUrl);

        holder.setViewVisibility(R.id.package_time, (isTreasure || TextUtils.isEmpty(treasure.gmtExpired)) ? View.GONE : View.VISIBLE);
        if (!TextUtils.isEmpty(treasure.gmtExpired)) {
            int minutes = TimeUtils.differMinutes(treasure.gmtExpired, gmtNow);
            String text = minutes >= 60 ? minutes / 60 + "小时" : minutes + "分钟";
            holder.setText(R.id.package_time, text);
        }
    }

    @Override
    public void onItemClick(View view, final int position) {
        final TreasureList.Treasure treasure = mDatas.get(position);
        if (!isTreasure)//不是宝箱不能点击
            return;

        final TreasureBoxOpenDialog1 openDialog = TreasureBoxOpenDialog1.newInstance(treasure);
        openDialog.setOnDismissListener(new TreasureBoxOpenDialog1.OnDismissListener() {
            @Override
            public void onDismiss(long count, ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList) {
                treasure.currentTotalCount = count;
                if (count <= 0) {
                    mDatas.remove(treasure);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mAdapter.notifyItemChanged(position);
                }
                showTreasureOpenAnimDialog(treasureOpenList, treasure);
            }
        });
        openDialog.show(getChildFragmentManager());

    }

    private void showTreasureOpenAnimDialog(ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList, TreasureList.Treasure treasure) {
        TreasureBoxOpenDialog2 openDialog2 = TreasureBoxOpenDialog2.newInstance(treasure.title, treasure.subTypeCode, treasureOpenList);
        openDialog2.setOnDismissListener(new TreasureBoxOpenDialog2.OnDismissListener() {
            @Override
            public void onDismiss() {
                onRefresh(true);
            }
        });
        openDialog2.show(getChildFragmentManager());
    }

    @Override
    public void onSuccess(RequestContainer request, TreasureList treasureList) {
        gmtNow = treasureList == null ? TimeUtils.getCurrentTime() : treasureList.nowDate;
        mPaginator = treasureList == null ? null : treasureList.paginator;
        loadCompleted(treasureList == null ? null : isTreasure ? treasureList.treasureChestList : treasureList.userGiftList);
        //mRecycler.hideOverLoadView();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
            loadCompleted(null);
        }
    }
}
