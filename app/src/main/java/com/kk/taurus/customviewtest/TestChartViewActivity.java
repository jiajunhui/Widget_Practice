package com.kk.taurus.customviewtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kk.taurus.customviewtest.widget.ChartView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestChartViewActivity extends AppCompatActivity {

    private ChartView mChartView;

    private Random random = new Random();

    private int mCount;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mCount++;

            if(mCount % 12 == 0){
                mChartView.addPoint(getRandom(36,42));
            }

            mHandler.sendEmptyMessageDelayed(0, 5000);
        }
    };

    private int getRandom(int min, int max){
        return random.nextInt(max + 1)%(max - min + 1) + min;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chart_view);

        mChartView = (ChartView) findViewById(R.id.chart_view);

        mChartView.setTextSize(36);
        mChartView.setYAxisGroup(new int[]{36,37,38,39,40,41,42});
        mChartView.setYAxisTitle("温度（'c）");
        mChartView.updateXAxis();

        mChartView.setOnChangeDataListener(new ChartView.OnChangeDataListener() {
            @Override
            public void onLeftClick() {
                System.out.println("left click");
            }
            @Override
            public void onRightClick() {
                System.out.println("right click");
            }
        });

        List<ChartView.Item> data = new ArrayList<>();

        long time = System.currentTimeMillis();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        calendar.set(Calendar.HOUR_OF_DAY,getHour(time));
        calendar.set(Calendar.MINUTE,0);
        time = calendar.getTimeInMillis();

        for(int i=0;i<12;i++){
            data.add(new ChartView.Item(time + (i * 5 * 60000),getRandom(37,40) + random.nextFloat()));
        }
        mChartView.setData(data);

//        mHandler.sendEmptyMessage(0);



    }

    private int getHour(long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
