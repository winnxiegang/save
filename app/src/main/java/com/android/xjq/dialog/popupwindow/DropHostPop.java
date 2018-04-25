package com.android.xjq.dialog.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.ListView;

import com.android.banana.commlib.coupon.BasePopupWindow;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveDropHostAdapter;
import com.android.xjq.bean.live.AnchorUserInfoClientSimple;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.utils.SocialTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingjiu on 2018/1/30.
 */

public class DropHostPop extends BasePopupWindow {

    private ListView listView;
    private List<AnchorUserInfoClientSimple> mList = new ArrayList<>();
    private LiveDropHostAdapter adapter;
    private int popupWidth;

    public DropHostPop(Context context) {
        super(context, -2, -2);
        initView(context);
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.view_drop_host_pop, null);
        listView = (ListView) parentView.findViewById(R.id.listView);
        setContentView(parentView);
    }

    public void showPop(List<AnchorUserInfoClientSimple> anchorUserInfoList, View anchorView) {
        mList.clear();
        mList.addAll(anchorUserInfoList);
        adapter.notifyDataSetChanged();
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWidth = parentView.getMeasuredWidth();
        LogUtils.e("top", "popupWidth=" + popupWidth);
        setWidth(popupWidth);
        showAsDropDown(anchorView, 0, -mContext.getResources().getDimensionPixelOffset(R.dimen.live_portrait_guest_header_height));
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //给ListView添加数据
        adapter = new LiveDropHostAdapter(mContext, mList);
        listView.setAdapter(adapter);

        adapter.setClickListener(new LiveDropHostAdapter.ClickListener() {
            @Override
            public void onClickFocus(int pos) {
                AnchorUserInfoClientSimple bean = mList.get(pos);
                if (bean.focus) {
                    SocialTools.cancelAttention(bean.userId, "FOLLOWCANCEL", new SocialCallback(bean));
                } else {
                    SocialTools.payAttention(bean.userId, new SocialCallback(bean));
                }
            }

            @Override
            public void onClickUserInfo(final int pos) {
                new PersonalInfoDialog(mContext, mList.get(pos).userId, new PersonalInfoDialog.Callback() {
                    @Override
                    public void focusStatusChanged(String userId, boolean isFocus) {
                        mList.get(pos).focus = isFocus;
                        adapter.notifyDataSetChanged();
                    }
                }).show();
                dismiss();
            }
        });
    }

    private class SocialCallback implements SocialTools.onSocialCallback {
        AnchorUserInfoClientSimple bean;

        SocialCallback(AnchorUserInfoClientSimple bean) {
            this.bean = bean;
        }

        @Override
        public void onResponse(JSONObject response, boolean success) {

            if (success) {
                String msg;
                if (bean.focus) {
                    msg = mContext.getString(R.string.cancel_focus);
                } else {
                    msg = mContext.getString(R.string.focus_success);
                }
                ToastUtil.showShort(mContext.getApplicationContext(), msg);
                bean.focus = !bean.focus;
                adapter.notifyDataSetChanged();
            } else {
                try {
                    ((LiveActivity) mContext).operateErrorResponseMessage(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
