package com.android.xjq.controller.draw;

import android.text.Html;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.base.BaseController4JCZJ;
import com.android.banana.commlib.view.VerticalScrollTextView2;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.bean.draw.DrawRoundInfoEntity;
import com.android.xjq.bean.draw.IssueStatusType;
import com.android.xjq.bean.draw.LuckyDrawOpenUserEntity;
import com.android.xjq.view.wheelview.WheelView;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/8.
 */

public class DrawProgressingController extends BaseController4JCZJ<BaseActivity> {


    private TextView prizeNameTv;
    private TextView prizeNumTv;
    private VerticalScrollTextView2 prizedUserNameTv;
    private WheelView wheelView;
    //当前抽奖用户集
    private List<LuckyDrawOpenUserEntity.PrizeUser> prizeUsers;

    @Override
    public void setContentView(ViewGroup parent) {
        setContentView(parent, R.layout.layout_draw_progressing);
    }

    @Override
    public void onSetUpView() {
        createCircularReveal(true);
        prizeNameTv = findViewOfId(R.id.prizeNameTv);
        prizeNumTv = findViewOfId(R.id.prizeNumTv);
        prizedUserNameTv = findViewOfId(R.id.prizedUserNameTv);
        wheelView = findViewOfId(R.id.wheelView);
    }

    public void setUserData(LuckyDrawOpenUserEntity luckyDrawOpenUserEntity) {
        luckyDrawOpenUserEntity.operatorData();
        prizeUsers = luckyDrawOpenUserEntity.prizeUsers;
        wheelView.setData(prizeUsers);
        wheelView.startScrolling();
    }


    public void setDrawRoundInfo(DrawRoundInfoEntity drawRoundInfoEntity) {
        int prizedPosition = 0;
        DrawRoundInfoEntity.LuckyDrawRoundSimple currentRound = drawRoundInfoEntity.currentRound;
        if (currentRound != null && currentRound.issuePrizeItem != null) {
            String prizeMemo = "<font color='#999999'>当前抽取" + currentRound.issuePrizeItem.prizeItemTypeMessage + "</font>" +
                    "<font color='#BF8D74'>" + currentRound.issuePrizeItem.prizeItemName + "</font>";
            prizeNameTv.setText(Html.fromHtml(prizeMemo));
            prizeNumTv.setText(String.format(context.getString(R.string.total_num), currentRound.issuePrizeItem.totalCount));

            for (int i = 0; i < prizeUsers.size(); i++) {
                if (TextUtils.equals(prizeUsers.get(i).userId, currentRound.prizedUserId)) {
                    prizedPosition = i;
                    break;
                }
            }

        }
        List<DrawRoundInfoEntity.LuckyDrawRoundSimple> prizedRoundList = drawRoundInfoEntity.prizedRoundList;
        if (prizedRoundList != null && prizedRoundList.size() > 0) {
            prizedUserNameTv.addMsg(prizedRoundList);
            //prizedUserNameTv.setText(content);
        }

        String statusName = currentRound == null || currentRound.status == null ? null : currentRound.status.getName();
        if (TextUtils.equals(statusName, IssueStatusType.DRAWING) && !wheelView.isScrolling()) {
            wheelView.startScrolling();
            LogUtils.e("LuckyDraw", "抽奖中------");
        } else if (TextUtils.equals(statusName, IssueStatusType.FINISH) && wheelView.isScrolling()) {
            wheelView.stopToPosition(prizedPosition);
            LogUtils.e("LuckyDraw", "抽奖结果------" + prizeUsers.get(prizedPosition).userName);
        }
    }
}
