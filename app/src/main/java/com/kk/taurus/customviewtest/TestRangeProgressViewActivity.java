package com.kk.taurus.customviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.kk.taurus.customviewtest.widget.RangeProgressView;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestRangeProgressViewActivity extends AppCompatActivity {

    private RangeProgressView mRangeView;

    private TextView mTvInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test_range_progress_view);

        mRangeView = (RangeProgressView) findViewById(R.id.range_view);
        mTvInfo = (TextView) findViewById(R.id.tv_info);

        mRangeView.setBgColor(Color.GREEN);
        mRangeView.setProgressColor(Color.CYAN);
        mRangeView.setLeftControlColor(Color.MAGENTA);
        mRangeView.setRightControlColor(Color.RED);
        mRangeView.setMinValueTextColor(Color.WHITE);
        mRangeView.setMaxValueTextColor(Color.WHITE);

        mRangeView.setBoundaryMinValue(2);
        mRangeView.setBoundaryMaxValue(200);

        mRangeView.setOnProgressChangeListener(new RangeProgressView.OnProgressChangeListener() {
            @Override
            public void onStartTrack(RangeProgressView progressView) {
                System.out.println("RangeCallback : onStartTrack......" );
            }
            @Override
            public void onStopTrack(RangeProgressView progressView) {
                System.out.println("RangeCallback : onStopTrack......" );
            }
            @Override
            public void onProgressChange(RangeProgressView progressView, int minValue, int maxValue) {
                System.out.println("RangeCallback : onProgressChange : minValue = " + minValue + " maxValue = " + maxValue );
                mTvInfo.setText(minValue + "-" + maxValue);
            }
        });



    }
}
