package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Taurus on 2017/6/16.
 */

public class SlideIndexBar extends BaseView {

    private Paint mBackgroundPaint, mIndexPaint;

    private int mNormalBackgroundColor;
    private int mTouchBackgroundColor;
    private int mSelectElementColor;

    private int mTextColor;

    private String[] mIndexGroupElements;

    private float mTouchY;

    private OnSlideListener mOnSlideListener;

    private int mCurrSelectIndex = -1;

    private boolean mTouchState;

    public SlideIndexBar(Context context) {
        super(context);
    }

    public SlideIndexBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideIndexBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        super.init(context);

        mNormalBackgroundColor = Color.argb(100,0,0,0);
        mTouchBackgroundColor = Color.argb(180,0,0,0);

        mTextColor = Color.BLACK;
        mSelectElementColor = Color.RED;

        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(mNormalBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mIndexPaint = new Paint();
        mIndexPaint.setStyle(Paint.Style.FILL);
        mIndexPaint.setColor(mTextColor);
        mIndexPaint.setStrokeWidth(2f);
        mIndexPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mIndexPaint.setAntiAlias(true);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener){
        this.mOnSlideListener = onSlideListener;
    }

    public void setIndexGroupElements(String[] elements){
        this.mIndexGroupElements = elements;
        invalidate();
    }

    public void setTextColor(int normalColor, int selectColor){
        this.mTextColor = normalColor;
        this.mSelectElementColor = selectColor;
        mIndexPaint.setColor(mTextColor);
        invalidate();
    }

    public void setColor(int normalBgColor, int touchBgColor){
        this.mNormalBackgroundColor = normalBgColor;
        this.mTouchBackgroundColor = touchBgColor;
        mBackgroundPaint.setColor(mNormalBackgroundColor);
        invalidate();
    }

    private void calcIndexByTouchY(){
        if(mOnSlideListener!=null){
            int len = mIndexGroupElements.length;
            int itemH = mHeight/len;
            int touchY = (int) mTouchY;
            int d_index = (touchY/itemH) - 1;
            if(touchY%itemH > 0){
                d_index++;
            }
            if(mCurrSelectIndex != d_index && d_index >= 0 && d_index < len){
                mOnSlideListener.onSelect(mIndexGroupElements[d_index]);
                mCurrSelectIndex = d_index;
            }
            mBackgroundPaint.setColor(mTouchBackgroundColor);
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchState = true;
                mTouchY = event.getY();
                calcIndexByTouchY();
                if(mOnSlideListener!=null){
                    mOnSlideListener.onStartSlide();
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                mTouchY = event.getY();
                calcIndexByTouchY();
                return true;
            case MotionEvent.ACTION_UP:
                mTouchState = false;
                mBackgroundPaint.setColor(mNormalBackgroundColor);
                invalidate();
                if(mOnSlideListener!=null){
                    mOnSlideListener.onEndSlide();
                }
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(new Rect(0,0,mWidth,mHeight),mBackgroundPaint);
        if(mIndexGroupElements == null)
            return;
        int len = mIndexGroupElements.length;
        float x;
        float y;
        float addHeight = 0;
        int itemH = mHeight/len;
        mIndexPaint.setTextSize((float) (Math.min(mWidth,itemH) * 0.75));
        Paint.FontMetrics fontMetrics = mIndexPaint.getFontMetrics();
        float fontH = fontMetrics.descent - fontMetrics.ascent;
        for(int i=0;i<len;i++){
            String s = mIndexGroupElements[i];
            int s_len = s.length();
            x = mWidth / 2 - mIndexPaint.measureText(s) / 2;
            y = addHeight + (itemH + fontH)/2;
            addHeight += itemH;
            if(mTouchState && mCurrSelectIndex==i){
                mIndexPaint.setColor(mSelectElementColor);
            }else{
                mIndexPaint.setColor(mTextColor);
            }
            canvas.drawText(s, 0, s_len, x, y, mIndexPaint);
        }
    }

    public interface OnSlideListener{
        void onStartSlide();
        void onSelect(String select);
        void onEndSlide();
    }

}
