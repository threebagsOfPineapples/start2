package com.tachyon5.kstart.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tachyon5.kstart.R;

/**
 * Created by guofe on 2017/3/24 0024.
 */
public class DeviceBindDialog extends Dialog {
    private TextView tv1;
    private ProgressBar progressBar;
    private ImageView iv1;
    private final int LOAD_SUCC = 0x001;
    private final int LOAD_FAIL = 0x002;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case LOAD_SUCC:
                    dismiss();
                    break;
                case LOAD_FAIL:
                    dismiss();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public DeviceBindDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    /* public DeviceBindDialog(@NonNull Context context) {
         super(context);
     }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_process_dialog);
        initView();
    }

    private void initView() {
        tv1 = (TextView) findViewById(R.id.tv1);
        iv1 = (ImageView) findViewById(R.id.iv1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
    }

    public void dimissSuc() {// 加载成功
        progressBar.setVisibility(View.GONE);
        tv1.setText("加载成功");
        iv1.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(LOAD_SUCC, 1000);
    }

    public void dimissFail() {// 加载失败
        progressBar.setVisibility(View.GONE);
        tv1.setText("加载失败");
        iv1.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(LOAD_FAIL, 1000);
    }

    public void dimissSucForCali() {// 加载成功
        progressBar.setVisibility(View.GONE);
        tv1.setText("校准成功");
        iv1.setVisibility(View.VISIBLE);
        mHandler.sendEmptyMessageDelayed(LOAD_SUCC, 1000);
    }
}
