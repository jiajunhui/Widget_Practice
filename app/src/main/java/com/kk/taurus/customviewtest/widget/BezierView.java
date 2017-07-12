package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Taurus on 2017/6/14.
 */

public class BezierView extends BaseView {

    private Paint mPaint;

    private PointF start,end, control1, control2;

    private Path mPath;

    public BezierView(Context context) {
        super(context);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        start = new PointF();
        start.x = mCenterX - 200;
        start.y = mCenterY;

        control1 = new PointF();
        control1.x = mCenterX;
        control1.y = mCenterY;

        control2 = new PointF();
        control2.x = mCenterX;
        control2.y = mCenterY;

        end = new PointF();
        end.x = mCenterX + 200;
        end.y = mCenterY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointCount = event.getPointerCount();
        for(int i=0;i<pointCount;i++){
            switch (i){
                case 0:
                    control1.x = event.getX(i);
                    control1.y = event.getY(i);
                    break;
                case 1:
                    control2.x = event.getX(i);
                    control2.y = event.getY(i);
                    break;
            }

        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPath = new Path();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(5f);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(start.x,start.y,20,mPaint);
        canvas.drawCircle(end.x,end.y,20,mPaint);
        if(control1.x != mCenterX || control1.y != mCenterY || control2.x != mCenterX || control2.y != mCenterY){
            mPaint.setColor(Color.YELLOW);
            canvas.drawCircle(control1.x, control1.y,20,mPaint);
            canvas.drawCircle(control2.x, control2.y,20,mPaint);
        }

        mPaint.setColor(Color.RED);
        canvas.drawLine(start.x, start.y, control1.x, control1.y, mPaint);
        canvas.drawLine(control1.x, control1.y, control2.x, control2.y, mPaint);
        canvas.drawLine(end.x, end.y, control2.x, control2.y, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10f);
        mPath.moveTo(start.x, start.y);

        mPath.cubicTo(control1.x, control1.y,control2.x, control2.y, end.x, end.y);

        canvas.drawPath(mPath,mPaint);

    }
}
