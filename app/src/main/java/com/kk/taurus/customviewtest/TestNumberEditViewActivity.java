package com.kk.taurus.customviewtest;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kk.taurus.customviewtest.widget.NumberEditView;


/**
 * Created by Taurus on 2017/6/13.
 */

public class TestNumberEditViewActivity extends AppCompatActivity {

    private NumberEditView mEditView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_number_edit_view);

        mEditView = (NumberEditView) findViewById(R.id.edit_view);
        mEditView.setBorderColor(Color.BLACK);
        mEditView.setBorderWidth(3);
        mEditView.setValueTextSize(40);
        mEditView.setValueColor(Color.BLUE);

        mEditView.setOnEditListener(new NumberEditView.OnEditListener() {
            @Override
            public void onIncrease(int value) {
//                Toast.makeText(TestNumberEditViewActivity.this, "add", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onReduce(int value) {
//                Toast.makeText(TestNumberEditViewActivity.this, "reduce", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onValueClick(int value, int min, int max) {
                Toast.makeText(TestNumberEditViewActivity.this, "value click", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
