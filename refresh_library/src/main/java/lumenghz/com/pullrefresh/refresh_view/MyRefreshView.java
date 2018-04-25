package lumenghz.com.pullrefresh.refresh_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import lumenghz.com.pullrefresh.PullToRefreshView;
import lumenghz.com.pullrefresh.R;
import lumenghz.com.pullrefresh.util.Utils;

/**
 * @author lumeng on 2016-07-01.
 */
public class MyRefreshView extends BaseRefreshView {

    private static final float HEIGHT_RATIO = 1.0f;

    private PullToRefreshView mParent;

    private Matrix mMatrix;

    private Context mContext;

    //private Bitmap mBox;

    private float mBoxTopOffset;

    private int num = 1;

    private float mPercent;
    private Paint paint;

    //private Bitmap mSky;

    public enum RefreshState {
        NONE, REFRESHING, REFRESH_COMPLETE
    }

    private RefreshState currentRefreshState = RefreshState.NONE;

    /**
     * height of landscape
     */
    private int mSenceHeight;
    /**
     * width of landscape
     */
    private int mScreenWidth;
    /**
     * distance between bottom of landscape and top of landscape
     */
    private int mTop;

    private Timer timer;

    private MyTimerTask timeTask;

    private boolean isDropdown = false;

    /**
     * 背景属性
     */
    private int mSkyHeight;

    private float mSkyTopOffset;

    private float mSkyMoveOffset;

    /**
     * 重绘延时时间
     */
    private long drawDelayedTime = 5;


    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            handler.sendEmptyMessage(0);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidateSelf();
        }
    };

    public MyRefreshView(Context context, final PullToRefreshView layout) {
        super(context, layout);
        mParent = layout;
        mMatrix = new Matrix();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#eff0f3"));
        mContext = getContext();
        setupAnimations();
        layout.post(new Runnable() {
            @Override
            public void run() {
                initialDimens(layout.getWidth());
            }
        });
    }


    @Override
    protected void initialDimens(int viewWidth) {

        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        createBitmaps();

        Bitmap mBox = CreateBitmapFactory.getBitmapFromImage(R.drawable.icon_refresh_01, mContext);

        mScreenWidth = viewWidth;

        mSenceHeight = (int) (HEIGHT_RATIO * mScreenWidth);

        mTop = -mParent.getTotalDragDistance();

        mBoxTopOffset = -mTop - mBox.getHeight();

        mSkyHeight = (int) (0.3 * mScreenWidth);

        mSkyTopOffset = mSkyHeight * 0.65f;

        mSkyMoveOffset = Utils.convertDpToPixel(getContext(), 15);

        //mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);

        //mSky.recycle();

        mBox.recycle();

        //mSky = null;

    }

    private void createBitmaps() {

        //mBox = CreateBitmapFactory.getBitmapFromImage(R.drawable.icon_refresh_01, mContext);

        //mSky = CreateBitmapFactory.getBitmapFromImage(R.drawable.icon_refresh_bg, mContext);

    }

    @Override
    public void draw(Canvas canvas) {

        if (mScreenWidth <= 0) return;

        Log.e("执行绘图", "-------------------");


        final int saveCount = canvas.save();

        drawBg(canvas);

        drawBox(canvas);

        canvas.restoreToCount(saveCount);

    }

    private void drawBg(Canvas canvas) {

//        if (!mHasDrawBg) {
//            return;
//        }
//        mHasDrawBg = true;

        //if (mSky == null) {
//        Bitmap mSky = CreateBitmapFactory.getBitmapFromImage(R.drawable.icon_refresh_bg, mContext);
//        mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);
        //}

        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));
        float skyScale;
        float scalePercentDelta = dragPercent - 0.5f;
        if (scalePercentDelta > 0) {
            float scalePercent = scalePercentDelta / (1.0f - 0.5f);
            skyScale = 1.05f - (1.05f - 1.0f) * scalePercent;
        } else
            skyScale = 1.05f;

        float offsetX = -(mScreenWidth * skyScale - mScreenWidth) / 2.0f;
       /* float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() - mSkyTopOffset
                - mSkyHeight * (skyScale - 1.0f) / 2
                + mSkyMoveOffset * dragPercent;*/

        matrix.postScale(skyScale, skyScale);
        matrix.postTranslate(offsetX, 0);

