package com.tencent.ilivesdk.core.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;

import com.tencent.ilivesdk.core.ILiveLog;

import java.util.ArrayList;
import java.util.List;

/**
 * 加速度控制器 用来控件对焦
 */
public class SensorControl implements SensorEventListener {
    private static final String TAG = "ILVB-SensorControl";

    private Camera camera;
    private SensorManager sensorManager;
    private Sensor sensor;

    public static int mScreenWidth = 0;
    public static int mScreenHeight = 0;

    private Camera.Size cameraSize;
    private float lastX = 0, lastY = 0, lastZ = 0;
    private boolean bFocusLock = false;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    public SensorControl(Context context){
        DisplayMetrics mDisplayMetrics = context.getResources()
                .getDisplayMetrics();
        mScreenWidth = mDisplayMetrics.widthPixels;
        mScreenHeight = mDisplayMetrics.heightPixels;

        sensorManager = (SensorManager) context.getSystemService(Activity.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (null == event.sensor)
            return;

        if (Sensor.TYPE_ACCELEROMETER == event.sensor.getType()) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            if (0 == lastX && 0 == lastY && 0 == lastZ) {
                // do nothing
            }else{
                float dx = x - lastX;
                float dy = y - lastY;
                float dz = z - lastZ;
                double value = Math.sqrt(dx*dx + dy*dy + dz*dz);
                if (value > 1.0 && !bFocusLock){
                    ILiveLog.v(TAG, "onSensorChanged->focus touched:"+value+"/"+bFocusLock);
                    bFocusLock = true;
                    mainHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 摄像头打开才对焦
                            Point point = new Point(mScreenWidth/2, mScreenHeight/2);
                            boolean bRet = onFocus(point, new Camera.AutoFocusCallback() {
                                @Override
                                public void onAutoFocus(final boolean success, Camera camera) {
                                    mainHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            //一秒之后才能再次对焦
                                            bFocusLock = false;
                                            ILiveLog.v(TAG, "onFocus->result: "+success);
                                        }
                                    }, 1000);
                                }
                            });
                            if (!bRet){
                                bFocusLock = false;
                            }
                        }
                    }, 0);
                }
            }

            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    private Camera.Size getBestPreviewSize(int surfaceViewWidth, int surfaceViewHeight, Camera.Parameters parameters) {
        Camera.Size bestSize=null;

        //不同机器 尺寸大小排序方式不一样  有的从小到大有的从大到小
        for (Camera.Size size : parameters.getSupportedPreviewSizes())
        {
            if (size.width <= surfaceViewWidth && size.height <= surfaceViewHeight)
            {
                if (bestSize == null) //初始化一个值
                    bestSize=size;
                else
                {
                    int tempArea=bestSize.width * bestSize.height;
                    int newArea=size.width * size.height;

                    if (newArea > tempArea) //取满足条件里面最大的
                        bestSize=size;
                }
            }
        }

        return bestSize;
    }


    /**
     * 手动聚焦
     *
     * @param point 触屏坐标
     */
    private Camera.Parameters frontCamera;
    protected boolean onFocus(Point point, Camera.AutoFocusCallback callback) {
        if (camera == null) {
            return false;
        }

        Camera.Parameters parameters = null;
        ILiveLog.v(TAG, "onFocus->camera:"+camera+"/paramters:"+parameters);
        try {
            parameters = camera.getParameters();
            ILiveLog.v(TAG, "onFocus->camera parameters:"+parameters.getPreviewSize().width+","+parameters.getPreviewSize().height);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //不支持设置自定义聚焦，则使用自动聚焦，返回

        if(Build.VERSION.SDK_INT >= 14) {

            if (parameters.getMaxNumFocusAreas() <= 0) {
                return focus(camera, callback);
            }

            ILiveLog.i(TAG, "onCameraFocus:" + point.x + "," + point.y);

            List<Camera.Area> areas = new ArrayList<>();
            int left = point.x - 300;
            int top = point.y - 300;
            int right = point.x + 300;
            int bottom = point.y + 300;
            left = left < -1000 ? -1000 : left;
            top = top < -1000 ? -1000 : top;
            right = right > 1000 ? 1000 : right;
            bottom = bottom > 1000 ? 1000 : bottom;
            areas.add(new Camera.Area(new Rect(left, top, right, bottom), 100));
            parameters.setFocusAreas(areas);
            try {
                //本人使用的小米手机在设置聚焦区域的时候经常会出异常，看日志发现是框架层的字符串转int的时候出错了，
                //目测是小米修改了框架层代码导致，在此try掉，对实际聚焦效果没影响
                camera.setParameters(parameters);
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                return false;
            }
        }


        return focus(camera, callback);
    }

    private boolean focus(Camera camera, Camera.AutoFocusCallback callback) {
        try {
            camera.autoFocus(callback);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void startListener(Camera camera){
        updateCamera(camera);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void resetParam(){
        lastX = 0;
        lastY = 0;
        lastZ = 0;

        bFocusLock = false;
    }

    public void updateCamera(Camera camera){
        this.camera = camera;
        resetParam();
    }

    public void stopListener(){
        sensorManager.unregisterListener(this, sensor);
    }
}
