package com.android.xjq.view.banner;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.xjq.R;
import com.android.library.Utils.LogUtils;

import java.util.ArrayList;

/**
 * 广告图片自动轮播控件</br>
 * <p/>
 * <pre>
 *   集合ViewPager和指示器的一个轮播控件，主要用于一般常见的广告图片轮播，具有自动轮播和手动轮播功能
 *   使用：只需在xml文件中使用{@code <com.minking.imagecycleview.ImageCycleView/>} ，
 *   然后在页面中调用  {@link #setImageResources(ArrayList, ImageCycleViewListener) }即可!
 *
 *   另外提供{@link #startImageCycle() } \ {@link #pushImageCycle() }两种方法，用于在Activity不可见之时节省资源；
 *   因为自动轮播需要进行控制，有利于内存管理
 * </pre>
 */
public class ImageCycleView extends LinearLayout {

    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 图片轮播视图
     */
    private CycleViewPager mBannerPager = null;

    /**
     * 滚动图片视图适配器
     */
    private ImageCycleAdapter mAdvAdapter;

    /**
     * 图片轮播指示器控件
     */
    private ViewGroup mIndicator;

    private LinearLayout bannerTitleLL;

    /**
     * 图片轮播指示器-个图
     */
    private View mImageView = null;

    /**
     * 图片轮播指示器-个图
     */
    private TextView bannerTitle = null;

    /**
     * 滚动图片指示器-视图列表
     */
    private View[] mImageViews = null;

    /**
     * 图片滚动当前图片下标
     */
    private int mImageIndex = 1;

    /**
     * 手机密度
     */
    private float mScale;
    private ArrayList<String> infoList;

    /**
     * @param context
     */
    public ImageCycleView(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public ImageCycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        LayoutInflater.from(context).inflate(R.layout.view_banner_content, this);
        mBannerPager = (CycleViewPager) findViewById(R.id.pager_banner);
        bannerTitleLL= (LinearLayout) findViewById(R.id.bannerTitleLL);
        bannerTitle = (TextView) findViewById(R.id.bannerTitle);
        mBannerPager.setOnPageChangeListener(new GuidePageChangeListener());
        mBannerPager.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        // 开始图片滚动
                        startImageTimerTask();
                        break;
                    default:
                        // 停止图片滚动
                        stopImageTimerTask();
                        break;
                }
                return false;
            }
        });
        // 滚动图片右下指示器视图
        mIndicator = (ViewGroup) findViewById(R.id.ads_indicator);
    }


    /**
     * 装填图片数据
     *
     * @param infoList
     * @param imageCycleViewListener
     */
    public void setImageResources(ArrayList<String> infoList, ImageCycleViewListener imageCycleViewListener) {

        this.infoList = infoList;
        // 清除所有子视图
        mIndicator.removeAllViews();

        // 图片广告数量
        final int imageCount = infoList.size();

        mImageViews = new View[imageCount];

        for (int i = 0; i < imageCount; i++) {

            mImageView = new View(mContext);

            LayoutParams layout = new LayoutParams(10, 10);

            layout.setMargins(0, 0, 10, 0);

            mImageView.setLayoutParams(layout);

            mImageViews[i] = mImageView;

            if (i == 0) {

                mImageViews[i].setBackgroundResource(R.drawable.dot_selected);

            } else {

                mImageViews[i].setBackgroundResource(R.drawable.dot_normal);

            }

            mIndicator.addView(mImageViews[i]);
        }
        if(infoList.size()==1&&infoList!=null){


//            bannerTitle.setText(infoList.get(0).getTitle());
        }

        mAdvAdapter = new ImageCycleAdapter(mContext, infoList, imageCycleViewListener);

        mBannerPager.setAdapter(mAdvAdapter);

        startImageTimerTask();
    }

    /**
     * 开始轮播(手动控制自动轮播与否，便于资源控制)
     */
    public void startImageCycle() {
        startImageTimerTask();
    }

    /**
     * 暂停轮播——用于节省资源
     */
    public void pushImageCycle() {
        stopImageTimerTask();
    }

    /**
     * 开始图片滚动任务
     */
    private void startImageTimerTask() {
        stopImageTimerTask();
        // 图片每3秒滚动一次
        mHandler.postDelayed(mImageTimerTask, 2000);
    }

    /**
     * 停止图片滚动任务
     */
    private void stopImageTimerTask() {
        mHandler.removeCallbacks(mImageTimerTask);
    }

    private Handler mHandler = new Handler();

    /**
     * 图片自动轮播Task
     */
    private Runnable mImageTimerTask = new Runnable() {

        @Override
        public void run() {

            if (mImageViews != null) {

                // 下标等于图片列表长度说明已滚动到最后一张图片,重置下标

                if ((++mImageIndex) == mImageViews.length + 1) {

                    mImageIndex = 1;
                }
                mBannerPager.setCurrentItem(mImageIndex);

            }
        }
    };

    /**
     * 轮播图片状态监听器
     *
     * @author minking
     */
    private final class GuidePageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE)
                startImageTimerTask(); // 开始下次计时
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int index) {

            if (index == 0 || index == mImageViews.length + 1) {
                return;
            }
            // 设置图片滚动指示器背景
            mImageIndex = index;
            index -= 1;
            mImageViews[index].setBackgroundResource(R.drawable.dot_selected);

//            bannerTitle.setText(infoList.get(index).getTitle());

            for (int i = 0; i < mImageViews.length; i++) {
                if (index != i) {
                    mImageViews[i].setBackgroundResource(R.drawable.dot_normal);
                }
            }
        }

    }

    /**
     * 轮播控件的监听事件
     *
     * @author minking
     */
    public static interface ImageCycleViewListener {

        /**
         * 加载图片资源
         *
         * @param imageURL
         * @param imageView
         */
        public void displayImage(String imageURL, ImageView imageView);

        /**
         * @param postion
         * @param imageView
         */
        public void onImageClick(String info, int postion, View imageView);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtils.e("ImageCycleView","当前离开界面");
    }
}
