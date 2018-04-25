package com.android.banana.pullrecycler.multisupport;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.view.LabelTextView;
import com.android.banana.view.LianRedTextView;
import com.android.banana.view.WithRedTextView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by mrs on 2017/4/5.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    // 用来存放子View减少findViewById的次数
    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<>();
    }


    public <T extends View> T getView(int viewId) {
        // 先从缓存中找
        View view = mViews.get(viewId);
        if (view == null) {
            // 直接从ItemView中找
            view = itemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public ViewHolder setText(@IdRes int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null)
            tv.setText(text);
        return this;
    }


    public ViewHolder append(@IdRes int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        if (tv != null)
            tv.append(text);
        return this;
    }

    public ViewHolder setEllipsizeText(@IdRes int viewId, String text) {
        LabelTextView tv = getView(viewId);
        if (tv != null)
            tv.setEllipsizeText(text);
        return this;
    }

    public ViewHolder setLabelTextTop(@IdRes int viewId, String topLabel) {
        LabelTextView tv = getView(viewId);
        if (tv != null)
            tv.setLabelTextTop(topLabel);
        return this;
    }

    public ViewHolder setLabelTextBottom(@IdRes int viewId, String bottomLabel) {
        LabelTextView tv = getView(viewId);
        if (tv != null)
            tv.setLabelTextBottom(bottomLabel);
        return this;
    }

    public ViewHolder setLabelTextLeft(@IdRes int viewId, String leftLabel) {
        LabelTextView tv = getView(viewId);
        if (tv != null)
            tv.setLabelTextLeft(leftLabel);
        return this;
    }

    public ViewHolder setLabelTextRight(@IdRes int viewId, String rightLabel) {
        LabelTextView tv = getView(viewId);
        if (tv != null)
            tv.setLabelTextRight(rightLabel);
        return this;
    }


    public ViewHolder setViewVisibility(@IdRes int viewId, int visibility) {
        View view = getView(viewId);
        if (view != null)
            view.setVisibility(visibility);
        return this;
    }

    /*连红*/
    public ViewHolder setLianRedLabel(@IdRes int viewId, int labelNum) {
        LianRedTextView view = getView(viewId);
        if (view != null)
            view.setLianRedNum(labelNum);
        return this;
    }

    /**
     * 设置ImageView的资源
     */
    public ViewHolder setImageResource(@IdRes int viewId, @DrawableRes int resourceId) {
        ImageView imageView = getView(viewId);
        if (imageView != null)
            imageView.setImageResource(resourceId);
        return this;
    }

    public ViewHolder setCompoundDrawables(@IdRes int viewId, @DrawableRes int left, @DrawableRes int top, @DrawableRes int right, @DrawableRes int bottom) {
        TextView view = getView(viewId);
        if (view != null)
            view.setCompoundDrawablesWithIntrinsicBounds(left, top, right, bottom);
        return this;
    }

    /**
     * 设置图片通过路径,这里稍微处理得复杂一些，因为考虑加载图片的第三方可能不太一样
     * 也可以直接写死
     */
    public ViewHolder setImageByUrl(Context context, @IdRes int viewId, String path, @DrawableRes int placeHolder) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {

            if (TextUtils.isEmpty(path)) {
                imageView.setImageResource(placeHolder);
            } else {
                if (placeHolder != 0)
                    imageView.setImageResource(placeHolder);
                PicUtils.load(context, imageView, path, placeHolder);
            }
        }
        return this;
    }

    public ViewHolder setImageByFresco(@IdRes int viewId, String logoUrl, boolean enable) {
        SimpleDraweeView simpleDraweeView = getView(viewId);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(logoUrl))
                .setAutoPlayAnimations(enable)
                .build();
        simpleDraweeView.setController(controller);
        return this;
    }


    public ViewHolder setImageByUrl(Context context, @IdRes int viewId, String path) {
        return setImageByUrl(context, viewId, path, 0);
    }


    public ViewHolder setOnClickListener(@IdRes int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(@IdRes int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view != null)
            view.setOnLongClickListener(listener);
        return this;
    }


    public void onItemClick(View view) {

    }

    public void onItemLongClick(View view) {

    }


    public ViewHolder setTextColor(@IdRes int viewId, @ColorInt int color) {
        View view = getView(viewId);
        if (view != null) {
            ((TextView) view).setTextColor(color);
        }
        return this;

    }

    public ViewHolder setBackgroundDrawable(@IdRes int viewId, Drawable backgroundDrawable) {
        View view = getView(viewId);
        if (view != null) {
            if (view instanceof ImageView && backgroundDrawable == null) {
                ((ImageView) view).setImageDrawable(null);
            } else {
                view.setBackground(backgroundDrawable);
            }
        }
        return this;
    }

    public ViewHolder setImageDrawable(@IdRes int viewId, Drawable backgroundDrawable) {
        ImageView view = getView(viewId);
        if (view != null) {
            if (backgroundDrawable == null) {
                view.setImageDrawable(null);
            } else {
                view.setImageDrawable(backgroundDrawable);
            }
        }
        return this;
    }

    public ViewHolder setMember(@IdRes int withRed, int withRedNum, @DrawableRes int iconRes) {
        WithRedTextView tv = getView(withRed);
        if (tv != null)
            tv.setMember(withRedNum, iconRes);
        return this;
    }

    public ViewHolder setTextSize(@IdRes int withRed, int size) {
        View view = getView(withRed);
        if (view != null)
            ((TextView) view).setTextSize(size);
        return this;
    }


    /**
     * 设置background的资源
     */
    public ViewHolder setBackgroundRes(int viewId, int resourceId) {
        View view = getView(viewId);
        view.setBackgroundResource(resourceId);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        if (view != null)
            view.setChecked(checked);
        return this;
    }
}
