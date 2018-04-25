package com.android.xjq.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.OnMyClickListener;
import com.android.xjq.R;
import com.android.xjq.bean.gamePK.PkOptionEntryBean;

import java.util.List;

/**
 * Created by lingjiu on 2018/2/28.
 */

public class PkPortraitLinearLayout extends LinearLayout {

    private int mHeight;
    private int mWidht;
    private int childViewCount = 5;

    public PkPortraitLinearLayout(Context context) {
        super(context);
    }

    public PkPortraitLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PkPortraitLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        for (int i = 0; i < childViewCount; i++) {
            final View view = View.inflate(getContext(), R.layout.layout_pk_portrait, null);
            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(view, (Integer) v.getTag(), null);
                    }
                }
            });
            addView(view);
        }
    }


    public void setListener(OnMyClickListener listener) {
        this.listener = listener;
    }

    private OnMyClickListener listener;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = getMeasuredHeight();
        mWidht = getMeasuredWidth();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    public void setImageUrlList(List<PkOptionEntryBean.RankUserListBean> list) {
        setImageUrlList(list, false);
    }

    public void setImageUrlList(List<PkOptionEntryBean.RankUserListBean> list, boolean isVisibleName) {
        for (int i = 0; i < childViewCount; i++) {
            View child = getChildAt(i);
            if (list != null && i < list.size()) {
                child.setVisibility(View.VISIBLE);
                ImageView portraitIv = getView(child, R.id.portraitIv);
                TextView tagTv = getView(child, R.id.tagTv);
                TextView userNameTv = getView(child, R.id.userNameTv);
                PkOptionEntryBean.RankUserListBean bean = list.get(i);
                if (isVisibleName && i == 0) {
                    userNameTv.setText(bean.getLoginName());
                    userNameTv.setVisibility(View.VISIBLE);
                } else {
                    userNameTv.setVisibility(View.GONE);
                }
                tagTv.setVisibility(bean.isGuest() ? View.VISIBLE : View.GONE);
                PicUtils.load(getContext().getApplicationContext(), portraitIv, bean.getUserLogoUrl());
            } else {
                child.setVisibility(View.GONE);
            }
        }
    }

    private <T extends View> T getView(View parent, int viewId) {
        return (T) parent.findViewById(viewId);
    }

}
