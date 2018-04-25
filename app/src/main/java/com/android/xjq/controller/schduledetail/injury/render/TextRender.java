package com.android.xjq.controller.schduledetail.injury.render;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.android.banana.commlib.utils.LibAppUtil;

import java.util.List;

/**
 * Created by kokuma on 2017/8/10.
 */

public abstract class TextRender {

    public List<String> list;

    public Paint linePaint, txtPaint;
    public float txtHeight;
    public int cellWidth ;

    public int fixedCellWidth ;
    public boolean shouldDrawBotLine;


    public int viewWidth , viewHeight;
    protected  int mainTxtColor;
    float[] dpCellWidths;

    Context context;

    public  TextRender(Context context){
        this.context=context;
        shouldDrawBotLine = false;
        cellWidth = LibAppUtil.dip2px(context, 45);
        fixedCellWidth = LibAppUtil.dip2px(context, 90);
    }

    public  TextRender setPaint(Paint linePaint, Paint txtPaint){
        this.linePaint=linePaint;
        this.txtPaint=txtPaint;
        mainTxtColor= txtPaint.getColor();
        return this;
    }
    public  TextRender setCellInfo(int cellWidth, int fixedCellWidth,float txtHeight){
        this.cellWidth=cellWidth;
        this.fixedCellWidth=fixedCellWidth;
        this.txtHeight=txtHeight;
        return this;
    }

    public  TextRender setCellfixedWidth(float[] dpCellWidths){
        this.dpCellWidths=dpCellWidths;
        return this;
    }

    public  TextRender setDataList(List<String> list){
        this.list=list;
        return this;
    }

    public abstract void onDraw(Canvas canvas);
}
