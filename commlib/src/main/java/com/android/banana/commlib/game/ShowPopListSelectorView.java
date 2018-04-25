package com.android.banana.commlib.game;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.android.banana.commlib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajiao on 2018\2\28 0028.
 */

public class ShowPopListSelectorView extends FrameLayout {
    private OnSelectRateListener mListener;
    private List<Integer> mData = new ArrayList<>();
    private Button mSelectedBtn;
    private PopupWindow mPopWindow;
    private SelectorListAdapter adapter;
    private int currentNum = 1;

    public ShowPopListSelectorView(Context context) {
        this(context, null);
    }

    public ShowPopListSelectorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowPopListSelectorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ShowPopListSelectorView, defStyleAttr, 0);
        int anInt = array.getInt(R.styleable.ShowPopListSelectorView_background_color, 0);
        int backColor = anInt;
        LayoutInflater.from(context).inflate(R.layout.show_pop_lay, this);
        //initDate();
        initPop(context, backColor);
        final View holderView = (View) findViewById(R.id.place_holder_view);
        mSelectedBtn = (Button) findViewById(R.id.selector_btn);
        mSelectedBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData == null || mData.size() < 1)
                    return;
                if (mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                } else {
                    adapter.notifyDataSetChanged();
                    mPopWindow.showAsDropDown(holderView);
                }
            }
        });
        setBubbleBackgroundColor(backColor, mSelectedBtn);

        array.recycle();

    }

    private void setBubbleBackgroundColor(int backColor, View view) {
        if (backColor == 0) {
            view.setBackgroundResource(R.drawable.shape_round_radius_bg);
        } else {
            view.setBackgroundResource(R.drawable.shape_round_radius_blue_bg);
        }
    }


    private void initPop(Context ctx, int backColor) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.pop_selector_view, null);
        adapter = new SelectorListAdapter(ctx, mData);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        setBubbleBackgroundColor(backColor, listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                currentNum = mData.get(i);
                mSelectedBtn.setText("x" + mData.get(i));
                if (mListener != null) {
                    mListener.onSelectRate(mData.get(i));
                }
                if (mPopWindow != null) {
                    mPopWindow.dismiss();
                }
            }
        });
        mPopWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopWindow.setFocusable(true);
        mPopWindow.setContentView(view);
    }

    //设置数据
    public void setData(List<Integer> list) {
        if (list == null || list.size() == 0)
            return;
        mData.clear();
        mData.addAll(list);
    }

    //获取当前选择数量,默认为1
    public int getCurrentNum() {
        return currentNum;
    }

    public interface OnSelectRateListener {
        public void onSelectRate(int rate);
    }

    public void setOnSelectRateListener(OnSelectRateListener listener) {
        mListener = listener;
    }

}
