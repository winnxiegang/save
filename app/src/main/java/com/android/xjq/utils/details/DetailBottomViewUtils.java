package com.android.xjq.utils.details;


import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.dialog.OnMyClickListener;
import com.android.banana.commlib.utils.LibAppUtil;
import com.android.banana.view.BadgeView;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.activity.message.WriteMySubjectActivity;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterCallback;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterHelper;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Duzaoqiu on 2016/9/13 14:28.
 * 详情页底部显示处理
 */
public class DetailBottomViewUtils implements SubjectMultiAdapterCallback {

    private ViewHolder viewHolder;

    private View bottomView;

    private String id;

    private SubjectMultiAdapterHelper mAdapterHelper;

    private RequestFailedListener  requestFailedListener;

    private int replyCount = 0;

    private int supportCount = 0;

    private boolean liked;

    private String objectType;

    private String objectId;

    private boolean isCommentOff;

    public void setRequestFailedListener(RequestFailedListener requestFailedListener) {
        this.requestFailedListener = requestFailedListener;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public void setCommentOff(boolean commentOff) {
        isCommentOff = commentOff;
    }

    public DetailBottomViewUtils() {

        mAdapterHelper = new SubjectMultiAdapterHelper(this);
    }

    public void setData(String id, int replyCount, int supportCount, boolean liked,
                        String objectType, String objectId, boolean isCommentOff) {
        this.id = id;
        this.replyCount = replyCount;
        this.supportCount = supportCount;
        this.liked = liked;
        this.objectType = objectType;
        this.objectId = objectId;
        this.isCommentOff = isCommentOff;
        if(bottomView!=null){
            notifyView();
        }
    }

    private OnMyClickListener replyLocationListener;

    public void setReplyLocationListener(OnMyClickListener replyLocationListener) {
        this.replyLocationListener = replyLocationListener;
    }

    public View getView(final Context context) {
        if (bottomView == null) {
            bottomView = View.inflate(XjqApplication.getContext(), R.layout.layout_details_bottom, null);
        }
        viewHolder = new ViewHolder(bottomView);

        viewHolder.replyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCommentOff) {
                    LibAppUtil.showTip(XjqApplication.getContext(), XjqApplication.getContext().getString(R.string.comment_off_tip));
                    return;
                }
                WriteMySubjectActivity.startWriteMySubjectActivity(context, WriteMySubjectActivity.WriteTypeEnum.WRITE_COMMENT, objectId, objectType);

            }
        });

        viewHolder.supportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked) {
                    mAdapterHelper.unLike(0, String.valueOf(id), objectType);
                } else {
                    mAdapterHelper.like(0, String.valueOf(id), objectType);
                }
            }
        });

        viewHolder.locationReplyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(replyLocationListener!=null){
                    replyLocationListener.onClick(v);
                }
            }
        });

        notifyView();

        return bottomView;
    }

    private void notifyView() {

        if (liked) {
            viewHolder.likeCountTv.setTextColor(XjqApplication.getContext().getResources().getColor(R.color.main_red1));
            viewHolder.supportImageIv.setImageResource(R.drawable.ic_support_red);
        } else {
            viewHolder.likeCountTv.setTextColor(XjqApplication.getContext().getResources().getColor(R.color.gray_text_color));
            viewHolder.supportImageIv.setImageResource(R.drawable.ic_support_black);
        }
        viewHolder.replyCountTv.setBadgeNum(replyCount);
        if (supportCount > 0) {
            viewHolder.likeCountTv.setText(String.valueOf(supportCount));
        } else {
            viewHolder.likeCountTv.setText("");
        }
    }

    @Override
    public void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest) {
        if (success){
            if (!setTopRequest) {
                liked = true;
                supportCount++;
                notifyView();
            }
        }else{
            requestFailedListener.requestFailed(jsonObject);
        }

    }

    @Override
    public void onUnLikeResult(JSONObject jsonObject, int position, boolean success) {
        if (success){
            liked = false;
            supportCount--;
            notifyView();
        }else{
            requestFailedListener.requestFailed(jsonObject);
        }
    }


    static class ViewHolder {
        @BindView(R.id.replyTv)
        TextView replyTv;
        @BindView(R.id.replyCountTv)
        BadgeView replyCountTv;
        @BindView(R.id.locationReplyLayout)
        FrameLayout locationReplyLayout;
        @BindView(R.id.supportImageIv)
        ImageView supportImageIv;
        @BindView(R.id.likeCountTv)
        TextView likeCountTv;
        @BindView(R.id.supportLayout)
        LinearLayout supportLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface RequestFailedListener{
        void requestFailed(JSONObject jsonObject);
    }
}
