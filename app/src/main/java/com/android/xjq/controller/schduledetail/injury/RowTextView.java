package com.android.xjq.controller.schduledetail.injury;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.android.banana.commlib.utils.LibAppUtil;
import com.android.xjq.R;
import com.android.xjq.controller.schduledetail.injury.render.TextRender;
import com.android.xjq.controller.schduledetail.injury.render.TwoRowColorsRender;

import java.util.List;

import static com.android.xjq.controller.schduledetail.injury.TextViewType.HasTwoRowColors;


/**
 * Created by kokuma on 2017/8/10.
 */

public class RowTextView extends AppCompatTextView {
    protected List<String> list;

    protected Paint linePaint, txtPaint;

    protected float txtHeight;


    protected int cellWidth = LibAppUtil.dip2px(getContext(), 45);//单元格宽度，默认45dp，如果不够充满屏幕，单元格宽度会被增大

    protected int fixedCellWidth ;//离父布局左侧多大距离

    protected int txtWidth;
    TextRender textRender;
    TextViewType textViewType ;
    public int colorBg = Color.WHITE;
    float[] dpCellWidths;

    public RowTextView(Context context) {
        super(context);
    }

    public RowTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RowTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cellWidth= 0;
        init();
    }

    public  RowTextView  initType(TextViewType textViewType){
        if(this.textViewType!=null&&this.textViewType==textViewType){
            return this;
        }
        this.textViewType = textViewType;
        switch (this.textViewType ){
            case HasTwoRowColors:
                textRender = new TwoRowColorsRender(getContext());
                break;
        }
        if(textRender!=null){
            textRender.setPaint( linePaint,  txtPaint)
                    .setDataList(list)
                    .setCellInfo( cellWidth,  fixedCellWidth, txtHeight)
                    .setCellfixedWidth(dpCellWidths);
        }else {
           // Log.e("xxl","xxlinitType-null");
        }
        return this;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (list == null)
            return;

        int width = list.size() * cellWidth;
        width = Math.max(width, getMeasuredWidth()) - fixedCellWidth;
        cellWidth = width / list.size();
        int measureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        setMeasuredDimension(measureSpec,heightMeasureSpec);
       // LogUtils.e("row222","rowH222"+getMeasuredHeight());
    }


    public void setTextList(List<String> lists) {
        this.list = lists;
       // LogUtils.e("xxlcellWidth","cellWidth"+cellWidth);
        invalidate();
    }

    void computeCellWidth(){
        if(list==null||list.size()==0){
            return;
        }
          if(dpCellWidths!=null&&dpCellWidths.length==list.size()){
              float  fixedWidth = 0;
              int count=0;
              for(float dpCellW:dpCellWidths){
                  fixedWidth += dpCellW;
                  if(dpCellW>0){
                      count++;
                  }
              }
              cellWidth = (getMeasuredWidth()-LibAppUtil.dip2px(getContext(), fixedWidth)) / (list.size()-count);

          }else {
              dpCellWidths=null;
              cellWidth = getMeasuredWidth() / list.size();
            //  LogUtils.i("xxl","computeCellWidthError");
          }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(textRender==null){
            initType(HasTwoRowColors);
        }
        if (list == null || list.size() <= 0)
            return;
        textRender.viewWidth = getMeasuredWidth();
        textRender.viewHeight = getMeasuredHeight();
        computeCellWidth();
        textRender.setDataList(list);
        textRender.setCellInfo( cellWidth,  fixedCellWidth, txtHeight)
                  .setCellfixedWidth(dpCellWidths);
        canvas.drawColor(colorBg);
        textRender.onDraw(canvas);
    }

    protected void init() {
        if (linePaint == null) {
            linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            linePaint.setColor(ContextCompat.getColor(getContext(), R.color.gray));
            txtPaint.setTextSize(getTextSize());
            txtPaint.setColor(getCurrentTextColor());

            Paint.FontMetrics fontMetrics = txtPaint.getFontMetrics();
            txtHeight = fontMetrics.descent - fontMetrics.ascent;
        }
    }

    public void  setDpCellWidths(float...  dpCellWidths){
        this.dpCellWidths = dpCellWidths;
    }

    public void setTxtPaintColor(@ColorInt int color) {
        txtPaint.setColor(color);
        if (textRender!=null) {
            textRender.setPaint( linePaint,  txtPaint);
        }
    }
}
