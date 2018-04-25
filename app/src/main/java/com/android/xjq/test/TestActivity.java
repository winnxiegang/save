package com.android.xjq.test;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.dialog.CustomDialog;
import com.android.banana.commlib.view.VerticalScrollTextView2;
import com.android.xjq.R;
import com.android.xjq.dialog.live.LimitHandSpeedDialog;
import com.android.xjq.view.MySeekBar;
import com.jzxiang.pickerview.adapters.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by lingjiu on 2017/6/13.
 */

public class TestActivity extends BaseActivity implements View.OnClickListener {

    private NumericWheelAdapter mAdapter;
    private MySeekBar seekBar;
    private EditText et;
    private ImageView marqueeIv1, marqueeIv2;
    private View marqueeLayout;
    private VerticalScrollTextView2 verticalTv;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.at_test);
        setTitleBar(true, "测试");
        ButterKnife.bind(this);
        et = ((EditText) findViewOfId(R.id.et));
        ((View) findViewOfId(R.id.btn_scroll_up)).setOnClickListener(this);
        ((View) findViewOfId(R.id.startAnimTv)).setOnClickListener(this);
        marqueeIv1 = ((ImageView) findViewOfId(R.id.marqueeIv1));
        marqueeIv2 = ((ImageView) findViewOfId(R.id.marqueeIv2));
        marqueeLayout = ((View) findViewOfId(R.id.marqueeLayout));

        verticalTv = findViewOfId(R.id.verticalTv);

        List<Text> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.add(new Text("数据" + i));
        }
        verticalTv.addMsg(datas);
    }

    public class Text implements VerticalScrollTextView2.IGeneralNotice {
        String text;

        public Text(String text) {
            this.text = text;
        }

        @Override
        public CharSequence getText() {
            return text;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scroll_up:
                new LimitHandSpeedDialog(this, null, null, 0).show();

                new CustomDialog(TestActivity.this).show();
                //wheelView.setCurrentItem(3, true);
              /*  int channelId;
                try {
                    channelId = Integer.valueOf(et.getText().toString());
                } catch (Exception e) {
                    channelId = 100091;
                }

                LiveActivity.startLiveActivity(TestActivity.this, channelId);*/
               /* LiveCouponPop liveCouponPop = new LiveCouponPop(this, SCREEN_ORIENTATION_PORTRAIT,
                        CouponPlatformType.CHANNEL.name(), "20024");
                liveCouponPop.show();*/
                break;
            case R.id.startAnimTv:


                break;
        }
    }

    private static final long ANIM_DURATION = 8 * 1000;

    private void startTranslateAnim(final View view, final long delayTime) {
        final TranslateAnimation translateAnimation =
                new TranslateAnimation(
                        Animation.RELATIVE_TO_SELF, -1,
                        Animation.RELATIVE_TO_SELF, 1,
                        Animation.RELATIVE_TO_SELF, 0f,
                        Animation.RELATIVE_TO_SELF, 0f
                );
        translateAnimation.setFillAfter(false);
        translateAnimation.setDuration(ANIM_DURATION);
        translateAnimation.setRepeatCount(Integer.MAX_VALUE);
        translateAnimation.setRepeatMode(Animation.RESTART);
        translateAnimation.setInterpolator(new LinearInterpolator());
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.startAnimation(translateAnimation);
            }
        }, delayTime);
    }

}
