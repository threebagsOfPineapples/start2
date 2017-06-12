package com.tachyon5.kstart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tachyon5.kstart.R;

public class FeedBackActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout activityFeedBack;
    private EditText etAdvise;
    private EditText etContactWay;
    private Button btnTellus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initData();
    }

    private void initData() {
        activityFeedBack.setOnClickListener(this);
        etAdvise.setOnClickListener(this);
        etContactWay.setOnClickListener(this);
        btnTellus.setOnClickListener(this);
    }

    private void initView() {
        activityFeedBack = (RelativeLayout) findViewById(R.id.activity_feed_back);
        etAdvise = (EditText) findViewById(R.id.et_advise);
        etContactWay = (EditText) findViewById(R.id.et_contact_way);
        btnTellus = (Button) findViewById(R.id.btn_tellus);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_feed_back:
                break;
            case R.id.et_advise:
                break;
            case R.id.btn_tellus:
                break;
            default:
        }
    }
}
