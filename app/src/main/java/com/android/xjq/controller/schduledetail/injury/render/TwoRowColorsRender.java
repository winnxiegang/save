package com.android.xjq.controller.schduledetail.injury.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.android.banana.commlib.utils.LibAppUtil;
import com.android.jjx.sdk.utils.LogUtils;


/**
 * Created by kokuma on 2017/8/10.
 */

public class TwoRowColorsRender extends TextRender {

    protected int colorGray = Color.parseColor("#666666");
    protected int colorRed = Color.parseColor("#f63f3f");
    protected int colorBlue = Color.parseColor("#52acff");
    protected int colorGreen = Color.parseColor("#56ba37");

    public TwoRowColorsRender(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {
        Paint p = new Paint();
        for (int i = 0; i < list.size(); i++) {
            String needDraw = list.get(i) == null ? "" : list.get(i);
            if (needDraw.indexOf(",") != -1) {
                String[] txts = needDraw.split(",");
                if (txts != null && txts.length >= 2) {
                    float txtTopWidth = txtPaint.measureText(txts[0]);
                    float txtBottWidth = txtPaint.measureText(txts[1]);
                    txtPaint.setColor(colorGray);
                    float  leftW= getLeftLength(i);

                    float curDp = dpCellWidths==null?0:dpCellWidths[i];
                    float curLength = curDp>0? LibAppUtil.dip2px(context, curDp):cellWidth;
                    LogUtils.i("xxl","curLength1-"+curLength);
                    canvas.drawText(txts[0], leftW - txtTopWidth / 2, (float) (viewHeight / 2) - 4, txtPaint);
                    txtPaint.setColor(mainTxtColor);
                    canvas.drawText(txts[1], leftW - txtBottWidth / 2, (float) (viewHeight / 2) + txtHeight, txtPaint);
                }
//                StaticLayout myStaticLayout = new StaticLayout(message, tp, canvas.getWidth(), Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
//                myStaticLayout.draw(canvas);
            } else{
                 if (needDraw.equals("赢")) {
                    txtPaint.setColor(colorRed);
                } else if(needDraw.equals("走")||needDraw.equals("平")){
                    txtPaint.setColor(colorBlue);
                } else if(needDraw.equals("输")){
                    txtPaint.setColor(colorGreen);
                }else {
                    txtPaint.setColor(mainTxtColor);
                }
                int txtWidth = (int) txtPaint.measureText(needDraw);
                float curDp = dpCellWidths==null?0:dpCellWidths[i];
                float curLength = curDp>0?LibAppUtil.dip2px(context, curDp):cellWidth;
                LogUtils.i("xxl","curLength2-"+curLength);

                if (curLength > txtWidth) {
                    canvas.drawText(needDraw,getLeftLength(i) - txtWidth / 2 , (float) (viewHeight / 2) + txtHeight / 4, txtPaint);
                } else {
                    int overLength = needDraw.length() - getCellFixLength(needDraw, txtWidth);
                    overLength = overLength < 0 ? 0 : overLength;
                    String needTop = needDraw.substring(0, overLength);
                    String needBottom = needDraw.substring(overLength, needDraw.length());
                    float txtTopWidth = txtPaint.measureText(needTop);
                    float txtBottWidth = txtPaint.measureText(needBottom);
                    float  leftW= getLeftLength(i);
                    canvas.drawText(needTop,leftW  - txtTopWidth / 2, (float) (viewHeight / 2) - 4, txtPaint);
                    canvas.drawText(needBottom,leftW- txtBottWidth / 2, (float) (viewHeight / 2) + txtHeight, txtPaint);
                    //canvas.drawText(list.get(i), i * cellWidth + cellWidth / 2 - txtWidth / 2, (float) (height / 2) + txtHeight / 4, txtPaint);
                    // canvas.drawLine(cellWidth * i, 0, cellWidth * i, height, linePaint);//右边的线
                }
            }
        }
    }

    float getLeftLength(int index){
        float leftPosition = 0;
        if(dpCellWidths!=null){
            for(int i=0;i<index;i++){
                if(dpCellWidths[i]>0){
                    leftPosition += LibAppUtil.dip2px(context, dpCellWidths[i]) ;
                }else {
                    leftPosition += cellWidth;
                }
            }
            float curDp=dpCellWidths[index];
            float curLength = curDp>0?LibAppUtil.dip2px(context, curDp):cellWidth;
            leftPosition += curLength/2;
        }else {
            leftPosition = index * cellWidth ;
            leftPosition += cellWidth/2;
        }
        return leftPosition;
    }


    protected int getCellFixLength(String txt, int txtWidth) {
        int overLength = txtWidth - cellWidth;//15

        int byteLength = txtWidth / txt.length();//10
        if (overLength <= byteLength)
            return 1;
        int i = overLength / byteLength + 1;

        return i < 0 ? 0 : i;
    }
}
