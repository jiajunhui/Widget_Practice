package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by Taurus on 2017/6/13.
 */

public class RadarView extends BaseView {

    private Paint mNetsPaint;
    private Paint mValuePaint;
    private Paint mTextPaint;

    private int count = 6;

    private float angle = (float) (Math.PI*2/count);

    private String[] names = {"a","b","c","d","e","f"};

    private double[] values = {100,90,75,80,60,50};

    private int maxValue = 100;

    private int maxRadius;

    public RadarView(Context context) {
        super(context);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void init(Context context) {
        mNetsPaint = new Paint();
        mNetsPaint.setColor(Color.GRAY);
        mNetsPaint.setStyle(Paint.Style.STROKE);
        mNetsPaint.setStrokeWidth(2f);

        mValuePaint = new Paint();
        mValuePaint.setColor(Color.BLUE);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setStrokeWidth(2f);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.RED);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(2f);
        mTextPaint.setTextSize(30);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRadius = (int) (Math.min(mWidth,mHeight)*0.8/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawRadarNets(canvas);
        drawLines(canvas);
        drawValueArea(canvas);
        drawNames(canvas);
    }

    private void drawNames(Canvas canvas) {
        int textRadius = maxRadius + 20;
        for(int i=0;i<count;i++){
            float x = (float) (mCenterX + textRadius * Math.cos(angle * i));
            float y = (float) (mCenterY + textRadius * Math.sin(angle * i));
            canvas.drawText(names[i], x, y, mTextPaint);
        }
    }

    private void drawValueArea(Canvas canvas) {
        Path linePath = new Path();
        mValuePaint.setAlpha(255);
        for(int i=0;i<count;i++){
            double value = values[i];
            double xR = (value/maxValue)*maxRadius;
            float x = (float) (mCenterX + xR * Math.cos(angle * i));
            float y = (float) (mCenterY + xR * Math.sin(angle * i));
            if(i==0){
                linePath.moveTo(x, y);
            }else{
                linePath.lineTo(x, y);
            }
            canvas.drawCircle(x, y, 10, mValuePaint);
        }
        linePath.close();
        mValuePaint.setAlpha(150);
        canvas.drawPath(linePath,mValuePaint);
    }

    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for(int i=0;i<count;i++){
            path.reset();
            path.moveTo(mCenterX,mCenterY);
            float x = (float) (mCenterX + maxRadius * Math.cos(angle * i));
            float y = (float) (mCenterY + maxRadius * Math.sin(angle * i));
            path.lineTo(x, y);
            canvas.drawPath(path,mNetsPaint);
        }
    }

    private void drawRadarNets(Canvas canvas) {
        Path path = new Path();
        float r = maxRadius/(count - 1);
        for(int i=1;i<count;i++){
            float currR = r*i;
            path.reset();
            for(int j=0;j<count;j++){
                if(j==0){
                    path.moveTo(mCenterX + currR,mCenterY);
                }else{
                    float x = (float) (mCenterX + currR * Math.cos(angle * j));
                    float y = (float) (mCenterY + currR * Math.sin(angle * j));
                    path.lineTo(x,y);
                }
            }
            path.close();
            canvas.drawPath(path,mNetsPaint);
        }
    }
}
