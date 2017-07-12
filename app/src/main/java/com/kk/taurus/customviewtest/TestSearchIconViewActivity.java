package com.kk.taurus.customviewtest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.kk.taurus.customviewtest.widget.SearchIconView;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestSearchIconViewActivity extends AppCompatActivity {

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

        }
    };

    private SearchIconView mSearchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_search_icon_view);

        mSearchView = (SearchIconView) findViewById(R.id.searchView);

        mSearchView.setColor(getResources().getColor(R.color.colorPrimary));
        mSearchView.setAnimationDuration(1500);

        mHandler.postDelayed(mStartRunnable, 1500);

        mHandler.postDelayed(mEndRunnable, 6000);
    }

    private Runnable mStartRunnable = new Runnable() {
        @Override
        public void run() {
            mSearchView.setState(SearchIconView.STATE_STARTING);
        }
    };

    private Runnable mEndRunnable = new Runnable() {
        @Override
        public void run() {
            mSearchView.setState(SearchIconView.STATE_END);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mStartRunnable);
        mHandler.removeCallbacks(mEndRunnable);
    }

}
