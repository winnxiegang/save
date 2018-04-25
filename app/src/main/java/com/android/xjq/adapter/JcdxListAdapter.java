package com.android.xjq.adapter;

import android.content.Context;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.bean.SubjectsComposeBean;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterCallback;
import com.android.xjq.controller.maincontroller.SubjectMultiAdapterHelper;
import com.android.xjq.model.SubjectObjectType;
import com.android.xjq.utils.SubjectUtils;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/12/4.
 * <p>
 * 首页精彩大学列表
 */

public class JcdxListAdapter extends MultiTypeSupportAdapter<SubjectsComposeBean> implements SubjectMultiAdapterCallback {
    private SubjectMultiAdapterHelper mAdapterHelper;
    private HashMap<String, List<SubjectTag>> mSubjectTagMap;

    public JcdxListAdapter(Context context, List<SubjectsComposeBean> list, HashMap<String, List<SubjectTag>> subjectTagMap, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
        mSubjectTagMap = subjectTagMap;
        mAdapterHelper = new SubjectMultiAdapterHelper(this);
    }


    @Override
    public void onBindNormalHolder(ViewHolder holder, final SubjectsComposeBean composeBean, int position) {
        int itemViewType = holder.getItemViewType();

        setNormalItemView(holder, composeBean, position);
        switch (itemViewType) {
            case R.layout.layout_subject_normal:
                //什么都没插入 只有标题摘要 基础信息
                break;
            case R.layout.layout_subject_item_artical:
                //精彩大学类型
                SubjectUtils.setArticleItemView(mContext, holder, composeBean, position);
                break;
            case R.layout.layout_subject_item_photos:
                SubjectUtils.setPhotoItemView(mContext, holder, composeBean, position, true);
                holder.setOnClickListener(R.id.photo_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FullScreenImageGalleryActivity.startFullScreenImageGalleryActivity(mContext, (ArrayList<String>) composeBean.properties.midImageUrl, 0);
                    }
                });
                break;
        }
    }

    //什么都没插入 只有标题摘要 基础信息
    private void setNormalItemView(ViewHolder holder, final SubjectsComposeBean composeBean, final int position) {
        SubjectUtils.setNormalSubjectItemView(mContext, holder, composeBean, mSubjectTagMap, composeBean.subjectId);
        holder.setOnClickListener(R.id.item_support_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeBean.liked) {
                    mAdapterHelper.unLike(position, composeBean.subjectId, SubjectObjectType.SUBJECT);
                } else {
                    mAdapterHelper.like(position, composeBean.subjectId, SubjectObjectType.SUBJECT);
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        SubjectsComposeBean composeBean = mDatas.get(position);
        SubjectDetailActivity.startSubjectDetailActivity((BaseActivity) mContext, composeBean.subjectId);
//        DynamicDetailsActivity.startDynamicDetailsActivity((BaseActivity) mContext, composeBean.subjectId);
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
            SubjectsComposeBean composeBean = mDatas.get(position);
            composeBean.liked = true;
            composeBean.likeCount++;
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

        SubjectsComposeBean composeBean = mDatas.get(position);
        composeBean.liked = false;
        composeBean.likeCount--;
        notifyItemChanged(position);
    }
}
