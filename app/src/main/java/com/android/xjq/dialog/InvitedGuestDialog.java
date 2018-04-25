package com.android.xjq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.bean.program.SpecialGuestBean;
import com.android.xjq.dialog.adapter.SpecialGuestAdapter;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.xjq.dialog.adapter.SpecialGuestAdapter.PURCHASE_TYPE;
import static com.android.xjq.utils.XjqUrlEnum.CHANNEL_AREA_GUEST_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.PURCHASE_USER_LIST_QUERY_BY_OPTION;
import static com.android.xjq.utils.XjqUrlEnum.USER_FOLLOW_CANCEL;
import static com.android.xjq.utils.XjqUrlEnum.USER_FOLLOW_CREATE;

/**
 * Created by ajiao on 2018\2\7 0007.
 */

public class InvitedGuestDialog implements OnHttpResponseListener {
    private InvitedGuestDialog.Builder mBuilder;
    private Dialog mDialog;
    private View mDialogView;
    @BindView(R.id.title_lay)
    RelativeLayout mTitleLay;
    @BindView(R.id.title)
    TextView titleTxt;
    @BindView(R.id.close_img)
    ImageView closeTxt;
    @BindView(R.id.invited_guest_list)
    ListView invitedGuestLv;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private SpecialGuestAdapter mAdapter;
    private List<SpecialGuestBean.GuestInfoListBean> mListData;
    private HttpRequestHelper mHttpRequestHelper;
    private long mChannelAreaId;
    private String mGameBoardId;
    private String mGameBoardOptionId;
    private int mCurPos = -1;

    public InvitedGuestDialog(Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mBuilder.getContext(), R.style.NormalDialogStyle);
        mDialogView = View.inflate(mBuilder.getContext(), R.layout.widget_dialog_invited_dialog, null);
        mDialog.setContentView(mDialogView);
        ButterKnife.bind(this, mDialogView);

        mListData = new ArrayList<>();
        mHttpRequestHelper = new HttpRequestHelper(mBuilder.getContext(), this);
        mAdapter = new SpecialGuestAdapter(mBuilder.getContext(), mListData);

