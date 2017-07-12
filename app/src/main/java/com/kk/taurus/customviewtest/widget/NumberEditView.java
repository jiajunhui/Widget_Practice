package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Taurus on 2017/7/10.
 */

public class NumberEditView extends View {

    private final int DEFAULT_VALUE_MIN = 1;
    private final int DEFAULT_VALUE_MAX = Integer.MAX_VALUE;

    private final int DEFAULT_BORDER_WIDTH = 2;
    private final int DEFAULT_VALUE_PADDING = 15;

    private final int DEFAULT_COLOR = Color.BLACK;
    private final int DEFAULT_UNABLE_COLOR = Color.GRAY;

    private final int FLAG_LEFT_BUTTON = 0;
    private final int FLAG_RIGHT_BUTTON = 1;

    private int mWidth, mHeight;

    private Paint mPaint;
    private Paint mValuePaint;
    private Paint mEditButtonPaint;

    private int mBorderColor = DEFAULT_COLOR;
    private int mButtonColor = DEFAULT_COLOR;
    private int mValueColor = DEFAULT_COLOR;
    private int mUnableColor = DEFAULT_UNABLE_COLOR;

    private int mBorderWidth = DEFAULT_BORDER_WIDTH;

    private int mValueTextSize = 36;

    private int mValue = DEFAULT_VALUE_MIN;

    private int mValuePadding = DEFAULT_VALUE_PADDING;

    private int mMaxValue = DEFAULT_VALUE_MAX;
    private int mMinValue = DEFAULT_VALUE_MIN;

    private RectF mLeftButton, mRightButton;
    private float mButtonW;

    private OnEditListener onEditListener;

    public NumberEditView(Context context) {
        this(context, null);
    }

    public NumberEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NumberEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {

        mPaint = new Paint();
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setAntiAlias(true);

        mValuePaint = new Paint();
        mValuePaint.setColor(mValueColor);
        mValuePaint.setStyle(Paint.Style.FILL);
        mValuePaint.setStrokeWidth(mBorderWidth);
        mValuePaint.setTextSize(mValueTextSize);

        mEditButtonPaint = new Paint();
        mEditButtonPaint.setColor(mButtonColor);
        mEditButtonPaint.setStyle(Paint.Style.FILL);
        mEditButtonPaint.setStrokeWidth(mBorderWidth * 2);

    }

