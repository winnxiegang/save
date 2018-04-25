package com.android.xjq.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.android.xjq.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by zhouyi on 2017/3/14.
 */

public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;

    private int MAX_NUM = 30;

    private Paint bgPaint;

    //private MyThread myThread;

    private List<HengFuPro> list = new ArrayList<>();

    private int MAX_STOP = 60;

    private boolean start = false;

    public MySurfaceView(Context context) {
        super(context);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MySurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        holder = getHolder();
        holder.addCallback(this);
        bgPaint = new Paint();
        //myThread = new MyThread();
        initList();
    }

    private void initList() {
        HengFuPro hengFu = createHengFu();
        list.add(hengFu);
    }

    public void addHengFu() {
        HengFuPro hengFu = createHengFu();
        list.add(hengFu);
        if (!start) {
            start = true;
            //myThread = new MyThread();
            //myThread.start();
        }
    }

    private HengFuPro createHengFu() {
        HengFuPro pro = new HengFuPro();
        pro.top = getCalculateHeight();
        pro.left = -768;
        pro.bottom = 156 + pro.top;
        pro.right = 0;
        pro.currentRes = R.mipmap.juesha_0;
        return pro;
    }

    private int getCalculateHeight() {
        int height = 0;
        for (HengFuPro pro : list) {
            if (height == pro.top) {
                height += 156;
            } else {
                return height;
            }
        }
        return height;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        start = false;
        //myThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        holder.removeCallback(this);
    }

    class MyThread extends Thread {

        @Override
        public void run() {
            while (start) {
                //long startTime = System.currentTimeMillis();

                if (list.size() > 0) {
                    draw();
                    calculateHengFu();
                } else {
                    Canvas canvas = holder.lockCanvas();
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    holder.unlockCanvasAndPost(canvas);
                    start = false;
                }

                //long endTime = System.currentTimeMillis();

                //Log.e("渲染消耗的时间", (endTime - startTime) + " ");
            }
        }

        private void calculateHengFu() {

            removeFinish();


            for (int i = 0; i < list.size(); i++) {
                HengFuPro pro = list.get(i);
                if (pro.left >= 0 && pro.stopCount < MAX_STOP) {
                    int currentNum = pro.currentNum++;
                   /* if (currentNum == MAX_NUM) {
                        pro.currentNum = 0;
                    } else {
                        pro.currentNum++;
                    }*/
                    pro.currentRes = getImageResource(currentNum);
                    pro.stopCount++;
                } else {
                    pro.left += 76.8;
                    pro.right += 76.8;
                }
            }
        }


        private void removeFinish() {
            List<HengFuPro> tempList = new ArrayList<>();
            for (HengFuPro pro : list) {
                if (pro.left < getMeasuredWidth()) {
                    tempList.add(pro);
                }
            }
            list.clear();
            list.addAll(tempList);
        }

        private void draw() {
            if (list.size() == 0) return;
            Canvas canvas = holder.lockCanvas();
            if (canvas == null) {
                return;
            }
            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            drawBitmap(canvas);
            holder.unlockCanvasAndPost(canvas);
        }

        private void drawBitmap(Canvas canvas) {

            HashMap<Integer, Bitmap> map = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                HengFuPro pro = list.get(i);
                if (map.get(pro.currentRes) == null) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), pro.currentRes);
                    map.put(pro.currentRes, bitmap);
                }
            }

            for (int i = 0; i < list.size(); i++) {
                HengFuPro pro = list.get(i);
                RectF rectF = new RectF();
                rectF.top = pro.top;
                rectF.bottom = pro.bottom;
                rectF.left = pro.left;
                rectF.right = pro.right;
                canvas.drawBitmap(map.get(pro.currentRes), null, rectF, bgPaint);
            }
        }
    }

    private int getImageResource(int currentNum) {
        int currentRes = 0;
        if (currentNum <= MAX_NUM) {
            currentNum += 1;
        } else {
            currentNum = 1;
        }
        try {
            currentRes = (int) R.mipmap.class.getField("juesha_" + currentNum).get(new R.mipmap());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return currentRes;
    }

    class HengFuPro {

        int top;

        int bottom;

        int left;

        int right;

        boolean isStop;

        int currentRes;

        int currentNum;

        int stopCount = 0;


    }
}
