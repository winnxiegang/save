package com.android.banana.commlib.liveScore.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.banana.commlib.R;
import com.android.banana.commlib.bean.liveScoreBean.PathAnimEventResultBean;
import com.android.banana.commlib.liveScore.livescoreEnum.AnimTypeEnum;
import com.android.banana.commlib.utils.AnimationListUtil;

import java.util.ArrayList;


/**
 * Created by qiaomu on 2017/8/2.
 */

public class PathAnimView extends AnimView {
    private static float ANGLE = 52;
    private static float TAN = (float) Math.tan(Math.PI * ANGLE / 180.0);//tan角度值

    //进攻，控球，危险进攻
    private static float MARGIN_BOTTOM;// dp2px(24);//球场下边距
    private static float MARGIN_TOP;//dp2px(16);//球场上边距
    private static float MARGIN_RIGHT;//dp2px(31);//球场右下边距
    private static float MARGIN_LEFT;//dp2px(32);//球场左下边距
    private static float MARGIN_LEFT_TOP;//dp2px(32);//球场左上边距
    private static float MARGIN_RIGHT_TOP;//dp2px(31);//球场右上边距
    private static float MARGIN_BALL_HAND_END;//dp2px(31);//控球终点离中间距离

    //危险进攻
    private static float MAX_TOP_X;//dp2px(161);//危险进攻 梯形第二个top点x值 右距离边界值
    private static float MAX_TOP_X_RIGHT;//dp2px(130);//危险进攻 梯形第二个top点x值 距离左边界值

    //点球
    private static float ACTION_PENALTY_MARGIN;//dp2px(110);//点球距离左右边界的距离
    private static float ACTION_PENALTY_LEFT_MARGIN;//dp2px(100);//点球距离左右边界的距离
    private static float PENALTY_RADIUS;//点球圆环半径

    //箭头，阴影
    private static float ARROW_LENGTH;//箭头边长
    private static float SHADOW_RADIUS;//阴影半径
    //角球
    private static float ACTION_CORNER_TOP;//dp2px(28);//角球上边距
    private static float ACTION_CORNER_LEFT;//dp2px(85);//角球左边距
    private static float ACTION_CORNER_LEFT_SHADOW;//dp2px(75);//角球阴影左边距
    private static float ACTION_CORNER_RIGHT_SHADOW;//dp2px(45);//角球阴影右边距
    private static float ACTION_CORNER_RIGHT;//dp2px(50);//角球动作右边距
    //界外球
    private static float ACTION_OUT_BOUNDS_LENx;  //动作水平长度
    private static float ACTION_OUT_BOUNDS_LENy;   //动作垂直长度
    private static float ACTION_OUT_BOUNDS_MARGIN; //起始点偏离中间位置长度
    private static float BEAT_LENGTH;//弹跳高度

    //球门球
    private static float ACTION_GOAL_START_LEFT;//dp2px(101);//左侧开始位置
    private static float ACTION_GOAL_START_LEFT_TOP;//dp2px(42);//左侧开始顶部位置
    private static float ACTION_GOAL_END_RIGHT;//dp2px(97);//左侧开始右侧结束位置
    private static float ACTION_GOAL_END_RIGHT_BEAT;//左侧开始右侧弹回结束为止

    private static float ACTION_GOAL_START_RIGHT;//dp2px(72);//右侧开始位置
    private static float ACTION_GOAL_START_RIGHT_TOP;//dp2px(42);//右侧开始顶部位置
    private static float ACTION_GOAL_END_LEFT;//右侧开始左侧结束为止
    private static float ACTION_GOAL_END_LEFT_BEAT;//右侧开始左侧回弹位置

    //任意球
    private static float ACTION_FREE_KICK;//任意球 弧度大小
    private static float ACTION_FREE_KICK_DANGER_LEFT_END;
    private static float ACTION_FREE_KICK_DANGER_RIGHT_END;

    //射偏
    private static float ACTION_SHOT_WIDE_TOP;//射频顶部距离
    private static float ACTION_SHOT_WIDE_RIGHT;//射偏右侧距离
    private static float ACTION_SHOT_WIDE_LEFT;//射偏左侧距离
    private static float ACTION_SHOT_WIDE_START_RIGHT;//射偏右侧开始
    private static float ACTION_SHOT_WIDE_START_LEFT;//射偏左侧开始
    //射正
    private static float ACTION_SHOT_TARGET_TOP; //射正顶部距离
    private static float ACTION_SHOT_TARGET_RIGHT; //射正右侧距离
    private static float ACTION_SHOT_TARGET_LEFT; //射正左侧距离

    private static int alpha = (int) (0.75 * 255);//透明度
    private static int smallGoalResId = R.drawable.icon_ft_small;//足球小 资源id

    private Path mPath = new Path();
    private Path mLeftAttackPath = new Path();
    private Path mRightAttackPath = new Path();
    private PathMeasure mPathMeasure = new PathMeasure();

    private float[] positions = new float[2];//路径坐标
    private float[] tans = new float[2];//路径横切

    private float startX, startY, controlX, controlY, endX, endY, cubEndx, cubEndy;//分别是贝瑟尔曲线开始点 控制点 结束点 以及第二结束点

    private Path mDstPath = new Path();
    private Path mArrowPath = new Path();
    private Path mShadowPath = new Path();
    private Matrix mMatrix = new Matrix();
    private RectF mRectF;
    private float mMiddleX;//多边形顶点x
    private float mMiddleY;//多边形顶点y

