package com.android.xjq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.android.library.Utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.adapter.matchLive.MatchFilterAdapter;
import com.android.xjq.bean.matchLive.LeagueInfo;
import com.android.xjq.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MatchFilterDialog {

    private Builder mBuilder;
    private Dialog mDialog;
    private View mDialogView;
    private MatchFilterAdapter mMatchFilterAdapter;
    @BindView(R.id.okBtn)
    Button mConfirmBtn;
    @BindView(R.id.cancelBtn)
    Button mCancelBtn;
    @BindView(R.id.bfDrawGv)
    MyGridView myGridView;
    @BindView(R.id.select_all_txt)
    TextView mSelectAllTxt;
    //@BindView(R.id.select_reverse_all_txt)
    //TextView mReverseAllTxt;
    @BindView(R.id.five_types_txt)
    TextView mFiveTypesTxt;
    @BindView(R.id.clear_txt)
    TextView mClearTxt;
    @BindView(R.id.widget_close_txt)
    CheckedTextView closeTxt;
    @BindView(R.id.blank_view)
    View blankView;


    public MatchFilterDialog (Builder builder) {
        mBuilder = builder;
        mDialog = new Dialog(mBuilder.getContext(), R.style.NormalDialogStyle);
        mDialogView = View.inflate(mBuilder.getContext(), R.layout.widget_match_filter_dialog, null);
        mDialog.setContentView(mDialogView);
        ButterKnife.bind(this, mDialogView);
        mMatchFilterAdapter = new MatchFilterAdapter(mBuilder.getContext(), mBuilder.getFilterList());
        myGridView.setAdapter(mMatchFilterAdapter);
        setDialogPosition();
        initDialog();
    }

    private void setDialogPosition() {
        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.TOP);
        lp.width = LibAppUtil.getScreenWidth(mBuilder.getContext());
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.y = (int)DimensionUtils.dpToPx(45, mBuilder.getContext());
        lp.dimAmount = 0.0f;
        dialogWindow.setAttributes(lp);
    }

    private void initDialog() {
        mDialog.setCanceledOnTouchOutside(mBuilder.isTouchOutside());
        mSelectAllTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBuilder.getFilterList() == null) return;
                for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                    mBuilder.getFilterList().get(i).setSelected(true);
                }
                mMatchFilterAdapter.notifyDataSetChanged();
            }
        });
        mClearTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBuilder.getFilterList() == null) return;
                for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                    mBuilder.getFilterList().get(i).setSelected(false);
                }
                mMatchFilterAdapter.notifyDataSetChanged();
            }
        });
        if (mBuilder.isBasketballFilter()) {
            //mReverseAllTxt.setVisibility(View.GONE);
            mFiveTypesTxt.setText("反选");
            mFiveTypesTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBuilder.getFilterList() == null) return;
                    for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                        if (mBuilder.getFilterList().get(i).isSelected()) {
                            mBuilder.getFilterList().get(i).setSelected(false);
                        } else {
                            mBuilder.getFilterList().get(i).setSelected(true);
                        }
                    }
                    mMatchFilterAdapter.notifyDataSetChanged();
                }
            });
        } else {
            if (mBuilder.getMatchFiveIds() == null || mBuilder.getMatchFiveIds().size() == 0) mFiveTypesTxt.setVisibility(View.GONE);
 /*           mReverseAllTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBuilder.getFilterList() == null) return;
                    for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                        if (mBuilder.getFilterList().get(i).isSelected()) {
                            mBuilder.getFilterList().get(i).setSelected(false);
                        } else {
                            mBuilder.getFilterList().get(i).setSelected(true);
                        }
                    }
                    mMatchFilterAdapter.notifyDataSetChanged();
                }
            });*/

            mFiveTypesTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mBuilder.getMatchFiveIds() == null) {
                        mFiveTypesTxt.setVisibility(View.GONE);
                        return;
                    }
                    mFiveTypesTxt.setVisibility(View.VISIBLE);
                    if (mBuilder.getFilterList() == null) return;
                    for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                        mBuilder.getFilterList().get(i).setSelected(false);
                    }
                    for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                        if (mBuilder.getMatchFiveIds().contains(mBuilder.getFilterList().get(i).getId())) {
                            mBuilder.getFilterList().get(i).setSelected(true);
                            continue;
                        }
                    }
                    mMatchFilterAdapter.notifyDataSetChanged();
                }
            });
        }
        closeTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mCancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mConfirmBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ArrayList<String> selectedIdList = new ArrayList<>();
                if (mBuilder.getFilterList() != null && mBuilder.getFilterList().size() > 0) {
                    for (int i = 0; i < mBuilder.getFilterList().size(); i++) {
                        if (mBuilder.getFilterList().get(i).isSelected()) {
                            selectedIdList.add(mBuilder.getFilterList().get(i).getCnShortName());
                        }
                    }
                }
                v.setTag(selectedIdList);
                mBuilder.getOnSelectedListener().onClick(v);
                mDialog.dismiss();
            }
        });
        blankView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }


    public void show() {
        mDialog.show();
    }

    public void dismiss() {
        mDialog.dismiss();
    }

    public Dialog getDialog() {
        return mDialog;
    }


    public static class Builder {
        private Context context;
        private int width;
        private boolean isTouchOutside = true;
        private ArrayList<LeagueInfo> filterList;
        private boolean isBasketballFilter;
        private List<String> matchFiveIds;
        private View.OnClickListener onSelectedListener;

        public Builder (Context ctx) {
            context = ctx;
        }

        public View.OnClickListener getOnSelectedListener() {
            return onSelectedListener;
        }

        public Builder setOnSelectedListener(View.OnClickListener onSelectedListener) {
            this.onSelectedListener = onSelectedListener;
            return this;
        }

        public List<String> getMatchFiveIds() {
            return matchFiveIds;
        }

        public Builder setMatchFiveIds(List<String> matchFiveIds) {
            this.matchFiveIds = matchFiveIds;
            return this;
        }

        public boolean isBasketballFilter() {
            return isBasketballFilter;
        }

        public Builder setIsBasketballFilter(boolean basketballFilter) {
            isBasketballFilter = basketballFilter;
            return this;
        }

        public boolean isTouchOutside() {
            return isTouchOutside;
        }

        public Builder setTouchOutside(boolean touchOutside) {
            isTouchOutside = touchOutside;
            return this;
        }


        public ArrayList<LeagueInfo> getFilterList() {
            return filterList;
        }

        public Builder setFilterList(ArrayList<LeagueInfo> filterList) {
            this.filterList = filterList;
            return this;
        }
        public Context getContext() {
            return context;
        }

        public int getWidth() {
            return width;
        }

        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }


        public MatchFilterDialog build() {
            return new MatchFilterDialog(this);
        }

    }

}
