package com.android.banana.commlib.utils;

/**
 * Created by qiaomu on 2017/6/16.
 */

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/****
 * 此工具类源于stack over flow
 * 原文链接:http://stackoverflow.com/questions/8692328/causing-outofmemoryerror-in-frame-by-frame-animation-in-android
 * 主要使用了BitmapFactory.decodeByteArray方法通过底层C来绘制图片,有效防止OOM
 * 使用了第三方类库:org.apache.commons.io.IOUtils,将Inputstream转为byte字节数组
 * *******/
public class AnimationListUtil {

    public static class MyFrame {
        byte[] bytes;
        int duration;
        Drawable drawable;
        boolean isReady = false;
    }

    static class PackageAnimation {
        List<MyFrame> list;
        int repeatCount;
        int currentRepeatCount = 1;
        int resId;
    }

    static HashMap<Integer, PackageAnimation> map = new HashMap<>();

    public static void stopAnimation(int resId) {
        if(map.get(resId)!=null){
            map.get(resId).list.clear();
            map.remove(resId);
        }
    }

    public interface OnDrawableLoadedListener {
        public void onDrawableLoaded(PackageAnimation myFrames);
    }
// 1

    /***
     * 性能更优
     * 在animation-list中设置时间
     * **/
    public static void startAnimationList(int resourceId,
                                          final ImageView imageView, final Runnable onStart,
                                          final Runnable onComplete) {
        loadRaw(resourceId, imageView.getContext(),
                new OnDrawableLoadedListener() {
                    @Override
                    public void onDrawableLoaded(PackageAnimation myFrames) {
                        if (onStart != null) {
                            onStart.run();
                        }
                        animateRawManually(myFrames, imageView, onComplete);
                    }
                }, 1);
    }

    /**
     * @param resourceId
     * @param imageView
     * @param onStart
     * @param onComplete
     * @param repeatCount 帧动画的播放次数
     */
    public static void startAnimationList(final int resourceId,
                                          final ImageView imageView, final Runnable onStart,
                                          final Runnable onComplete, final int repeatCount) {
        /*startAnimationList(resourceId, imageView, onStart, new Runnable() {
            @Override
            public void run() {
                currentRepeatCount++;
                if (currentRepeatCount >= repeatCount) {
                    currentRepeatCount = 0;
                    if (onComplete != null) {
                        onComplete.run();
                    }
                    return;
                }
                startAnimationList(resourceId, imageView, onStart, this);
            }
        });*/
        loadRaw(resourceId, imageView.getContext(),
                new OnDrawableLoadedListener() {
                    @Override
                    public void onDrawableLoaded(PackageAnimation myFrames) {
                        if (onStart != null) {
                            onStart.run();
                        }
                        animateRawManually(myFrames, imageView, onComplete);
                    }
                }, repeatCount);
    }

    // 2
    private static void loadRaw(final int resourceId, final Context context,
                                final OnDrawableLoadedListener onDrawableLoadedListener,
                                int repeatCount) {
        loadFromXml(resourceId, context, onDrawableLoadedListener, repeatCount);
    }

