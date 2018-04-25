package com.android.banana.commlib.bet;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.R;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.BetGiftEntity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.bet.anim.HeartLayout;
import com.android.banana.commlib.coupon.BasePopupWindow;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.library.Utils.LogUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.android.banana.commlib.bet.BetUrlEnum.GIFT_QUERY;
import static com.android.banana.commlib.bet.BetUrlEnum.PURCHASE_BASKETBALL_NORMAL;

/**
 * Created by ajiao on 2017/10/30 0030.
 */

public class BetGiftPop extends BasePopupWindow implements OnHttpResponseListener {
    String match_date;
    String raceId;
    String raceType;
    String boardId;
    @OptionCode
    String optionCode;
    String playType;


    public @interface OptionCode {
        String GUEST_WIN = "GUEST_WIN";
        String HOME_WIN = "HOME_WIN";
    }


    private HeartLayout heartLayout;
    private BetAdapter betAdapter;
    private RecyclerView recyclerView;
    private int gravity = Gravity.BOTTOM;
    private HttpRequestHelper httpRequestHelper;
    private List<BetGiftEntity.BetGiftBean> mList = new ArrayList<>();
    //礼物动画的锚点view
    private View anchorView;
    //当前选中礼物的信息
    private BetGiftEntity.BetGiftBean bean;
    //需要外面传进来当场比赛信息
    private IonSendBetGiftSuccess mListener;
    private IonPopWdrDismiss mOnDismissListener;

    public BetGiftPop(Context mContext,
                      String match_date,
                      String raceId,
                      String raceType,
                      String boardId,
                      @OptionCode String optionCode,
                      String playType) {
        this(mContext);
        this.match_date = match_date;
        this.raceId = raceId;
        this.raceType = raceType;
        this.boardId = boardId;
        this.optionCode = optionCode;
        this.playType = playType;

    }

    public BetGiftPop(final Context mContext) {
        super(mContext, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(LayoutInflater.from(mContext).inflate(R.layout.widget_anim_popwindow_view, null));
        this.setAnimationStyle(R.style.popwdr_anim);
        recyclerView = ((RecyclerView) parentView.findViewById(R.id.recyclerView));
        parentView.findViewById(R.id.contentLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnDismissListener != null) {
                    mOnDismissListener.onPopWdrDismiss();
                }
            }
        });
        parentView.findViewById(R.id.closeIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnDismissListener != null) {
                    mOnDismissListener.onPopWdrDismiss();
                }
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        betAdapter = new BetAdapter(mContext, mList);
        recyclerView.setAdapter(betAdapter);
        heartLayout = (HeartLayout) parentView.findViewById(R.id.heartLayout);
        parentView.findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bean == null) {
                    Toast.makeText(mContext, "请先选择礼物", Toast.LENGTH_LONG).show();
                    return;
                }
                if (heartLayout != null && bean != null) {
                    heartLayout.addFavor(anchorView, bean.getGiftImageUrl(), true);
                    giftPurchase();
                }
            }
        });
        betAdapter.setOnItemClickListener(new BetAdapter.MyItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                anchorView = view;
                bean = mList.get(position);
            }
        });
    }


    public void show() {
        showAtLocation(parentView, gravity, 0, 0);
        httpRequestHelper = new HttpRequestHelper(mContext, this);
        requestGift();
    }

    private void requestGift() {
        XjqRequestContainer map = new XjqRequestContainer(GIFT_QUERY, false);
        map.put("giftType", "BET");
        httpRequestHelper.startRequest(map);
    }

    //赠送礼物
    private void giftPurchase() {
        if (bean == null) return;
        XjqRequestContainer map = new XjqRequestContainer(PURCHASE_BASKETBALL_NORMAL, true);
        map.put("createrType", "USER_CREATE");
        map.put("payCoinType", "GOLD_COIN");
        map.put("payType", "GIFT");//固定
        map.put("payTypeNo", bean.getId());//礼物id
        map.put("keyAndOptions", boardId + "@" + optionCode);
        map.put("boardId", boardId);
        //raceId,raceType,totalFee可传可不传
        map.put("raceId", raceId);
        map.put("raceType", raceType);
        map.put("totalFee", bean.getPrice());
        map.put("playType", playType);
        httpRequestHelper.addRequest(map);
    }


    private void responseGiftQuery(JSONObject jsonObject) {
        BetGiftEntity entity = new Gson().fromJson(jsonObject.toString(), BetGiftEntity.class);
        List<BetGiftEntity.BetGiftBean> giftListBean = entity.getGiftList();
        if (giftListBean != null && giftListBean.size() > 0) {
            mList.addAll(giftListBean);
            betAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        switch ((BetUrlEnum) requestContainer.getRequestEnum()) {
            case GIFT_QUERY:
                responseGiftQuery(jsonObject);
                break;
            case PURCHASE_BASKETBALL_NORMAL:
                if (mListener != null) {
                    mListener.onSendBetGiftSuccess(bean.getPrice());
                }
                break;
            case PURCHASE_FOOTBALL_NORMAL:
                break;
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ErrorBean errorBean = new ErrorBean(jsonObject);
            ErrorBean.ErrorEntity error = errorBean.getError();

            ((BaseActivity) mContext).operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void showMessageDialog(String message) {
        try {
            new AlertDialog.Builder(mContext)
                    .setTitle("提示")
                    .setMessage(message)
                    .setPositiveButton("确定", null)
                    .setNegativeButton("取消", null)
                    .show();
        } catch (Exception e) {
            if (e instanceof WindowManager.BadTokenException) {
                LogUtils.e("", e.getMessage());
            }
        }
    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    public interface IonSendBetGiftSuccess {
        public void onSendBetGiftSuccess(double price);
    }

    public void setOnSendBetGiftSuccess(IonSendBetGiftSuccess listener) {
        mListener = listener;
    }

    public interface IonPopWdrDismiss {
        public void onPopWdrDismiss();
    }

    public void setOnPopWdrDismissListener(IonPopWdrDismiss listener) {
        this.mOnDismissListener = listener;
    }

    public void setTeamName(String teamName) {
        TextView teamTxt = (TextView) parentView.findViewById(R.id.descTv);
        CharSequence charSequence;
        String content = "<font color='#787878'>请选择礼物，助威</font><b><font color='#FFFFFF'>【" + teamName + "】</font></b>";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            charSequence = Html.fromHtml(content, Html.FROM_HTML_MODE_COMPACT);
        } else {
            charSequence = Html.fromHtml(content);
        }
        teamTxt.setText(charSequence);
        //teamTxt.setText("请选择礼物，助威" + teamName);
    }
}