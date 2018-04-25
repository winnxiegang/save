package com.tencent.ilivesdk.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.GestureDetector;

import com.tencent.av.opengl.GraphicRendererMgr;
import com.tencent.av.opengl.glrenderer.GLCanvas;
import com.tencent.av.opengl.texture.YUVTexture;
import com.tencent.av.opengl.utils.Utils;
import com.tencent.av.utils.QLog;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLog;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.tools.quality.ILiveQualityData;

/**
 * 视频渲染基础控件
 */
public class BaseVideoView extends GLVideoView{
    private static String TAG = "AVVideoView";

    /**
     * 渲染模式
     */
    public enum BaseRenderMode {
        /** 全屏模式 */
        SCALE_TO_FIT,
        /** 黑边模式 */
        BLACK_TO_FILL,
    }

    private boolean rotate = false;
    private BaseRenderMode sameDirectionRenderMode = BaseRenderMode.SCALE_TO_FIT;
    private BaseRenderMode diffDirectionRenderMode = BaseRenderMode.BLACK_TO_FILL;

    private boolean bRendering = false;     // 是否正在渲染
    private boolean isLandscape;
    private boolean notDegreeFix = false;
    private boolean needLog = true;

    private GestureDetector.SimpleOnGestureListener gestureListener;
    private VideoListener videoListener;

    public BaseVideoView() {
    }

    public BaseVideoView(Context context, GraphicRendererMgr graphicRenderMgr) {
        super(context, graphicRenderMgr);

        // 覆盖到达回调
        mYuvTexture.setGLRenderListener(new YUVTexture.GLRenderListener() {
            @Override
            public void onRenderFrame() {
                invalidate();
            }

            @Override
            public void onRenderReset() {
                flush();
                invalidate();
            }

            @Override
            public void onRenderFlush() {
                flush();
                invalidate();
            }

            @Override
            public void onRenderInfoNotify(int width, int height, int angle) {
                if (QLog.isColorLevel()) {
                    QLog.d(TAG, QLog.CLR, "onRenderInfoNotify uin: " + mIdentifier + ", mVideoSrcType: " + mVideoSrcType + ", width: " + width + ", height: " + height + ", angle: " + angle);
                }

                if (isFristFrame == false) {
                    if (null != videoListener){
                        videoListener.onFirstFrameRecved(width, height, angle, getIdentifier());
                    }
                    isFristFrame = true;
                }
                mImageWidth = width;
                mImageHeight = height;
                mImageAngle = angle;
                mYuvTexture.setTextureSize(width, height);
                // refresh();
                invalidate();
                if (mIdentifier != null) {
                    ILiveQualityData.addLive(mIdentifier, width, height);
                }
            }
        });
    }

    /**
     * 设置Gesture事件
     * @param gestureListener
     */
    public void setGestureListener(GestureDetector.SimpleOnGestureListener gestureListener) {
        this.gestureListener = gestureListener;
    }

    /**
     * 设置是否旋转画面(与view长宽比一致)
     * @param rotate
     */
    public void setRotate(boolean rotate) {
        this.rotate = rotate;
        needLog = true;
    }

    /**
     * 设置方向一致(画面与view)的渲染模式
     * @param sameDirectionRenderMode 渲染模式
     * @see BaseRenderMode
     */
    public void setSameDirectionRenderMode(BaseRenderMode sameDirectionRenderMode) {
        this.sameDirectionRenderMode = sameDirectionRenderMode;
        needLog = true;
    }

    /**
     * 设置方向不一致(画面与view)的渲染模式
     * @param diffDirectionRenderMode 渲染模式
     * @see BaseRenderMode
     */
    public void setDiffDirectionRenderMode(BaseRenderMode diffDirectionRenderMode) {
        this.diffDirectionRenderMode = diffDirectionRenderMode;
        needLog = true;
    }

