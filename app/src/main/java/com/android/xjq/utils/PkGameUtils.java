package com.android.xjq.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.banana.commlib.game.ShowPopListSelectorView;
import com.android.banana.commlib.utils.toast.ToastUtil;
import com.android.banana.commlib.view.OnMyClickListener;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PkGameBoarInfoBean;
import com.android.xjq.bean.gamePK.PkOptionEntryBean;
import com.android.xjq.view.PkPortraitLinearLayout;

import java.util.List;

/**
 * Created by lingjiu on 2018/3/1.
 */

public class PkGameUtils {

    /**
     * 绑定 PK 对应的视图
     *
     * @param context
     * @param holder
     * @param infoBean
     * @param position
     */
    public static void bindViewHolder(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean, List<Integer> multipleList, int position, PurchasePkListener purchasePkListener) {
        int itemViewType = holder.getItemViewType();
        switch (itemViewType) {
            case R.layout.list_item_pk_completed:
                bindCompletedView(context, holder, infoBean, purchasePkListener);
                break;
            case R.layout.list_item_pk_pause:
                bindPauseView(context, holder, infoBean, purchasePkListener);
                break;
            case R.layout.list_item_pk_invalid:
                bindInvalidView(context, holder, infoBean);
                break;
            case R.layout.list_item_pk_progressing:
                bindProgressingView(context, holder, infoBean, multipleList, purchasePkListener);
                break;
        }
        holder.setText(R.id.themeTv, infoBean.title);
        holder.setText(R.id.leftPointTv, infoBean.optionOneEntry.optionName);
        holder.setText(R.id.rightPointTv, infoBean.optionTwoEntry.optionName);
    }

    private static void bindCompletedView(Context context, ViewHolder holder, final PkGameBoarInfoBean infoBean, final PurchasePkListener purchasePkListener) {
        bindCurrentUserGiftNumView(context, holder, infoBean, false);
        bindTotalUserGiftNumView(context, holder, infoBean, false);
        ImageView winTagIv = holder.getView(R.id.winTagIv);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) winTagIv.getLayoutParams();
        if (infoBean.isLeftWin()) {
            holder.setBackgroundRes(R.id.mainBgIv, R.drawable.icon_pk_left_win_bg);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            if (winTagIv.getRotation() % 360 == 0)
                winTagIv.setRotation(315);
        } else {
            holder.setBackgroundRes(R.id.mainBgIv, R.drawable.icon_pk_right_win_bg);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if ((winTagIv.getRotation() - 315) % 360 == 0)
                winTagIv.setRotation(0);
        }

        PkPortraitLinearLayout portraitLayout = holder.getView(R.id.portraitLayout);
        final List<PkOptionEntryBean.RankUserListBean> userList = infoBean.isLeftWin() ? infoBean.optionOneEntry.rankUserList :
                infoBean.optionTwoEntry.rankUserList;
        portraitLayout.setImageUrlList(userList, true);

