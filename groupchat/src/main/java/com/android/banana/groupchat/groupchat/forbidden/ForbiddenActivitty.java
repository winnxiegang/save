package com.android.banana.groupchat.groupchat.forbidden;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.android.banana.R;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.groupchat.base.BaseActivity4Jczj;
import com.android.banana.http.JczjURLEnum;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.banana.pullrecycler.recyclerview.WrapRecyclerView;
import com.android.banana.view.LabelTextView;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LibAppUtil;
import com.google.gson.JsonElement;
import com.jzxiang.pickerview.MyTimePickerDialog;
import com.jzxiang.pickerview.OnCalerndarSetListener;
import com.jzxiang.pickerview.data.Type;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2017/6/1.
 */

public class ForbiddenActivitty extends BaseActivity4Jczj implements IHttpResponseListener<JsonElement> {
    public static final int FORBIDDEN_REQUEST_CODE = 1001;
    WrapRecyclerView mRecyclerView;

    private GagAdapter gagAdapter;
    private ArrayList<GagItem> mDatas = new ArrayList<>();
    private MyTimePickerDialog dialog;
    private WrapperHttpHelper httpHelper;

    private int lastSelectedPos = 0;
    private String userId = "", groupId;
    private long totalSeconds = 0L;

    @Override
    protected void initEnv() {
        super.initEnv();
        userId = getIntent().getStringExtra("userId");
        groupId = getIntent().getStringExtra("groupId");
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_gag, getString(R.string.gag_time_title), R.menu.menu_ok, MODE_BACK);

        httpHelper = new WrapperHttpHelper(this);
        mRecyclerView = findViewOfId(R.id.WrapRecyclerView);
    }

    public static void start4GagActivity(Context from, String userId, String groupId) {
        Intent intent = new Intent(from, ForbiddenActivitty.class);
        intent.putExtra("userId", userId);
        intent.putExtra("groupId", groupId);
        if (from instanceof Activity) {
            ((Activity) from).startActivityForResult(intent, FORBIDDEN_REQUEST_CODE);
        } else {
            from.startActivity(intent);
        }
    }

    @Override
    protected void setUpView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gagAdapter = new GagAdapter(this, mDatas, 0, null);
        mRecyclerView.setAdapter(gagAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.base_divider_list_withpadding));
    }

    @Override
    protected void setUpData() {
        String[] gagArray = getResources().getStringArray(R.array.gag_array);
        for (int i = 0; i < gagArray.length; i++) {
            mDatas.add(new GagItem(i, gagArray.length, gagArray[i]));
        }
        gagAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        RequestFormBody container = new RequestFormBody(JczjURLEnum.USER_FORBBIDEN_ACTION_OPERATE, true);
        if (lastSelectedPos >= 0 && lastSelectedPos <= 4) {
            totalSeconds = mDatas.get(lastSelectedPos).totalMinute;
        }
        if (totalSeconds <= 0 && lastSelectedPos != 4) {
            showToast(getString(R.string.forbidden_custom_tip));
            return false;
        }
        container.put("userId", userId);
        container.put("groupId", groupId);
        container.put("forbiddenSeconds", totalSeconds);
        httpHelper.startRequest(container, false);
        return super.onMenuItemClick(item);
    }


    @Override
    public void onSuccess(RequestContainer request, JsonElement result) {
        LibAppUtil.showTip(this, getString(R.string.perform_success));
        setResult(RESULT_OK);
        this.finish();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject object, boolean netFailed) {
        try {
            operateErrorResponseMessage(object, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    class GagAdapter extends MultiTypeSupportAdapter<GagItem> {
        public GagAdapter(Context context, ArrayList<GagItem> list, int layoutRes, MultiTypeSupport typeSupport) {
            super(context, list, R.layout.activity_gag_list_item, null);
        }

        @Override
        public void onBindNormalHolder(ViewHolder holder, GagItem item, int position) {
            LabelTextView labeltv = holder.getView(R.id.LabelTextView);
            holder.getView(R.id.checkBox).setVisibility(item.isSelected ? View.VISIBLE : View.INVISIBLE);

            Drawable arrow = ContextCompat.getDrawable(mContext, R.drawable.right_arrow);
            labeltv.setCompoundDrawablesWithIntrinsicBounds(null, null, arrow, null);

            labeltv.setText(item.gagStr);
            labeltv.setLabelTextRight(item.rightTip);
        }

        @Override
        public void onItemClick(View view, int position) {

            if (position != getItemCount() - 1) {
                mDatas.get(lastSelectedPos).isSelected = false;
                mDatas.get(position).isSelected = true;
                mDatas.get(getItemCount() - 1).rightTip = null;
                notifyDataSetChanged();
                lastSelectedPos = position;
            } else {
                showTimePickDialog();
            }
        }
    }

    private void showTimePickDialog() {
        if (dialog == null) {
            dialog = new MyTimePickerDialog.Builder()
                    .setTitleStringId(getString(R.string.time_select))
                    .setThemeColor(getResources().getColor(R.color.main_red))
                    .setCurrentMillseconds(System.currentTimeMillis())
                    .setWheelItemTextSize(14)
                    .setType(Type.DAY_HOUR_MINS)
                    .startFromZero()
                    .setCyclic(true)
                    .setCalendarSetListener(dateSetListener)
                    .build(this);
        }
        dialog.show();
    }

    OnCalerndarSetListener dateSetListener = new OnCalerndarSetListener() {
        @Override
        public void onDateSet(int year, int month, int day, int hour, int min) {
            totalSeconds = day * 3600 * 24 + hour * 3600 + min * 60;//禁言时长 秒
            int itemCount = gagAdapter.getItemCount() - 1;
            mDatas.get(itemCount).rightTip = day + "天" + hour + "时" + min + "分";
            mDatas.get(lastSelectedPos).isSelected = false;
            lastSelectedPos = itemCount;
            gagAdapter.notifyDataSetChanged();
        }
    };

    class GagItem {//== 0, i == gagArray.length - 1

        public GagItem(int i, int length, String gagStr) {
            this.isSelected = i == 0;
            this.gagStr = gagStr;
            this.rightTip = null;
            this.showArrow = i == length - 1;
            switch (i) {
                case 0:
                    totalMinute = 10 * 60;
                    break;
                case 1:
                    totalMinute = 60 * 60;
                    break;
                case 2:
                    totalMinute = 60 * 12 * 60;
                    break;
                case 3:
                    totalMinute = 60 * 24 * 60;
                    break;
                case 4:
                    totalMinute = 0;
                    break;
            }

        }

        public boolean showArrow;
        public boolean isSelected;
        public String gagStr;
        public String rightTip;
        public int totalMinute;
    }
}
