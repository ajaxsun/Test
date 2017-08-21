package com.example.myapplication;

import android.annotation.SuppressLint;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.pickerview.helper.PopDateHelper;
import com.example.myapplication.pickerview.helper.PopDateHelper2;
import com.example.myapplication.pickerview.helper.PopDateHelper3;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    PopDateHelper3 popDateHelper3 = null;
    PopDateHelper popDateHelper1 = null;
    public static DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 选择时间!
     */
    private static final String TAG = "MainActivity";
    View view;
    /**
     * 控件
     */
    private Button mBtn;
    private Button mBtn2;

    /**
     * 选择时间!
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        popDateHelper3 = new PopDateHelper3(MainActivity.this);
        popDateHelper3.setOnClickOkListener(new PopDateHelper3.OnClickOkListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClickOk(String date, String time, String second) {
                Log.d(TAG, "onClickOk: data:" + date + "time:" + time + "second:" + second);
                Log.d(TAG, "onClickOk: " + date);
            }
        });
        popDateHelper1 = new PopDateHelper(MainActivity.this, new PopDateHelper.OnClickOkListener() {
            @Override
            public void onClickOk(String date) {
                Log.d(TAG, "onClickOk: " + date);
            }
        });

        view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_picker_list, null);
        mBtn = (Button) findViewById(R.id.btn);
        mBtn.setOnClickListener(this);
        mBtn2 = (Button) findViewById(R.id.btn2);
        mBtn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
                popDateHelper3.show(view);
                break;
            case R.id.btn2:
                popDateHelper1.show(view);
                break;

        }

    }
}
