package com.android.xjq.activity.message.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.android.banana.commlib.base.MyBaseAdapter;
import com.android.xjq.R;
import com.android.xjq.bean.message.SystemNotifyBean;
import com.android.xjq.controller.message.CouponMessageViewController;
import com.android.xjq.controller.message.CouponViewController;
import com.android.xjq.controller.message.DefaultShowController;
import com.android.xjq.controller.message.SpeechViewController;
import com.android.xjq.controller.message.SystemMessageViewController;
import com.android.xjq.model.message.SystemMessageSubTypeEnum;
import com.android.xjq.model.message.SystemMessageTypeEnum;

import java.util.List;

/**
 * Created by zhouyi on 2016/6/8 10:49.
 */
public class MessageNotifyAdapter extends MyBaseAdapter<SystemNotifyBean.NoticesBean> {

    private CouponViewController couponViewController;

    private DefaultShowController defaultShowController;

    private SystemMessageViewController systemMessageViewController;

    private CouponMessageViewController couponMessageViewController;

    private SpeechViewController speechViewController;


    public MessageNotifyAdapter(Context context, List<SystemNotifyBean.NoticesBean> list) {

       super(context, list);

        couponViewController = new CouponViewController(context, this);

        defaultShowController = new DefaultShowController(context);

        systemMessageViewController = new SystemMessageViewController(context);

        couponMessageViewController = new CouponMessageViewController(context);

        speechViewController = new SpeechViewController(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            convertView = layoutInflater.inflate(R.layout.item_system_notify, null);

            holder = new ViewHolder(convertView);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        setItemView(position, holder);

        return convertView;

    }

    private void setItemView(int position, ViewHolder holder) {

        SystemNotifyBean.NoticesBean bean = list.get(position);

        holder.defaultShowViewHolder.defaultShowLayout.setVisibility(View.GONE);

        holder.couponShowViewHolder.couponLayout.setVisibility(View.GONE);

        holder.systemMessageViewHolder.systemMessageShowLayout.setVisibility(View.GONE);

        holder.couponMessageViewHolder.couponNotifyLayout.setVisibility(View.GONE);

        holder.speechShowViewHolder.speechMessageNotify.setVisibility(View.GONE);

        switch (SystemMessageTypeEnum.saveValueOf(bean.getMsgType().getName())) {
            case COUPON:
                setCouponLayout(position, holder);
                break;
            case SYSTEM:
                setSystemLayout(position, holder);
                break;
            default:
                defaultShowController.setDefaultShow(holder.defaultShowViewHolder, bean);
                break;
        }
    }

    private void setSystemLayout(int position, ViewHolder holder) {

        SystemNotifyBean.NoticesBean bean = list.get(position);
        switch (SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName())) {

            case LT_FOLLOWER_CANCEL:
            case LT_PURCHASE_NEAR_STOP_SELL:
            case ARTICLE_SELECTED_NOTICE_USER:
            case ARTICLE_SET_ETITLE_NOTICE_USER:
            case ARTICLE_SET_TOP_NOTICE_USER:
            case LT_FOLLOWER_FAIL_SOLD:
            case CHANNEL_AREA_LIVE_NOTICE:
                systemMessageViewController.setSystemMessageShow(holder.systemMessageViewHolder, bean);
                break;
            case ARTICLE_SELECTED_UNDO_NOTICE_USER:
            case SUBJECT_DELETE_NOTICE_TO_USER:
            case COMMENT_DELETE_NOTICE_TO_USER:
            case USER_INFO_AUDIT_APPLY_NOTICE_TO_USER:
            case USER_FORBIDDEN_ACTION_NOTICE:
                speechViewController.setSpeechViewShow(holder.speechShowViewHolder, bean);
                break;

            default:
                defaultShowController.setDefaultShow(holder.defaultShowViewHolder, bean);
                break;
        }

    }

    private void setCouponLayout(int position, ViewHolder holder) {

        SystemNotifyBean.NoticesBean bean = list.get(position);

        switch (SystemMessageSubTypeEnum.saveValueOf(bean.getMsgSubType().getName())) {
            case DIRECTED_COUPON:
                couponViewController.setCouponView(holder.couponShowViewHolder, bean);
                break;
            case FOLLOW_USER_CAN_FETCH:
            case COPY_PROJECT_CAN_FETCH:
            case USER_GUESS_PRIZE_NOTICE:
                couponMessageViewController.setDefaultShow(holder.couponMessageViewHolder, bean);
                break;
            default:
                defaultShowController.setDefaultShow(holder.defaultShowViewHolder, bean);
                break;
        }
    }

    static class ViewHolder {

        CouponViewController.ViewHolder couponShowViewHolder;

        DefaultShowController.ViewHolder defaultShowViewHolder;

        SystemMessageViewController.ViewHolder systemMessageViewHolder;

        CouponMessageViewController.ViewHolder couponMessageViewHolder;

        SpeechViewController.ViewHolder speechShowViewHolder;

        ViewHolder(View view) {

            couponShowViewHolder = new CouponViewController.ViewHolder(view);

            defaultShowViewHolder = new DefaultShowController.ViewHolder(view);

            systemMessageViewHolder = new SystemMessageViewController.ViewHolder(view);

            couponMessageViewHolder = new CouponMessageViewController.ViewHolder(view);

            speechShowViewHolder = new SpeechViewController.ViewHolder(view);
        }
    }
}
