package com.tachyon5.kstart.activity;

import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.ble.BluetoothLeService;
import com.tachyon5.kstart.utils.Constant;
import com.tachyon5.kstart.utils.ToastUtil;
import com.tachyon5.kstart.view.AVLoadingIndicatorView;
import com.tachyon5.kstart.view.LineView;
import com.tachyon5.kstart.view.RippleLayout;

import static com.tachyon5.kstart.utils.Constant.makeGattUpdateIntentFilter;

public class CollectActivity extends AppCompatActivity implements View.OnClickListener {
    private AVLoadingIndicatorView actvityCollectCv;
    private TextView activityCollectTvNumber;
    private TextView activityCollectTvNotice;
    private TextView tvStartcollect, datalist, tvStopcollect;
    private RippleLayout rippleLayout;
    private ImageView imageView, green_right;
    private LineView lineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initData();
    }

    private void initData() {
        rippleLayout.setOnClickListener(this);
        tvStartcollect.setOnClickListener(this);
        datalist.setOnClickListener(this);
        tvStopcollect.setOnClickListener(this);
        green_right.setOnClickListener(this);
    }

    private void initView() {
        green_right = (ImageView) findViewById(R.id.iv_green_right);
        actvityCollectCv = (AVLoadingIndicatorView) findViewById(R.id.actvity_collect_cv);
        activityCollectTvNumber = (TextView) findViewById(R.id.activity_collect_tv_number);
        activityCollectTvNotice = (TextView) findViewById(R.id.activity_collect_tv_notice);
        tvStartcollect = (TextView) findViewById(R.id.tv_startcollect);
        tvStopcollect = (TextView) findViewById(R.id.tv_stopcollect);
        rippleLayout = (RippleLayout) findViewById(R.id.ripple_layout);
        imageView = (ImageView) findViewById(R.id.iv_rip);
        lineView = (LineView) findViewById(R.id.lv);
        datalist = (TextView) findViewById(R.id.activity_collect_tv_dataList);
        android.view.ViewGroup.LayoutParams lp, lps, lpss, lvs, bgreen;
        lp = actvityCollectCv.getLayoutParams();
        lvs = lineView.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = (int) (dm.widthPixels) * 93 / 375;
        Logger.d("宽度" + dm.heightPixels + "长度：" + dm.widthPixels);
        lp.height = lp.width;
        actvityCollectCv.setLayoutParams(lp);
        lineView.setLayoutParams(lp);
        lps = rippleLayout.getLayoutParams();
        lps.width = Constant.widpx * 250 / 375;
        lps.height = lps.width;
        rippleLayout.setLayoutParams(lps);
        Logger.d("宽度" + lps.width + "长度：" + lps.height);
        lpss = imageView.getLayoutParams();
        lpss.width = Constant.widpx * 190 / 375;
        lpss.height = lpss.width;
        imageView.setLayoutParams(lpss);
        green_right.setLayoutParams(lp);
        Logger.e(lpss.height + "lpss宽度：" + lpss.height);
        actvityCollectCv.hide();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_startcollect:
                if (tvStartcollect.getText().toString().equals("开始采集")) {
                    lineView.setVisibility(View.GONE);
                    actvityCollectCv.show();
                    rippleLayout.startRippleAnimation();
                    if (Constant.mBluetoothLeService != null) {
                        byte[] a = {0x01};
                        Constant.mBluetoothLeService.sendMessage(a, (byte) 0x54, (byte) 0x00);
                    }
                    tvStartcollect.setText("正在采集");
                    tvStartcollect.setEnabled(false);
                } else {
                    tvStartcollect.setText("开始采集");
                    lineView.setVisibility(View.VISIBLE);
                    actvityCollectCv.hide();
                    green_right.setVisibility(View.GONE);
                    rippleLayout.stopRippleAnimation();
                }
                break;
            case R.id.ripple_layout:

                break;
            case R.id.activity_collect_tv_dataList:
                Intent intent = new Intent(CollectActivity.this, DataListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_stopcollect:
           /*    tvStartcollect.setText("开始采集");
               lineView.setVisibility(View.VISIBLE);
               actvityCollectCv.hide();
               rippleLayout.stopRippleAnimation();*/
                break;
        }
    }

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
            // 通过intent获得的不同action，来区分广播该由谁接收(只有action一致,才能接收)。
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                Logger.d("接收到连接成功广播");
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
            }
            // 发现服务后，自动执行回调方法onServicesDiscovered(),发送一个action=ACTION_GATT_SERVICES_DISCOVERED的广播，其他情况同理
            else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                Logger.d("接收到发现服务的广播");
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Logger.d("接收到设备可用的广播");

            } else if (BluetoothLeService.ACTION_TO_DETECTION.equals(action)) {

            } else if (BluetoothLeService.ACTION_PAIR.equals(action)) {
                Logger.d("接收到匹配的广播");
            } else if (BluetoothLeService.ACTION_COLLECT.equals(action)) {
                Logger.d("接收到采集消息 并且isclicked=true");
            } else if (BluetoothLeService.ACTION_CONN_AUTHORIZE.equals(action)) {
                Logger.d("接收到 身份认证的广播");
            } else if (BluetoothLeService.ACTION_GET_WAVE_MAP.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_WAVE_MAP.equals(action)) {

            } else if (BluetoothLeService.ACTION_CALI_DARK_FINISHED.equals(action)) {
                ToastUtil.showToast(getApplicationContext(), "采集完毕");
                tvStartcollect.setText("继续采集");
                tvStartcollect.setEnabled(true);
                activityCollectTvNumber.setTextColor(Color.BLUE);
                activityCollectTvNumber.setText("成功的添加了一条数据");
                tvStopcollect.setVisibility(View.VISIBLE);
                green_right.setVisibility(View.VISIBLE);
                actvityCollectCv.hide();
                rippleLayout.stopRippleAnimation();
            } else if (BluetoothLeService.ACTION_CALI_REF1_FINISHED.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_DARK_SPECTRUM_DATA.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_REF_SPECTRUM_DATA.equals(action)) {

            } else if (BluetoothLeService.ACTION_UPGRADE.equals(action)) {

            } else if (BluetoothLeService.ACTION_GET_FW_VER.equals(action)) {

            } else if (BluetoothLeService.ACTION_GET_CALIBRATE_TIME.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_TEST_OBJ.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_AUTO_CALIBRATE_TIME.equals(action)) {

            } else if (BluetoothLeService.ACTION_GET_BATTERY_VAL.equals(action)) {

            } else if (BluetoothLeService.ACTION_ERROR_INFO.equals(action)) {
                Logger.d("接收到未添加有效目标的广播");
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }
}