        portraitLayout.setListener(new OnMyClickListener() {
            @Override
            public void onClick(View view, int position, Object o) {
                if (purchasePkListener != null)
                    purchasePkListener.showPersonCard(userList, position);
            }
        });
       /* portraitLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasePkListener != null)
                    purchasePkListener.showPurchaseList(infoBean);
            }
        });*/
    }

    private static void bindInvalidView(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean) {
        bindCurrentUserGiftNumView(context, holder, infoBean, true);
        bindTotalUserGiftNumView(context, holder, infoBean, true);
    }

    private static void bindPauseView(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean, PurchasePkListener purchasePkListener) {
        bindPortraitView(holder, infoBean, purchasePkListener);
        bindJoinPeopleView(context, holder, infoBean);
    }

    private static void bindProgressingView(final Context context, ViewHolder holder, final PkGameBoarInfoBean infoBean, List<Integer> multipleList, final PurchasePkListener purchasePkListener) {
        holder.setImageByUrl(context, R.id.leftGiftIv, infoBean.optionOneEntry.betFormImageUrl);
        holder.setImageByUrl(context, R.id.rightGiftIv, infoBean.optionTwoEntry.betFormImageUrl);
        bindCurrentUserGiftNumView(context, holder, infoBean, false);
        bindPortraitView(holder, infoBean, purchasePkListener);
        bindJoinPeopleView(context, holder, infoBean);

        final ShowPopListSelectorView rightNumSelectorView = holder.getView(R.id.rightNumSelectorView);
        rightNumSelectorView.setData(multipleList);
        final ShowPopListSelectorView leftNumSelectorView = holder.getView(R.id.leftNumSelectorView);
        leftNumSelectorView.setData(multipleList);
        holder.setOnClickListener(R.id.shareIv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showLong(context, "分享");
                if (purchasePkListener != null)
                    purchasePkListener.sharePk(infoBean);
            }
        });
        holder.setOnClickListener(R.id.leftGiftIv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasePkListener != null)
                    purchasePkListener.purchasePk(infoBean.optionOneEntry, leftNumSelectorView.getCurrentNum(), PurchasePkListener.OPTION_ONE);
            }
        });
        holder.setOnClickListener(R.id.rightGiftIv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasePkListener != null)
                    purchasePkListener.purchasePk(infoBean.optionTwoEntry, rightNumSelectorView.getCurrentNum(), PurchasePkListener.OPTION_TWO);
            }
        });
    }

    private static void bindJoinPeopleView(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean) {
        holder.setText(R.id.leftJoinPeopleNumTv, String.format(context.getString(R.string.pk_total_join_num),
                String.valueOf(infoBean.optionOneEntry.totalPlayUserCount)));
        holder.setText(R.id.rightJoinPeopleNumTv, String.format(context.getString(R.string.pk_total_join_num),
                String.valueOf(infoBean.optionTwoEntry.totalPlayUserCount)));
    }

    private static void bindPortraitView(ViewHolder holder, final PkGameBoarInfoBean infoBean, final PurchasePkListener purchasePkListener) {
        PkPortraitLinearLayout leftPortraitView = holder.getView(R.id.leftPortraitLayout);
        leftPortraitView.setImageUrlList(infoBean.optionOneEntry.rankUserList);
        PkPortraitLinearLayout rightPortraitView = holder.getView(R.id.rightPortraitLayout);
        rightPortraitView.setImageUrlList(infoBean.optionTwoEntry.rankUserList);

        leftPortraitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasePkListener != null)
                    purchasePkListener.showPurchaseList(infoBean.optionOneEntry);
            }
        });

        rightPortraitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (purchasePkListener != null)
                    purchasePkListener.showPurchaseList(infoBean.optionTwoEntry);
            }
        });
    }


    private static void bindTotalUserGiftNumView(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean, boolean invalid) {
        if (infoBean.optionOneEntry == null || infoBean.optionOneEntry.totalMultiple == 0) {
            holder.setViewVisibility(R.id.leftTotalGiftNumTv, View.GONE);
        } else {
            holder.setViewVisibility(R.id.leftTotalGiftNumTv, View.VISIBLE);
            holder.setText(R.id.leftTotalGiftNumTv, String.format(invalid ? context.getString(R.string.pk_total_multiple_invalid) : context.getString(R.string.pk_total_multiple),
                    String.valueOf(infoBean.optionOneEntry.totalMultiple), String.valueOf(infoBean.optionOneEntry.betFormName)));
        }
        if (infoBean.optionTwoEntry == null || infoBean.optionTwoEntry.totalMultiple == 0) {
            holder.setViewVisibility(R.id.rightTotalGiftNumTv, View.GONE);
        } else {
            holder.setViewVisibility(R.id.rightTotalGiftNumTv, View.VISIBLE);
            holder.setText(R.id.rightTotalGiftNumTv, String.format(invalid ? context.getString(R.string.pk_total_multiple_invalid) : context.getString(R.string.pk_total_multiple),
                    String.valueOf(infoBean.optionTwoEntry.totalMultiple), String.valueOf(infoBean.optionTwoEntry.betFormName)));
        }
    }

    private static void bindCurrentUserGiftNumView(Context context, ViewHolder holder, PkGameBoarInfoBean infoBean, boolean invalid) {
        if (infoBean.optionOneEntry == null || infoBean.optionOneEntry.currentUserTotalMultiple == 0) {
            holder.setViewVisibility(R.id.leftGiftNumTv, View.GONE);
        } else {
            holder.setViewVisibility(R.id.leftGiftNumTv, View.VISIBLE);
            holder.setText(R.id.leftGiftNumTv, String.format(invalid ? context.getString(R.string.pk_multiple_invalid) : context.getString(R.string.pk_multiple),
                    String.valueOf(infoBean.optionOneEntry.currentUserTotalMultiple), String.valueOf(infoBean.optionOneEntry.betFormName)));
        }
        if (infoBean.optionTwoEntry == null || infoBean.optionTwoEntry.currentUserTotalMultiple == 0) {
            holder.setViewVisibility(R.id.rightGiftNumTv, View.GONE);
        } else {
            holder.setViewVisibility(R.id.rightGiftNumTv, View.VISIBLE);
            holder.setText(R.id.rightGiftNumTv, String.format(invalid ? context.getString(R.string.pk_multiple_invalid) : context.getString(R.string.pk_multiple),
                    String.valueOf(infoBean.optionTwoEntry.currentUserTotalMultiple), String.valueOf(infoBean.optionTwoEntry.betFormName)));
        }
    }

    public interface PurchasePkListener {
        String OPTION_ONE = "OPTION_ONE";
        String OPTION_TWO = "OPTION_TWO";

        void purchasePk(PkOptionEntryBean infoBean, int count, String option);

        void sharePk(PkGameBoarInfoBean infoBean);

        void showPurchaseList(PkOptionEntryBean optionEntryBean);

        void showPersonCard(List<PkOptionEntryBean.RankUserListBean> userList, int position);
    }
}