    // 3
    private static void loadFromXml(final int resourceId,
                                    final Context context,
                                    final OnDrawableLoadedListener onDrawableLoadedListener,
                                    final int repeatCount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<MyFrame> myFrames = new ArrayList<>();
                XmlResourceParser parser = context.getResources().getXml(resourceId);
                try {
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        if (eventType == XmlPullParser.START_DOCUMENT) {
                        } else if (eventType == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("item")) {
                                byte[] bytes = null;
                                int duration = 1000;
                                for (int i = 0; i < parser.getAttributeCount(); i++) {
                                    if (parser.getAttributeName(i).equals(
                                            "drawable")) {
                                        int resId = Integer.parseInt(parser
                                                .getAttributeValue(i)
                                                .substring(1));
                                        InputStream inputStream = context.getResources().openRawResource(resId);
                                        bytes = toByteArray(inputStream);
                                    } else if (parser.getAttributeName(i).equals("duration")) {
                                        duration = parser.getAttributeIntValue(i, 1000);
                                    }
                                }
                                MyFrame myFrame = new MyFrame();
                                myFrame.bytes = bytes;
                                myFrame.duration = duration;
                                myFrames.add(myFrame);
                            }
                        } else if (eventType == XmlPullParser.END_TAG) {
                        } else if (eventType == XmlPullParser.TEXT) {
                        }
                        eventType = parser.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
// TODO: handle exception
                    e2.printStackTrace();
                }
// Run on UI Thread
                new Handler(context.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (onDrawableLoadedListener != null) {
                            PackageAnimation pa = new PackageAnimation();
                            pa.repeatCount = repeatCount;
                            pa.list = myFrames;
                            pa.resId = resourceId;
                            map.put(resourceId, pa);
                            onDrawableLoadedListener.onDrawableLoaded(pa);
                        }
                    }
                });
            }
        }).run();
    }

    // 4
    private static void animateRawManually(PackageAnimation myFrames,
                                           ImageView imageView, Runnable onComplete) {
        animateRawManually(myFrames, imageView, onComplete, 0);
    }


    public static Bitmap byteToBitmap(byte[] imgByte) {
        InputStream input = null;
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        input = new ByteArrayInputStream(imgByte);
        SoftReference softRef = new SoftReference(BitmapFactory.decodeStream(
                input, null, options));
        bitmap = (Bitmap) softRef.get();
        if (imgByte != null) {
            imgByte = null;
        }

        try {
            if (input != null) {
                input.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }

    // 5
    private static void animateRawManually(final PackageAnimation myFrames,
                                           final ImageView imageView, final Runnable onComplete,
                                           final int frameNumber) {

        if (!map.containsKey(myFrames.resId)) return;
        final MyFrame thisFrame = myFrames.list.get(frameNumber);
        if (frameNumber == 0) {
            thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), BitmapFactory.decodeByteArray(
                    thisFrame.bytes, 0, thisFrame.bytes.length));
           /* thisFrame.drawable = new BitmapDrawable(imageView.getContext()
                    .getResources(), byteToBitmap(thisFrame.bytes));*/
        } else {
            MyFrame previousFrame = myFrames.list.get(frameNumber - 1);
            ((BitmapDrawable) previousFrame.drawable).getBitmap().recycle();
            previousFrame.drawable = null;
            previousFrame.isReady = false;
        }
        imageView.setImageDrawable(thisFrame.drawable);

        int tempNumber = frameNumber;
        if (frameNumber + 1 == myFrames.list.size() && myFrames.currentRepeatCount <= myFrames.repeatCount) {
            tempNumber = -1;
            myFrames.currentRepeatCount++;
        }
        final int myFrameNumber = tempNumber;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Make sure ImageView hasn't been changed to a different Image
                // in this time
                if (imageView.getDrawable() == thisFrame.drawable) {
                    if (myFrameNumber + 1 < myFrames.list.size()) {
                        MyFrame nextFrame = myFrames.list.get(myFrameNumber + 1);
                        if (nextFrame.isReady) {
                            // Animate next frame
                            animateRawManually(myFrames, imageView, onComplete,
                                    myFrameNumber + 1);
                        } else {
                            nextFrame.isReady = true;
                        }
                    } else {
                        if (onComplete != null) {
                            myFrames.list.clear();
                            map.remove(myFrames.resId);
                            onComplete.run();
                        }
                    }
                }
            }
        }, thisFrame.duration);

        // Load next frame
        if (myFrameNumber + 1 < myFrames.list.size()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MyFrame nextFrame = myFrames.list.get(myFrameNumber + 1);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(nextFrame.bytes, 0, nextFrame.bytes.length);
                    nextFrame.drawable = new BitmapDrawable(imageView.getContext().getResources(), bitmap);
                    if (nextFrame.isReady) {
                        // Animate next frame
                        animateRawManually(myFrames, imageView, onComplete,
                                myFrameNumber + 1);
                    } else {
                        nextFrame.isReady = true;
                    }
                }
            }).run();
        }
    }

    public static byte[] toByteArray(InputStream input)
            throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static int copy(InputStream input, OutputStream output)
            throws IOException {
        long count = copyLarge(input, output);
        if (count > 2147483647L) {
            return -1;
        }
        return (int) count;
    }

    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        byte[] buffer = new byte[4096];
        long count = 0L;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}
