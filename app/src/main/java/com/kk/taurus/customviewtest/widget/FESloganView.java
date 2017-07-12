package com.kk.taurus.customviewtest.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Taurus on 2017/6/17.
 */

public class FESloganView extends View implements ValueAnimator.AnimatorUpdateListener {

    private int mWidth,mHeight;
    private int mCenterX,mCenterY;

    private Paint mPaint;

    private int mColor;

    private Path mF_Path;
    private Path mE_Path;
    private Path mCirclePath;

    private PathMeasure mFPathMeasure;
    private PathMeasure mEPathMeasure;

    private ValueAnimator mFEValueAnimator;

    private float mFEAnimatorValue;

    public static final int STATE_START = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_IDLE = 2;

    private int mState = STATE_START;

    public FESloganView(Context context) {
        this(context, null);
    }

    public FESloganView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FESloganView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mColor = Color.BLUE;
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setColor(mColor);
        mPaint.setAntiAlias(true);

        mFEValueAnimator = ValueAnimator.ofFloat(0,1).setDuration(2000);

        mFEValueAnimator.addUpdateListener(this);

        mFPathMeasure = new PathMeasure();
        mEPathMeasure = new PathMeasure();

    }

    public void setColor(int color){
        mColor = color;
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
        initPath();
    }

    private void initPath(){
        int maxR = (int) (Math.min(mWidth,mHeight)/2 * 0.8);

        float maxCubeL = (float) (maxR * Math.sin(Math.PI/4) * 2);

        mPaint.setStrokeWidth(maxCubeL * 0.05f);

        mF_Path = new Path();
        mF_Path.moveTo(-0.05f * maxCubeL, -0.5f * maxCubeL);
        mF_Path.lineTo(-0.5f * maxCubeL, -0.5f * maxCubeL);
        mF_Path.lineTo(-0.5f * maxCubeL, 0.5f * maxCubeL);
        mF_Path.lineTo(-0.4f * maxCubeL, 0.5f * maxCubeL);
        mF_Path.lineTo(-0.4f * maxCubeL, 0.05f * maxCubeL);
        mF_Path.lineTo(-0.05f * maxCubeL, 0.05f * maxCubeL);
        mF_Path.lineTo(-0.05f * maxCubeL, -0.05f * maxCubeL);
        mF_Path.lineTo(-0.4f * maxCubeL, -0.05f * maxCubeL);
        mF_Path.lineTo(-0.4f * maxCubeL, -0.4f * maxCubeL);
        mF_Path.lineTo(-0.05f * maxCubeL, -0.4f * maxCubeL);
        mF_Path.close();

        mFPathMeasure.setPath(mF_Path, false);

        mE_Path = new Path();
        mE_Path.moveTo(0.05f * maxCubeL, -0.5f * maxCubeL);
        mE_Path.lineTo(0.05f * maxCubeL, 0.5f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, 0.5f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, 0.4f * maxCubeL);
        mE_Path.lineTo(0.15f * maxCubeL, 0.4f * maxCubeL);
        mE_Path.lineTo(0.15f * maxCubeL, 0.05f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, 0.05f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, -0.05f * maxCubeL);
        mE_Path.lineTo(0.15f * maxCubeL, -0.05f * maxCubeL);
        mE_Path.lineTo(0.15f * maxCubeL, -0.4f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, -0.4f * maxCubeL);
        mE_Path.lineTo(0.5f * maxCubeL, -0.5f * maxCubeL);
        mE_Path.close();

        mEPathMeasure.setPath(mE_Path, false);
    }

    public void setState(int state){
        this.mState = state;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(mCenterX, mCenterY);

        switch (mState){
            case STATE_START:
                mFEValueAnimator.start();
                mFEValueAnimator.addListener(mAnimatorListener);
                setState(STATE_RUNNING);
                break;
            case STATE_IDLE:
                mFEAnimatorValue = 1;
                mFEValueAnimator.removeAllListeners();
                break;
        }

        Path dstF = new Path();
        float distanceF = mFPathMeasure.getLength();
        mFPathMeasure.getSegment(0, mFEAnimatorValue * distanceF, dstF, true);

        canvas.drawPath(dstF, mPaint);

        Path dstE = new Path();
        float distanceE = mEPathMeasure.getLength();
        mEPathMeasure.getSegment(0, mFEAnimatorValue * distanceE, dstE, true);

        canvas.drawPath(dstE, mPaint);

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mFEAnimatorValue = (float) animation.getAnimatedValue();
        invalidate();
    }

    private Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setState(STATE_IDLE);
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };
}
