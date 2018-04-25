package com.android.xjq.controller.maincontroller;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.MainActivity;
import com.android.xjq.activity.NewsDetailActivity;
import com.android.xjq.bean.NewsInfoBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by qiaomu on 2017/11/29.
 */

public class NewsAdapter extends MultiTypeSupportAdapter<NewsInfoBean.NewsInfo> implements SubjectMultiAdapterCallback {
    private SubjectMultiAdapterHelper mAdapterHelper;

    public NewsAdapter(Context context, List<NewsInfoBean.NewsInfo> list, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
        mAdapterHelper = new SubjectMultiAdapterHelper(this);
    }

    @Override
    public void onBindNormalHolder(final ViewHolder holder, final NewsInfoBean.NewsInfo newsInfo, final int position) {
        TextView itemTitleTv = holder.getView(R.id.item_title);
        itemTitleTv.setText(newsInfo.title);//标题
        itemTitleTv.setMaxLines(TextUtils.isEmpty(newsInfo.summary) ? 2 : 1);
        holder.setText(R.id.item_summary, newsInfo.summary)//摘要
                //.setViewVisibility(R.id.item_author, View.GONE)//作者
                .setText(R.id.item_support_tv, String.valueOf(newsInfo.likeCount))//点赞数
                .setTextColor(R.id.item_support_tv, newsInfo.liked ? ContextCompat.getColor(mContext, R.color.main_red1) : ContextCompat.getColor(mContext, R.color.subject_footer_gray))
                //.setImageResource(R.id.item_support_iv, newsInfo.liked ? R.drawable.icon_red_support : R.drawable.icon_gray_support)
                .setText(R.id.item_comment, String.valueOf(newsInfo.replyCount))//评论数
                //.setText(R.id.item_date, TimeUtils.formatTime(TimeUtils.getCurrentTime(), newsInfo.gmtPublish))//发布时间
                .setImageByUrl(mContext, R.id.item_news_iv, newsInfo.imageUrl, R.drawable.image_empty)//资讯图片
                .setOnClickListener(R.id.item_support_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newsInfo.liked) {
                            mAdapterHelper.unLike(position, String.valueOf(newsInfo.id), "CMS_NEWS");
                        } else {
                            mAdapterHelper.like(position, String.valueOf(newsInfo.id), "CMS_NEWS");
                        }
                    }
                });

    }

    @Override
    public void onItemClick(View view, int position) {
        NewsDetailActivity.startNewsDetailActivity((MainActivity) mContext, String.valueOf(mDatas.get(position).id));
    }

    @Override
    public void onLikeOrSetTopResult(JSONObject jsonObject, int position, boolean success, boolean setTopRequest) {
        if (!success) {
            try {
                getActivity().operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        if (!setTopRequest) {
            NewsInfoBean.NewsInfo newsInfo = mDatas.get(position);
            newsInfo.liked = true;
            newsInfo.likeCount++;
            notifyItemChanged(position);
        }

    }

    @Override
    public void onUnLikeResult(JSONObject jsonObject, int position, boolean success) {
        if (!success) {
            try {
                getActivity().operateErrorResponseMessage(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return;
        }
        NewsInfoBean.NewsInfo newsInfo = mDatas.get(position);
        newsInfo.liked = false;
        newsInfo.likeCount--;
        notifyItemChanged(position);
    }
}
