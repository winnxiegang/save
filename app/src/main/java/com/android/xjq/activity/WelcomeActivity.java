package com.android.xjq.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.banana.commlib.LoginInfoHelper;
import com.android.banana.commlib.base.BaseActivity;
import com.android.banana.commlib.bean.ErrorBean;
import com.android.banana.commlib.http.XjqRequestContainer;
import com.android.banana.commlib.utils.SharePreferenceUtils;
import com.android.banana.commlib.utils.StatusBarCompat;
import com.android.bughdupdate.UpdateConfigUtil;
import com.android.httprequestlib.HttpRequestHelper;
import com.android.httprequestlib.OnHttpResponseListener;
import com.android.httprequestlib.RequestContainer;
import com.android.xjq.R;
import com.android.xjq.activity.login.LoginActivity;
import com.android.xjq.bean.live.main.AdInfo;
import com.android.xjq.presenters.InitBusinessHelper;
import com.android.xjq.presenters.LoginHelper2;
import com.android.xjq.presenters.viewinface.UserLoginView;
import com.android.xjq.utils.XjqUrlEnum;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.android.banana.commlib.utils.SharePreferenceUtils.IS_PLAY_VIDEO;
import static com.android.xjq.utils.XjqUrlEnum.CLIENT_START_GRAPH_QUERY;

public class WelcomeActivity extends BaseActivity implements OnHttpResponseListener, UserLoginView {

    private final int WELCOME_DURATION = 3000;

    private final int AD_DURATION = 3000;

    private final int WELCOME_ANIMATION_DURATION = 1000;

    private final int TIME_INTERVALS = 1000;

    @BindView(R.id.adIv)
    SimpleDraweeView adIv;

    @BindView(R.id.welcomeIv)
    ImageView welcomeIv;

    @BindView(R.id.adLayout)
    FrameLayout adLayout;

    @BindView(R.id.countDownTimeTv)
    TextView countDownTimeTv;

    @BindView(R.id.contentLayout)
    RelativeLayout contentLayout;

    @BindView(R.id.videoView)
    VideoView videoView;

    private HttpRequestHelper httpRequestHelper;

    private Handler handler = new Handler();

    private boolean isCacheSuccess = false;

    private AdInfo adInfo = new AdInfo();

    private AlphaAnimation welcomeAnimation;

    private CountDownTimer countDownTimer;

    private boolean clickAd = false;

    private boolean clickSkip = false;

    private boolean haveFinished = false;

    private LoginHelper2 mLoginHelper;

    /**
     * 是否播放视频
     */
    private boolean isPlayVideo;
    private String channelId;

    private Unbinder unbinder;

    @Override
    protected void onResume() {
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        super.onResume();
    }

    @OnClick(R.id.countDownTimeTv)
    public void clickSkip() {
        clickSkip = true;
        countDownTimer.cancel();
        toMainActivity();
    }

    @OnClick(R.id.welcomeIv)
    public void welcomeClick() {
        //欢迎界面什么都不做，防止点击到广告页
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        StatusBarCompat.hideSystemUI(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        unbinder = ButterKnife.bind(this);

        getIntentData();

        mLoginHelper = new LoginHelper2(this);

        httpRequestHelper = new HttpRequestHelper(null, this);

        isPlayVideo = (boolean) SharePreferenceUtils.getData(IS_PLAY_VIDEO, false);

        if (isPlayVideo) {
            startShowVideo();
        }

        initJudge();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            channelId = uri.getQueryParameter("channelId");
        }
    }

    private void initJudge() {
        //如果当前
        if (channelId == null || !InitBusinessHelper.isHasInitApp()) {
            if (LoginInfoHelper.getInstance().isLogin()) {
                mLoginHelper.autonmLogin();
            } /*else {
                LoginMainActivity.startLoginMainActivity(this);
            }*/
        }
        startShowAd();

    }

