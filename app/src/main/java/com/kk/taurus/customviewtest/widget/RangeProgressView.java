package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Taurus on 2017/7/10.
 */

public class RangeProgressView extends View {

    private final String TAG = "RangeView";

    public static final float DEFAULT_LINE_HEIGHT_RATE = 0.4f;

    private final int DEFAULT_BG_COLOR = Color.GRAY;
    private final int DEFAULT_PROGRESS_COLOR = Color.BLUE;
    private final int DEFAULT_LEFT_CONTROL_COLOR = Color.GREEN;
    private final int DEFAULT_RIGHT_CONTROL_COLOR = Color.GREEN;
    private final int DEFAULT_MIN_VALUE_COLOR = Color.RED;
    private final int DEFAULT_MAX_VALUE_COLOR = Color.RED;

    private final int DEFAULT_TEXT_SIZE = 24;

    private final int DEFAULT_BOUNDARY_MAX_VALUE = 100;

    private int mWidth, mHeight;

    private Paint mPaint;
    private Paint mControlPaint;
    private Paint mTextPaint;

    private int mBgColor = DEFAULT_BG_COLOR;
    private int mProgressColor = DEFAULT_PROGRESS_COLOR;
    private int mLeftControlColor = DEFAULT_LEFT_CONTROL_COLOR;
    private int mRightControlColor = DEFAULT_RIGHT_CONTROL_COLOR;
    private int mMinValueTextColor = DEFAULT_MIN_VALUE_COLOR;
    private int mMaxValueTextColor = DEFAULT_MAX_VALUE_COLOR;

    private int mTextSize = DEFAULT_TEXT_SIZE;

    private int mLineHeight;

    private float lineHeightRate = DEFAULT_LINE_HEIGHT_RATE;

    private int mControlRadius;

    private int minValue;
    private int maxValue = DEFAULT_BOUNDARY_MAX_VALUE;

    private int boundaryMinValue;
    private int boundaryMaxValue = DEFAULT_BOUNDARY_MAX_VALUE;

    private Point mLeftPoint, mRightPoint;

    private int leftBoundaryX, rightBoundaryX;
    private int mFlag = -1;

    private float downX;

    private OnProgressChangeListener onProgressChangeListener;

    public RangeProgressView(Context context) {
        this(context, null);
    }

    public RangeProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RangeProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mLeftPoint = new Point();
        mRightPoint = new Point();

        mPaint = new Paint();
        mPaint.setStrokeWidth(mLineHeight);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mControlPaint = new Paint();
        mControlPaint.setStyle(Paint.Style.FILL);

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mLineHeight = (int) (mHeight * lineHeightRate);
        mPaint.setStrokeWidth(mLineHeight);

        mControlRadius = (int) (mHeight * 0.85f / 2);

        leftBoundaryX = mLineHeight;
        rightBoundaryX = mWidth - mLineHeight;

