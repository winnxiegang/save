package com.android.xjq.controller.draw;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.banana.commlib.game.ShowPopListSelectorView;
import com.android.banana.commlib.utils.SubjectMedalEnum;
import com.android.banana.commlib.utils.TimeUtils;
import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.CountdownTextView;
import com.android.banana.commlib.view.MedalLayout;
import com.android.xjq.R;
import com.android.xjq.bean.UserMedalLevelBean;
import com.android.xjq.bean.draw.IssuePrizeItemListBean;
import com.android.xjq.bean.draw.LiveDrawInfoEntity;
import com.android.xjq.bean.draw.LuckyDrawParticipateConditionSimpleBean;
import com.android.xjq.model.gift.PayType;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by lingjiu on 2018/3/8.
 */

public class DrawJoinController extends BaseController4JCZJ<BaseActivity> implements View.OnClickListener {

    private CircleImageView hostPortraitIv;
    private TextView hostNameTv;
    private TextView selfSendNumTv;
    private TextView joinMemoTv;
    private FrameLayout sendGiftBtn;
    private TextView joinTotalNumTv;
    private ListView prizeItemLv;
    private int userParticipateCount;
    private ImageView giftIv;
    private CountdownTextView countdownTv;
    private List<IssuePrizeItemListBean> issuePrizeItemList;
    private DrawCallback drawCallback;
    private TextView memoTv;
    private ShowPopListSelectorView multipleView;
    private MedalLayout medalLayout;
    private Animation btnAnim;

    public DrawJoinController(DrawCallback drawCallback) {
        this.drawCallback = drawCallback;
    }

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_join_lucky_draw);
    }

    @Override
    public void onSetUpView() {
        medalLayout = findViewOfId(R.id.medalLayout);
        hostPortraitIv = findViewOfId(R.id.hostPortraitIv);
        hostNameTv = findViewOfId(R.id.hostNameTv);
        prizeItemLv = findViewOfId(R.id.prizeItemLv);
        selfSendNumTv = findViewOfId(R.id.selfSendNumTv);
        joinMemoTv = findViewOfId(R.id.joinMemoTv);
        sendGiftBtn = findViewOfId(R.id.sendGiftBtn);
        joinTotalNumTv = findViewOfId(R.id.joinTotalNumTv);
        giftIv = findViewOfId(R.id.giftIv);
        countdownTv = findViewOfId(R.id.countdownTv);
        memoTv = findViewOfId(R.id.memoTv);
        multipleView = findViewOfId(R.id.multipleView);
        sendGiftBtn.setOnClickListener(this);
        btnAnim = AnimationUtils.loadAnimation(getContext(), com.android.banana.R.anim.rocket_button_click);
        countdownTv.setOnCountdownListener(new CountdownTextView.OnCountdownListener() {
            @Override
            public void countdownDuring(long countdownTime) {
            }

            @Override
            public void countdownEnd() {
                countdownTv.setVisibility(View.GONE);
            }
        });
    }

    public void setDrawBaseInfo(String gmtEndParticipate, LiveDrawInfoEntity liveDrawInfoEntity) {
        if (selfSendNumTv == null)
            return;
        List<LuckyDrawParticipateConditionSimpleBean> drawParticipateSimpleBeanList = liveDrawInfoEntity.luckyDrawParticipateConditionSimple;
        LuckyDrawParticipateConditionSimpleBean drawParticipateSimpleBean = drawParticipateSimpleBeanList.get(0);
        PicUtils.load(context.getApplicationContext(), hostPortraitIv, liveDrawInfoEntity.patronUrl);
        hostNameTv.setText(liveDrawInfoEntity.patronLoginName);
        userParticipateCount = liveDrawInfoEntity.userParticipateCount;
        selfSendNumTv.setText(String.format(context.getString(R.string.self_send_gift_num), String.valueOf(userParticipateCount)));
        joinTotalNumTv.setText(String.format(context.getString(R.string.prized_join_people_num), String.valueOf(liveDrawInfoEntity.participateCount)));
        PicUtils.load(context.getApplicationContext(), giftIv, liveDrawInfoEntity.giftUrl);
        //赠送[啤酒](2礼金/个)夺好礼,点击越快,中奖几率越高
        String content = "<font color='#BF8D74'>赠送</font>" +
                "<font color='#F0885F'>[" + liveDrawInfoEntity.giftName + "]" +
                "(" + drawParticipateSimpleBean.perPrice + PayType.valueOf(drawParticipateSimpleBean.defaultCurrencyType).getMessage() + "/个)</font>" +
                "<br/><font color='#BF8D74'>参与抽奖</font>";
        joinMemoTv.setText(Html.fromHtml(content));

        //礼物
        issuePrizeItemList = liveDrawInfoEntity.issuePrizeItemList;
        prizeItemLv.setAdapter(new PrizeItemAdapter(context));

        if (TextUtils.equals(liveDrawInfoEntity.awardType, LiveDrawInfoEntity.AwardType.BY_TIME)) {
            long timeDiff = TimeUtils.dateSubDate(gmtEndParticipate, liveDrawInfoEntity.nowDate);
            countdownTv.setVisibility(View.VISIBLE);
            countdownTv.start(timeDiff, "%1$s");
            memoTv.setText("计时结束后开奖,参与次数越多,中奖几率越高");
        } else {
            countdownTv.setVisibility(View.GONE);
            memoTv.setText("等待主播开奖,参与次数越多,中奖几率越高");
        }

        multipleView.setData(liveDrawInfoEntity.multipleList);

        if (liveDrawInfoEntity.userMedalLevelList != null) {
            for (UserMedalLevelBean userMedalLevelBean : liveDrawInfoEntity.userMedalLevelList) {
                medalLayout.addMedal(SubjectMedalEnum.getMedalResourceId(context,
                        userMedalLevelBean.medalConfigCode, userMedalLevelBean.currentMedalLevelConfigCode));
            }
        }
    }

    public void sendGiftSuccess() {
        if (selfSendNumTv == null)
            return;
        userParticipateCount += multipleView.getCurrentNum();
        selfSendNumTv.setText(String.format(context.getString(R.string.self_send_gift_num), String.valueOf(userParticipateCount)));
    }

    @Override
    public void onDestroy() {
        if (countdownTv != null) countdownTv.cancel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendGiftBtn:
                sendGiftBtn.clearAnimation();
                sendGiftBtn.startAnimation(btnAnim);
                drawCallback.sendGift(multipleView.getCurrentNum());
                break;
        }
    }


    class PrizeItemAdapter extends MyBaseAdapter {

        public PrizeItemAdapter(Context context) {
            super(context);
        }

        @Override
        public int getCount() {
            return issuePrizeItemList == null ? 0 : issuePrizeItemList.size();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            IssuePrizeItemListBean bean = issuePrizeItemList.get(i);
            convertView = View.inflate(context, R.layout.item_prize_item_listview, null);
            ((TextView) convertView.findViewById(R.id.prizeTv)).setText(bean.prizeItemTypeMessage + "    " + bean.prizeItemName);
            ((TextView) convertView.findViewById(R.id.prizeNumTv)).setText(String.format(context.getString(R.string.total_num), bean.totalCount));
            return convertView;
        }
    }
}
