package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by Taurus on 2017/6/13.
 */

public class CircleProgressView extends View {

    private int mWidth,mHeight;

    private Paint mPaint;
    private List<ItemData> mDataList;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2.0f);
    }

    public void setDataList(List<ItemData> dataList){
        this.mDataList = getList(dataList);
        invalidate();
    }

    private List<ItemData> getList(List<ItemData> dataList){
        if(dataList!=null){
            float total = 0f;
            for(ItemData item : dataList){
                total += item.getValue();
            }
            for(ItemData item : dataList){
                item.setPercent(item.getValue()/total);
                item.setAngle(item.getPercent()*360);
            }
        }
        return dataList;
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
        if(mDataList==null)
            return;
        float startAngle = 0;
        canvas.translate(mWidth/2,mHeight/2);
        float r = Math.min(mWidth,mHeight)/2*0.8f;
        RectF rectF = new RectF(-r,-r,r,r);
        for(ItemData data : mDataList){
            mPaint.setColor(data.getColor());
            canvas.drawArc(rectF,startAngle,data.getAngle(),true,mPaint);
            startAngle += data.getAngle();
        }
    }

    public static class ItemData{
        private String name;
        private long value;
        private int color;
        private float percent;
        private float angle;

        public ItemData(String name, long value, int color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getValue() {
            return value;
        }

        public void setValue(long value) {
            this.value = value;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public float getPercent() {
            return percent;
        }

        public void setPercent(float percent) {
            this.percent = percent;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(float angle) {
            this.angle = angle;
        }
    }

}