    private Bitmap mGoalBitmap;//小球bitmap
    private ImageView mAnimActionIv;
    private TextView mAnimActionTv;

    private float percent;
    private ArrayList<PathAnimEventResultBean.AnimEventBean> flashScoreList;
    private ValueAnimator animator;
    private long currentTimeMillis;
    private AnimTypeEnum[] mActiontype;
    private boolean isRuning;
    private boolean mIsLeft;
    private boolean mIsNextLeft;

    public PathAnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        MARGIN_BOTTOM = yp2px(0.26f);// dp2px(24);//球场下边距
        MARGIN_TOP = yp2px(0.18f);//dp2px(16);//球场上边距
        MARGIN_RIGHT = xp2px(0.07f);//dp2px(31);//球场右边距
        MARGIN_LEFT = xp2px(0.08f);//dp2px(32);//球场左边距
        MARGIN_RIGHT_TOP = xp2px(0.165f);//dp2px(32);//球场右上边距
        MARGIN_LEFT_TOP = xp2px(0.235f);//dp2px(32);//球场右上边距

        mRectF = new RectF(MARGIN_LEFT, MARGIN_TOP, getWidth() - MARGIN_RIGHT, getHeight() - MARGIN_BOTTOM);

        //危险进攻
        MAX_TOP_X = xp2px(0.31f);//dp2px(161);//危险进攻 梯形第二个top点x值 右距离边界值
        MAX_TOP_X_RIGHT = xp2px(0.25f);//dp2px(130);//危险进攻 梯形第二个top点x值 距离左边界值
        MARGIN_BALL_HAND_END = xp2px(0.1f);

        //点球
        ACTION_PENALTY_MARGIN = xp2px(0.28f);//dp2px(110);//点球距离左右边界的距离
        ACTION_PENALTY_LEFT_MARGIN = xp2px(0.28f);//dp2px(100);//点球距离左右边界的距离
        PENALTY_RADIUS = dp2px(12);//点球圆环半径

        //箭头，阴影
        ARROW_LENGTH = dp2px(2.5f);//箭头边长
        SHADOW_RADIUS = dp2px(1.5f);//阴影半径

        //角球
        ACTION_CORNER_TOP = yp2px(0.33f);//dp2px(28);//角球上边距
        ACTION_CORNER_LEFT = xp2px(0.21f);//dp2px(85);//角球左边距
        ACTION_CORNER_LEFT_SHADOW = xp2px(0.08f);//dp2px(75);//角球阴影左边距
        ACTION_CORNER_RIGHT_SHADOW = xp2px(0.07f);//dp2px(45);//角球阴影右边距
        ACTION_CORNER_RIGHT = xp2px(0.13f);//dp2px(50);//角球动作右边距

        //界外球
        ACTION_OUT_BOUNDS_LENx = dp2px(30);  //动作水平长度
        ACTION_OUT_BOUNDS_LENy = dp2px(30);   //动作垂直长度
        ACTION_OUT_BOUNDS_MARGIN = dp2px(30); //起始点偏离中间位置长度
        BEAT_LENGTH = dp2px(10);

        //球门球
        ACTION_GOAL_START_LEFT = xp2px(0.25f);//dp2px(101);//左侧开始位置
        ACTION_GOAL_START_LEFT_TOP = yp2px(0.55f);//dp2px(42);//左侧开始顶部位置
        ACTION_GOAL_END_RIGHT = xp2px(0.25f);//dp2px(97);//左侧开始右侧结束位置
        ACTION_GOAL_END_RIGHT_BEAT = xp2px(0.18f);//左侧开始右侧弹回结束为止

        ACTION_GOAL_START_RIGHT = xp2px(0.18f);//dp2px(72);//右侧开始位置
        ACTION_GOAL_START_RIGHT_TOP = yp2px(0.54f);//dp2px(42);//右侧开始顶部位置
        ACTION_GOAL_END_LEFT = xp2px(0.32f);//右侧开始左侧结束为止
        ACTION_GOAL_END_LEFT_BEAT = xp2px(0.30f);//右侧开始左侧回弹位置

        //任意球
        ACTION_FREE_KICK = dp2px(19);//任意球 弧度大小
        ACTION_FREE_KICK_DANGER_LEFT_END = xp2px(0.25f);//dp2px(101);//左侧开始位置
        ACTION_FREE_KICK_DANGER_RIGHT_END = xp2px(0.18f);//dp2px(101);//左侧开始位置

        //射偏
        ACTION_SHOT_WIDE_TOP = yp2px(0.185f);//dp2px(12);//射频顶部距离
        ACTION_SHOT_WIDE_RIGHT = xp2px(0.12f);//射偏右侧距离
        ACTION_SHOT_WIDE_LEFT = xp2px(0.20f);//射偏左侧距离
        ACTION_SHOT_WIDE_START_RIGHT = xp2px(0.24f);//dp2px(97);//左侧开始右侧结束位置
        ACTION_SHOT_WIDE_START_LEFT = xp2px(0.31f);//右侧开始左侧结束为止

