package com.android.xjq.controller.live;

import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.android.xjq.XjqApplication;
import com.android.xjq.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lingjiu on 2017/5/8.
 */

public class LiveStateChangeController {

    private static Unbinder unbinder;
    private LiveStateViewHolder mViewHolder;


    public LiveStateChangeController(View view) {
        init(view);
    }

    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new LiveStateViewHolder(view);
            initData();
        }
    }

    private void initData() {
        setImageView(mViewHolder.preparePlayIv, "res://" + XjqApplication.getContext().getPackageName() + "/" + R.drawable.icon_video_loading, true);
    }

    public void liveStateChange(boolean pushStreamStatus) {
        if (mViewHolder == null) return;
        if (mViewHolder.anchorOfflineLayout == null) return;
        if (pushStreamStatus) {
            mViewHolder.anchorOfflineLayout.setVisibility(View.INVISIBLE);
        } else {
            mViewHolder.anchorOfflineLayout.setVisibility(View.VISIBLE);
        }
        startOrStopAnim(!pushStreamStatus);
    }

    public void receiveFirstFrame() {
        if (mViewHolder.anchorOfflineLayout != null)
            mViewHolder.anchorOfflineLayout.setVisibility(View.GONE);
        startOrStopAnim(false);
    }

    private void setImageView(SimpleDraweeView audioPlayIv, String url, boolean autoPlayAnimation) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(autoPlayAnimation)
                .setUri(uri)
                .build();

        audioPlayIv.setController(controller);
    }

    public void startOrStopAnim(boolean isStart) {
        if (mViewHolder.preparePlayIv == null) return;
        Animatable animatable = mViewHolder.preparePlayIv.getController().getAnimatable();
        if (animatable != null) {
            if (isStart) {
                animatable.start();
            } else {
                animatable.stop();
            }
        }
        mViewHolder.preparePlayIv.setVisibility(isStart ? View.VISIBLE : View.GONE);
    }

    public void onDestroy() {
//        if (mViewHolder.preparePlayIv != null)
//            mViewHolder.preparePlayIv.setController(null);
        unbinder.unbind();
        mViewHolder = null;
    }

    static class LiveStateViewHolder {
        @BindView(R.id.anchorOfflineLayout)
        FrameLayout anchorOfflineLayout;
        @BindView(R.id.preparePlayIv)
        SimpleDraweeView preparePlayIv;
        @BindView(R.id.liveStateLayout)
        FrameLayout liveStateLayout;


        LiveStateViewHolder(View view) {
            unbinder = ButterKnife.bind(this, view);
        }
    }
}
