package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Taurus on 2017/6/13.
 */

public class MultiplicationView extends View {

    private int mWidth,mHeight;
    private Paint mPaint;

    public MultiplicationView(Context context) {
        this(context, null);
    }

    public MultiplicationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiplicationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(2f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int tableW = (int) (mWidth*0.9);
        int tableH = (int) (mHeight*0.9);

        int itemW = tableW/9;
        int itemH = tableH/9;

        mPaint.setTextSize((float) (itemH * 0.6));

        int paddingHorizontal = (mWidth - tableW)/2;
        int paddingVertical = (mHeight - tableH)/2;

        int startLeft;
        int startRight;

        int startTop;
        int startBottom;

        Rect rect;

        int textH;
        int textW;

        float leftX;
        float bottomY;

        Rect textRect;

        for(int i=0;i<9;i++){
            startLeft = (paddingHorizontal + (i * itemW));
            startRight = (paddingHorizontal + ((i + 1) * itemW));
            for(int j=i;j<9;j++){

                startTop = (paddingVertical + (j * itemH));
                startBottom = (paddingVertical + ((j + 1) * itemH));

                rect = new Rect(startLeft,startTop,startRight,startBottom);
                mPaint.setStyle(Paint.Style.STROKE);
                canvas.drawRect(rect,mPaint);

                String text = getItemText(i + 1,j + 1);

                textRect = new Rect();

                mPaint.getTextBounds(text,0,text.length(),textRect);

                textH = textRect.height();

                textW = textRect.width();

                leftX   = (startLeft + (itemW/2) - textW/2);
                bottomY = (startTop + (itemH/2) + textH/2);

                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawText(text,leftX,bottomY,mPaint);
            }

        }
    }

    private String getItemText(int i, int j){
        return i + "x" + j + "=" + (i*j);
    }
}
