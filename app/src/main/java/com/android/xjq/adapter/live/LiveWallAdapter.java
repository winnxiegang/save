package com.android.xjq.adapter.live;

import android.content.Context;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.wall.LiveWallDetailActivity;
import com.android.xjq.bean.liveWall.TopicSimpleListBean;

import java.util.List;

/**
 * Created by lingjiu on 2018/2/23.
 */

public class LiveWallAdapter extends MultiTypeSupportAdapter<TopicSimpleListBean> {


    public LiveWallAdapter(Context context, List list, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
    }

    @Override
    public void onBindNormalHolder(ViewHolder holder, TopicSimpleListBean topicSimpleListBean, int position) {
        TopicSimpleListBean.PropertiesBean properties = topicSimpleListBean.properties;
        holder.setImageByFresco(R.id.coverIv, properties == null ? null : properties.videoFirstFrameImageUrl, false);
        holder.setText(R.id.titleTv, topicSimpleListBean.summary);
        holder.setText(R.id.userNameTv, topicSimpleListBean.loginName);
        holder.setImageByUrl(mContext, R.id.portraitIv, topicSimpleListBean.userLogoUrl, R.drawable.user_default_logo);
    }

    @Override
    public void onItemClick(View view, int position) {
        TopicSimpleListBean topicSimpleListBean = mDatas.get(position);
        LiveWallDetailActivity.startLiveWallDetailActivity((BaseActivity) mContext, topicSimpleListBean.subjectId);
    }
}
