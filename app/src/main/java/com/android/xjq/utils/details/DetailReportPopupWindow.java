package com.android.xjq.utils.details;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.xjq.R;

/**
 * Created by zhouyi on 2015/12/17 12:22.
 */
public class DetailReportPopupWindow {

    protected PopupWindow mPopupWindow;
    private Context context;

    private View mDropDownView;

    private int popWindowHeight;

    private ReportDialogUtils reportDialogUtils;

    public DetailReportPopupWindow(Context context,View view) {
        this.context = context;
        this.mDropDownView = view;
    }

    private void initPopupWindow() {

        View view = getPopupWindowView();

        popWindowHeight = (int) context.getResources().getDimension(R.dimen.popup_collect_height);

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, popWindowHeight, true);

        mPopupWindow.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_container_shadow));

        // 点击其他地方消失
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;
                }
                return false;
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
    }

    private void setPopupWindow() {

        if (null != mPopupWindow) {

            mPopupWindow.dismiss();

            return;

        } else {

            initPopupWindow();
        }

    }

    /**
     * 话题详情页显示举报
     *
     */
    public void showPopupWindow() {


        setPopupWindow();

        mPopupWindow.showAsDropDown(mDropDownView);

    }





    private View getPopupWindowView() {

        LayoutInflater inflate = LayoutInflater.from(context);

        View view = inflate.inflate(R.layout.layout_collect_popup_window, null);

        LinearLayout reportLayout = (LinearLayout) view.findViewById(R.id.reportLayout);

        reportLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(reportDialogUtils==null){
                    reportDialogUtils = new ReportDialogUtils();
                }
                reportDialogUtils.showReportDialog(context);

                dismiss();
            }
        });

        return view;
    }


    public void dismiss() {

        mPopupWindow.dismiss();

        mPopupWindow = null;

    }


}