        setDialogPosition(-1);
        initDialog();
    }

    private void initDialog() {
        mAdapter.setOnOperateFocusClickListener(new SpecialGuestAdapter.onOperateFocusClick() {
            @Override
            public void onOperateFocus(boolean focusStatus, String userId, int pos) {
                mCurPos = pos;
                if (focusStatus) {
                    reqCancelFocus(userId);
                } else {
                    reqFocus(userId);
                }
            }
        });
        invitedGuestLv.setAdapter(mAdapter);
        mDialog.setCanceledOnTouchOutside(mBuilder.isTouchOutside());
        titleTxt.setText(mBuilder.getTitleStr());
        closeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }

    private void reqFocus(String userId) {
        XjqRequestContainer map = new XjqRequestContainer(USER_FOLLOW_CREATE, true);
        map.put("userId", userId);
        mHttpRequestHelper.startRequest(map, false);
    }

    private void reqCancelFocus(String userId) {
        XjqRequestContainer map = new XjqRequestContainer(USER_FOLLOW_CANCEL, true);
        map.put("userId", userId);
        map.put("userType", "FOLLOWCANCEL");
        mHttpRequestHelper.startRequest(map, false);
    }

    //show前需要调这个方法塞给控件数据
    private void setDialogPosition(int height) {
        float wdrHeight = DimensionUtils.dpToPx(400, mBuilder.getContext());
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        if (height <= 0) {
            lp.height = 800;
        } else {
            lp.height = (int)wdrHeight;
        }
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    //节目单列表
    public InvitedGuestDialog generateGuestData(long channelAreaId) {
        progressBar.setVisibility(View.VISIBLE);
        mChannelAreaId = channelAreaId;
        reqSpecialGuestList();
        return this;
    }

    //直播间pk列表
    public InvitedGuestDialog generateGuestData(String gameBoardId, String gameBoardOptionId, int channelAreaId) {
        progressBar.setVisibility(View.VISIBLE);
        mGameBoardId = gameBoardId;
        mGameBoardOptionId = gameBoardOptionId;
        mChannelAreaId = Long.valueOf(channelAreaId);
        reqSpecialGuestList();
        return this;
    }

    //节目单列表/直播间pk列表
    private void reqSpecialGuestList() {
        XjqRequestContainer map;
        if (TextUtils.isEmpty(mGameBoardId)) {
            map = new XjqRequestContainer(CHANNEL_AREA_GUEST_QUERY, true);
            map.put("channelAreaId", mChannelAreaId);
        } else {
            map = new XjqRequestContainer(PURCHASE_USER_LIST_QUERY_BY_OPTION, true);
            map.put("gameBoardOptionId", mGameBoardOptionId);
            map.put("gameBoardId", mGameBoardId);
            map.put("channelAreaId", mChannelAreaId);
        }
        mHttpRequestHelper.startRequest(map, false);
    }

    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public Dialog getDialog() {
        return mDialog;
    }

    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            case CHANNEL_AREA_GUEST_QUERY:
                responseReqGuestSuccess(jsonObject);
                break;
            case PURCHASE_USER_LIST_QUERY_BY_OPTION:
                responseReqPurchaseSuccess(jsonObject);
                break;
            case USER_FOLLOW_CREATE:
                responseFocusSuccess(jsonObject);
                break;
            case USER_FOLLOW_CANCEL:
                responseCancelFocusSuccess(jsonObject);
                break;
            default:
                break;
        }
    }

    private void responseReqPurchaseSuccess(JSONObject jsonObject) {
        mListData.clear();
        SpecialGuestBean specialGuestBean = new Gson().fromJson(jsonObject.toString(), SpecialGuestBean.class);
        specialGuestBean.operatorData();
        if (specialGuestBean.getUserInfoList() != null && specialGuestBean.getUserInfoList().size() > 0) {
            mListData.addAll(specialGuestBean.getUserInfoList());
        }
        progressBar.setVisibility(View.GONE);
        mAdapter.setType(PURCHASE_TYPE);
        mAdapter.notifyDataSetChanged();
    }

    private void responseCancelFocusSuccess(JSONObject jsonObject) {
        setOperateFocusUI(false);
    }

    private void responseFocusSuccess(JSONObject jsonObject) {
        setOperateFocusUI(true);
    }

    private void setOperateFocusUI(boolean focus) {
        String tip = focus ? "成功关注" : "成功取消关注";
        Toast.makeText(mBuilder.getContext(), tip, Toast.LENGTH_SHORT).show();
        mListData.get(mCurPos).setFocus(focus);
        mAdapter.notifyDataSetChanged();
    }

    private void responseReqGuestSuccess(JSONObject jsonObject) {
        mListData.clear();
        SpecialGuestBean specialGuestBean = new Gson().fromJson(jsonObject.toString(), SpecialGuestBean.class);
        List<SpecialGuestBean.GuestInfoListBean> list = specialGuestBean.getUserInfoList();
        if (list != null && list.size() > 0) {
            mListData.addAll(list);
        }
        progressBar.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
        int totalHeight = mTitleLay.getHeight();
        for (int i = 0, n = mListData.size() < 5 ? mListData.size() : 5; i < n; i++) {
            View listItem = mAdapter.getView(i, null, invitedGuestLv);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        totalHeight += invitedGuestLv.getDividerHeight() * 4;//没有5行我也给你5行的高度
        setDialogPosition(totalHeight);
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            if (jsonObject == null)
                return;
            ErrorBean bean = new ErrorBean(jsonObject);
        /*    if (bean.getError() == null)
                return;
            String name = bean.getError().getName();
            String tip = bean.getError().getMessage();*/
            String errMsg = bean.getDetailMessage();
            Toast.makeText(mBuilder.getContext(), errMsg, Toast.LENGTH_SHORT).show();
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

    public static class Builder {
        private Context context;
        private String titleStr;
        private int width;
        private int height;
        private boolean isTouchOutside = true;

        public Builder(Context ctx) {
            context = ctx;
        }

        public Builder setTitle(String title) {
            titleStr = title;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public String getTitleStr() {
            return titleStr;
        }

        public Builder setTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }

        public Builder setDialogWidth(int width) {
            width = width;
            return this;
        }

        public Builder setDialogHeight(int height) {
            height = height;
            return this;
        }


        public Context getContext() {
            return context;
        }

        public InvitedGuestDialog create() {
            InvitedGuestDialog dialog = new InvitedGuestDialog(this);
            return dialog;
        }


    }


}
