package com.kk.taurus.customviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.kk.taurus.customviewtest.widget.SlideIndexBar;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestSlideIndexBarActivity extends AppCompatActivity {

    private String[] mIndexGroup = {"↑","A","B","C","D","E","F","G","H","I","J","K","L","M"
            ,"N","O","P","Q","R","S","T","U","V","W","X","Y","Z","#"};

    private SlideIndexBar mIndexBar;

    private TextView mCurrSelect;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_slide_index_bar);

        mIndexBar = (SlideIndexBar) findViewById(R.id.slideIndexBar);

        mCurrSelect = (TextView) findViewById(R.id.tv_select);

        mIndexBar.setIndexGroupElements(mIndexGroup);

        mIndexBar.setTextColor(Color.BLUE,Color.YELLOW);

        mIndexBar.setColor(Color.parseColor("#22000000"),Color.parseColor("#77000000"));

        mIndexBar.setOnSlideListener(new SlideIndexBar.OnSlideListener() {
            @Override
            public void onStartSlide() {
                mCurrSelect.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSelect(String select) {
                System.out.println("select_index = " + select);
                mCurrSelect.setText(select);
            }

            @Override
            public void onEndSlide() {
                mCurrSelect.setVisibility(View.GONE);
            }
        });

    }
}