//        canvas.drawBitmap(mSky, matrix, null);

        Log.e("tag","mScreenWidth"+mScreenWidth+"offsetX="+offsetX);
//        canvas.drawLine(0,0,mScreenWidth,mSkyHeight,paint);
        canvas.drawRect(new Rect(0,0,mScreenWidth,mSkyHeight),paint);

    }

    /**
     * Draw box
     *
     * @param canvas canvas
     */
    private void drawBox(Canvas canvas) {

        if (num < 13) {
            num++;
        }

        Bitmap mBox = CreateBitmapFactory.getBitmapFromImage(getBallResId(), mContext);

        final Matrix matrix = mMatrix;

        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float buildingScale;

        buildingScale = 1.0f - (1.2f - 1.0f) * dragPercent;

//        matrix.preScale(buildingScale, buildingScale);

        final float offsetX = mScreenWidth / 2 - mBox.getWidth() / 2; /*+ (1f - buildingScale) * mBox.getWidth() / 2;*/

        final float offsetY = mBoxTopOffset + mTop + 50;//+ dragPercent *100; //30;

        matrix.postTranslate(offsetX, offsetY);

//        mBox.recycle();

        canvas.drawBitmap(mBox, matrix, null);

    }

    private int getBallResId() {
        int resId = 0;
        if (RefreshState.REFRESHING == currentRefreshState) {
            resId = mParent.getResources().getIdentifier("icon_refresh_" + (num < 10 ? ("0" + num) : ("" + num)), "drawable", mParent.getContext().getPackageName());
            if (num == 8) {
                num = 0;
            }
        } else if (RefreshState.NONE == currentRefreshState) {
            resId = mParent.getResources().getIdentifier("icon_refresh_" + (num < 10 ? ("0" + num) : ("" + num)), "drawable", mParent.getContext().getPackageName());
            if (num == 8) {
                num = 0;
            }
        } else if (RefreshState.REFRESH_COMPLETE == currentRefreshState) {
            resId = mParent.getResources().getIdentifier("icon_refresh_" + (num < 10 ? ("0" + num) : ("" + num)), "drawable", mParent.getContext().getPackageName());
            if (num == 17) {
                num = 0;
            }
        }
        Log.e("当前resId", resId + " ");
        return resId;
    }


    @Override
    protected void setupAnimations() {

    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSenceHeight + top);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
    }

    private void setPercent(float percent) {
        this.mPercent = percent;
    }

    @Override
    public void offsetTopAndBottom(int offset, boolean requiresUpdate) {
        mTop += offset;

        //正在移动,开启重绘任务
        if (!isDropdown && requiresUpdate) {
            timer = new Timer(true);
            timeTask = new MyTimerTask();
            timer.schedule(timeTask, 0, drawDelayedTime * 10); //延时20ms后执行，drawDelayedTime*10执行一次
            isDropdown = true;
        }

    }


    /**
     * 刷新进行中
     */
    @Override
    public void start() {

        currentRefreshState = RefreshState.REFRESHING;

        num = 0;
    }

    /**
     * 刷新完成
     */
    @Override
    public void stop() {
        currentRefreshState = RefreshState.REFRESH_COMPLETE;
    }

    /**
     * 如果刷新成功时
     */
    public void refreshSuccess() {
        currentRefreshState = RefreshState.REFRESH_COMPLETE;
    }


    /**
     * 刷新结束
     */
    @Override
    public void refreshEnd() {

        resetOrigins();

    }

    @Override
    public boolean isRunning() {
        return false;
    }


    private void resetOrigins() {
        num = 0;
        currentRefreshState = RefreshState.NONE;
        if (timeTask != null) {
            timeTask.cancel();
        }
        if (timer != null) {
            timer.cancel();
        }
        isDropdown = false;
    }

    public void onDestroy() {
        //mSky.recycle();
        //mBox.recycle();
    }


}
