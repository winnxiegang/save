package com.android.xjq.dialog.popupwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.banana.commlib.utils.AnimationListUtil;
import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;

/**
 * Created by lingjiu on 2017/10/9.
 */

public class FullScreenAnimPop {

    private Context context;

    PopupWindow mPopupWindow;
    View view;
    private ImageView imageView;
    private String levelCode;
    private String medalName;
    private ImageView levelIv;
    private boolean isLandScape;

    public FullScreenAnimPop(Context context, String levelCode, String medalName, boolean isLandscape) {
        this.context = context;
        this.levelCode = levelCode;
        this.medalName = medalName;
        this.isLandScape = isLandscape;
    }

    public void show(View view) {

        initPopupWindow();

        start();

        mPopupWindow.showAsDropDown(view);
    }

    private void initPopupWindow() {
        view = getPopupWindowView();

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (onDismissListener != null) {

                    onDismissListener.onDismissListener();
                }
            }
        });

        int resId = context.getResources().getIdentifier("icon_first_get_medal_" + levelCode.toLowerCase(), "drawable", context.getPackageName());
        levelIv.setImageResource(resId);
    }

    private View getPopupWindowView() {
        final View view = LayoutInflater.from(context).inflate(R.layout.pop_first_get_medal_anim, null);

        imageView = (ImageView) view.findViewById(R.id.imageView);

        levelIv = ((ImageView) view.findViewById(R.id.levelIv));

        imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.anim_send_coupon_success));

        ((TextView) view.findViewById(R.id.levelTv)).setText("【" + medalName + "】");

        if (!isLandScape) {
            int bottom = LibAppUtil.getScreenHeight(context) - context.getResources().getDimensionPixelOffset(R.dimen.live_dialog_height);

            view.setPadding(0, 0, 0, bottom);
        }

        return view;
    }

    private void start() {
        AnimationListUtil.startAnimationList(R.drawable.anim_first_get_fans_medal, imageView, null, new Runnable() {
            @Override
            public void run() {
                imageView.setImageDrawable(null);
                levelIv.setImageDrawable(null);
                mPopupWindow.dismiss();
            }
        }, 3);
    }

    private OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismissListener();
    }
}
