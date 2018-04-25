package com.android.xjq.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.http.RequestFormBody;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.AnimationListUtil;
import com.android.banana.commlib.utils.DimensionUtils;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.jjx.sdk.utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.bean.draw.LuckyDrawParticipateConditionSimpleBean;
import com.android.xjq.bean.live.DecreeBean;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.XjqUrlEnum;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.android.xjq.utils.XjqUrlEnum.DECREE_PARTICIPATE;
import static com.android.xjq.utils.XjqUrlEnum.DECREE_QUERY;
import static com.android.xjq.utils.XjqUrlEnum.GET_DECREE_TOTAL_AMOUNT;

/**
 * Created by ajiao on 2018\3\10 0010.
 */

public class ShengZhiWindowPop implements OnHttpResponseListener {

    private ShengZhiWindowPop.Builder mBuilder;
    //    private Dialog mDialog;
    private PopupWindow mPopupWindow;
    private View mDialogView;
    private LuckyDrawParticipateConditionSimpleBean mDrawParticipateBean;
    private HttpRequestHelper mHttpRequestHelper;
    private Handler handler;
    private int mSzBgPaperW;
    @BindView(R.id.marqueeIv1)
    ImageView marqueeIv1;
    @BindView(R.id.marqueeIv2)
    ImageView marqueeIv2;
    @BindView(R.id.per_gift_txt)
    TextView perMoney;
    @BindView(R.id.sheng_zhi_title)
    TextView shengZhiTitle;
    @BindView(R.id.sheng_zhi_content)
    TextView shengZhiContent;
    //@BindView(R.id.start_anim_btn)
    //Button startAnimBtn;
    @BindView(R.id.decree_paper_bg)
    ImageView szBgPaper;
    @BindView(R.id.sheng_zhi_lay)
    RelativeLayout szBgLay;
    @BindView(R.id.sheng_zhi_left)
    ImageView szLeftBang;
    @BindView(R.id.sheng_zhi_right)
    ImageView szRightBang;
    @BindView(R.id.recover)
    ImageView coverLightShine;
    @BindView(R.id.red_hong_bao_top)
    ImageView redHongBaoTop;
    @BindView(R.id.red_hong_bao_bottom)
    ImageView redHongBaoBottom;
    @BindView(R.id.jie_zhi_img)
    ImageView jieZhiImg;
    @BindView(R.id.txt_lay)
    LinearLayout txtLay;

    private CountDownTimer mCountDownTimer;

    @OnClick(R.id.close_img)
    public void setOnClickClose() {
        AnimationListUtil.stopAnimation(R.drawable.anim_light_shine_sheng_zhi_bg);
        handler.removeCallbacksAndMessages(null);
        marqueeIv1.clearAnimation();
        marqueeIv1.setImageDrawable(null);
        marqueeIv2.clearAnimation();
        marqueeIv2.setImageDrawable(null);
        mHttpRequestHelper = null;
        mDialogView = null;
        mBuilder = null;
        coverLightShine.setImageDrawable(null);
        szLeftBang.setImageDrawable(null);
        szLeftBang = null;
        szRightBang.setImageDrawable(null);
        szRightBang = null;
        szBgPaper.setImageDrawable(null);
        szBgPaper = null;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mPopupWindow.dismiss();
        mPopupWindow = null;
    }

    private onFinishListener mListener;
    private static final long ANIM_DURATION = 8 * 1000;

