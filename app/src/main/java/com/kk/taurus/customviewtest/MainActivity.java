package com.kk.taurus.customviewtest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_circle_progress).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestCircleProgressActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_multiplication_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestMultiplicationViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_radar_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestRadarViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_bezier_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestBezierViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_drag_point_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestCircleArrowViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_slide_bar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestSlideIndexBarActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestSearchIconViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_fe).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestFEViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_chart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestChartViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestNumberEditViewActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btn_range_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),TestRangeProgressViewActivity.class);
                startActivity(intent);
            }
        });
    }

}
