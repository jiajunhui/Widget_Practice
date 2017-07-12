package com.kk.taurus.customviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kk.taurus.customviewtest.widget.CircleProgressView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Taurus on 2017/6/13.
 */

public class TestCircleProgressActivity extends AppCompatActivity {

    private CircleProgressView mProgressView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_circle_progress);

        mProgressView = (CircleProgressView) findViewById(R.id.cpv_view);

        List<CircleProgressView.ItemData> dataList = new ArrayList<>();
        dataList.add(new CircleProgressView.ItemData("video",800*1024*1024, Color.BLUE));
        dataList.add(new CircleProgressView.ItemData("photo",1400*1024*1024, Color.YELLOW));
        dataList.add(new CircleProgressView.ItemData("audio",2000*1024*1024, Color.RED));
        dataList.add(new CircleProgressView.ItemData("apk",320*1024*1024, Color.GREEN));
        dataList.add(new CircleProgressView.ItemData("zip",70*1024*1024, Color.GRAY));

        mProgressView.setDataList(dataList);

    }
}