    public GestureDetector.SimpleOnGestureListener getGestureListener() {
        return gestureListener;
    }

    public boolean isRendering() {
        return bRendering;
    }

    public void setRendering(boolean bRendering) {
        this.bRendering = bRendering;
    }


    //  视频相关数据获取

    /**
     * 获取视频宽度
     * @return
     */
    public int getImageWidth(){
        return mImageWidth;
    }

    /**
     * 获取视频高度
     * @return
     */
    public int getImageHeight(){
        return mImageHeight;
    }

    /**
     * 获取视频旋转角度
     * @return
     */
    public int getImageAngle(){
        return mImageAngle;
    }

    /**
     * 获取视频首帧是否到达
     * @return
     */
    public boolean isFirstFrameRecved(){
        return isFristFrame;
    }

    @Override
    protected void render(GLCanvas canvas) {
        Rect p = getPaddings();
        renderBackground(canvas);
        if (null != mIdentifier && mYuvTexture != null && mYuvTexture.canRender() && mNeedRenderVideo) {
            isLandscape = ILiveSDK.getInstance().getAppContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            enableLoading(false);
            int uiWidth = getWidth();
            int uiHeight = getHeight();
            int width = uiWidth - p.left - p.right;
            int height = uiHeight - p.top - p.bottom;
            int angle = mYuvTexture.getImgAngle();
            int rotation = (angle + mRotation + 4) % 4;
            if (!notDegreeFix) {
                notDegreeFix = angle!=0&&!isLocal();
            }
            if (rotation % 2 == 0) {
                rotation = 1;
            }
            float imgW = mYuvTexture.getImgWidth();
            float imgH = mYuvTexture.getImgHeight();
            float sRatio = imgW / imgH;
            float tmpRatio = (float)width / (float)height;

            float x = p.left;
            float y = p.top;
            float w = width;
            float h = height;
            boolean switched = false;   // 是否旋转

           if (switchWH(w/h, sRatio, isLocal()) || notDegreeFix&&(sRatio > 1||rotation % 2 != 0)) {
                float tmp = x;
                x = y;
                y = tmp;
                tmp = w;
                w = h;
                h = tmp;
                tmp = width;
                width = height;
                height = (int) tmp;
                switched = true;
            }



            float targetW = w;
            float targetH = h;
            float tRatio = targetW / targetH;

            boolean hasBorder = false;      // 是否黑边显示

            if (hasBlackBorder(tRatio, sRatio) && !notDegreeFix) {
                hasBorder = true;
                if (tRatio < sRatio) {
                    w = targetW;
                    h = w / sRatio;
                    if (h > targetH) {
                        h = targetH;
                        w = h * sRatio;
                        x += (targetW - w) / 2;
                        // y = 0;
                    } else {
                        // x = 0;
                        y += (targetH - h) / 2;
                    }
                } else {
                    h = targetH;
                    w = h * sRatio;
                    if (w > targetW) {
                        w = targetW;
                        h = w / sRatio;
                        // x = 0;
                        y += (targetH - h) / 2;
                    } else {
                        x += (targetW - w) / 2;
                        // y = 0;
                    }
                }
                targetW = w;
                targetH = h;
                tRatio = targetW / targetH;
            } else {
                int tempW = (int)imgW;
                if (tempW % 8 !=0) {
                    //opengl补了黑边，此处要裁剪
                    imgW = (float)(tempW * tempW) / (float)((tempW / 8 + 1) * 8);
                    imgH = imgW / sRatio;
                }
            }

            x = x * mScale + mPivotX * (1 - mScale);
            y = y * mScale + mPivotY * (1 - mScale);
            w = w * mScale;
            h = h * mScale;

            if (!mDragging && mPosition != NONE) {
                if ((mPosition & (LEFT | RIGHT)) == (LEFT | RIGHT)) {
                    mOffsetX = width / 2 - (x + w / 2);
                } else if ((mPosition & LEFT) == LEFT) {
                    mOffsetX = -x;
                } else if ((mPosition & RIGHT) == RIGHT) {
                    mOffsetX = width - w - x;
                }
                if ((mPosition & (TOP | BOTTOM)) == (TOP | BOTTOM)) {
                    mOffsetY = height / 2 - (y + h / 2);
                } else if ((mPosition & TOP) == TOP) {
                    mOffsetY = -y;
                } else if ((mPosition & BOTTOM) == BOTTOM) {
                    mOffsetY = height - h - y;
                }
                mPosition = NONE;
            }
            x += mOffsetX;
            y += mOffsetY;

            mX = (int) x;
            mY = (int) y;
            mWidth = (int) w;
            mHeight = (int) h;

            if (sRatio > tRatio) {
                float newSourceH = imgH;
                float newSourceW = newSourceH * tRatio;
                if (Utils.getGLVersion(mContext) == 1) {
                    newSourceW = imgW * newSourceW / Utils.nextPowerOf2((int) imgW);
                }
                float offset = (imgW - newSourceW) / 2;
                mYuvTexture.setSourceSize((int) newSourceW, (int) newSourceH);
                mYuvTexture.setSourceLeft((int) offset);
                mYuvTexture.setSourceTop(0);
            } else {
                float newSourceW = imgW;
                float newSourceH = newSourceW / tRatio;
                if (Utils.getGLVersion(mContext) == 1) {
                    newSourceH = imgH * newSourceH / Utils.nextPowerOf2((int) imgH);
                }
                float offset = (imgH - newSourceH) / 2;
                mYuvTexture.setSourceSize((int) newSourceW, (int) newSourceH);
                mYuvTexture.setSourceLeft(0);
                mYuvTexture.setSourceTop((int) offset);
            }

            if (Utils.getGLVersion(mContext) == 1) {
                float newSourceW = imgW;
                float newSourceH = imgH;
                float offset = 0;
                mYuvTexture.setSourceSize((int) newSourceW, (int) newSourceH);
                mYuvTexture.setSourceLeft(0);
                mYuvTexture.setSourceTop((int) offset);
            }

            canvas.save(GLCanvas.SAVE_FLAG_MATRIX);
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            canvas.translate(centerX, centerY);
            if (mMirror) {
                if (mRotation % 2 == 0) {
                    canvas.scale(-1, 1, 1);
                } else {
                    canvas.scale(1, -1, 1);
                }
            }
            if (notDegreeFix) {
                canvas.rotate(rotation * 90, 0, 0, 1);
                if (rotation % 2 != 0) {
                    canvas.translate(-centerY, -centerX);
                } else {
                    canvas.translate(-centerX, -centerY);
                }
            }else {
                if (isLocal()) {
                    if (isLandscape) {
                        if (ILiveRoomManager.getInstance().getCurCameraId() != ILiveConstants.BACK_CAMERA) {
                            canvas.rotate(angle%2 == 0 ? 0:180, 0, 0, 1);
                        }
                        canvas.translate(-centerX, -centerY);
                    }else {
                        canvas.rotate(rotation * 90, 0, 0, 1);
                        canvas.translate(-centerY, -centerX);
                    }

                }else {
                    if (!rotate) {
                        if (isLandscape) {

                            canvas.rotate((mRotation+2)%4*90, 0, 0, 1);
                        }else {
                            canvas.rotate(mRotation*90, 0, 0, 1);
                        }
                    }else {
                        if (!isSameDegree(tmpRatio, sRatio)) {
                            if (mRotation == 0 || mRotation == 3) {
                                canvas.rotate(270, 0, 0, 1);
                            }else {
                                canvas.rotate(isLandscape?270:90, 0, 0, 1);
                            }
                        }
                    }
                    if (!rotate) {
                        if (mRotation%2 == 0 && !isLandscape || isLandscape && mRotation%2 != 1) {
                            canvas.translate(-centerX, -centerY);
                        }else {
                            canvas.translate(-centerY, -centerX);
                        }
                    }else {
                        if (isSameDegree(tmpRatio, sRatio)) {
                            canvas.translate(-centerX, -centerY);
                        }else {
                            canvas.translate(-centerY, -centerX);
                        }

                    }


                }
            }

            if (needLog){
                ILiveLog.d(TAG, "render->same:"+sameDirectionRenderMode+", diff:"+diffDirectionRenderMode);
                ILiveLog.d(TAG, "render->id:"+getIdentifier()+", switched:"+switched+", hasBorder:"+hasBorder);
                needLog = false;
            }

            mYuvTexture.draw(canvas, mX, mY, mWidth, mHeight);
            canvas.restore();
        }
        // render loading
        if (mLoading && mLoadingTexture != null) {
            mLoadingAngle = mLoadingAngle % 360;
            int uiWidth = getWidth();
            int uiHeight = getHeight();
            int width = mLoadingTexture.getSourceWidth();
            int height = mLoadingTexture.getSourceHeight();
            if (width > uiWidth) {
                width = uiWidth;
            }
            if (height > uiHeight) {
                height = uiHeight;
            }
            canvas.save(GLCanvas.SAVE_FLAG_MATRIX);
            canvas.translate(uiWidth / 2, uiHeight / 2);
            canvas.rotate(mLoadingAngle, 0, 0, 1);
            canvas.translate(-width / 2, -height / 2);
            mLoadingTexture.draw(canvas, 0, 0, width, height);
            canvas.restore();
            long now = System.currentTimeMillis();
            if (now - mLastLoadingTime >= LOADING_ELAPSE) {
                mLastLoadingTime = now;
                mLoadingAngle += 8;
            }
        }
        // render text
        if (mStringTexture != null) {
            int uiWidth = getWidth();
            int uiHeight = getHeight();
            int width = mStringTexture.getSourceWidth();
            int height = mStringTexture.getSourceHeight();
            if (width > uiWidth) {
                width = uiWidth;
            }
            if (height > uiHeight) {
                height = uiHeight;
            }
            canvas.save(GLCanvas.SAVE_FLAG_MATRIX);
            canvas.translate(uiWidth / 2 - width / 2, uiHeight / 2 - height / 2);
            mStringTexture.draw(canvas, 0, 0, width, height);
            canvas.restore();
        }
    }



