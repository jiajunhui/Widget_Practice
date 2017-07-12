package com.kk.taurus.customviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kk.taurus.customviewtest.widget.FESloganView;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestFEViewActivity extends AppCompatActivity {

    private FESloganView sloganView;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fe_view);

        sloganView = (FESloganView) findViewById(R.id.slogan_view);

        sloganView.setColor(Color.YELLOW);


    }
}