    public ShengZhiWindowPop(ShengZhiWindowPop.Builder builder) {
        mBuilder = builder;
        handler = new Handler();
//        mDialog = new Dialog(mBuilder.getContext(), R.style.NormalDialogStyle);

        if (mBuilder.isLandscape()) {
            mDialogView = View.inflate(mBuilder.getContext(), R.layout.widget_dialog_sheng_zhi_hor, null);
        } else {
            mDialogView = View.inflate(mBuilder.getContext(), R.layout.widget_dialog_sheng_zhi_ver, null);
        }

//        mDialog.setContentView(mDialogView);
//        ButterKnife.bind(this, mDialogView);

        mPopupWindow = new PopupWindow(mDialogView, -1, -1, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ButterKnife.bind(this, mDialogView);
        mPopupWindow.setOutsideTouchable(mBuilder.isTouchOutside());
        mPopupWindow.setTouchable(true);

        mHttpRequestHelper = new HttpRequestHelper(builder.getContext(), this);
//        setDialogPosition();
//        initDialog();
        initPopupWindow();
        reqDecreeQuery();
    }

    private void startTranslateAnim(final View view, final long delayTime) {
        final TranslateAnimation translateAnimation =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f
                );
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(ANIM_DURATION);
        translateAnimation.setRepeatCount(Integer.MAX_VALUE);
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setInterpolator(new LinearInterpolator());
        //   translateAnimation.setStartOffset(delayTime);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setVisibility(View.VISIBLE);
                view.startAnimation(translateAnimation);
            }
        }, delayTime);
    }


    private void initDialog() {
//        mDialog.setCanceledOnTouchOutside(mBuilder.isTouchOutside());
    }

    private void initPopupWindow() {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeResource(mBuilder.getContext().getResources(), R.drawable.decree_paper, options);
        mSzBgPaperW = (options.outWidth);
        LogUtils.e("initPopupWindow", "options.outWidth=" + options.outWidth);
        szBgPaper.getLayoutParams().height = options.outHeight;
        szBgLay.getLayoutParams().height = options.outHeight;
        //mSzBgPaperW = DimensionUtils.getScreenWidth(mBuilder.context);
        startTranslateAnim(marqueeIv2, 0);
        startTranslateAnim(marqueeIv1, ANIM_DURATION / 2);
        alphaTranslate(txtLay);
        // 步骤1：设置属性数值的初始值 & 结束值
        ValueAnimator valueAnimator = ValueAnimator.ofInt(szBgLay.getLayoutParams().width, mSzBgPaperW - 50);
        // 步骤2：设置动画的播放各种属性
        valueAnimator.setDuration(2000);
        // 步骤3：将属性数值手动赋值给对象的属性:此处是将值赋给按钮的宽度
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (mBuilder != null) {
                    int currentValue = (Integer) animator.getAnimatedValue();
                    //szBgLay.getLayoutParams().width = currentValue;
                    //横竖屏切换会有适配问题，这里需要做区分处理。圣旨背景分辨率（916x398），加上透明边的分辨率（1242*890）。
                    int realW = mBuilder.isLandscape() ? 1020 : 888;
                    LogUtils.e("onAnimationUpdate", "realW = " + realW);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) szLeftBang.getLayoutParams();
                    layoutParams.rightMargin = (int) (currentValue * realW * 1f / 1242 / 2);
                    szLeftBang.setLayoutParams(layoutParams);

                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) szRightBang.getLayoutParams();
                    layoutParams2.leftMargin = (int) (currentValue * realW * 1f / 1242 / 2);
                    szRightBang.setLayoutParams(layoutParams2);
                    // 步骤4：刷新视图，即重新绘制，从而实现动画效果
                    szBgLay.requestLayout();
                    ClipDrawable clipDrawable = (ClipDrawable) szBgPaper.getDrawable();
                    float percent = currentValue * 1f / (mSzBgPaperW - 50);
                    int level = (int) (percent * 10000);
                    clipDrawable.setLevel(level - 500);
                    //圣旨全部展开后开始做帧动画
                    LogUtils.e("onAnimationUpdate: ", "currentValue = " + currentValue + ">>>> full =  "
                            + (mSzBgPaperW - 50) + ", level = " + level + ", percent =" + percent);

                    if (currentValue == mSzBgPaperW - 50) {
                        //去除圣旨轴和圣旨背景的间隙
                        layoutParams.rightMargin = (int) (currentValue * realW * 1f / 1242 / 2) - 10;
                        szLeftBang.setLayoutParams(layoutParams);
                        layoutParams2.leftMargin = (int) (currentValue * realW * 1f / 1242 / 2) - 5;
                        szRightBang.setLayoutParams(layoutParams2);

                        coverLightShine.setVisibility(View.VISIBLE);
                        AnimationListUtil.startAnimationList(R.drawable.anim_light_shine_sheng_zhi_bg, coverLightShine, null, new Runnable() {
                            @Override
                            public void run() {
                                coverLightShine.setVisibility(View.GONE);
                            }
                        }, 3);
                    }
                }
            }
        });
        valueAnimator.start();
        redHongBaoBottom.setVisibility(View.GONE);
        jieZhiImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redHongBaoBottom.setVisibility(View.VISIBLE);
                redHongBaoTop.animate().setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        super.onAnimationStart(animation);
                        redHongBaoTop.setVisibility(View.VISIBLE);
                    }
                });
                redHongBaoTop.animate().translationY(0).setDuration(0).start();
                int margin = (int) DimensionUtils.dpToPx(60, mBuilder.getContext());
                redHongBaoTop.animate().setInterpolator(new OvershootInterpolator(2)).translationY(margin).setDuration(300).start();
                reqDecreeParticipate();
            }
        });
    }


    public void alphaTranslate(View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
        objectAnimator.setDuration(6000);
        objectAnimator.start();
    }

    private void reqDecreeQuery() {
        XjqRequestContainer map = new XjqRequestContainer(DECREE_QUERY, true);
        //map.put("issueId", "4000637769321600000980058276");
        map.put("issueId", mBuilder.getIssueId());
        mHttpRequestHelper.startRequest(map);
    }

    private void reqDecreeParticipate() {
        /*issueId(期次id),
        participateConditionId(参与条件id),
        participateValue(参与值, 例如点一次送1个礼物是1，点一次送五个是5，本次项目写死为1),
        totalAmount(总金额，参与值 * 每个礼物单价后的总金额)，
        currencyAppCode(货币应用代码, GIFTCOIN(礼金)，POINTCOIN(香蕉币)，GOLDCOIN(金锭)),
        channelAreaId：频道空间id*/
        if (mDrawParticipateBean != null) {
            RequestFormBody map = new RequestFormBody(DECREE_PARTICIPATE, true);
            //map.put("issueId", "4000637769321600000980058276");
            map.put("issueId", mBuilder.getIssueId());
            map.put("participateConditionId", mDrawParticipateBean.id);
            map.put("participateValue", "1");
            map.put("totalAmount", mDrawParticipateBean.perPrice);
            map.put("currencyType", mDrawParticipateBean.defaultCurrencyType);
            map.put("channelAreaId", CurLiveInfo.channelAreaId);
            mHttpRequestHelper.startRequest(map);
        }
    }

    private void reqDecreeTotal() {
        XjqRequestContainer map = new XjqRequestContainer(GET_DECREE_TOTAL_AMOUNT, true);
        //map.put("issueId", "4000637769321600000980058276");
        map.put("issueId", mBuilder.getIssueId());
        mHttpRequestHelper.startRequest(map);
    }


    private void setDialogPosition() {
//        Window dialogWindow = mDialog.getWindow();
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        int screenHeight = DimensionUtils.getScreenHeight(mDialog.getContext());
//        int screenWidth = DimensionUtils.getScreenWidth(mDialog.getContext());
//        lp.height = screenHeight;
//        lp.width = screenWidth;
//        dialogWindow.setAttributes(lp);
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        if (mBuilder != null) {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
                case DECREE_QUERY:
                    LiveDrawInfoEntity liveDrawInfoEntity = new Gson().fromJson(jsonObject.toString(), LiveDrawInfoEntity.class);
                    mDrawParticipateBean = liveDrawInfoEntity.luckyDrawParticipateConditionSimple.get(0);
                    shengZhiTitle.setText(liveDrawInfoEntity.patronLoginName + "曰:");
                    shengZhiContent.setText(liveDrawInfoEntity.content);
                    perMoney.setText("(" + (int) mDrawParticipateBean.perPrice + "礼金" + ")");
                    startCountDownTimer(liveDrawInfoEntity.nowDate);
                    break;
                case GET_DECREE_TOTAL_AMOUNT:
                    //弹出红包
                    DecreeBean decreeBean = new Gson().fromJson(jsonObject.toString(), DecreeBean.class);
                    String moneyType = "";
                    double totalPrice = 0.00;
                    if (decreeBean.getTotalAmountSimple() != null && decreeBean.getTotalAmountSimple().size() > 0) {
                        DecreeBean.PrizeBean bean = decreeBean.getTotalAmountSimple().get(0);
                        moneyType = bean.getCurrencyCode();
                        totalPrice = bean.getTotalAmount();
                        if ((int) totalPrice == 0) {
                            //Toast.makeText(mBuilder.getContext(), "抱歉，您未抢到红包，手速再快点就能抢到啦", Toast.LENGTH_SHORT).show();
                            //mDialog.dismiss();
                            if (mBuilder.getOnFinishListener() != null) {
                                mBuilder.getOnFinishListener().onOpenRedPacketMoney(moneyType, totalPrice, false);
                            }
                        } else {
                            String toastStr = "恭喜您抢到了" + String.valueOf(totalPrice) + "礼金";
                            Toast.makeText(mBuilder.getContext(), toastStr, Toast.LENGTH_SHORT).show();
                            if (mBuilder.getOnFinishListener() != null) {
                                mBuilder.getOnFinishListener().onOpenRedPacketMoney(moneyType, totalPrice, true);
                            }
                        }
                    } else {
                        if (mBuilder.getOnFinishListener() != null) {
                            mBuilder.getOnFinishListener().onOpenRedPacketMoney(moneyType, totalPrice, false);
                        }
                        //mDialog.dismiss();
                        //Toast.makeText(mBuilder.getContext(), "抱歉，您未抢到红包，手速再快点就能抢到啦", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case DECREE_PARTICIPATE:
                    Log.i("yjj", "response DECREE_PARTICIPATE success");
                    break;
                default:
                    break;
            }
        }

    }

    private void startCountDownTimer(String nowDate) {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mCountDownTimer = new CountDownTimer(mBuilder.getCountDownTime(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                //关闭popWindow的时候也会掉这个
                LogUtils.i("yjj", "CountDownTimer onFinish");
                if (mBuilder != null) {
                    reqDecreeTotal();
                }
            }
        }.start();

    }


    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            ((BaseActivity) mBuilder.getContext()).operateErrorResponseMessage(jsonObject);
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

