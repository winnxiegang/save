package com.android.banana.commlib.dialog;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.android.banana.commlib.R;

/**
 * Created by mrs on 2017/4/23.
 */

public abstract class BaseDialogFragment extends DialogFragment {
    protected View rootView;
    protected Dialog dialog;

    @Override
    public Dialog getDialog() {
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new Dialog(getContext(), getDialogTheme());

        Window window = dialog.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = getGravity();
        window.setAttributes(attributes);

        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        rootView = LayoutInflater.from(getContext()).inflate(getDialogLayoutId(), null);
        dialog.setContentView(rootView);


        onDialogCreate();
        return dialog;
    }

    public void show(FragmentManager manager) {
        show(manager, getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (fillScreenWidth()) {
            getDialog().getWindow().setLayout(-1, -2);
        }else{
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.75), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public boolean fillScreenWidth() {
        return true;
    }

    protected abstract int getDialogTheme();

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected abstract int getDialogLayoutId();

    protected abstract void onDialogCreate();

    public interface Theme {
        int NORAML_THEME = R.style.normal_dimendisable_theme;//不悬浮 背景不虚化
        int NORAML_THEME_DIMENABLE = R.style.normal_dimenable_theme;//悬浮 背景虚化

        int SLIDE_FROM_TO_TOP_WINANIM = R.style.slide_from_to_top_style;//从上滑入  滑出式的窗口动画
        //int SLIDE_FROM_TO_TOP_THEME = R.style.slide_from_to_top_theme;//从上滑入  滑出式的dialog theme

        int SLIDE_FROM_TO_BOTTOM_THEME = R.style.slide_from_to_bottom_theme;//底部弹出  弹出式为上滑，消失时为下滑ialog theme
        int SLIDE_FROM_TO_BOTTOM_WINANIM = R.style.slide_from_to_bottom_style;//底部弹出  弹出式为上滑，消失时为下滑窗口动画

        int SLIDE_FROM_TO_LEFT_THEME = R.style.slide_from_to_left_theme;
        int SLIDE_FROM_TO_LEFT_TWINANIM = R.style.slide_from_to_left_animation;
    }


}