    public void setOnEditListener(OnEditListener onEditListener) {
        this.onEditListener = onEditListener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mButtonW = mHeight;
        mLeftButton = new RectF(0,0, mButtonW,mHeight);
        mRightButton = new RectF(mWidth - mButtonW,0,mWidth,mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpec = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpec = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float v = mValuePaint.measureText(String.valueOf(mValue));
        Paint.FontMetrics fontMetrics = mValuePaint.getFontMetrics();
        float h = fontMetrics.descent - fontMetrics.ascent;
        switch (heightSpec){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = (int) (h * 1.3f);
                break;
        }

        switch (widthSpec){
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = (int) ((v + (2 * mValuePadding)) + (heightSize * 2));
                break;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBorder(canvas);
        drawButton(canvas);
        drawValue(canvas);

    }

    private void drawValue(Canvas canvas) {
        String value = String.valueOf(mValue);
        Rect rect = new Rect();
        mValuePaint.getTextBounds(value,0,value.length(),rect);
        float x = (mWidth - rect.width())/2;
        float y = (mHeight + rect.height())/2;
        canvas.drawText(value,x,y,mValuePaint);
    }

    private void drawButton(Canvas canvas) {
        //left button
        mEditButtonPaint.setColor(isMinValue()?mUnableColor:mButtonColor);
        float len = mButtonW / 2;
        canvas.drawLine((mButtonW - len)/2,mHeight/2,(mButtonW + len)/2,mHeight/2,mEditButtonPaint);
        //right button
        mEditButtonPaint.setColor(isMaxValue()?mUnableColor:mButtonColor);
        canvas.drawLine((mWidth - mButtonW) + ((mButtonW - len)/2)
                ,mHeight/2
                ,(mWidth - mButtonW) + ((mButtonW + len)/2)
                ,mHeight/2,mEditButtonPaint);
        canvas.drawLine(((mWidth - mButtonW) + (mButtonW/2))
                ,(mHeight - len)/2
                ,(mWidth - mButtonW) + (mButtonW/2)
                ,(mHeight + len)/2,mEditButtonPaint);
    }

    private void drawBorder(Canvas canvas) {
        Path path = new Path();
        path.addRect(mLeftButton, Path.Direction.CW);
        path.addRect(mRightButton, Path.Direction.CW);
        path.moveTo(mButtonW,0);
        path.lineTo(mWidth - mButtonW,0);
        path.moveTo(mButtonW,mHeight);
        path.lineTo(mWidth - mButtonW,mHeight);
        path.close();
        canvas.drawPath(path,mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return true;
            case MotionEvent.ACTION_UP:
                int flag = judgeClickArea(event.getX(),event.getY());
                if(flag!=-1){
                    switch (flag){
                        case FLAG_LEFT_BUTTON:
                            if(mValue > mMinValue){
                                reduceValue();
                            }
                            break;
                        case FLAG_RIGHT_BUTTON:
                            if(mValue < mMaxValue){
                                increaseValue();
                            }
                            break;
                    }
                    return true;
                }else{
                    if(onEditListener!=null){
                        onEditListener.onValueClick(mValue,mMinValue,mMaxValue);
                    }
                }
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private int judgeClickArea(float x, float y){
        if(x >= mLeftButton.left
                && x <= mLeftButton.right
                && y >= mLeftButton.top
                && y <= mLeftButton.bottom){
            return 0;
        }
        if(x >= mRightButton.left
                && x <= mRightButton.right
                && y >= mRightButton.top
                && y <= mRightButton.bottom){
            return 1;
        }
        return -1;
    }

    private void increaseValue(){
        mValue++;
        requestLayout();
        invalidate();
        if(onEditListener!=null){
            onEditListener.onIncrease(mValue);
        }
    }

    private void reduceValue(){
        mValue--;
        requestLayout();
        invalidate();
        if(onEditListener!=null){
            onEditListener.onReduce(mValue);
        }
    }

    private boolean isMinValue(){
        return mValue <= mMinValue;
    }

    private boolean isMaxValue(){
        return mValue >= mMaxValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int mMinValue) {
        this.mMinValue = mMinValue;
    }

    public int getValue() {
        return mValue;
    }

    public void setValue(int value) {
        this.mValue = value;
        invalidate();
    }

    public int getBorderColor() {
        return mBorderColor;
    }

    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
        mPaint.setColor(mBorderColor);
        invalidate();
    }

    public int getButtonColor() {
        return mButtonColor;
    }

    public void setButtonColor(int mButtonColor) {
        this.mButtonColor = mButtonColor;
        mEditButtonPaint.setColor(mButtonColor);
        invalidate();
    }

    public int getValueColor() {
        return mValueColor;
    }

    public void setValueColor(int mValueColor) {
        this.mValueColor = mValueColor;
        mValuePaint.setColor(mValueColor);
        invalidate();
    }

    public int getUnableColor() {
        return mUnableColor;
    }

    public void setUnableColor(int mUnableColor) {
        this.mUnableColor = mUnableColor;
        invalidate();
    }

    public int getBorderWidth() {
        return mBorderWidth;
    }

    public void setBorderWidth(int borderWidth) {
        this.mBorderWidth = borderWidth;
        mPaint.setStrokeWidth(mBorderWidth);
        invalidate();
    }

    public int getValueTextSize() {
        return mValueTextSize;
    }

    public void setValueTextSize(int valueTextSize) {
        this.mValueTextSize = valueTextSize;
        mValuePaint.setTextSize(mValueTextSize);
        invalidate();
    }

    public int getValuePadding() {
        return mValuePadding;
    }

    public void setValuePadding(int valuePadding) {
        this.mValuePadding = valuePadding;
        requestLayout();
    }

    public interface OnEditListener{
        void onIncrease(int value);
        void onReduce(int value);
        void onValueClick(int value, int min, int max);
    }

}