//    public void show() {
//        mDialog.show();
//    }

    public void dismiss() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        mPopupWindow.dismiss();
        mPopupWindow = null;
    }

    public PopupWindow getDialog() {
        return mPopupWindow;
    }

    public static class Builder {
        private Context context;
        private String titleStr;
        private int width;
        private int height;
        private boolean isTouchOutside = true;
        private boolean isLandscape = false;
        private String issueId;
        private onFinishListener mListener;
        private long countDownTime;

        public Builder(Context ctx) {
            context = ctx;
        }

        public ShengZhiWindowPop.Builder setTitle(String title) {
            titleStr = title;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public String getTitleStr() {
            return titleStr;
        }

        public ShengZhiWindowPop.Builder setTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }

        public ShengZhiWindowPop.Builder setOrientation(boolean islandscape) {
            isLandscape = islandscape;
            return this;
        }

        public ShengZhiWindowPop.Builder setDialogWidth(int width) {
            width = width;
            return this;
        }

        public ShengZhiWindowPop.Builder setDialogHeight(int height) {
            height = height;
            return this;
        }


        public ShengZhiWindowPop.Builder setReqIssueId(String issueid) {
            issueId = issueid;
            return this;
        }

        public String getIssueId() {
            return issueId;
        }

        public boolean isLandscape() {
            return isLandscape;
        }

        public ShengZhiWindowPop.Builder setOnFinishListener(onFinishListener listener) {
            mListener = listener;
            return this;
        }

        public onFinishListener getOnFinishListener() {
            return mListener;
        }

        public ShengZhiWindowPop.Builder setCountDownTime(long ctdTime) {
            countDownTime = ctdTime;
            return this;
        }

        public long getCountDownTime() {
            return countDownTime;
        }


        public Context getContext() {
            return context;
        }

        public ShengZhiWindowPop create() {
            ShengZhiWindowPop dialog = new ShengZhiWindowPop(this);
            return dialog;
        }


    }

    public interface onFinishListener {
        public void onOpenRedPacketMoney(String moneyType, double totalPrice, boolean success);
    }

    public void setOnFinishListener(onFinishListener listener) {
        mListener = listener;
    }

}
