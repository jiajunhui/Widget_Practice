package com.kk.taurus.customviewtest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Taurus on 2017/7/6.
 */

public class ChartView extends View {

    private final String TAG = "ChartView";

    private final int DEFAULT_COLOR = Color.BLACK;

    //X轴左侧空余占比
    private final float X_AXIS_LEFT_WIDTH_RATE = 0.1f;
    //Y轴下侧空余占比
    private final float Y_AXIS_BOTTOM_HEIGHT_RATE = 0.1f;

    //一小时所对应的毫秒值
    private final int hour_ms = 1 * 60 * 60 * 1000;

    //Y轴箭头下方留白高度
    private final int AXIS_TOP_BLANK = 25;
    //箭头宽度
    private final int AXIS_ARROW_W_H = 15;

    private final int AXIS_STOKE_WIDTH = 3;
    private final int LINE_STOKE_WIDTH = 2;
    private final int LINE_STOKE_WIDTH_1 = 1;

    protected int mWidth,mHeight;

    //X,Y轴画笔
    private Paint mAxisPaint;
    //文字画笔
    private Paint mTextPaint;
    //表格画笔
    private Paint mTablePaint;
    //折线画笔
    private Paint mBrokenLinePaint;
    //三角画笔
    private Paint mButtonPaint;

    //折线path
    private Path mLinePath;

    private int mAxisColor;
    private int mTableColor;
    private int mBrokenLineColor;
    private int mButtonColor;

    private float mTextSize = 36;

    private int mStartX;
    private int mStartY;

    private int mCurrHour = -1;
    private List<Item> mData = new ArrayList<>();

    private String yTitle;

    //X轴刻度数据
    private long[] xAxisPoint;
    //Y轴刻度数据
    private int[] yAxisPoint;
    //X轴最大长度
    private float end_w;
    //Y轴最大高度
    private float end_h;

    //Y轴最左边文本边界
    private float left_y_text_boundary;
    //X轴最左边文本边界
    private float left_x_text_boundary;
    //X轴最右边文本边界
    private float right_x_text_boundary;

    //左边三角边界
    private float[] leftButtonArea = new float[4];
    //右边三角边界
    private float[] rightButtonArea = new float[4];

    private OnChangeDataListener onChangeDataListener;

    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnChangeDataListener(OnChangeDataListener onChangeDataListener) {
        this.onChangeDataListener = onChangeDataListener;
    }

