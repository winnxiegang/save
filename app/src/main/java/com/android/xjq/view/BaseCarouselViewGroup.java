package com.android.xjq.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.library.Utils.LogUtils;
import com.android.xjq.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kokuma on 2017/3/26.
 */
public abstract class BaseCarouselViewGroup<T> extends RelativeLayout implements View.OnClickListener{

    protected  Context context;
   protected ViewPager carouselViewPager;
    protected CarouselAdaper carouselAdaper;
    protected List<RadioButton> listDotView;
    protected  int currentIndex = 0;
    protected int count;
    protected Handler hd;
    protected  CircleRunnable circleRunnable = null;
    protected boolean isStartedCircleRunnable;
    protected ViewPager.OnPageChangeListener pageListener;
    protected List<View> mListNetIv;
    protected List<T> mListData;
    RadioGroup btnDotGroup;

    public BaseCarouselViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.carousel_viewgroup, this, true);
        init();
        listDotView = new ArrayList<>();
        mListNetIv = new ArrayList<>();
        mListData= new ArrayList<>();
        carouselAdaper = new CarouselAdaper(mListNetIv);
        carouselViewPager.setAdapter(carouselAdaper);
         btnDotGroup = (RadioGroup) this.findViewById(R.id.dot_group);
    }


    protected void init() {
        isStartedCircleRunnable = false;
        carouselViewPager = (ViewPager) this.findViewById(R.id.carousel_pager);
        hd = new Handler();
        pageListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (listDotView.size() <= position) {
                    return;
                }
                setItemIndex(position);
                LogUtils.i("xxl","banner-onPageScrolled-"+position);
            }

            @Override
            public void onPageSelected(int position) {
//                if (listDotView.size() <= position) {
//                    return;
//                }
//                setItemIndex(position);
                LogUtils.i("xxl","banner-onPageSelectSelected-"+position);

                // listDotView.get(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        carouselViewPager.addOnPageChangeListener(pageListener);
    }

    public  void update(final List<T> mList){
        mListData=mList;
        if(mListData==null||mListData.isEmpty()){
            return;
        }
       initCount();
        mListNetIv.clear();
        for(int i=0;i<count;i++){
            View v= createItemView(mListData.get(i));
            mListNetIv.add(v);
        }
        carouselAdaper.notifyDataSetChanged();
        setDotGroup(count);
      //  initRunnable();
        if(count>0){
            setItemIndex(0);
        }
    }

    protected   void initCount(){
        count = mListData.size();
//        if (count > 6) {
//            count = 6;
//        }
    }

    protected abstract View createItemView(T bean);



    protected void setItemIndex(int item) {
        btnDotGroup.clearCheck();
        try {
            for(int i=0;i<listDotView.size();i++){
                if(i!=item){
                   // carouselViewPager.setCurrentItem(i);
                    listDotView.get(i).setChecked(false);
                }
            }
                listDotView.get(item).setChecked(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setDotGroup(int count) {
        RadioButton btnDot;
      //  btnDotGroup.clearCheck();
        btnDotGroup.removeAllViews();
        listDotView.clear();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        if(count>0){
            for (int i = 0; i < count; i++) {
                btnDot = (RadioButton) inflater.inflate(R.layout.dot_one,null);
                // btnDot.setId(View.generateViewId());
//                RadioGroup.LayoutParams params =  new RadioGroup.LayoutParams(w,w);
//                params.setMargins(0,0,20,0);
//                btnDot.setLayoutParams(params);
                btnDotGroup.addView(btnDot);
                RadioGroup.LayoutParams params = (RadioGroup.LayoutParams) btnDot.getLayoutParams();
                params.setMargins(0,0,20,0);
                listDotView.add(btnDot);
            }
//            btnDotGroup.invalidate();
//            btnDotGroup.requestLayout();
        }
        //
    }
    private void initRunnable(){
        if(circleRunnable!=null) {
            hd.removeCallbacks(circleRunnable);
            isStartedCircleRunnable=false;
            currentIndex=0;
            circleRunnable= new CircleRunnable();
        }else {
            circleRunnable= new CircleRunnable();
        }
        if (!isStartedCircleRunnable) {
            hd.postDelayed(circleRunnable, 5000);
        }
    }
    protected  class CircleRunnable implements Runnable {
        @Override
        public void run() {
            isStartedCircleRunnable = true;
            if (currentIndex < count) {
                setItemIndex(currentIndex);

            }
            currentIndex++;
            if (currentIndex >= count) {
                currentIndex = 0;
            }
            if (count > 1 && circleRunnable != null) {
                hd.postDelayed(circleRunnable,5000);
            }
        }
    }

    public static class CarouselAdaper extends PagerAdapter {
       private List<View> mList;
        /**
         * 图片视图缓存列表
         */
       // private ArrayList<ImageView> mImageViewCacheList;
        public CarouselAdaper(List<View>  mList){
            this.mList=mList;
        //    mImageViewCacheList= new ArrayList<ImageView>();
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v=mList.get(position% mList.size());
            ViewGroup parent = (ViewGroup) v.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
            container.addView(v);
            return mList.get(position% mList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position% mList.size()));
        }

        private int mChildCount = 0;

        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object)   {
            if ( mChildCount > 0) {
                mChildCount --;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hd.removeCallbacks(circleRunnable);
        circleRunnable=null;
        isStartedCircleRunnable=false;
        currentIndex=0;
    }


}
