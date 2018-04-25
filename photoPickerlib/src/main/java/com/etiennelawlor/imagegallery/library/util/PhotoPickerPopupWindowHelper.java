package com.etiennelawlor.imagegallery.library.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.adapters.PopupWindowAdapter;
import com.etiennelawlor.imagegallery.library.entity.PhotoDirectory;

import java.util.List;

/**
 * Created by 周毅 on 2015/6/6 10:08.
 */
public class PhotoPickerPopupWindowHelper {

    protected PopupWindow mPopupWindow;

    private Context mContext;

    private int mCurrentSelected;

    private List<PhotoDirectory> mList;

    private View.OnClickListener mOnClickListener;

    private View mDropDownView;

    public PhotoPickerPopupWindowHelper(Context context, List<PhotoDirectory> list, View dropDownView, View.OnClickListener onClickListener) {

        this.mContext = context;

        this.mList = list;

        this.mDropDownView = dropDownView;

        mOnClickListener = onClickListener;

    }

    public PhotoPickerPopupWindowHelper(Context context, View dropDownView, BaseAdapter adapter) {

        this.mContext = context;

        this.mDropDownView = dropDownView;

    }


    private void initPopupWindow() {

        View view = getPopupWindowView();

        mPopupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);

        mPopupWindow.setBackgroundDrawable(new ColorDrawable());

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

    private void setPopupWindow() {

        if (null != mPopupWindow) {

            mPopupWindow.dismiss();

            return;

        } else {

            initPopupWindow();

        }

    }

    public void showPopupWindow() {

        setPopupWindow();

        mPopupWindow.showAsDropDown(mDropDownView);

    }

    private View getPopupWindowView() {

        LayoutInflater inflate = LayoutInflater.from(mContext);

        View view = inflate.inflate(R.layout.view_photo_picker_popup, null, false);

        ListView listView = (ListView) view.findViewById(R.id.myLv);

        PopupWindowAdapter adapter = new PopupWindowAdapter(mContext, mList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setTag(position);
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setSelected(false);
                }
                mList.get(position).setSelected(true);
                mOnClickListener.onClick(view);
            }
        });

        return view;
    }

    public void dismiss() {

        mPopupWindow.dismiss();

        mPopupWindow = null;

    }


}
