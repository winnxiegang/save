package com.android.xjq.controller.live;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.banana.commlib.utils.picasso.PicUtils;
import com.android.banana.commlib.view.loading.DownLoadBar;
import com.android.banana.commlib.view.loading.ProgressListener;
import com.android.banana.commlib.view.pk.PKProgressView;
import com.android.library.Utils.LogUtils;
import com.android.xjq.R;
import com.android.xjq.activity.LiveActivity;
import com.android.xjq.adapter.live.LiveHeaderGuestAdapter;
import com.android.xjq.bean.live.AnchorUserInfoClientSimple;
import com.android.xjq.bean.live.ChannelUserBean;
import com.android.xjq.bean.live.EnterRoomBean;
import com.android.xjq.bean.live.LiveCommentBean;
import com.android.xjq.controller.GestureDetectorController;
import com.android.xjq.dialog.live.PersonalInfoDialog;
import com.android.xjq.dialog.live.PublishVideoDialog;
import com.android.xjq.dialog.popupwindow.DropHostPop;
import com.android.xjq.model.live.CurLiveInfo;
import com.android.xjq.utils.SocialTools;
import com.android.xjq.utils.live.BitmapBlurHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhouyi on 2017/3/7.
 */

public class LivePortraitController extends BaseLiveController<LiveActivity> implements View.OnClickListener {

    private PortraitViewHolder mViewHolder;

    private static final int SHOW_COMMENT = 0;

    private static final int SHOW_WALL = 1;

    private static final int SHOW_ATTITUDE = 2;

    private static final int SHOW_CELEBRITY_LIST = 3;

    private static final int SHOW_GUEST = 4;

    private String[] mTitles = new String[]{"公屏", "上墙", "态度", "榜单", "贵宾"};

    private int[] mTags = new int[]{SHOW_COMMENT,
            SHOW_WALL,
            SHOW_ATTITUDE,
            SHOW_CELEBRITY_LIST,
            SHOW_GUEST
    };

    private LayoutInflater layoutInflater;
    private View[] mPagerView = new View[5];
    private LiveMainController mMainController;
    private LiveGuestController mGuestController;
    private LiveCelebrityController mCelebrityController;
    private LiveWallController mWallController;
    private LiveAttitudeController mAttitudeController;
    private BaseLiveController currentPortraitController;
    private List<BaseLiveController> mControllerList;
    private List<ChannelUserBean> mUserInfoList = new ArrayList<>();
    private List<AnchorUserInfoClientSimple> anchorUserInfoList = new ArrayList<>();
    //主主播信息
    public AnchorUserInfoClientSimple firstAnchorUserInfo;
    // 是否关闭视频
    private boolean isCloseVideo;
    // 手势识别,控制功能快捷点的显示与隐藏
    private GestureDetectorController gestureDetectorController;
    private LiveHeaderGuestAdapter headerGuestAdapter;
    //录屏时长
    private long recordScreenDuration;

    public BaseLiveController getCurrentPortraitController() {
        return currentPortraitController;
    }

    public LiveMainController getMainController() {
        return mMainController;
    }

    public LiveGuestController getGuestController() {
        return mGuestController;
    }

    public LiveCelebrityController getCelebrityController() {
        return mCelebrityController;
    }

    public LiveWallController getWallController() {
        return mWallController;
    }

    public LiveAttitudeController getAttitudeController() {
        return mAttitudeController;
    }

