package com.android.banana.commlib.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.LtTypeData;
import com.android.banana.commlib.view.MyCheckLinearLayout;

import java.util.ArrayList;

public class KjggIssueSelectorAdapter extends BaseAdapter {

    /**
     * 大神选择
     */
    public static final int SELECT_DS = 0x10;

    /**
     * 默认选择
     */
    public static final int SELECT_DEFAULT = 0X11;

    /**
     * 彩票玩法子类型选择
     */
    public static final int SELECT_LOTTERY_SUB_TYPE = 0X12;

    /**
     * 彩票类型选择
     */
    public static final int SELECT_LOTTERY_TYPE = 0X13;


    private int mSelectType = SELECT_DEFAULT;

    private ArrayList<Object> mContentList = null;

    private LayoutInflater inflate;

    private OnClickListener mOnClickListener;

    private String mSelectedContent;

    private String mLotteryType;

    public KjggIssueSelectorAdapter(Context context, String selectedContent,
                                    ArrayList<Object> contentList, OnClickListener clickListener) {

        mContentList = contentList;

        inflate = LayoutInflater.from(context);

        this.mOnClickListener = clickListener;

        mSelectedContent = selectedContent;
    }

    public void setSelectType(int type) {

        mSelectType = type;

    }

    @Override
    public int getCount() {
        if (mContentList == null) {
            return 0;
        }
        return mContentList.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public boolean isEnabled(int position) {

        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflate.inflate(R.layout.item_kjgg_issue_selector, null);
        MyCheckLinearLayout linear = (MyCheckLinearLayout) view.findViewById(R.id.mainLayout);
        linear.setTag(position);
        linear.setOnClickListener(mOnClickListener);
        setType(view, position, linear);

        return view;
    }

    private void setType(View view, int pos, MyCheckLinearLayout linear) {

        TextView tv = (TextView) view.findViewById(R.id.content);

        switch (mSelectType) {

            case SELECT_DEFAULT: {

                String text = (String) mContentList.get(pos);

                if (mSelectedContent != null) {

                    if (mSelectedContent.equals(text)) {

                        linear.setChecked(true);
                    }

                }
                tv.setText(text);
            }
            break;
            case SELECT_LOTTERY_TYPE: {
                LtTypeData data = (LtTypeData) mContentList.get(pos);

                if (mSelectedContent != null) {

                    if (mSelectedContent.equals(data.getCode())) {

                        linear.setChecked(true);
                    }
                }
                tv.setText(data.getName());
            }
//            case SELECT_DS: {
//                DSData data = (DSData) mContentList.get(pos);
//                ImageView fireIv = (ImageView) view.findViewById(R.id.fireIv);
//
//                TextView countTv = (TextView) view.findViewById(R.id.countTv);
//
//                if (data.isFocus()) {
//                    fireIv.setVisibility(View.VISIBLE);
//                }
//
//                if (data.getCount() > 0) {
//
//                    countTv.setVisibility(View.VISIBLE);
//
//                    countTv.setText("(" + data.getCount() + ")");
//
//                }
//
//                tv.setText(data.getUserName());
//
//
//                if (mSelectedContent != null) {
//
//                    if (mSelectedContent.equals(data.getUserName())) {
//
//                        linear.setChecked(true);
//
//                    }
//
//                }
//            }
//            break;

//            case SELECT_LOTTERY_SUB_TYPE: {
//
//                String text = (String) mContentList.get(pos);
//
//                if (mSelectedContent != null) {
//
//                    if (mSelectedContent.equals(text)) {
//
//                        linear.setChecked(true);
//
//                    }
//
//                }
//                tv.setText(LotterySelecterEnum.valueOf(text).getTip());
//            }
//            break;


        }

    }

}