        mLeftPoint.x = leftBoundaryX;
        mRightPoint.x = rightBoundaryX;

    }

    public void setLineHeightRate(float lineHeightRate) {
        this.lineHeightRate = lineHeightRate;
        requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                mFlag = judgeLocation(event.getX());
                if(mFlag!=-1 && onProgressChangeListener!=null){
                    onProgressChangeListener.onStartTrack(this);
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                //当两个控制点重合时，根据滑动方向决定那个点移动。
                if(mFlag==2){
                    if(event.getX() - downX > 0){
                        mFlag = 1;
                    }else{
                        mFlag = 0;
                    }
                }
                if(mFlag == 0 && mLeftPoint.x <= mRightPoint.x){
                    int x = (int) event.getX();
                    if(x <= mRightPoint.x && x >= leftBoundaryX){
                        mLeftPoint.x = x;
                        invalidate();
                    }
                }
                if(mFlag == 1 && mRightPoint.x >= mLeftPoint.x){
                    int x = (int) event.getX();
                    if(x >= mLeftPoint.x && x <= rightBoundaryX){
                        mRightPoint.x = x;
                        invalidate();
                    }
                }
                return super.onTouchEvent(event);

            case MotionEvent.ACTION_UP:
                mFlag = -1;
                if(onProgressChangeListener!=null){
                    onProgressChangeListener.onStopTrack(this);
                }
                return super.onTouchEvent(event);

        }
        return super.onTouchEvent(event);
    }

    private int judgeLocation(float x){
        float leftX1 = mLeftPoint.x - mControlRadius;
        float leftX2 = mLeftPoint.x + mControlRadius;
        float rightX1 = mRightPoint.x - mControlRadius;
        float rightX2 = mRightPoint.x + mControlRadius;
        boolean left = x >= leftX1 && x <= leftX2;
        boolean right = x >= rightX1 && x <= rightX2;
        if(left && right){
            return 2;
        }
        if(left){
            return 0;
        }
        if(right){
            return 1;
        }
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mWidth <= 0)
            return;
        float start_x = mLineHeight;
        float end_x = mWidth-mLineHeight;
        mPaint.setColor(mBgColor);
        canvas.drawLine(start_x,mHeight/2,end_x,mHeight/2,mPaint);

        //draw progress
        mPaint.setColor(mProgressColor);
        canvas.drawLine(mLeftPoint.x,mHeight/2,mRightPoint.x,mHeight/2,mPaint);

        Log.d(TAG,"onDraw ....");

        Rect rect = new Rect();
        boolean needCallback = false;
        if(mLeftPoint.x >= leftBoundaryX && mLeftPoint.x <= mRightPoint.x){
            mControlPaint.setColor(mLeftControlColor);
            canvas.drawCircle(mLeftPoint.x,mHeight/2,mControlRadius,mControlPaint);
            mTextPaint.setColor(mMinValueTextColor);
            minValue = getValue(mLeftPoint.x);
            Log.d(TAG,"left_value = " + minValue);
            String text = String.valueOf(minValue);
            mTextPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,mLeftPoint.x - (rect.width()/2),(mHeight + rect.height())/2,mTextPaint);
            needCallback = true;
        }

        if(mRightPoint.x <= rightBoundaryX && mRightPoint.x >= mLeftPoint.x){
            mControlPaint.setColor(mRightControlColor);
            canvas.drawCircle(mRightPoint.x,mHeight/2,mControlRadius,mControlPaint);
            mTextPaint.setColor(mMaxValueTextColor);
            maxValue = getValue(mRightPoint.x);
            Log.d(TAG,"right_value = " + maxValue);
            String text = String.valueOf(maxValue);
            mTextPaint.getTextBounds(text,0,text.length(),rect);
            canvas.drawText(text,mRightPoint.x - (rect.width()/2),(mHeight + rect.height())/2,mTextPaint);
            needCallback = true;
        }

        if(needCallback && onProgressChangeListener!=null){
            onProgressChangeListener.onProgressChange(this,minValue,maxValue);
        }

    }

    private int getValue(float x){
        float totalX = rightBoundaryX - leftBoundaryX;
        return ((int) ((x - leftBoundaryX) / totalX * ((boundaryMaxValue - boundaryMinValue) * 1.0f))) + boundaryMinValue;
    }

    public int getBoundaryMinValue() {
        return boundaryMinValue;
    }

    public void setBoundaryMinValue(int boundaryMinValue) {
        this.boundaryMinValue = boundaryMinValue;
        invalidate();
    }

    public int getBoundaryMaxValue() {
        return boundaryMaxValue;
    }

    public void setBoundaryMaxValue(int boundaryMaxValue) {
        this.boundaryMaxValue = boundaryMaxValue;
        invalidate();
    }

    public int getMinValue() {
        return minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getBgColor() {
        return mBgColor;
    }

    public void setBgColor(int bgColor) {
        this.mBgColor = bgColor;
        invalidate();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int progressColor) {
        this.mProgressColor = progressColor;
        invalidate();
    }

    public int getLeftControlColor() {
        return mLeftControlColor;
    }

    public void setLeftControlColor(int leftControlColor) {
        this.mLeftControlColor = leftControlColor;
        invalidate();
    }

    public int getRightControlColor() {
        return mRightControlColor;
    }

    public void setRightControlColor(int rightControlColor) {
        this.mRightControlColor = rightControlColor;
        invalidate();
    }

    public int getMinValueTextColor() {
        return mMinValueTextColor;
    }

    public void setMinValueTextColor(int minValueTextColor) {
        this.mMinValueTextColor = minValueTextColor;
        invalidate();
    }

    public int getMaxValueTextColor() {
        return mMaxValueTextColor;
    }

    public void setMaxValueTextColor(int maxValueTextColor) {
        this.mMaxValueTextColor = maxValueTextColor;
        invalidate();
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.onProgressChangeListener = onProgressChangeListener;
    }

    public interface OnProgressChangeListener{
        void onStartTrack(RangeProgressView progressView);
        void onStopTrack(RangeProgressView progressView);
        void onProgressChange(RangeProgressView progressView, int minValue, int maxValue);
    }

}
