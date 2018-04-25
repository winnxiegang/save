package com.android.xjq.dialog.live;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.xjq.R;
import com.android.xjq.liveanim.RocketFlyLayout;

/**
 * Created by zaozao on 2018/3/8.
 */

public class RocketFlyPop {

    private Context context;

    PopupWindow mPopupWindow;

    private RocketFlyLayout rocketFlyLayout;

    public RocketFlyPop(Context context) {
        this.context = context;

    }

    public void show(final View view, final boolean up,String title) {
        initPopupWindow();
        mPopupWindow.showAsDropDown(view);
       if (up){
           rocketFlyLayout.startSuccessAnim(title,endListener);
       }else {
           rocketFlyLayout.startRocketFailedAnim(title,endListener);

       }
    }

    AnimEndListener endListener = new AnimEndListener() {
        @Override
        public void end() {
            mPopupWindow.dismiss();
        }
    };

    private void initPopupWindow() {
        View view = getPopupWindowView();
        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));
    }

    private View getPopupWindowView() {
        View view = LayoutInflater.from(context).inflate(R.layout.pop_rocket_fly, null);
        rocketFlyLayout = (RocketFlyLayout) view.findViewById(R.id.rocketFlyLayout);
        return view;
    }

    public interface  AnimEndListener{
        void end();
    }

}