        //射正
        ACTION_SHOT_TARGET_TOP = yp2px(0.17f); //射正顶部距离
        ACTION_SHOT_TARGET_RIGHT = xp2px(0.07f); //射正右侧距离
        ACTION_SHOT_TARGET_LEFT = xp2px(0.14f); //射正左侧距离

    }

    public RectF getRect() {
        if (mRectF == null)
            mRectF = new RectF(MARGIN_LEFT, MARGIN_TOP, getWidth() - MARGIN_RIGHT, getHeight() - MARGIN_BOTTOM);
        return mRectF;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAnimActionIv = (ImageView) getChildAt(0);
        mAnimActionTv = (TextView) getChildAt(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mActiontype == null)
            return;
        for (int i = 0; i < mActiontype.length; i++) {
            AnimTypeEnum typeEnum = mActiontype[i];
            if (typeEnum != null)
                doPathAnim(canvas, typeEnum, i == 0 ? mIsLeft : mIsNextLeft);
        }
    }

    //射正
    private void makeShotTargetPath(boolean fromLeft) {
        mPath.reset();
        startX = fromLeft ? getRect().right - ACTION_SHOT_WIDE_START_RIGHT : ACTION_SHOT_WIDE_START_LEFT;
        startY = getRect().bottom / 2;

        endX = fromLeft ? getRect().right - ACTION_SHOT_TARGET_RIGHT : ACTION_SHOT_TARGET_LEFT;
        endY = ACTION_SHOT_TARGET_TOP;

        controlX = startX + (fromLeft ? 1 : -1) * ACTION_SHOT_TARGET_TOP;
        controlY = ACTION_SHOT_TARGET_TOP;

        cubEndx = fromLeft ? endX - 3 * BEAT_LENGTH : endX + 3 * BEAT_LENGTH;
        cubEndy = endY + 1.5f * BEAT_LENGTH;

        mPath.moveTo(startX, startY);
        mPath.cubicTo(controlX, controlY, endX, endY, cubEndx, cubEndy);
        mPath.lineTo(cubEndx + (fromLeft ? -1 : 1) * BEAT_LENGTH / 2, cubEndy + BEAT_LENGTH / 4);
        mPath.lineTo(cubEndx + (fromLeft ? -2 : 2) * BEAT_LENGTH / 2, cubEndy);
        mPath.lineTo(cubEndx + (fromLeft ? -3 : 3) * BEAT_LENGTH / 2, cubEndy + BEAT_LENGTH / 4);
        mPathMeasure.setPath(mPath, false);
    }

    //射正
    private void doShotTargetPathAnim(Canvas canvas, boolean fromLeft) {
        getPosTan();
        if (percent <= 0.8) {
            getEndSegment();
            float shadowy = Math.max(endY, startY);
            float shadowx = positions[0];
            drawShadow(canvas, fromLeft, new RectF(shadowx - 2 * SHADOW_RADIUS, shadowy - SHADOW_RADIUS, shadowx + 2 * SHADOW_RADIUS, shadowy + SHADOW_RADIUS));
        }
        makeMatrix();
        canvas.drawPath(mDstPath, getDashPaint());
        canvas.drawBitmap(getGoalBitmap(), mMatrix, getDashPaint());
        if (percent >= 0.65) {
            float degrees = fromLeft ? -120 : 120;
            drawArrow(canvas, degrees, cubEndx, cubEndy);
        }

    }

    //射偏  druation 1500
    private void makeShotWidePath(boolean fromLeft) {
        mPath.reset();
        startX = fromLeft ? getRect().right - ACTION_SHOT_WIDE_START_RIGHT : ACTION_SHOT_WIDE_START_LEFT;
        startY = getRect().bottom / 2 - dp2px(2.5f);

        endX = fromLeft ? getRect().right - ACTION_SHOT_WIDE_RIGHT : ACTION_SHOT_WIDE_LEFT;
        endY = ACTION_SHOT_WIDE_TOP;

        controlX = startX + (fromLeft ? 1 : -1) * ACTION_SHOT_WIDE_TOP;
        controlY = ACTION_SHOT_WIDE_TOP;

        mPath.moveTo(startX, startY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPathMeasure.setPath(mPath, false);
    }

    //射偏  druation 1500
    private void doShotWidePathAnim(Canvas canvas, boolean fromLeft) {
        makeMatrixSegmentPostTan(endX, fromLeft);
        boolean drawShadow = isDrawShadow(fromLeft);

        if (drawShadow) {
            float tan = (Math.abs(endY - startY)) / (Math.abs(endX - startX));
            float dis = Math.abs(positions[0] - startX);
            float shadowy = Math.max(endY, startY - tan * dis / 4);
            float shadowx = positions[0];
            drawShadow(canvas, fromLeft, new RectF(shadowx - 2 * SHADOW_RADIUS, shadowy - SHADOW_RADIUS, shadowx + 2 * SHADOW_RADIUS, shadowy + SHADOW_RADIUS));
        }

        canvas.drawPath(mDstPath, getDashPaint());
        canvas.drawBitmap(getGoalBitmap(), mMatrix, getDashPaint());
    }

    //任意球  druation 1500
    private void makeFreeKickPath(boolean fromLeft, boolean danger) {
        mPath.reset();
        startX = getWidth() / 2 + (fromLeft ? -1 : 1) * ACTION_FREE_KICK;
        startY = getRect().bottom / 2;

        controlX = startX + (fromLeft ? 1 : -1) * ACTION_GOAL_START_LEFT / 3;
        controlY = startY - dp2px(30);

        endX = fromLeft ? (danger ? getRect().right - ACTION_FREE_KICK_DANGER_RIGHT_END : getRect().right - ACTION_GOAL_END_RIGHT)
                : (danger ? ACTION_FREE_KICK_DANGER_LEFT_END : ACTION_GOAL_END_LEFT);
        endY = getRect().bottom / 2 - dp2px(2.5f);
        mPath.moveTo(startX, startY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPath.lineTo(endX + (fromLeft ? 1 : -1) * BEAT_LENGTH / 2, endY);
        mPath.lineTo(endX + (fromLeft ? 2 : -2) * BEAT_LENGTH / 2, endY - BEAT_LENGTH / 2);
        mPath.lineTo(endX + (fromLeft ? 3 : -3) * BEAT_LENGTH / 2, endY);
        mPathMeasure.setPath(mPath, false);
    }

    //任意球
    private void doFreeKickPathAnim(Canvas canvas, boolean fromLeft) {
        float degrees = fromLeft ? 120 : -120;
        makeMatrixSegmentPostTan(endX, fromLeft);
        drawArrow(canvas, !fromLeft, degrees);
        boolean drawShadow = isDrawShadow(fromLeft);

        if (drawShadow) {
            float tan = (Math.abs(endY - startY)) / (Math.abs(endX - startX));
            float dis = Math.abs(positions[0] - startX);
            float shadowy = Math.max(endY, startY - tan * dis / 4);
            float shadowx = positions[0];
            drawShadow(canvas, fromLeft, new RectF(shadowx - 2 * SHADOW_RADIUS, shadowy - SHADOW_RADIUS, shadowx + 2 * SHADOW_RADIUS, shadowy + SHADOW_RADIUS));
        }
        canvas.drawPath(mDstPath, getDashPaint());
        canvas.drawBitmap(getGoalBitmap(), mMatrix, getDashPaint());
    }

    //球门球
    private void makeGoalDoorPath(boolean fromLeft) {
        mPath.reset();
        startX = fromLeft ? ACTION_GOAL_START_LEFT : getRect().right - ACTION_GOAL_START_RIGHT;
        startY = fromLeft ? ACTION_GOAL_START_LEFT_TOP : ACTION_GOAL_START_RIGHT_TOP;

        endX = fromLeft ? getRect().right - ACTION_GOAL_END_RIGHT : ACTION_GOAL_END_LEFT;
        endY = getRect().bottom / 2 - dp2px(2.5f);

        controlX = startX + (fromLeft ? 1 : -1) * 2 * ACTION_GOAL_START_LEFT / 3;
        controlY = startY - dp2px(40);
        mPath.moveTo(startX, startY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPath.lineTo(endX + (fromLeft ? 1 : -1) * BEAT_LENGTH / 2, endY);
        mPath.lineTo(endX + (fromLeft ? 2 : -2) * BEAT_LENGTH / 2, endY - BEAT_LENGTH / 2);
        mPath.lineTo(endX + (fromLeft ? 3 : -3) * BEAT_LENGTH / 2, endY);
        mPathMeasure.setPath(mPath, false);

    }

    //球门球
    private void doGoalDoorPathAnim(Canvas canvas, boolean fromLeft) {
        float degrees = fromLeft ? 120 : -120;
        makeMatrixSegmentPostTan(endX, fromLeft);
        drawArrow(canvas, !fromLeft, degrees);

        boolean drawShadow = isDrawShadow(fromLeft);

        if (drawShadow) {
            float tan = (Math.abs(endY - startY)) / (Math.abs(endX - startX));
            float dis = Math.abs(positions[0] - startX);
            float shadowy = Math.max(endY, startY - tan * dis / 4);
            float shadowx = positions[0];
            drawShadow(canvas, fromLeft, new RectF(shadowx - 2 * SHADOW_RADIUS, shadowy - SHADOW_RADIUS, shadowx + 2 * SHADOW_RADIUS, shadowy + SHADOW_RADIUS));
        }
        canvas.drawPath(mDstPath, getDashPaint());
        canvas.drawBitmap(getGoalBitmap(), mMatrix, getDashPaint());
    }

    private boolean isDrawShadow(boolean fromLeft) {
        boolean drawShadow;
        if (fromLeft) {
            if (positions[0] < endX) {
                drawShadow = true;
            } else drawShadow = false;
        } else if (positions[0] > endX) {
            drawShadow = true;
        } else
            drawShadow = false;
        return drawShadow;
    }

    //界外球路径
    private void makeOutBoundsPath(boolean fromLeft) {
        mPath.reset();

        startX = getRect().right / 2 + (fromLeft ? -1 / 2 : 1) * ACTION_OUT_BOUNDS_MARGIN;
        startY = getRect().bottom + dp2px(7);

        controlY = getRect().bottom - ACTION_OUT_BOUNDS_LENy;
        controlX = startX - (fromLeft ? 1 : -1) * 2 * BEAT_LENGTH;

        endX = startX + (fromLeft ? -1 : 1) * ACTION_OUT_BOUNDS_LENx;
        endY = controlY + 2 * BEAT_LENGTH;

        mPath.moveTo(startX, startY);
        mPath.quadTo(controlX, controlY, endX, endY);
        mPath.lineTo(endX + (fromLeft ? -1 : 1) * 1 * BEAT_LENGTH / 2, endY);
        mPath.lineTo(endX + (fromLeft ? -1 : 1) * 2 * BEAT_LENGTH / 2, endY - BEAT_LENGTH / 2);
        mPath.lineTo(endX + (fromLeft ? -1 : 1) * 3 * BEAT_LENGTH / 2, endY);
        mPathMeasure.setPath(mPath, false);
    }

    //界外球
    private void doOutBoundsPathAnim(Canvas canvas, boolean fromLeft) {
        float degrees = fromLeft ? -120 : 120;
        makeMatrixSegmentPostTan(endX, !fromLeft);
        drawArrow(canvas, fromLeft, degrees);
        canvas.drawPath(mDstPath, getDashPaint());
        canvas.drawBitmap(getGoalBitmap(), mMatrix, getDashPaint());

        boolean drawShadow = isDrawShadow4OutBounds(fromLeft);
        if (drawShadow) {
            float tan = ACTION_OUT_BOUNDS_LENy / ACTION_OUT_BOUNDS_LENx;
            float dis = Math.abs(positions[0] - startX);
            float shadowy = Math.max(endY, startY - tan * dis / 2);
            float shadowx = positions[0];
            drawShadow(canvas, fromLeft, new RectF(shadowx - 2 * SHADOW_RADIUS, shadowy - SHADOW_RADIUS, shadowx + 2 * SHADOW_RADIUS, shadowy + SHADOW_RADIUS));
        }
    }

    private void drawArrow(Canvas canvas, boolean fromLeft, float degrees) {
        if (fromLeft) {
            if (positions[0] < endX)
                drawArrow(canvas, degrees, endX, endY);
        } else if (positions[0] > endX)
            drawArrow(canvas, degrees, endX, endY);
    }

    private boolean isDrawShadow4OutBounds(boolean fromLeft) {
        boolean drawShadow;
        if (fromLeft) {
            if (positions[0] > endX) {
                drawShadow = true;
            } else drawShadow = false;
        } else if (positions[0] < endX) {
            drawShadow = true;
        } else
            drawShadow = false;
        return drawShadow;
    }

    //角球
    private void makeCornerPath(boolean fromLeft) {
        mPath.reset();
        moveToStart(fromLeft);
        float dis = getRect().bottom - ACTION_CORNER_TOP;
        //终点
        endX = fromLeft ? ACTION_CORNER_LEFT : getRect().right - ACTION_CORNER_RIGHT;
        endY = ACTION_CORNER_TOP;
        //控制点
        controlX = fromLeft ? endX + ACTION_CORNER_RIGHT / 5 : endX - ACTION_CORNER_RIGHT / 5;
        controlY = endY + dis;

        mPath.quadTo(controlX, controlY, endX, endY);
        mPathMeasure.setPath(mPath, false);
    }

    //角球  druation 1500
    private void doCornerPathAnim(Canvas canvas, boolean fromLeft) {
        makeMatrixSegmentPostTan(0, false);
        canvas.drawPath(mPath, getDashPaint());

        float degrees = fromLeft ? -30 : 30;
        drawArrow(canvas, degrees, endX, endY);
        canvas.drawBitmap(getGoalBitmap(), mMatrix, null);

        float y = positions[1];
        float x = fromLeft ? getRect().left + (getRect().bottom - y) * TAN : getRect().right - (getRect().bottom - y) * TAN;
        drawShadow(canvas, fromLeft, new RectF(x - 2 * SHADOW_RADIUS, y - SHADOW_RADIUS, x + 2 * SHADOW_RADIUS, y + SHADOW_RADIUS));
    }

    //点球
    private void doPenaltyPathAnim(Canvas canvas, boolean fromLeft) {
        int intrinsicWidth = getGoalBitmap().getWidth();
        getPaint().setColor(Color.WHITE);
        getPaint().setShader(null);
        float centerX = (fromLeft ? ACTION_PENALTY_LEFT_MARGIN : getWidth() - ACTION_PENALTY_MARGIN) + intrinsicWidth / 2;
        float centerY = getRect().bottom / 2;

        RectF rectF = new RectF(centerX - PENALTY_RADIUS / 4f, centerY - PENALTY_RADIUS / 10f, centerX + PENALTY_RADIUS / 4f, centerY + PENALTY_RADIUS / 10f);
        getPaint().setAlpha((int) ((1 - percent / 2) * 255));
        canvas.drawOval(rectF, getPaint());

        float rippleRadiusX = PENALTY_RADIUS / 4f + 3f * PENALTY_RADIUS / 4f * percent;
        float rippleRadiusY = PENALTY_RADIUS / 10f + 3f * PENALTY_RADIUS / 10f * percent;

        getPaint().setAlpha(Math.max((int) (1 - percent) * 255, 50));
        RectF rippleRect = new RectF(centerX - rippleRadiusX, centerY - rippleRadiusY, centerX + rippleRadiusX, centerY + rippleRadiusY);
        canvas.drawOval(rippleRect, getPaint());
        canvas.drawBitmap(getGoalBitmap(), centerX - intrinsicWidth / 2, getRect().height() / 2 - PENALTY_RADIUS * percent, null);
    }

    //控球
    //percent from 0.5
    private void doBallControlPathAnim(Canvas canvas, boolean fromLeft) {
        float p = Math.min(0.8f + percent / 5, 1f);
        float topLeft;
        if (fromLeft) {
            topLeft = getRect().right / 2 * p - MARGIN_BALL_HAND_END;
        } else {
            topLeft = getRect().right - (getRect().right / 2 - 1.7f * MARGIN_BALL_HAND_END) * p;
        }
        if (fromLeft)
            mLeftAttackPath.reset();
        else
            mRightAttackPath.reset();
        moveToStart(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop2(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToMiddle(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToBottom(topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        if (fromLeft)
            mLeftAttackPath.close();
        else
            mRightAttackPath.close();
        getPaint().setShader(null);
        getPaint().setColor(ContextCompat.getColor(getContext(), fromLeft ? R.color.left_ball_control : R.color.ball_control));
        getPaint().setAlpha(alpha);
        canvas.drawPath(fromLeft ? mLeftAttackPath : mRightAttackPath, getPaint());
    }

    //进攻
    //percent from 0.5
    private void doAttackPathAnim(Canvas canvas, boolean fromLeft) {
        float topLeft;
        if (fromLeft) {
            topLeft = (getRect().right / 2) * (0.5f + percent / 2);
        } else {
            topLeft = getRect().right - (getRect().right / 2) * (0.5f + percent / 2);
        }
        topLeft += MARGIN_RIGHT / 2;
        if (fromLeft)
            mLeftAttackPath.reset();
        else
            mRightAttackPath.reset();
        moveToStart(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop2(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToMiddle(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToBottom(topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        if (fromLeft)
            mLeftAttackPath.close();
        else
            mRightAttackPath.close();


        Shader shader = makeLinearGradientShader(fromLeft ? getRect().left : getRect().right, fromLeft ? getRect().top : getRect().bottom,
                mMiddleX, mMiddleY, null,
                ContextCompat.getColor(getContext(), fromLeft ? R.color.left_attack_start : R.color.attack_start),
                ContextCompat.getColor(getContext(), fromLeft ? R.color.left_attack_end : R.color.attack_end));
        getPaint().setShader(shader);
        getPaint().setAlpha(alpha);
        canvas.drawPath(fromLeft ? mLeftAttackPath : mRightAttackPath, getPaint());
    }

    //危险进攻
    //percent from 0.5
    private void doDangerousAttackPathAnim(Canvas canvas, boolean fromLeft) {

        float topLeft;
        if (fromLeft) {
            topLeft = getRect().right * (0.4f + percent / 5 * 3);
            topLeft = Math.min(getRect().right - MAX_TOP_X_RIGHT, topLeft);
        } else {
            topLeft = getRect().right * (1 - (0.4f + percent / 5 * 3));
            topLeft = Math.max(MAX_TOP_X, topLeft);
        }

        if (fromLeft)
            mLeftAttackPath.reset();
        else
            mRightAttackPath.reset();
        moveToStart(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop(fromLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToTop2(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToMiddle(fromLeft, topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        lineToBottom(topLeft, fromLeft ? mLeftAttackPath : mRightAttackPath);
        if (fromLeft)
            mLeftAttackPath.close();
        else
            mRightAttackPath.close();


        Shader shader = makeLinearGradientShader(fromLeft ? getRect().left : getRect().right, getRect().bottom,
                mMiddleX, mMiddleY, null,
                ContextCompat.getColor(getContext(), fromLeft ? R.color.left_dangerous_attack_start : R.color.dangerous_attack_start),
                ContextCompat.getColor(getContext(), fromLeft ? R.color.left_dangerous_attack_middle : R.color.dangerous_attack_middle),
                ContextCompat.getColor(getContext(), fromLeft ? R.color.left_dangerous_attack_end : R.color.dangerous_attack_end));
        getPaint().setShader(shader);

        getPaint().setAlpha(alpha);
        canvas.drawPath(fromLeft ? mLeftAttackPath : mRightAttackPath, getPaint());
    }

    private void moveToStart(boolean fromLeft) {
        moveToStart(fromLeft, mPath);
    }

    private void moveToStart(boolean fromLeft, Path path) {
        if (fromLeft) {
            path.moveTo(getRect().left, getRect().bottom);
        } else
            path.moveTo(getRect().right, getRect().bottom);
    }

    //连接到梯形上边第一个点
    private void lineToTop(boolean fromLeft) {
        lineToTop(fromLeft, mPath);
    }

    //连接到梯形上边第一个点
    private void lineToTop(boolean fromLeft, Path path) {
        float rightTopX = fromLeft ? MARGIN_LEFT_TOP : getRect().right - MARGIN_RIGHT_TOP;
        float rightTopY = getRect().top;
        path.lineTo(rightTopX, rightTopY);
    }

    //连接到多边形顶点，根据左上x值计算
    private void lineToMiddle(boolean fromLeft, float leftTop) {
        lineToMiddle(fromLeft, leftTop, mPath);
    }

    private void lineToMiddle(boolean fromLeft, float leftTop, Path path) {
        mMiddleX = leftTop - (fromLeft ? -1 : 1) * getRect().height() / 2 * TAN;
        mMiddleY = getRect().bottom / 2;
        path.lineTo(mMiddleX, mMiddleY);
    }

    //连接到梯形上边第二个点
    private void lineToTop2(boolean fromLeft, float leftTop, Path path) {
        float topX = fromLeft ?
                getRect().left + getRect().height() * TAN :
                getRect().right - getRect().height() * TAN;

        leftTop = fromLeft ? Math.max(topX, leftTop) : Math.min(leftTop, topX);
        float middleTopY = getRect().top;
        path.lineTo(leftTop, middleTopY);
    }

    private void lineToTop2(boolean fromLeft, float leftTop) {
        lineToTop2(fromLeft, leftTop, mPath);
    }

    //连接到下角 根据左上角x值来计算
    private void lineToBottom(float left) {
        lineToBottom(left, mPath);
    }

    private void lineToBottom(float left, Path path) {
        float middleBottomX = left;
        float middleBottomY = getRect().bottom;
        path.lineTo(middleBottomX, middleBottomY);
    }

    private Path getArrowPath() {
        return getArrowPath(positions[0], positions[1]);
    }

    private Path getArrowPath(float endx, float endy) {

        mArrowPath.reset();
        mArrowPath.moveTo(endx + ARROW_LENGTH, endy);
        mArrowPath.lineTo(endx - ARROW_LENGTH, endy);
        mArrowPath.lineTo(endx, endy - ARROW_LENGTH);
        mArrowPath.close();
        return mArrowPath;
    }

    private Bitmap getGoalBitmap() {
        if (mGoalBitmap == null) {
            mGoalBitmap = BitmapFactory.decodeResource(getResources(), smallGoalResId);
        }
        return mGoalBitmap;
    }

    //如果 end==0  lessThan 将无意义
    private void makeMatrixSegmentPostTan(float end, boolean lessThan) {
        getPosTan();
        makeMatrix();
        if (end != 0) {
            if (lessThan) {
                if (positions[0] <= end) {
                    getEndSegment();
                }
            } else if (positions[0] >= end) {
                getEndSegment();
            }
        } else {
            getEndSegment();
        }
    }


    private void makeMatrix() {
        mMatrix.reset();
        float degrees = (float) (Math.atan2(tans[1], tans[0]) * 180.0 / Math.PI);
        mMatrix.postRotate(degrees, getGoalBitmap().getWidth() / 2, getGoalBitmap().getHeight() / 2);   // 旋转图片
        mMatrix.postTranslate(positions[0] - getGoalBitmap().getWidth() / 2, positions[1] - getGoalBitmap().getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
    }

    //得到path路径当前点的切点坐标---更多解析看下百度
    private void getPosTan() {
        float perLen = percent * mPathMeasure.getLength();
        mPathMeasure.getPosTan(perLen, positions, tans);
    }

    //得到当前百分比下的path段
    private void getEndSegment() {
        float perLen = percent * mPathMeasure.getLength();
        mDstPath.reset();
        mDstPath.lineTo(0, 0);
        mPathMeasure.getSegment(0, perLen, mDstPath, true);
    }

    //绘制阴影
    private void drawShadow(Canvas canvas, boolean fromLeft, RectF rectF) {
        canvas.drawOval(rectF, getShadowPaint());
    }

    //绘制三角形
    private void drawArrow(Canvas canvas, float degress, float endx, float endy) {
        canvas.save();
        canvas.rotate(degress, endx, endy);
        canvas.drawPath(getArrowPath(endx, endy), getArrowPaint());
        canvas.restore();
    }

    //绘制三角形
    private void drawArrow(Canvas canvas) {
        float degrees = (float) (Math.atan2(tans[1], tans[0]) * 180.0 / Math.PI);
        degrees = degrees + 90;
        canvas.save();
        canvas.rotate(degrees, positions[0], positions[1]);
        canvas.drawPath(getArrowPath(), getArrowPaint());
        canvas.restore();
    }

    public void startAnimEvents(long duration, boolean repeat, boolean isLeft, boolean isNextLeft, AnimTypeEnum... actiontypes) {
        reset();
        mIsLeft = isLeft;
        mIsNextLeft = isNextLeft;
        currentTimeMillis = System.currentTimeMillis();
        percent = 0;
        mActiontype = actiontypes;

        for (int i = 0; i < mActiontype.length; i++) {
            AnimTypeEnum typeEnum = mActiontype[i];
            if (typeEnum != null)
                startAnimEvent(typeEnum, duration, i == 0 ? isLeft : isNextLeft, repeat);
        }
    }

    private void startAnimEvent(AnimTypeEnum actionType, long duration, boolean fromLeft, boolean repeat) {

        if (!AnimTypeEnum.hasPictureAction(mActiontype)) {
            if (mAnimActionIv != null) mAnimActionIv.setImageDrawable(null);
            if (mAnimActionTv != null) mAnimActionTv.setText("");
        }

        switch (actionType) {
            case GOAL://进球
                if (mAnimActionIv != null)
                    AnimationListUtil.startAnimationList(fromLeft ? R.drawable.anim_goal_right : R.drawable.anim_goal_left, mAnimActionIv, null, new Runnable() {
                        @Override
                        public void run() {
                            if (mCallback != null && !isTwoAnimTogether())
                                mCallback.onUpdate(1f);
                        }
                    });
                break;
            case CORNER:
                makeCornerPath(!fromLeft);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;//角球
            case SHOT_ON_TARGET://射正
                makeShotTargetPath(fromLeft);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;
            case SHOT_OFF_TARGET://("射偏"),
                makeShotWidePath(fromLeft);
                startAnimation(duration == 0 ? 700 : duration, repeat);
                break;
            case ATTACK_FREE_KICK:
            case FREE_KICK://("任意球"),
            case SAFE_FREE_KICK:////("任意球"),
                makeFreeKickPath(fromLeft, false);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;
            case DANGER_FREE_KICK://("危险的任意球"),
                makeFreeKickPath(fromLeft, true);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;
            case GOAL_KICK:
                makeGoalDoorPath(fromLeft);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;//("球门球"),
            case THROW_IN_ATTACK:
            case THROW_IN_SAFE:
            case THROW_IN://("界外球"),
            case THROW_IN_DANGER://("危险的界外球"),
                makeOutBoundsPath(!fromLeft);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;
            case BALL:
            case BALL_SAFE://("控球"),
            case IN_POSSESSION://("控球"),
                startAnimation(duration == 0 ? 2000 : duration, repeat);
                break;
            case ATTACK://("进攻"),
                startAnimation(duration == 0 ? 3000 : duration, repeat);
                break;
            case PENALTY://("点球"),
                startAnimation(duration == 0 ? 500 : duration, repeat);
                break;
            case DANGEROS_ATTACK://("危险的进攻"),
                startAnimation(duration == 0 ? 3000 : duration, repeat);
                break;
            case YELLOW_CARD://("黄牌"),
            case RED_CARD://("红牌"),
            case SUBSTITUTION://("换人"),
            case OFFSIDE://("越位"),
            case FIRST_HALF_START://("中线开球"),
            case FIRST_HALF_END://("半场休息"),
            case MATCH_ENDED://("比赛结束"),
            case INJURY_TIME://("伤停补时"),
            case FIRST_HALF_ET_END://("加时赛 半场休息"),
            case FIRST_HALF_ET_START://("加时赛 上"),
            case SECOND_HALF_ET_START://("加时赛 下"),
            case SECOND_HALF_ET_END://("加时赛 完场"),
            case TIME_CHANGED://("开球时间更改"),
            case KICK_OFF_DELAYED://("开球延迟"),
            case PLAY_SUSPENDED://("比赛延期"),
            case ABANDONED://("比赛取消"),
            case SUSPENDED_BAD_WEATHER://("天气原因比赛暂停"),
            case SUSPENDED_CROWD_DISTURBANCE://("球场暴动比赛暂停"),
            case SUSPENDED_SERIOUS_INJURY://("球员受伤比赛暂停"),
            case SUSPENDED_FLOODLIGHT_FAILURE://("照明故障比赛暂停"),
            case SUSPENDED://("比赛暂停"),
            case SIGNAL_INTERRUPT://("信号中断"),
            case STANDBY_FOR_PENALTY_SHOOTOUT://("点球大战"),
                if (mAnimActionIv != null && actionType.getStatusResourceId() != 0)
                    mAnimActionIv.setImageResource(actionType.getStatusResourceId());
                if (mAnimActionTv != null)
                    mAnimActionTv.setText(actionType.getAction());
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null && !isTwoAnimTogether())
                            mCallback.onUpdate(1f);
                    }
                }, duration == 0 ? 1000 : duration);
                break;
            default:
                break;
        }
    }

    private void doPathAnim(Canvas canvas, AnimTypeEnum actionType, boolean fromLeft) {
        if (actionType == null)
            return;
        switch (actionType) {
            case CORNER:
                doCornerPathAnim(canvas, !fromLeft);
                break;//角球
            case SHOT_ON_TARGET://射正
                doShotTargetPathAnim(canvas, fromLeft);
                break;
            case SHOT_OFF_TARGET://("射偏"),
                doShotWidePathAnim(canvas, fromLeft);
                break;
            case ATTACK_FREE_KICK:
            case FREE_KICK://("任意球"),
            case SAFE_FREE_KICK:////("任意球"),
                doFreeKickPathAnim(canvas, fromLeft);
                break;
            case DANGER_FREE_KICK://("危险的任意球"),
                doFreeKickPathAnim(canvas, fromLeft);
                break;
            case GOAL_KICK:
                doGoalDoorPathAnim(canvas, fromLeft);
                break;//("球门球"),
            case THROW_IN_ATTACK:
            case THROW_IN_SAFE:
            case THROW_IN://("界外球"),
            case THROW_IN_DANGER://("危险的界外球"),
                doOutBoundsPathAnim(canvas, !fromLeft);
                break;
            case BALL:
            case BALL_SAFE://("控球"),
            case IN_POSSESSION://("控球"),
                doBallControlPathAnim(canvas, fromLeft);
                break;
            case ATTACK://("进攻"),
                doAttackPathAnim(canvas, fromLeft);
                break;
            case PENALTY://("点球"),
                doPenaltyPathAnim(canvas, fromLeft);
                break;
            case DANGEROS_ATTACK:
                doDangerousAttackPathAnim(canvas, fromLeft);
                break;//("危险的进攻"),
        }
    }


    private void startAnimation(final long duration, boolean repeat) {
        if (animator != null && isRuning)
            return;
        isRuning = true;
        animator = getValueAnimator();
        animator.setRepeatCount(repeat ? -1 : 0);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                animation.setDuration(1500);
            }
        });
        startValueAnimation(getListener());
    }

    public void cancelAnimation() {
        if (animator != null) {
            isRuning = false;
            animator.cancel();
            animator.removeAllListeners();
            animator.removeAllUpdateListeners();
        }
    }

    private boolean isTwoAnimTogether() {
        return mActiontype == null ? false : mActiontype.length >= 2 && mActiontype[1] != null;
    }

    public void reset() {
        cancelAnimation();
        mActiontype = null;
        invalidate();
    }

    public interface AnimatorCallback {
        void onUpdate(float percent);
    }

    private AnimatorCallback mCallback;

    public void setAnimatorUpdateCallback(AnimatorCallback callback) {
        this.mCallback = callback;
    }


    private ValueAnimator.AnimatorUpdateListener getListener() {
        ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = animation.getAnimatedFraction();
                invalidate();
                if (mCallback != null && percent >= 1f) {
                    long diff = System.currentTimeMillis() - currentTimeMillis;
                    if (diff <= 16) {
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mCallback.onUpdate(percent);
                            }
                        }, 16 - diff);
                    } else {
                        mCallback.onUpdate(percent);
                    }
                } else if (percent >= 1f) {
                    mCallback.onUpdate(percent);
                }

            }
        };

        return mUpdateListener;
    }
}
