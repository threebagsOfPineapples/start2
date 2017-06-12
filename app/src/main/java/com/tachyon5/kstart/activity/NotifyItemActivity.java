package com.tachyon5.kstart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.tachyon5.kstart.R;

public class NotifyItemActivity extends AppCompatActivity {
    private TextView tv_title, tv_time, tv_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_item);
        tv_time = (TextView) findViewById(R.id.activity_notice_item_tv_time);
        tv_content = (TextView) findViewById(R.id.activity_notice_item_tv_content);
        Bundle bundle = getIntent().getExtras();
        tv_title.setText(bundle.get("title") + "");
        tv_time.setText(bundle.get("time") + "");
        tv_content.setText(bundle.get("content") + "");
    }
}
