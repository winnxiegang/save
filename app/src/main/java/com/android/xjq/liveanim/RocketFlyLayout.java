package com.android.xjq.liveanim;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.AnimationListUtil;
import com.android.xjq.R;
import com.android.xjq.XjqApplication;
import com.android.xjq.dialog.live.RocketFlyPop;
import com.android.xjq.utils.UiUtils;

/**
 * Created by zaozao on 2018/3/6.
 */

public class RocketFlyLayout extends LinearLayout {

    ImageView flyFireImageIv;
    ImageView brustSuccessFogIv;
    ImageView failedFogIv;
    ImageView rocketIv;
    ImageView successFireIv;
    FrameLayout rocketAndFireLayout,flagLayout,flyLayout;
    ImageView titleFlagBgIv,successColorfulIv;
    ImageView rocketFailedWaterIv;
    private TextView titleTv;
    private View rocketFlyView;
    Animation animation,rotateAnim;
    MediaPlayer mediaPlayer;
    private float height;
    public RocketFlyLayout(Context context) {
        this(context, null);
    }

    public RocketFlyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RocketFlyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (rocketFlyView == null) {
            rocketFlyView = LayoutInflater.from(context).inflate(R.layout.view_rocket_fly, this,false);
            flyFireImageIv = (ImageView) rocketFlyView.findViewById(R.id.flyFireIv);//出现时候的火焰
            rocketIv = (ImageView)rocketFlyView.findViewById(R.id.rocketIv);//火箭
            brustSuccessFogIv = (ImageView)rocketFlyView.findViewById(R.id.successFogIv);//发射时的烟雾
            successFireIv = (ImageView)rocketFlyView.findViewById(R.id.successFireIv);//发射时的火焰
            titleFlagBgIv = (ImageView) rocketFlyView.findViewById(R.id.flagBgIv);//旗帜背景
            successColorfulIv= (ImageView) rocketFlyView.findViewById(R.id.successColorfulIv);//彩带
            rocketAndFireLayout = (FrameLayout) rocketFlyView.findViewById(R.id.rocketAndFireLayout);
            flagLayout= (FrameLayout) rocketFlyView.findViewById(R.id.flagLayout);
            flyLayout = (FrameLayout) rocketFlyView.findViewById(R.id.flyLayout);
            titleTv = (TextView) rocketFlyView.findViewById(R.id.titleTv);
            failedFogIv = (ImageView) rocketFlyView.findViewById(R.id.failedFogIv);//烟雾
            rocketFailedWaterIv = (ImageView) rocketFlyView.findViewById(R.id.rocketFailedWaterIv);//水
        }
        addView(rocketFlyView);
        animation = AnimationUtils.loadAnimation(XjqApplication.getContext(), R.anim.rocket_shake_anim);//4800
        rotateAnim = AnimationUtils.loadAnimation(XjqApplication.getContext(), R.anim.anim_rocket_rotat45);//4800
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(hasWindowFocus&&height==0){
            height = getHeight();
        }
    }



    //火箭落水
    public void startRocketFailedAnim(String title,final RocketFlyPop.AnimEndListener endListener){
        titleTv.setText(title);
        mediaPlayer =  MediaPlayer.create(XjqApplication.getContext(), R.raw.rocket_send_failed);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
//        LogUtils.e("kk", "U火箭在原本位置下的500dp),500)----"+UiUtils.dp2px(XjqApplication.getContext(),500)+"");
        final int flyheight = UiUtils.dp2px(XjqApplication.getContext(),500);
//        LogUtils.e("kk", "旗帜距离屏幕底部,310)----"+UiUtils.dp2px(XjqApplication.getContext(),310)+"");

        //旗帜升起后到达位置距离底部190dp   然后落下，距离底部30dp停止
        final float flagDownHeight = UiUtils.dp2px(XjqApplication.getContext(),310);
//        LogUtils.e("kk", "旗帜下降距离,310)----"+UiUtils.dp2px(XjqApplication.getContext(),310)+"");

        final int rocketToBottom = UiUtils.dp2px(XjqApplication.getContext(),350);
//        LogUtils.e("kk", "火箭距离屏幕底部,350)----"+UiUtils.dp2px(XjqApplication.getContext(),350)+"");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                LogUtils.e("kk",(-height/2-600)+"--------height/2-600"+"-------height/2-200:::;"+(-height/2-100));
                //火箭飞出来
                flyLayout.animate().translationY(-flyheight).setDuration(1500).start();
                //旗帜升起
                flagLayout.animate().translationY(-flyheight).setDuration(1500).start();
                //旗帜摆动
                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_flag, titleFlagBgIv, null, null, 8);
                // 飞行中的火焰 2400ms
                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_flying_fire, flyFireImageIv, null, null, 8);//2500

                //2400之后火箭下落，2400后冒黑烟
                flyLayout.postDelayed(new Runnable() {//延迟一秒后，火箭下落
                    @Override
                    public void run() {
                        flyFireImageIv.setVisibility(GONE);
                        failedFogIv.setVisibility(VISIBLE);
                        //冒黑烟
                        AnimationListUtil.startAnimationList(R.drawable.anim_rocket_black_fire, failedFogIv, null, null, 1);
                        rocketAndFireLayout.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rocketAndFireLayout.animate().translationY(-(rocketToBottom)).setDuration(2500).start();
                            }
                        },500);
                        rocketAndFireLayout.startAnimation(rotateAnim);

                    }
                },2400);


                //旗帜升起消耗1500，  停留2000后，也就是3000后
                flagLayout.postDelayed(new Runnable() {//3000ms后//旗帜下落
                    @Override
                    public void run() {//旗帜下落
                        //-height/2-100
                        flagLayout.animate().translationY(-flagDownHeight).setDuration(1500).withEndAction(new Runnable() {
                            @Override
                            public void run() {//水浪升起
                                rocketFailedWaterIv.setVisibility(VISIBLE);
                                flagLayout.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        flagLayout.animate().alpha(0).setDuration(1200).start();//旗帜消失
                                    }
                                },500);

                                //水
                                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_water, rocketFailedWaterIv, null, new Runnable() {
                                    @Override
                                    public void run() {
                                        rocketFailedWaterIv.setVisibility(GONE);
                                        endListener.end();
                                    }
                                }, 1);
                            }
                        }).start();
                    }
                },3500);
            }
        },800);

    }






    public void startSuccessAnim(String title,final RocketFlyPop.AnimEndListener endListener){
        titleTv.setText(title);
        mediaPlayer =  MediaPlayer.create(XjqApplication.getContext(), R.raw.rocket_send_success);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {//声音播放一秒钟后

                //旗帜升起
                flagLayout.animate().translationY(-(height/2+600)).setDuration(1000);
                //火箭升起
                flyLayout.animate().translationY(-(height/2+600)).setDuration(1000).withEndAction(new Runnable() {//火箭飞出来
                    @Override
                    public void run() {

                        rocketAndFireLayout.startAnimation(animation);
                    }
                }).start();
                //旗帜摆动8000ms
                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_flag, titleFlagBgIv, null, null, 20);
                // 飞行中的火焰   60*5 *10= 300*10 = 1500
                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_flying_fire, flyFireImageIv, null, new Runnable() {
                    @Override
                    public void run() {
                        flyFireImageIv.setVisibility(GONE);
                        successFireIv.setVisibility(VISIBLE);
                        //火苗爆发
                        AnimationListUtil.startAnimationList(R.drawable.anim_rocket_fire_brust, successFireIv, null,null, 1);
                    }
                }, 10);

            }
        },800);



        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {//左右晃动结束后

                brustSuccessFogIv.setVisibility(VISIBLE);

                // 发射烟雾
                AnimationListUtil.startAnimationList(R.drawable.anim_rocket_success_fog, brustSuccessFogIv, null, new Runnable() {
                    @Override
                    public void run() {
                        brustSuccessFogIv.animate().alpha(0).setDuration(100).start();
                    }
                }, 1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //火箭发射
                        rocketAndFireLayout.animate().translationY(-(height/2+600)).setDuration(1000).start();
                        successColorfulIv.setVisibility(VISIBLE);
                        // 彩带飘飘
                        AnimationListUtil.startAnimationList(R.drawable.anim_rocket_success_colorful, successColorfulIv, null, new Runnable() {
                            @Override
                            public void run() {
                                successColorfulIv.animate().alpha(0).setDuration(1000).start();
                                titleFlagBgIv.animate().alpha(0).setDuration(1000).start();
                                titleTv.animate().alpha(0).setDuration(1000).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        endListener.end();
                                    }
                                }).start();
                            }
                        }, 1);
                    }
                },300);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

}
