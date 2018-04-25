package com.android.xjq.view.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Duzaoqiu on 2016/11/16 17:10.
 */
public class ImageCycleAdapter extends PagerAdapter {

    /**
     * 图片视图缓存列表
     */
    private ArrayList<SimpleDraweeView> mImageViewCacheList;

    /**
     * 图片资源列表
     */
    private ArrayList<String> mAdList = new ArrayList<>();

    /**
     * 广告图片点击监听器
     */
    private ImageCycleView.ImageCycleViewListener mImageCycleViewListener;

    private Context mContext;

    public ImageCycleAdapter(Context context, ArrayList<String> adList, ImageCycleView.ImageCycleViewListener imageCycleViewListener) {

        mContext = context;

        mAdList = adList;

        mImageCycleViewListener = imageCycleViewListener;

        mImageViewCacheList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mAdList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        String imageUrl = mAdList.get(position);

        SimpleDraweeView imageView = null;

        if (mImageViewCacheList.isEmpty()) {

            imageView = new SimpleDraweeView(mContext);

            imageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

            GenericDraweeHierarchyBuilder builder =
                    new GenericDraweeHierarchyBuilder(mContext.getResources());
            GenericDraweeHierarchy hierarchy = builder
                    .setFadeDuration(300)
//                    .setPlaceholderImage()
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                    .build();
            imageView.setHierarchy(hierarchy);

        } else {

            imageView = mImageViewCacheList.remove(0);
        }
        // 设置图片点击监听
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mImageCycleViewListener.onImageClick(mAdList.get(position),position, v);
            }
        });

        imageView.setTag(imageUrl);

        container.addView(imageView);

        mImageCycleViewListener.displayImage(imageUrl, imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        SimpleDraweeView view = (SimpleDraweeView) object;

        container.removeView(view);

        mImageViewCacheList.add(view);
    }

}