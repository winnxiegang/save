package com.android.banana.commlib.utils;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.android.banana.commlib.R;

import java.util.ArrayList;

/**
 * Created by 周毅 on 2015/6/6 10:08.
 */
public class PopupWindowHelper {

    protected PopupWindow mPopupWindow;

    private Context mContext;

    private String mCurrentSelected;

    private ArrayList<Object> mList;

    private View.OnClickListener mOnClickListener;

    private View mDropDownView;

    private int mSelectType=KjggIssueSelectorAdapter.SELECT_DEFAULT;

    private int mGridViewNumColumns = 3;

    private BaseAdapter mCustomerAdapter;

    public PopupWindowHelper(Context context, ArrayList<Object> list, View.OnClickListener onClickListener, View dropDownView, int selectType) {
        this.mContext = context;
        this.mList = list;
        this.mOnClickListener = onClickListener;
        this.mDropDownView = dropDownView;
        this.mSelectType = selectType;
    }

    public PopupWindowHelper(Context context, View dropDownView, BaseAdapter adapter) {

        this.mContext = context;

        this.mDropDownView = dropDownView;

        this.mCustomerAdapter = adapter;

    }

    private void initPopupWindow() {

        View view = getPopupWindowView();

        if (Build.VERSION.SDK_INT >= 24&& Build.VERSION.SDK_INT < 26&&mDropDownView!=null) {

            view.setPadding(0, mDropDownView.getHeight(), 0, 0);
        }

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00ffffff));

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
    }

    public void setGridViewNumColumns(int numColumns){

        mGridViewNumColumns = numColumns;

    }

    private void setPopupWindow() {

        if (null != mPopupWindow) {

            mPopupWindow.dismiss();

            return;

        } else {

            initPopupWindow();

        }

    }

    public void showPopupWindow(String currentSelected) {
        this.mCurrentSelected = currentSelected;
        setPopupWindow();
        mPopupWindow.showAsDropDown(mDropDownView);
    }

    private View getPopupWindowView() {

        LayoutInflater inflate = LayoutInflater.from(mContext);

        View view=null;

        view = inflate.inflate(R.layout.view_issue_selector, null, false);

        GridView gridView = (GridView) view.findViewById(R.id.gridView1);

        if(mCustomerAdapter!=null){
            gridView.setPadding(0,0,0,0);
        }

        gridView.setNumColumns(mGridViewNumColumns);

        if(mCustomerAdapter!=null){
            gridView.setAdapter(mCustomerAdapter);
        }else{
            KjggIssueSelectorAdapter kjggIssueSelectorAdapter = new KjggIssueSelectorAdapter(mContext, mCurrentSelected, mList, mOnClickListener);

            kjggIssueSelectorAdapter.setSelectType(mSelectType);

            gridView.setAdapter(kjggIssueSelectorAdapter);
        }
        return view;
    }
    public void dismiss(){
        mPopupWindow.dismiss();
        mPopupWindow=null;
    }








}
