package com.android.xjq.liveanim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.xjq.XjqApplication;

/**
 * Created by zaozao on 2018/2/28.
 * 用处：直播火箭玩法，按钮点击动画
 */

public class RocketButtonClickLayout extends LinearLayout {

    TextView flyOneTv;
    TextView addWaterBtn;
    private View rocketClickButtonView;
    private int rocketClickTextColor;
    private Drawable buttonBackground;
    private boolean addFire;
    private String  payNumAndType;

    private OnClickListener onClickListener;

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public RocketButtonClickLayout(Context context) {
        this(context,null);
    }

    public RocketButtonClickLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RocketButtonClickLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //加载自定义的属性
        TypedArray a=context.obtainStyledAttributes(attrs,R.styleable.RocketButtonClickLayout);

        rocketClickTextColor=a.getColor(R.styleable.RocketButtonClickLayout_rocketClickTextColor, context.getResources().getColor(R.color.white));

        buttonBackground =a.getDrawable(R.styleable.RocketButtonClickLayout_buttonBackGround);

        addFire=a.getBoolean(R.styleable.RocketButtonClickLayout_addFire,true);

        payNumAndType=a.getString(R.styleable.RocketButtonClickLayout_payNumAndType);

        if(rocketClickButtonView == null){
            //加载视图的布局
            rocketClickButtonView = LayoutInflater.from(context).inflate(R.layout.view_rocket_button, null);
            addWaterBtn = (TextView) rocketClickButtonView.findViewById(R.id.addWaterBtn);

            //获取子控件
            if(addFire){
                rocketClickButtonView.findViewById(R.id.flyLeftTv).setVisibility(VISIBLE);
                rocketClickButtonView.findViewById(R.id.flyRightTv).setVisibility(GONE);
                flyOneTv = (TextView) rocketClickButtonView.findViewById(R.id.flyLeftTv);
            }else{
                rocketClickButtonView.findViewById(R.id.flyLeftTv).setVisibility(GONE);
                rocketClickButtonView.findViewById(R.id.flyRightTv).setVisibility(VISIBLE);
                flyOneTv = (TextView) rocketClickButtonView.findViewById(R.id.flyRightTv);
            }
            flyOneTv.setTextColor(rocketClickTextColor);
            addWaterBtn.setBackground(buttonBackground);

            final Animation animation = AnimationUtils.loadAnimation(XjqApplication.getContext(), R.anim.rocket_button_click);
            final Animation animation2 = AnimationUtils.loadAnimation(XjqApplication.getContext(), R.anim.rocket_button_click_one_right_fly);
            addWaterBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addFire){
                        flyOneTv.setText("+1");
                    }else{
                        flyOneTv.setText("-1");
                    }
                    addWaterBtn.startAnimation(animation);
                    flyOneTv.startAnimation(animation2);
                    if(onClickListener!=null){
                        onClickListener.onClick(v);
                    }

//                    flyOneTv.animate().translationY(0f).setDuration(0).start();
//                    flyOneTv.animate().translationY(-60f).alpha(1).setDuration(500).withEndAction(new Runnable() {
//                        @Override
//                        public void run() {
//                            flyOneTv.animate().alpha(0).setDuration(100).start();
//                            flyOneTv.setText("");
//                        }
//                    }).start();

                }
            });
        }
        addView(rocketClickButtonView);
    }

    /**
     * 此方法会在所有的控件都从xml文件中加载完成后调用
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }


    public void setPayNumAndType(String payNumAndType) {
        SpannableString spannableString = null;
        //获取子控件
        if(addFire){
            spannableString = new SpannableString("点火" + payNumAndType);
        }else{
            spannableString = new SpannableString("浇水" +payNumAndType);
        }
        RelativeSizeSpan sizeSpan01 = new RelativeSizeSpan(0.7f);
        spannableString.setSpan(sizeSpan01, 2, spannableString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        addWaterBtn.setText(spannableString);
    }
}