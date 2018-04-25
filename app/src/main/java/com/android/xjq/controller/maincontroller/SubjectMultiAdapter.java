package com.android.xjq.controller.maincontroller;

import android.content.Context;
import android.view.View;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupport;
import com.android.banana.pullrecycler.multisupport.MultiTypeSupportAdapter;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.xjq.R;
import com.android.xjq.activity.NewsDetailActivity;
import com.android.xjq.activity.SubjectDetailActivity;
import com.android.xjq.bean.SubjectTag;
import com.android.xjq.bean.SubjectsComposeBean;
import com.android.xjq.utils.SubjectUtils;
import com.etiennelawlor.imagegallery.library.fullscreen.FullScreenImageGalleryActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by qiaomu on 2017/11/28.
 */

public class SubjectMultiAdapter extends MultiTypeSupportAdapter<SubjectsComposeBean> implements SubjectMultiAdapterCallback {
    private SubjectMultiAdapterHelper mAdapterHelper;
    private HashMap<String, List<SubjectTag>> mSubjectTagMap;


    public SubjectMultiAdapter(Context context, List list, int layoutRes, MultiTypeSupport typeSupport) {
        super(context, list, layoutRes, typeSupport);
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
                //首页精彩大学类型
                SubjectUtils.setArticleItemView(mContext, holder, composeBean, position);
                break;
            case R.layout.layout_subject_item_photos:
                SubjectUtils.setPhotoItemView(mContext, holder, composeBean, position, false);
                holder.setOnClickListener(R.id.photo_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<String> midSubjectPictureUrls = composeBean.pictureMaterialClientSimple == null ? null : composeBean.pictureMaterialClientSimple.midSubjectPictureUrls;
                        FullScreenImageGalleryActivity.startFullScreenImageGalleryActivity(mContext, midSubjectPictureUrls, 0);
                    }
                });
                break;
            case R.layout.layout_subject_item_news:
                //首页新闻资讯类
               // holder.setViewVisibility(R.id.item_author, View.GONE);
                String url = composeBean.cmsSubject == null ? "" : composeBean.cmsSubject.url;
                holder.setImageByUrl(mContext, R.id.item_news_iv, url, R.drawable.icon_small_news_default);//资讯图片
                break;
        }

    }

    //什么都没插入 只有标题摘要 基础信息
    private void setNormalItemView(ViewHolder holder, final SubjectsComposeBean composeBean, final int position) {
        SubjectUtils.setNormalSubjectItemView(mContext, holder, composeBean, mSubjectTagMap, String.valueOf(composeBean.thirdObjectId));
        holder.setOnClickListener(R.id.item_support_tv, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (composeBean.liked) {
                    mAdapterHelper.unLike(position, String.valueOf(composeBean.thirdObjectId), composeBean.objectType.getName());
                } else {
                    mAdapterHelper.like(position, String.valueOf(composeBean.thirdObjectId), composeBean.objectType.getName());
                }
            }
        });
    }


    @Override
    public void onItemClick(View view, int position) {
        SubjectsComposeBean composeBean = mDatas.get(position);
        switch (composeBean.getTargetActivity()) {
            case SubjectsComposeBean.TargetActivity.ARTICLE:
                SubjectDetailActivity.startSubjectDetailActivity((BaseActivity) mContext, String.valueOf(composeBean.thirdObjectId));
                break;
            case SubjectsComposeBean.TargetActivity.PICTURE:
                SubjectDetailActivity.startSubjectDetailActivity((BaseActivity) mContext, String.valueOf(composeBean.thirdObjectId));
                break;
            case SubjectsComposeBean.TargetActivity.NEWS:
                NewsDetailActivity.startNewsDetailActivity(mContext, String.valueOf(composeBean.thirdObjectId));
                break;
        }
    }


    public void setSubjectTagMap(HashMap<String, List<SubjectTag>> subjectTagMap) {
        mSubjectTagMap = subjectTagMap;
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
