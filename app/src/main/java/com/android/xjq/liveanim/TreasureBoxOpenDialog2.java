package com.android.xjq.liveanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.banana.commlib.dialog.BaseDialogFragment;
import com.android.banana.commlib.view.StrokeTextView;
import com.android.banana.pullrecycler.ilayoutmanager.MyLinearLayoutManager;
import com.android.banana.pullrecycler.multisupport.ViewHolder;
import com.android.banana.pullrecycler.recyclerview.DividerItemDecoration;
import com.android.xjq.R;
import com.android.xjq.bean.TreasureOpenListBean;
import com.android.xjq.model.TreasureBoxEnum;

import java.util.ArrayList;

/**
 * Created by qiaomu on 2018/3/8.
 * <p>
 * 包裹 打开宝箱的动画
 */

public class TreasureBoxOpenDialog2 extends BaseDialogFragment implements View.OnClickListener {
    private static final int LIGHT_ROTATION_COUNT = 10;
    private static final int LIGHT_ROTATION_DURATION = 500 * LIGHT_ROTATION_COUNT;

    private static final int STAR_ALPHA_COUNT = 6;
    private static final int STAR_ALPHA_DURATION = 500 * STAR_ALPHA_COUNT;

    private static final int BOX_ROTATION_COUNT = 3;
    private static final int BOX_ROTATION_DURATION = 1000 * BOX_ROTATION_COUNT;

    private ImageView boxCloseIv, boxTitleIv, boxLightBg, boxStarBg, boxTreasure, boxOpenBg;
    private StrokeTextView mStrokeTextView;
    private Button btnOpen;
    private FrameLayout boxTreasureLayout;
    private RecyclerView mRecyclerView;
    private boolean isOpened = true;

    private ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList;
    private OnDismissListener mListener;