    public LivePortraitController(LiveActivity context) {
        super(context);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void init(View view) {
        if (mViewHolder == null) {
            mViewHolder = new PortraitViewHolder(view);
            initPagerView();
            setupController();
            setViewPager();
            setTabLayout();
            setListener();
        }
    }


    private void setupController() {
        mMainController = new LiveMainController(context);
        mWallController = new LiveWallController(context);
        mAttitudeController = new LiveAttitudeController(context);
        mCelebrityController = new LiveCelebrityController(context);
        mGuestController = new LiveGuestController(context);
        mControllerList = new ArrayList<>();
        mControllerList.add(mMainController);
        mControllerList.add(mWallController);
        mControllerList.add(mAttitudeController);
        mControllerList.add(mCelebrityController);
        mControllerList.add(mGuestController);

        mMainController.init(mPagerView[SHOW_COMMENT]);
        mWallController.init(mPagerView[SHOW_WALL]);
        mAttitudeController.init(mPagerView[SHOW_ATTITUDE]);
        mCelebrityController.init(mPagerView[SHOW_CELEBRITY_LIST]);
        mGuestController.init(mPagerView[SHOW_GUEST]);

        //设置Header控制器
    }

    SocialTools.onSocialCallback onSocialCallback = new SocialTools.onSocialCallback() {
        @Override
        public void onResponse(JSONObject response, boolean success) {
            if (success) {
                firstAnchorUserInfo.focus = !firstAnchorUserInfo.focus;
                if (mViewHolder.focusTv != null)
                    mViewHolder.focusTv.setText(firstAnchorUserInfo.focus ? "取消" : "关注");
            } else {
                try {
                    if (context != null)
                        context.operateErrorResponseMessage(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.focusTv:
                if (firstAnchorUserInfo == null) return;
                if (firstAnchorUserInfo.focus) {
                    SocialTools.cancelAttention(firstAnchorUserInfo.userId, "FOLLOWCANCEL", onSocialCallback);
                } else {
                    SocialTools.payAttention(firstAnchorUserInfo.userId, onSocialCallback);
                }
                break;
            case R.id.dropDownIv:
                if (anchorUserInfoList != null && anchorUserInfoList.size() > 1) {
                    DropHostPop dropHostPop = new DropHostPop(context);
                    dropHostPop.showPop(anchorUserInfoList, mViewHolder.anchorInfoLayout);
                }
                break;
            case R.id.shareIv:
                context.showShare();
                break;
            case R.id.scaleIv:
                if (isCloseVideo) {
                    return;
                }
                context.setRequestedOrientation(
                        context.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE ?
                                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.closeIv:
                context.showFloatWindow(false);
                break;

            case R.id.showPeopleLayout:
                context.showAudience(false);
                break;
            case R.id.closeVideoIv:
                isCloseVideo = !isCloseVideo;
                if (!CurLiveInfo.isAnchorPushStream()) {
                    return;
                }
                DraweeController controller = mViewHolder.audioPlayIv.getController();
                Animatable animatable = controller.getAnimatable();
                if (isCloseVideo) {
                    mViewHolder.audioPlayLayout.setVisibility(View.VISIBLE);
                    //加载头像
                    if (CurLiveInfo.getHostAvator() != null) {
                        Picasso.with(context).load(CurLiveInfo.getHostAvator()).into(mViewHolder.portraitIv);
//                        getFrescoCacheBitmap(handler, uri, context);
                        getPicassoCacheBitmap();
                        if (animatable != null) animatable.start();
                    }
                    context.cancelAllView();
                } else {
                    mViewHolder.audioPlayLayout.setVisibility(View.GONE);
                    if (animatable != null) animatable.stop();
                    context.showView();
                }
                break;
            case R.id.cancelRecordTv:
                mViewHolder.recordLayout.setVisibility(View.GONE);
                break;
            case R.id.recordProgressBar:
                if (recordScreenDuration != 0 && recordScreenDuration < 3 * 1000)
                    return;
                mViewHolder.recordLayout.setVisibility(View.VISIBLE);
                mViewHolder.cancelRecordTv.setVisibility(View.INVISIBLE);
                mViewHolder.recordTv.setVisibility(View.INVISIBLE);
                context.controlRecord();
                controlRecordProgress(true, false);
                break;
            case R.id.anchorInfoLayout:
                if (firstAnchorUserInfo == null) return;
                new PersonalInfoDialog(context, firstAnchorUserInfo.userId, new PersonalInfoDialog.Callback() {
                    @Override
                    public void focusStatusChanged(String userId, boolean isFocus) {
                        firstAnchorUserInfo.focus = isFocus;
                        if (mViewHolder.focusTv != null)
                            mViewHolder.focusTv.setText(firstAnchorUserInfo.focus ? "取消" : "关注");
                        context.getLiveLandscapeController().firstAnchorUserInfo.focus = isFocus;
                    }
                }).show();
                break;
            case R.id.pk_progress_view:
                getMainController().showPkView();
                break;
        }
    }

    private void setListener() {
        mViewHolder.dropDownIv.setOnClickListener(this);
        mViewHolder.shareIv.setOnClickListener(this);
        mViewHolder.scaleIv.setOnClickListener(this);
        mViewHolder.closeIv.setOnClickListener(this);
        mViewHolder.showPeopleLayout.setOnClickListener(this);
        mViewHolder.cancelRecordTv.setOnClickListener(this);
        mViewHolder.recordProgressBar.setOnClickListener(this);
        mViewHolder.closeVideoIv.setOnClickListener(this);
        mViewHolder.anchorInfoLayout.setOnClickListener(this);
        mViewHolder.pkProgressView.setOnClickListener(this);
        mViewHolder.focusTv.setOnClickListener(this);

        mViewHolder.recordProgressBar.setProgressListener(new ProgressListener() {
            @Override
            public void pause(long mCurrentProgress, long maxProgress) {
                LogUtils.e("RecordScreen", "停止录屏....");
                context.controlRecord();
            }

            @Override
            public void progressing(long currentProgress, long maxProgress) {
                //context.stopScreenRecord();
                recordScreenDuration = currentProgress;
                LogUtils.e("RecordScreen", "mCurrentProgress =" + currentProgress);
            }
        });

        gestureDetectorController = new GestureDetectorController(context, mViewHolder.functionView);

        mViewHolder.floatLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetectorController.setTouchEvent(event, isCloseVideo);
                return true;
            }
        });
        setImageView(mViewHolder.audioPlayIv, "res://" + context.getPackageName() + "/" + R.drawable.icon_audio_play_bg, false);
//        setImageView(mViewHolder.preparePlayIv, "res://" + context.getPackageName() + "/" + R.drawable.icon_video_loading, true);
    }

    /**
     * 获取Picasso保存的bitmap
     */
    private void getPicassoCacheBitmap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = null;
                try {
                    bitmap = Picasso.with(context).load(CurLiveInfo.getHostAvator()).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.obj = bitmap;
                handler.sendMessage(message);
            }
        }).start();

    }

    public void showMessage(LiveCommentBean bean) {
        mMainController.addMessage(bean);
    }

    /**
     * 模糊图像,显示背景
     */
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bitmap bitmap = (Bitmap) msg.obj;
            if (bitmap != null) {
                Bitmap bm = BitmapBlurHelper.doBlur(context, bitmap, 4);
                mViewHolder.audioPlayBgIv.setImageBitmap(bm);
            }
        }
    };

    private void setTabLayout() {
        mViewHolder.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTitles.length; i++) {
            TabLayout.Tab tab = mViewHolder.tabLayout.newTab();
            tab.setText(mTitles[i]);
            tab.setTag(mTags[i]);
            mViewHolder.tabLayout.addTab(tab);
        }

        mViewHolder.tabLayout.setupWithViewPager(mViewHolder.viewPager);
    }

    private void initPagerView() {
        View comment = layoutInflater.inflate(R.layout.item_live_comment, null);
        View wall = layoutInflater.inflate(R.layout.item_live_wall, null);
        View attitude = layoutInflater.inflate(R.layout.item_live_attitude, null);
        View celebrityList = layoutInflater.inflate(R.layout.item_live_celebrity_list, null);
        View guest = layoutInflater.inflate(R.layout.item_live_guest, null);
        mPagerView[SHOW_COMMENT] = comment;
        mPagerView[SHOW_WALL] = wall;
        mPagerView[SHOW_ATTITUDE] = attitude;
        mPagerView[SHOW_CELEBRITY_LIST] = celebrityList;
        mPagerView[SHOW_GUEST] = guest;

        mViewHolder.guestRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        headerGuestAdapter = new LiveHeaderGuestAdapter(context, mUserInfoList);
        mViewHolder.guestRecyclerView.setAdapter(headerGuestAdapter);
        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_from_right);
        mViewHolder.guestRecyclerView.setLayoutAnimation(controller);
        //着色
        Drawable mutate = ContextCompat.getDrawable(context, R.drawable.ic_close_x).mutate();
        DrawableCompat.setTint(mutate, Color.WHITE);
        mViewHolder.cancelRecordTv.setCompoundDrawablesWithIntrinsicBounds(mutate, null, null, null);

    }

    private void setViewPager() {

        mViewHolder.viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                if (mPagerView == null) return 0;
                return mPagerView.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = mPagerView[position];
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                View view = mPagerView[position];
                container.removeView(view);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }
        });

        mViewHolder.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case SHOW_COMMENT://显示评论
                        currentPortraitController = mMainController;
                        context.getGiftController().showDoubleHitShow();
                        context.setPrizedShowView(true);
                        break;
                    case SHOW_WALL://显示上墙
                        currentPortraitController = mWallController;
                        context.getGiftController().hideDoubleHitShow();
                        context.setPrizedShowView(false);
                        break;
                    case SHOW_ATTITUDE://显示态度
                        currentPortraitController = mAttitudeController;
                        context.getGiftController().hideDoubleHitShow();
                        context.setPrizedShowView(false);
                        break;
                    case SHOW_CELEBRITY_LIST://显示榜单
                        currentPortraitController = mCelebrityController;
                        context.getGiftController().hideDoubleHitShow();
                        context.setPrizedShowView(false);
                        break;
                    case SHOW_GUEST://显示贵宾
                        currentPortraitController = mGuestController;
                        context.getGiftController().hideDoubleHitShow();
                        context.setPrizedShowView(false);
                        break;
                }
                hideViewChange();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void hideViewChange() {
        if (mControllerList != null) {
            for (BaseLiveController baseLiveController : mControllerList) {
                baseLiveController.onHiddenChanged(!baseLiveController.equals(currentPortraitController));
            }
        }
    }

    public void changePortrait() {
        mMainController.changePortrait();
    }

    @Override
    public void setView() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewHolder.viewPager.removeAllViews();
        mPagerView = null;
        mMainController.onDestroy();
        mGuestController.onDestroy();
        mCelebrityController.onDestroy();
        mWallController.onDestroy();
        mAttitudeController.onDestroy();
        currentPortraitController = null;
        gestureDetectorController.onDestroy();
        mViewHolder = null;
    }

    public void readyRecord() {
        mViewHolder.recordLayout.setVisibility(View.VISIBLE);
        mViewHolder.recordProgressBar.setVisibility(View.VISIBLE);
        mViewHolder.cancelRecordTv.setVisibility(View.VISIBLE);
        mViewHolder.recordTv.setVisibility(View.VISIBLE);
    }

    public boolean recordViewIsVisibility() {
        return mViewHolder.recordLayout.getVisibility() == View.VISIBLE && mViewHolder.recordProgressBar.getVisibility() == View.VISIBLE;
    }

    public void controlRecordProgress(final boolean isLoading, final boolean isShowPublishView) {
        //录屏完成时间重置
        if (!isLoading && isShowPublishView) recordScreenDuration = 0;
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mViewHolder.recordProgressBar.controlProgress(isLoading);
                mViewHolder.recordProgressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
                if (isShowPublishView) {
                    PublishVideoDialog dialog = new PublishVideoDialog(context);
                    dialog.show();
                }
            }
        });
    }

    public void changePeopleCountTv(long userCount) {
        mViewHolder.peopleNumTv.setText(String.valueOf(userCount));
    }

    public void setAnchorView(EnterRoomBean enterRoomBean) {
        List<ChannelUserBean> channelAreaUserInfoList = enterRoomBean.getChannelAreaUserInfoList();
        if (channelAreaUserInfoList != null && channelAreaUserInfoList.size() != 0) {
            mUserInfoList.clear();
            mUserInfoList.addAll(channelAreaUserInfoList);
            headerGuestAdapter.notifyDataSetChanged();
            //mViewHolder.guestRecyclerView.scheduleLayoutAnimation();
        }
        AnchorUserInfoClientSimple firstAnchorUserInfo = enterRoomBean.getFirstAnchorUserInfo();
        anchorUserInfoList = enterRoomBean.getAnchorUserInfoList();
        this.firstAnchorUserInfo = firstAnchorUserInfo;
        if (this.firstAnchorUserInfo != null) {
            //主主播
            PicUtils.load(context.getApplicationContext(), mViewHolder.hostIv,
                    this.firstAnchorUserInfo.userLogoUrl, R.drawable.user_default_logo);
            mViewHolder.hostNameTv.setText(this.firstAnchorUserInfo.userName);
            mViewHolder.fansTv.setText(String.format(context.getString(R.string.fans_number),
                    "" + this.firstAnchorUserInfo.followMyUserCount));
        }

        if (anchorUserInfoList != null && anchorUserInfoList.size() == 1) {
            mViewHolder.dropDownIv.setVisibility(View.GONE);
            mViewHolder.focusTv.setVisibility(View.VISIBLE);
            mViewHolder.focusTv.setText(firstAnchorUserInfo.focus ? "取消" : "关注");
        } else {
            mViewHolder.dropDownIv.setVisibility(View.VISIBLE);
            mViewHolder.focusTv.setVisibility(View.GONE);
        }

        getMainController().showFastFunction();
    }

    /**
     * @param isHideFunction 是否隐藏直播时的功能按钮(分享/全屏/切换音视频等)
     */
    public void controlLiveFunctionHide(boolean isHideFunction) {
        mViewHolder.floatLayout.setVisibility(isHideFunction ? View.INVISIBLE : View.VISIBLE);
    }

    public void hide() {
        mViewHolder.portraitLayout.setVisibility(View.GONE);
    }

    public void show() {
        mViewHolder.portraitLayout.setVisibility(View.VISIBLE);
    }

    public void pushStreamStatusChange(Boolean pushStreamStatus) {
        if (pushStreamStatus) {
            mViewHolder.closeVideoIv.setChecked(false);
            mViewHolder.closeVideoIv.setEnabled(true);
        } else {
            mViewHolder.audioPlayLayout.setVisibility(View.GONE);
            mViewHolder.closeVideoIv.setChecked(true);
            mViewHolder.closeVideoIv.setEnabled(false);
        }
    }


    private void setImageView(SimpleDraweeView audioPlayIv, String url, boolean autoPlayAnimation) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(autoPlayAnimation)
                .setUri(uri)
                .build();
        audioPlayIv.setController(controller);
    }

    public void receiveFirstFrame() {
        mViewHolder.closeVideoIv.setChecked(false);
        mViewHolder.closeVideoIv.setEnabled(true);
//        gestureDetectorController.hideFunctionView();
    }

    public void refreshPKProgress(int leftMoney, String leftUrl, int rightMoney, String rightUrl) {
        mViewHolder.pkProgressView.setVisibility(View.VISIBLE);
        mViewHolder.pkProgressView.setLeftImageUrl(leftUrl);
        mViewHolder.pkProgressView.setRightImageUrl(rightUrl);
        mViewHolder.pkProgressView.setLeftMoney(leftMoney);
        mViewHolder.pkProgressView.setRightMoney(rightMoney);
        mViewHolder.pkProgressView.setSeekBarProgress(leftMoney, rightMoney);
    }

    public void hidePKProgressBar() {
        mViewHolder.pkProgressView.setVisibility(View.GONE);
    }


    static class PortraitViewHolder {
        @BindView(R.id.closeIv)
        ImageView closeIv;
        @BindView(R.id.closeVideoIv)
        CheckBox closeVideoIv;
        @BindView(R.id.scaleIv)
        ImageView scaleIv;
        @BindView(R.id.peopleNumTv)
        TextView peopleNumTv;
        @BindView(R.id.showPeopleLayout)
        LinearLayout showPeopleLayout;
        @BindView(R.id.floatLayout)
        RelativeLayout floatLayout;
        @BindView(R.id.tabLayout)
        TabLayout tabLayout;
        @BindView(R.id.viewPager)
        ViewPager viewPager;
        @BindView(R.id.portraitLayout)
        LinearLayout portraitLayout;
        @BindView(R.id.functionView)
        RelativeLayout functionView;
        @BindView(R.id.audioPlayLayout)
        FrameLayout audioPlayLayout;
        @BindView(R.id.audioPlayIv)
        SimpleDraweeView audioPlayIv;
        @BindView(R.id.audioPlayBgIv)
        ImageView audioPlayBgIv;
        @BindView(R.id.portraitIv)
        ImageView portraitIv;
        @BindView(R.id.shareIv)
        ImageView shareIv;

        @BindView(R.id.guestRecyclerView)
        RecyclerView guestRecyclerView;
        @BindView(R.id.hostIv)
        ImageView hostIv;
        @BindView(R.id.hostNameTv)
        TextView hostNameTv;
        @BindView(R.id.fansTv)
        TextView fansTv;
        @BindView(R.id.dropDownIv)
        ImageView dropDownIv;
        @BindView(R.id.focusTv)
        TextView focusTv;
        @BindView(R.id.liveHostInfoLayout)
        LinearLayout liveHostInfoLayout;
        @BindView(R.id.recordLayout)
        LinearLayout recordLayout;
        @BindView(R.id.cancelRecordTv)
        TextView cancelRecordTv;
        @BindView(R.id.recordProgressBar)
        DownLoadBar recordProgressBar;
        @BindView(R.id.recordTv)
        TextView recordTv;
        @BindView(R.id.anchorInfoLayout)
        RelativeLayout anchorInfoLayout;
        @BindView(R.id.pk_progress_view)
        PKProgressView pkProgressView;

        PortraitViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