    private void initAnimation() {

        welcomeAnimation = new AlphaAnimation(1, 0);

        welcomeAnimation.setDuration(WELCOME_ANIMATION_DURATION);

        welcomeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                welcomeIv.setVisibility(View.GONE);
                adLayout.setVisibility(View.VISIBLE);
                startCountDownTime();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void toMainActivity() {
        new UpdateConfigUtil().checkUpdate(WelcomeActivity.this.getApplicationContext(), false);

        stopAnimation();

        if (!LoginInfoHelper.getInstance().isLogin()) {
            LoginActivity.startLoginActivity(WelcomeActivity.this);
        } else if (channelId != null) {
            LiveActivity.startLiveActivity(this, Integer.valueOf(channelId));
        } else {
            MainActivity.startMainActivity(this);
        }
        finish();
    }

    private void startCountDownTime() {
        welcomeAnimation = null;
        countDownTimer = new CountDownTimer(AD_DURATION, TIME_INTERVALS) {
            @Override
            public void onTick(long millisUntilFinished) {
                countDownTimeTv.setText(millisUntilFinished / TIME_INTERVALS + "跳过");
            }

            @Override
            public void onFinish() {
                if (!clickAd && !clickSkip) {
                    toMainActivity();
                }

            }
        }.start();
    }

    /**
     * 如果是gif广告，需要关闭动画
     */
    private void stopAnimation() {
        if (adIv.getController() == null) {
            return;
        }
        Animatable animatable = adIv.getController().getAnimatable();
        if (animatable != null && animatable.isRunning()) {
            animatable.stop();
        }
    }

    private void startRequest() {
        XjqRequestContainer map = new XjqRequestContainer(CLIENT_START_GRAPH_QUERY, false);
        map.put("clientType", "ANDROID");
        httpRequestHelper.startRequest(map, false);
    }

    private Runnable toNextActivityRunnable = new Runnable() {
        @Override
        public void run() {
            if (isCacheSuccess) {
                return;
            } else {
                haveFinished = true;
                toMainActivity();
            }
        }
    };


    private void responseSuccessAdvertisementInfo(JSONObject jo) throws JSONException {

        handleAdData(jo);

        if (adInfo.getUrl() == null) {
            isCacheSuccess = false;
            return;
        }

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(Uri.parse(adInfo.getUrl()))
                .setControllerListener(new ControllerListener<ImageInfo>() {
                    @Override
                    public void onSubmit(String id, Object callerContext) {

                    }

                    @Override
                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                        isCacheSuccess = true;
                        if (haveFinished) {
                            return;
                        }
                        welcomeIv.startAnimation(welcomeAnimation);
                    }

                    @Override
                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onIntermediateImageFailed(String id, Throwable throwable) {
                        //progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {

                    }

                    @Override
                    public void onRelease(String id) {

                    }
                })
                .setAutoPlayAnimations(true)// 设置加载图片完成后是否直接进行播放
                .build();

        adIv.setController(draweeController);

    }

    private void handleAdData(JSONObject jo) throws JSONException {
        if (jo.has("imageUrl")) {
            String imageUrl = jo.getString("imageUrl");
            adInfo.setUrl(imageUrl);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoginHelper.detachView();
        mLoginHelper = null;
        videoView = null;
        adIv.setImageBitmap(null);
        welcomeIv.setImageBitmap(null);
        unbinder.unbind();
        unbinder = null;
        handler.removeCallbacks(toNextActivityRunnable);
        handler = null;
        toNextActivityRunnable = null;
        countDownTimer = null;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void executorSuccess(RequestContainer requestContainer, JSONObject jsonObject) {
        try {
            switch (((XjqUrlEnum) requestContainer.getRequestEnum())) {
            /*case HOME_PAGE:
                responseSuccessAdvertisementInfo(jo);
                break;*/
                case CLIENT_START_GRAPH_QUERY:
                    responseSuccessAdvertisementInfo(jsonObject);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executorFalse(RequestContainer requestContainer, JSONObject jsonObject) {

    }

    @Override
    public void executorFailed(RequestContainer requestContainer) {

    }

    @Override
    public void executorFinish() {

    }

    @Override
    public void authLoginSuccess(boolean userTagEnabled) {

    }

    @Override
    public void authLoginFailed(RequestContainer requestContainer, JSONObject jsonObject) {
        toMainActivity();
    }

    @Override
    public void showErrorMsg(JSONObject jsonObject) {
        try {
            ErrorBean bean = new ErrorBean(jsonObject);

            String name = bean.getError().getName();

            if ("LOGIN_ELSEWHERE".equals(name)) {
                return;
            }
            operateErrorResponseMessage(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void anonymousLoginSuccess() {

    }

    private void startShowAd() {
        if (isPlayVideo) {
            return;
        }
        welcomeIv.setVisibility(View.VISIBLE);
        adLayout.setVisibility(View.VISIBLE);
        startRequest();
        handler.postDelayed(toNextActivityRunnable, WELCOME_DURATION);
        initAnimation();
    }

    private void startShowVideo() {
        videoView.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams LayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置相对于父布局四边对齐
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        LayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //为VideoView添加属性
        videoView.setLayoutParams(LayoutParams);

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.icon_first_enter_app_video);

        //设置视频路径
        videoView.setVideoURI(uri);

        //播放完成回调
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                SharePreferenceUtils.putData(IS_PLAY_VIDEO, false);

                MainActivity.startMainActivity(WelcomeActivity.this);

                finish();
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {

                welcomeIv.setVisibility(View.GONE);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isPlayVideo) {
            //启动视频播放
            videoView.start();
            //设置获取焦点
            videoView.setFocusable(true);
        }
    }
}