    public static TreasureBoxOpenDialog2 newInstance(String giftName, String subCode, ArrayList<TreasureOpenListBean.treasureOpenBean> treasureOpenList) {

        Bundle args = new Bundle();
        args.putString("giftName", giftName);
        args.putString("subCode", subCode);
        args.putParcelableArrayList("list", treasureOpenList);
        TreasureBoxOpenDialog2 fragment = new TreasureBoxOpenDialog2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected int getDialogTheme() {
        return Theme.NORAML_THEME_DIMENABLE;
    }

    @Override
    protected int getDialogLayoutId() {
        return R.layout.dialog_layout_treasure_open2;
    }

    @Override
    protected void onDialogCreate() {
        treasureOpenList = getArguments().getParcelableArrayList("list");

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        MyLinearLayoutManager manager = new MyLinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), R.drawable.base_divider_list_10dp, LinearLayoutManager.HORIZONTAL));
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new TreasureAdapter());

        boxTreasureLayout = (FrameLayout) rootView.findViewById(R.id.box_treasure_layout);
        boxOpenBg = (ImageView) rootView.findViewById(R.id.box_open_bg);
        boxCloseIv = (ImageView) rootView.findViewById(R.id.box_close_iv);
        boxTitleIv = (ImageView) rootView.findViewById(R.id.box_title_iv);
        boxLightBg = (ImageView) rootView.findViewById(R.id.bg_box_light);
        boxStarBg = (ImageView) rootView.findViewById(R.id.bg_box_star);
        boxTreasure = (ImageView) rootView.findViewById(R.id.box_treasure);
        btnOpen = (Button) rootView.findViewById(R.id.box_btn_ok);
        mStrokeTextView = (StrokeTextView) rootView.findViewById(R.id.box_name);

        mStrokeTextView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.package_enable));
        mStrokeTextView.setStrokeWidth(2);
        String giftName = getArguments().getString("giftName");
        mStrokeTextView.setText(giftName);

        String subCode = getArguments().getString("subCode");
        TreasureBoxEnum boxEnum = TreasureBoxEnum.valueOf(subCode);
        if (boxEnum != null) boxTreasure.setImageResource(boxEnum.getIcon());

        boxCloseIv.setOnClickListener(this);
        btnOpen.setOnClickListener(this);
        btnOpen.setVisibility(View.GONE);
        boxTreasureLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                startOpenAnimation();
            }
        }, 500);
        //startDefaultAnimation();
    }


    private void startDefaultAnimation() {
        startLightRotationAnimation();

        startBoxRotationAnimation(0);

        startStarAlphaAnimation(0);
    }

    private void startLightRotationAnimation() {
        RotateAnimation rotateLight = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateLight.setRepeatMode(Animation.RESTART);
        rotateLight.setRepeatCount(Animation.INFINITE);
        rotateLight.setDuration(LIGHT_ROTATION_DURATION);
        rotateLight.setInterpolator(new LinearInterpolator());
        boxLightBg.startAnimation(rotateLight);
    }

    private void startStarAlphaAnimation(long startOffset) {
        boxStarBg.animate().alpha(0).setInterpolator(new CycleInterpolator(STAR_ALPHA_COUNT)).setDuration(STAR_ALPHA_DURATION).setStartDelay(startOffset).withEndAction(new Runnable() {
            @Override
            public void run() {
                startStarAlphaAnimation(1000);
            }
        }).start();

    }

    private void startBoxRotationAnimation(long startOffset) {
        boxTreasure.animate().rotation(5).setInterpolator(new CycleInterpolator(BOX_ROTATION_COUNT)).setDuration(BOX_ROTATION_DURATION).setStartDelay(startOffset).withEndAction(new Runnable() {
            @Override
            public void run() {
                startBoxRotationAnimation(1000);
            }
        }).start();

    }


    private void startOpenAnimation() {
        //背景光替换资源
        boxLightBg.setImageResource(R.drawable.box_bg_1);
        startLightRotationAnimation();

        //宝箱重新开始另一个动画，旋转跳跃
        boxTreasure.animate().cancel();
        PropertyValuesHolder rotationHolder = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 15f),
                Keyframe.ofFloat(.1f, -15f),
                Keyframe.ofFloat(.2f, 15f),
                Keyframe.ofFloat(.3f, -15f),
                Keyframe.ofFloat(.4f, 15f),
                Keyframe.ofFloat(.5f, -15f),
                Keyframe.ofFloat(.6f, 15f),
                Keyframe.ofFloat(.7f, -15f),
                Keyframe.ofFloat(.8f, 15f),
                Keyframe.ofFloat(.9f, -15f),
                Keyframe.ofFloat(1f, 15f));

        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .8f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.0f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .8f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.0f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );

        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(boxTreasure, pvhScaleX, pvhScaleY, rotationHolder).setDuration(1500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                boxStarBg.setVisibility(View.GONE);
                boxTitleIv.setVisibility(View.GONE);
                boxTreasure.setVisibility(View.GONE);
                mStrokeTextView.setVisibility(View.GONE);

                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.animate().scaleX(0f).scaleY(0).setDuration(0).start();
                mRecyclerView.animate().scaleX(1).scaleY(1).setInterpolator(new OvershootInterpolator(5)).setDuration(1000).start();
                boxOpenBg.animate().alpha(1).setDuration(500).start();

                btnOpen.setText(R.string.str_make_sure);
                btnOpen.setVisibility(View.VISIBLE);
                btnOpen.setEnabled(true);
            }
        });
        animator.start();
    }

    class TreasureAdapter extends RecyclerView.Adapter<ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.dialog_layout_treasure_open2_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TreasureOpenListBean.treasureOpenBean treasure = treasureOpenList.get(position);
            String typeCode = treasure.typeCode;

            StrokeTextView strokeTextView = holder.getView(R.id.gift_name);
            strokeTextView.setStrokeColor(ContextCompat.getColor(getContext(), R.color.package_enable));
            strokeTextView.setStrokeWidth(2);
            strokeTextView.setText(treasure.name);

            holder.setText(R.id.gift_count, (int) treasure.totalAmount + "");
            holder.setViewVisibility(R.id.gift_count, treasure.totalAmount <= 0 ? View.GONE : View.VISIBLE);


            if (TextUtils.equals(typeCode, "GOLDCOIN")) {
                strokeTextView.setText("金锭");
                holder.setImageResource(R.id.gift_logo, R.drawable.ic_package_gold);

            } else if (TextUtils.equals(typeCode, "GIFTCOIN")) {
                strokeTextView.setText("礼金");
                holder.setImageResource(R.id.gift_logo, R.drawable.ic_package_dollar);

            } else if (TextUtils.equals(typeCode, "GIFT")) {
                holder.setImageByUrl(getContext(), R.id.gift_logo, treasure.imageUrl);
            }
        }

        @Override
        public int getItemCount() {
            return treasureOpenList == null ? 0 : treasureOpenList.size();
        }
    }


    class SimpleAnimationCallback implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public interface OnDismissListener {

        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.box_close_iv:
                dismiss();
                break;
            case R.id.box_btn_ok:
                if (!isOpened) {
                    startOpenAnimation();
                    btnOpen.setEnabled(false);
                    isOpened = true;
                } else {
                    dismiss();
                }
                break;
        }
    }
}
