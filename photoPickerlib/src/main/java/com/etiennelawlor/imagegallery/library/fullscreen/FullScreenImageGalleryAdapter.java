package com.etiennelawlor.imagegallery.library.fullscreen;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.entity.Photo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public class FullScreenImageGalleryAdapter extends PagerAdapter {

    private int mType = -1;

    private List<Photo> mImages;

    private FullScreenImageGalleryActivity activity;

    private int mCurrentPosition = 0;

    private HashMap<Integer, Boolean> downLoadList = new HashMap<>();

    public FullScreenImageGalleryAdapter(FullScreenImageGalleryActivity activity, List<Photo> images) {

        this(activity, images, -1);

    }

    public FullScreenImageGalleryAdapter(FullScreenImageGalleryActivity activity, List<Photo> images, int type) {

        mImages = images;

        mType = type;

        this.activity = activity;

        if (images == null)
            return;

        for (int i = 0; i < images.size(); i++) {

            downLoadList.put(i, false);

        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        LayoutInflater inflater = (LayoutInflater) container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.fullscreen_image, null);

        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        final ImageView normalImage = (ImageView) view.findViewById(R.id.iv);

        SimpleDraweeView gifImage = (SimpleDraweeView) view.findViewById(R.id.gifIv);

        final String image = mImages.get(position).getPath();

        Context context = normalImage.getContext();

        if (!TextUtils.isEmpty(image)) {
            if (mType == -1) {
                Picasso.with(context)
                        .load(new File(image))
                        .error(R.drawable.full_preview_image_empty)
                        .placeholder(R.drawable.full_preview_image_empty)
                        .into(normalImage);
            } else {

                progressBar.setVisibility(View.VISIBLE);

                if (image.lastIndexOf(".gif") != -1) {

                    normalImage.setVisibility(View.GONE);

                    gifImage.setVisibility(View.VISIBLE);

                    DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                            .setUri(Uri.parse(image))
                            .setControllerListener(new ControllerListener<ImageInfo>() {
                                @Override
                                public void onSubmit(String id, Object callerContext) {

                                }

                                @Override
                                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                                    //Log.e("当前图片下载完成", "-------------" + position);
                                    progressBar.setVisibility(View.GONE);
                                    downLoadList.put(position, true);
                                    if (position == mCurrentPosition) {
                                        activity.saveImageBtn.setEnabled(true);
                                        setImageBtn(true);
                                    }
                                }

                                @Override
                                public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                                    //progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onIntermediateImageFailed(String id, Throwable throwable) {
                                    //progressBar.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFailure(String id, Throwable throwable) {
                                    progressBar.setVisibility(View.GONE);
                                    downLoadList.put(position, false);
                                    if (position == mCurrentPosition) {
                                        activity.saveImageBtn.setEnabled(false);
                                        setImageBtn(false);
                                    }
                                }

                                @Override
                                public void onRelease(String id) {

                                }
                            })
                            .setAutoPlayAnimations(true)// 设置加载图片完成后是否直接进行播放
                            .build();

                    gifImage.setController(draweeController);

                } else {

                    normalImage.setVisibility(View.VISIBLE);

                    gifImage.setVisibility(View.GONE);

                    Picasso.with(context).load(Uri.parse(image))
                            .error(R.drawable.full_preview_image_empty)
                            .placeholder(R.drawable.full_preview_image_empty)
                            .into(normalImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                    if (position == mCurrentPosition) {

                                        activity.saveImageBtn.setEnabled(true);

                                        setImageBtn(true);

                                    }

                                    downLoadList.put(position, true);

                                    progressBar.setVisibility(View.GONE);

                                }

                                @Override
                                public void onError() {

                                    if (position == mCurrentPosition) {

                                        activity.saveImageBtn.setEnabled(false);

                                        setImageBtn(false);

                                    }

                                    downLoadList.put(position, false);

                                    progressBar.setVisibility(View.GONE);

                                }
                            });
                }
            }

        } else {

            gifImage.setVisibility(View.GONE);

            normalImage.setImageResource(R.drawable.full_preview_image_empty);

        }

        container.addView(view, 0);

        return view;
    }

    private void setImageBtn(boolean status) {
        if (status) {
            activity.saveImageBtn.setTextColor(Color.WHITE);
        } else {
            activity.saveImageBtn.setTextColor(Color.parseColor("#585858"));
        }
    }

    @Override
    public int getCount() {

        return mImages == null ? 0 : mImages.size();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;

    }

    public void setCurrentSelectedPosition(int currentPosition) {

        this.mCurrentPosition = currentPosition;

    }

    public boolean getDownloadStatus(int pos) {

        return downLoadList.get(pos);

    }


}
