package com.kk.taurus.customviewtest.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Taurus on 2017/6/16.
 */

public class SearchIconView extends View implements ValueAnimator.AnimatorUpdateListener {

    private int mWidth,mHeight;
    private int mCenterX,mCenterY;

    private Paint mPaint;

    private int mColor;

    private Path mSearchPath;
    private Path mCirclePath;

    private PathMeasure mPathMeasure;

    private ValueAnimator mStartingAnimator;
    private ValueAnimator mSearchAnimator;
    private ValueAnimator mEndAnimator;

    private int mAnimationDuration = 2000;

    public static final int STATE_IDLE = 0;
    public static final int STATE_STARTING = 1;
    public static final int STATE_SEARCH = 2;
    public static final int STATE_END = 3;

    private int mState = STATE_IDLE;

    private float mCurrAnimationValue;

    private boolean needWaitSearchEnd;

    private final int MSG_CODE_STATE_CHANGE = 1;
    private final int MSG_CODE_ANIMATION_END = 2;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cancelAllAnimators();
            switch (msg.what){
                case MSG_CODE_STATE_CHANGE:
                    switch (mState){
                        case STATE_IDLE:
                            invalidate();
                            break;

                        case STATE_STARTING:
                            mStartingAnimator.removeAllListeners();
                            mStartingAnimator.start();
                            mStartingAnimator.addListener(mAnimatorListener);
                            break;

                        case STATE_SEARCH:
                            mSearchAnimator.removeAllListeners();
                            mSearchAnimator.start();
                            mSearchAnimator.addListener(mAnimatorListener);
                            break;

                        case STATE_END:
                            mEndAnimator.removeAllListeners();
                            mEndAnimator.start();
                            mEndAnimator.addListener(mAnimatorListener);
                            break;
                    }
                    break;

                case MSG_CODE_ANIMATION_END:
                    switch (mState){
                        case STATE_IDLE:

                            break;

                        case STATE_STARTING:
                            setState(STATE_SEARCH);
                            break;

                        case STATE_SEARCH:
                            if(needWaitSearchEnd){
                                needWaitSearchEnd = false;
                                mState = STATE_END;
                                setState(STATE_END);
                                return;
                            }
                            mSearchAnimator.removeAllListeners();
                            mSearchAnimator.start();
                            mSearchAnimator.addListener(mAnimatorListener);
                            break;

                        case STATE_END:
                            setState(STATE_IDLE);
                            break;
                    }
                    break;
            }

        }
    };

    public SearchIconView(Context context) {
        this(context, null);
    }

    public SearchIconView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchIconView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    protected void init(Context context) {
        mPaint = new Paint();
        mColor = Color.BLUE;
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mStartingAnimator = ValueAnimator.ofFloat(0,1).setDuration(mAnimationDuration);
        mSearchAnimator = ValueAnimator.ofFloat(0,1).setDuration(mAnimationDuration);
        mEndAnimator = ValueAnimator.ofFloat(1,0).setDuration(mAnimationDuration);

        mStartingAnimator.addUpdateListener(this);
        mSearchAnimator.addUpdateListener(this);
        mEndAnimator.addUpdateListener(this);

    }

    public void setAnimationDuration(int duration){
        this.mAnimationDuration = duration;
        mStartingAnimator.setDuration(mAnimationDuration);
        mSearchAnimator.setDuration(mAnimationDuration);
        mEndAnimator.setDuration(mAnimationDuration);
    }

    public void setColor(int color){
        this.mColor = color;
        mPaint.setColor(mColor);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
        mCenterX = mWidth / 2;
        mCenterY = mHeight / 2;

        mPaint.setStrokeWidth(Math.min(mWidth,mHeight)/10>15f?15f:(Math.min(mWidth,mHeight)/10));

        mSearchPath = new Path();
        mCirclePath = new Path();

        mPathMeasure = new PathMeasure();

        int searchR = (int) (Math.min(mWidth,mHeight)/2 * 0.4);
        int searchC = (int) (Math.min(mWidth,mHeight)/2 * 0.8);
        RectF rect1 = new RectF(-searchR,-searchR,searchR,searchR);
        mSearchPath.addArc(rect1,45f,359.9f);

        RectF rect2 = new RectF(-searchC,-searchC,searchC,searchC);
        mCirclePath.addArc(rect2,45,-359.9f);

        mPathMeasure.setPath(mCirclePath,false);
        float[] pos = new float[2];
        mPathMeasure.getPosTan(0,pos,null);

        mSearchPath.lineTo(pos[0],pos[1]);

    }

    private void cancelAllAnimators(){
        mStartingAnimator.cancel();
        mSearchAnimator.cancel();
        mEndAnimator.cancel();
    }

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }
        @Override
        public void onAnimationEnd(Animator animation) {
            mHandler.sendEmptyMessage(MSG_CODE_ANIMATION_END);
        }
        @Override
        public void onAnimationCancel(Animator animation) {

        }
        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };

    public void setState(int state){
        if(mState == STATE_SEARCH){
            needWaitSearchEnd = true;
            return;
        }
        this.mState = state;
        if(mState==STATE_END){
            mSearchAnimator.removeAllListeners();
        }
        mHandler.sendEmptyMessage(MSG_CODE_STATE_CHANGE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX,mCenterY);

        Path dst;

        switch (mState){
            case STATE_IDLE:
                canvas.drawPath(mSearchPath,mPaint);
                break;

            case STATE_STARTING:
                mPathMeasure.setPath(mSearchPath,false);
                dst = new Path();
                float distance = mPathMeasure.getLength();
                mPathMeasure.getSegment(distance * mCurrAnimationValue, distance, dst, true);
                canvas.drawPath(dst, mPaint);
                break;

            case STATE_SEARCH:
                mPathMeasure.setPath(mCirclePath,false);
                dst = new Path();
                float distance1 = mPathMeasure.getLength();
                float stop = distance1 * mCurrAnimationValue;
                float start = (float) (stop - ((0.5 - Math.abs(mCurrAnimationValue - 0.5)) * 200f));
                mPathMeasure.getSegment(start, stop, dst, true);
                canvas.drawPath(dst,mPaint);
                break;

            case STATE_END:
                mPathMeasure.setPath(mSearchPath,false);
                dst = new Path();
                float distance2 = mPathMeasure.getLength();
                mPathMeasure.getSegment(distance2 * mCurrAnimationValue, distance2, dst, true);
                canvas.drawPath(dst, mPaint);
                break;
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mCurrAnimationValue = (float) animation.getAnimatedValue();
        invalidate();
    }

}