    protected void init(Context context) {
        mAxisColor = DEFAULT_COLOR;
        mTableColor = Color.GRAY;
        mBrokenLineColor = Color.BLUE;
        mButtonColor = DEFAULT_COLOR;

        mLinePath = new Path();

        mAxisPaint = new Paint();
        mAxisPaint.setColor(mAxisColor);
        mAxisPaint.setStyle(Paint.Style.STROKE);
        mAxisPaint.setStrokeWidth(AXIS_STOKE_WIDTH);

        mTextPaint = new Paint();
        mTextPaint.setColor(mAxisColor);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setTextSize(mTextSize);

        mTablePaint = new Paint();
        mTablePaint.setColor(mTableColor);
        mTablePaint.setStyle(Paint.Style.STROKE);
        mTablePaint.setStrokeWidth(LINE_STOKE_WIDTH_1);

        mBrokenLinePaint = new Paint();
        mBrokenLinePaint.setColor(mBrokenLineColor);
        mBrokenLinePaint.setStyle(Paint.Style.STROKE);
        mBrokenLinePaint.setStrokeWidth(LINE_STOKE_WIDTH);

        mButtonPaint = new Paint();
        mButtonPaint.setColor(mButtonColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setStrokeWidth(LINE_STOKE_WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        //画布中心X坐标
        mStartX = (int) (mWidth * X_AXIS_LEFT_WIDTH_RATE);
        //画布中心Y轴坐标
        mStartY = (int) (mHeight * (1 - Y_AXIS_BOTTOM_HEIGHT_RATE));
    }

    public void setData(List<Item> data){
        this.mData.clear();
        this.mData.addAll(data);
        invalidate();
    }

    public void setTextSize(float px){
        this.mTextSize = px;
        mTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public void setYAxisGroup(int[] dataArea){
        this.yAxisPoint = dataArea;
        invalidate();
    }

    public void setYAxisTitle(String title){
        this.yTitle = title;
        invalidate();
    }

    public void updateXAxis(){
        long time = System.currentTimeMillis();
        mCurrHour = getHour(time);
        xAxisPoint = getHourTimeGroup(time);
    }

    private boolean isLegalData(){
        return xAxisPoint!=null && yAxisPoint!=null;
    }

    //添加一条记录
    public void addPoint(float value){
        long time = System.currentTimeMillis();
        if(mCurrHour==-1){
            mCurrHour = getHour(time);
            xAxisPoint = getHourTimeGroup(time);
        }
        if(mCurrHour != getHour(time)){
            mData.clear();
            mCurrHour = getHour(time);
            xAxisPoint = getHourTimeGroup(time);
        }
        mData.add(new Item(time,value));
        for(Item item:mData){
            Log.d(TAG,"value : " + item.value);
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(!isLegalData())
            return;

        Log.d(TAG,"startX : " + mStartX + " startY : " + mStartY);

        //画布位移到坐标系(0,0)点
        canvas.translate(mStartX, mStartY);

        end_w = mWidth * (1 - X_AXIS_LEFT_WIDTH_RATE);
        end_h = mHeight * (1 - Y_AXIS_BOTTOM_HEIGHT_RATE - 0.1f);

        //画X轴
        drawXAxis(canvas);
        //画Y轴
        drawYAxis(canvas);
        //画折线
        drawBrokenLine(canvas);
        //画三角
        drawButton(canvas);
    }

    private void drawXAxis(Canvas canvas) {

        float arrow_w = AXIS_ARROW_W_H;

        Path path = new Path();
        path.reset();
        path.moveTo(0,0);
        path.lineTo(end_w,0);
        path.moveTo(end_w,0);
        path.lineTo(end_w * 0.98f,-arrow_w);
        path.moveTo(end_w,0);
        path.lineTo(end_w * 0.98f,arrow_w);
        canvas.drawPath(path,mAxisPaint);


        int len = xAxisPoint.length;
        float step = (end_w - arrow_w)/len;

        float text_w = 0;
        float x;
        float y;
        float text_h;
        float sum_step = 0;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();

        for(int i=0;i<len;i++){
            String text = getHourMinute(xAxisPoint[i]);
            text_w = mTextPaint.measureText(text);
            text_h = fontMetrics.descent - fontMetrics.ascent;
            sum_step = i * step;

            if(i==0){
                x = -(text_w/2);
                left_x_text_boundary = x;
            }else{
                x = sum_step - (text_w/2);
                canvas.drawLine(sum_step,0,sum_step,-(end_h - AXIS_TOP_BLANK - AXIS_ARROW_W_H),mTablePaint);
            }
            y = text_h;
            canvas.drawText(text,x,y,mTextPaint);

        }

        right_x_text_boundary = sum_step + (text_w/2);

    }

    private void drawYAxis(Canvas canvas) {

        float arrow_h = AXIS_ARROW_W_H;

        float top_blank_h = AXIS_TOP_BLANK;

        Path path = new Path();
        path.reset();
        path.moveTo(0,0);
        path.lineTo(0, -end_h);
        path.moveTo(0, -end_h);
        path.lineTo(-arrow_h, -end_h * 0.98f);
        path.moveTo(0, -end_h);
        path.lineTo(arrow_h, -end_h * 0.98f);
        canvas.drawPath(path,mAxisPaint);

        if(!TextUtils.isEmpty(yTitle)){
            float title_w = mTextPaint.measureText(yTitle);
            canvas.drawText(yTitle,-(title_w/2),-end_h,mTextPaint);
        }

        int len = yAxisPoint.length;
        float step = (end_h - top_blank_h - arrow_h)/(len - 1);

        float text_w;
        float x;
        float y;
        float text_h;
        float sum_step;

        Rect rect = new Rect();

        for(int i=0;i<len;i++){
            String text = String.valueOf(yAxisPoint[i]);
            text_w = mTextPaint.measureText(text);
            mTextPaint.getTextBounds(text,0,text.length(),rect);
            x = -(text_w + 10);
            left_y_text_boundary = x;
            if(i==0){
                canvas.drawText(text,x,0,mTextPaint);
            }else{
                text_h = rect.height();
                sum_step = i * step;
                y = -(sum_step - (text_h / 2));
                canvas.drawText(text,x,y,mTextPaint);
                canvas.drawLine(0,-sum_step,end_w,-sum_step,mTablePaint);
            }
        }

    }

    private void drawBrokenLine(Canvas canvas) {

        int len_x = xAxisPoint.length;
        float x_total_px = (end_w - AXIS_ARROW_W_H)/len_x * (len_x - 1);

        int len_y = yAxisPoint.length;
        float y_total_px = end_h - AXIS_TOP_BLANK - AXIS_ARROW_W_H;

        mLinePath.reset();
        int len = mData.size();

        Log.d(TAG,"x_total_px : " + x_total_px + " y_total_px : " + y_total_px);

        for(int i=0;i<len;i++){
            Item item = mData.get(i);

            float x = x_total_px * (item.time - xAxisPoint[0]) * 1.0f / hour_ms;
            float y = y_total_px * (item.value - yAxisPoint[0]) * 1.0f / (yAxisPoint[len_y-1] - yAxisPoint[0]);

            if(i!=0){
                mLinePath.lineTo(x, -y);
            }

            mLinePath.moveTo(x, -y);

            Log.d(TAG,"point_x : " + x + " point_y : " + (-y));

        }

        canvas.drawPath(mLinePath, mBrokenLinePaint);
    }

    private void drawButton(Canvas canvas) {
        Path leftPath = new Path();
        float min_w = mWidth * X_AXIS_LEFT_WIDTH_RATE;
        float min_h = mHeight * Y_AXIS_BOTTOM_HEIGHT_RATE;
        float gap = min_w * 0.1f;
        float trangleW = min_w * 0.3f;
        float top_y = min_h * 0.3f;
        float top_y_blank = min_h * 0.2f;
        float left_boundary = Math.min(left_x_text_boundary,left_y_text_boundary);

        float start_x = Math.abs(left_boundary) + gap + trangleW;

        leftButtonArea[0] = 0;
        leftButtonArea[1] = end_h;
        leftButtonArea[2] = min_w - Math.abs(left_boundary);
        leftButtonArea[3] = mHeight;

        leftPath.moveTo(-start_x,top_y_blank + top_y);
        leftPath.lineTo(-(start_x - trangleW),top_y_blank);
        leftPath.lineTo(-(start_x - trangleW),top_y_blank + (top_y * 2));
        leftPath.close();
        canvas.drawPath(leftPath,mButtonPaint);


        //right button
        Path rightPath = new Path();

        rightButtonArea[0] = right_x_text_boundary + min_w;
        rightButtonArea[1] = end_h;
        rightButtonArea[2] = rightButtonArea[0] + trangleW + (gap * 2);
        rightButtonArea[3] = mHeight;


        rightPath.moveTo((right_x_text_boundary + gap),top_y_blank);
        rightPath.lineTo((right_x_text_boundary + gap + trangleW),top_y_blank + top_y);
        rightPath.lineTo((right_x_text_boundary + gap),top_y_blank + (top_y * 2));
        rightPath.close();
        canvas.drawPath(rightPath,mButtonPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int flag = -1;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //当触摸点在三角范围内，消费掉DOWN事件
                flag = judgeLocation(event.getX(),event.getY());
                return flag != -1;

            case MotionEvent.ACTION_UP:
                //根据flag相应回调
                flag = judgeLocation(event.getX(),event.getY());
                if(onChangeDataListener!=null){
                    switch (flag){
                        case 0:
                            onChangeDataListener.onLeftClick();
                            break;
                        case 1:
                            onChangeDataListener.onRightClick();
                            break;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private int judgeLocation(float x, float y){
        if(x >= leftButtonArea[0] && x <= leftButtonArea[2] && y >= leftButtonArea[1] && y <= leftButtonArea[3]){
            return 0;
        }
        if(x >= rightButtonArea[0] && x <= rightButtonArea[2] && y >= rightButtonArea[1] && y <= rightButtonArea[3]){
            return 1;
        }
        return -1;
    }

    public static class Item{
        public long time;
        public float value;

        public Item() {
        }

        public Item(long time, float value) {
            this.time = time;
            this.value = value;
        }
    }

    private int getHour(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private String getHourMinute(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        int minute = calendar.get(Calendar.MINUTE);
        String min_text = minute==0?"00":String.valueOf(minute);
        return calendar.get(Calendar.HOUR_OF_DAY) + ":" + min_text;
    }

    private long[] getHourTimeGroup(long time){
        int hour = getHour(time);
        int len = 7;
        long[] result = new long[len];
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        for(int i=0;i<len;i++){
            if(i==len-1){
                calendar.set(Calendar.HOUR_OF_DAY,hour + 1);
                calendar.set(Calendar.MINUTE,0);
            }else{
                calendar.set(Calendar.HOUR_OF_DAY,hour);
                calendar.set(Calendar.MINUTE,i * 10);
            }
            result[i] = calendar.getTimeInMillis();
        }
        return result;
    }

    public interface OnChangeDataListener{
        void onLeftClick();
        void onRightClick();
    }

}