    private boolean isSameDegree(double r1, double r2) {
        return (r1 > 1 && r2 > 1) || (r1 < 1 && r2 < 1);
    }

    boolean switchWH(double r1, double r2, boolean isLocal) {
        if (isLocal) {
            return !isLandscape;
        }
//        //本地高宽是否需调换（从观众角度看）
        return rotate && !isSameDegree(r1, r2) || !rotate && mRotation%2==1;
    }

    boolean isLocal() {
        return null != mIdentifier && mIdentifier.equals(ILiveLoginManager.getInstance().getMyUserId());
    }


    /**
     * 清除缓存数据
     */
    public void resetCache(){
        isFristFrame = false;
        mImageWidth = 0;
        mImageHeight = 0;
        mImageAngle = 0;
    }


    boolean hasBlackBorder(double r1, double r2) {
        if (null != mIdentifier && mIdentifier.equals("")) {
            return false;
        }
        if (isLocal()) {
            return false;
        }
        if (isSameDegree(r1, r2)) {
            return sameDirectionRenderMode == BaseRenderMode.BLACK_TO_FILL;
        }else {
            return diffDirectionRenderMode == BaseRenderMode.BLACK_TO_FILL;
        }
    }

    /**
     * 设置视频数据回调
     * @param listener
     */
    public void setVideoListener(VideoListener listener){
        this.videoListener = listener;
    }

    public VideoListener getVideoListener(){
        return videoListener;
    }
}
