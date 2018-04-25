package com.android.xjq.liveanim;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.BaseDialogFragment;
import com.android.banana.commlib.http.IHttpResponseListener;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.WrapperHttpHelper;
import com.android.banana.commlib.utils.SpannableStringHelper;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.TreasureList;
import com.android.xjq.bean.TreasureOpenListBean;
import com.android.xjq.model.TreasureBoxEnum;
import com.android.xjq.utils.XjqUrlEnum;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/8.
 * <p>
 * 包裹点击 打开弹窗 选择几个可以打开 然后执行TreasureBoxOpenDialog2
 */

public class TreasureBoxOpenDialog1 extends BaseDialogFragment implements View.OnClickListener, IHttpResponseListener<TreasureOpenListBean> {
    ImageView mPackageLogo;
    TextView mPackageTitle;
    TextView mPackageCount;
    Button mPackageBtnDelete;
    Button mPackageBtnAdd;
    Button mPackageBtnOk;
    TextView mCalculateTv;
    private TreasureList.Treasure treasure;
    private long nowCount;
    private WrapperHttpHelper mHttpHelper = new WrapperHttpHelper(this);
    private OnDismissListener mListener;
    private ArrayList<TreasureOpenListBean.treasureOpenBean> treasureChestList;

    public static TreasureBoxOpenDialog1 newInstance(TreasureList.Treasure treasure) {

        Bundle args = new Bundle();
        args.putParcelable("treasure", treasure);
        TreasureBoxOpenDialog1 fragment = new TreasureBoxOpenDialog1();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getDialogTheme() {
        return Theme.NORAML_THEME_DIMENABLE;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    public boolean fillScreenWidth() {
        return false;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_layout_treasure_open;
    }

    @Override
    protected void onDialogCreate() {
        mPackageLogo = (ImageView) rootView.findViewById(R.id.package_logo);
        mPackageTitle = (TextView) rootView.findViewById(R.id.package_title);
        mCalculateTv = (TextView) rootView.findViewById(R.id.package_calculate_tv);
        mPackageCount = (TextView) rootView.findViewById(R.id.package_count);
        mPackageBtnDelete = (Button) rootView.findViewById(R.id.package_btn_delete);
        mPackageBtnAdd = (Button) rootView.findViewById(R.id.package_btn_add);
        mPackageBtnOk = (Button) rootView.findViewById(R.id.package_btn_ok);

        mPackageBtnDelete.setOnClickListener(this);
        mPackageBtnAdd.setOnClickListener(this);
        mPackageBtnOk.setOnClickListener(this);
        mPackageBtnOk.setOnClickListener(this);
        rootView.findViewById(R.id.package_close).setOnClickListener(this);


        treasure = getArguments().getParcelable("treasure");

        TreasureBoxEnum boxEnum = TreasureBoxEnum.valueOf(treasure.subTypeCode);
        if (boxEnum != null)
            mPackageLogo.setImageResource(boxEnum.getIcon());

        mPackageTitle.setText(treasure.title);
        mCalculateTv.setText(String.valueOf((int) treasure.currentTotalCount));
        SpannableString string = SpannableStringHelper.changeTextColorAndSize("  " + (int) treasure.currentTotalCount, ContextCompat.getColor(getContext(), R.color.package_count), 18);
        mPackageCount.append(string);

        nowCount = treasure.currentTotalCount;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.package_close:
                dismiss();
                break;
            case R.id.package_btn_add:
                if (nowCount >= treasure.currentTotalCount)
                    return;
                nowCount++;
                mCalculateTv.setText(String.valueOf((int) nowCount));
                break;
            case R.id.package_btn_delete:
                if (nowCount <= 1)
                    return;
                nowCount--;
                mCalculateTv.setText(String.valueOf((int) nowCount));
                break;
            case R.id.package_btn_ok:
                if (nowCount < 1)
                    return;

                RequestFormBody formBody = new RequestFormBody(XjqUrlEnum.USER_PACKAGE_OPEN, true);
                formBody.put("subTypeId", treasure.subTypeId);
                formBody.put("number", (int) nowCount);
                mHttpHelper.startRequest(formBody);
                mPackageBtnOk.setEnabled(false);
                mPackageBtnOk.setAlpha(0.5f);
                break;
        }
    }

    @Override
    public void onSuccess(RequestContainer request, TreasureOpenListBean openListBean) {
        treasure.currentTotalCount = treasure.currentTotalCount - nowCount;
        treasureChestList = openListBean == null ? null : openListBean.treasureChestList;
        if (mListener == null)
            return;
        mListener.onDismiss(treasure.currentTotalCount, treasureChestList);
        this.dismiss();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mPackageBtnOk.setEnabled(true);
        mPackageBtnOk.setAlpha(1f);
        if (getActivity() != null)
            ((BaseActivity) getActivity()).operateErrorResponseMessage(jsonObject);
    }

    public interface OnDismissListener {
        /**
         * @param count            打开的那个宝箱还剩几个
         * @param treasureOpenList 打开宝箱后返回的礼物列表
         */
        void onDismiss(long count, ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mListener = listener;
    }

}
