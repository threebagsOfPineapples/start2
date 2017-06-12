package com.tachyon5.kstart.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.widget.Space;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.ble.BluetoothDeviceScanActivity;
import com.tachyon5.kstart.ble.BluetoothLeService;
import com.tachyon5.kstart.ble.LeDeviceListAdapter;
import com.tachyon5.kstart.utils.Constant;
import com.tachyon5.kstart.utils.SharePreferenceUtil;
import com.tachyon5.kstart.utils.ToastUtil;
import com.tachyon5.kstart.view.CustomDialogNew;
import com.tachyon5.kstart.view.DeviceBindDialog;

import java.io.File;
import java.util.List;

import static com.tachyon5.kstart.utils.Constant.Device_Mac;
import static com.tachyon5.kstart.utils.Constant.IsBind;
import static com.tachyon5.kstart.utils.Constant.mBluetoothLeService;
import static com.tachyon5.kstart.utils.Constant.mCharacteristic;
import static com.tachyon5.kstart.utils.Constant.mCharacteristicNotify;
import static com.tachyon5.kstart.utils.Constant.makeGattUpdateIntentFilter;
import static com.tachyon5.kstart.utils.Constant.mnotyGattService;

public class ScanBtActivity extends MPermissionsActivity implements View.OnClickListener {
    private List<BluetoothGattService> mServiceList = null;
    public static BluetoothGattService mOadService = null;
    public static BluetoothGattService mConnControlService = null;
    private ImageView iv_bt_open, pgbar_stop;
    private TextView tv_big, tv_small, tv_title, tv_not_open, tv_startcalibration;
    private LinearLayout ll_calibration_preparation, ll_startcalibration;
    private Space space;
    private ListView listView;
    private Handler mHandler;
    private ProgressBar pgbar;
    private boolean mScanning;
    private RelativeLayout rl_back;
    private BluetoothAdapter mBluetoothAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LeDeviceListAdapter leDeviceListAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    private LinearLayout ll_before, ll_scan;
    private boolean Connectted;
    CustomDialogNew customDialogNew;
    DeviceBindDialog deviceBindDialog;
    // 10秒后停止查找搜索.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_bt);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initData();
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        iv_bt_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        });
        if (mBluetoothAdapter.isEnabled()) {
            Logger.d("-----------蓝牙可用--------------");
            iv_bt_open.setVisibility(View.GONE);
            pgbar.setVisibility(View.VISIBLE);
            tv_big.setText(R.string.bluetooth_no_connect);
            tv_small.setText(R.string.click_to_sure);
            tv_title.setText("MSI");
            tv_not_open.setText(R.string.do_not_connect_now);
            //   scanLeDevice(true);
            requestPermission(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0x0002);
        } else {
            Logger.d("-----------蓝牙不可用----------------");
            iv_bt_open.setVisibility(View.VISIBLE);
            pgbar.setVisibility(View.GONE);
            tv_big.setText(R.string.scanning_msi);
            tv_small.setText(R.string.please_close_to_msi);
            tv_title.setText(R.string.bluetooth_no_connect);
            tv_not_open.setText(R.string.do_not_open_now);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                deviceBindDialog = new DeviceBindDialog(ScanBtActivity.this, R.style.MyDialog);
                deviceBindDialog.show();
                Window dialogWindow = deviceBindDialog.getWindow();
                WindowManager.LayoutParams p = dialogWindow.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                p.width = (int) (dm.widthPixels) * 112 / 375;
                Logger.d("宽度" + dm.heightPixels + "长度：" + dm.widthPixels);
                p.height = p.width;
                dialogWindow.setAttributes(p);
                //与服务建立连接
                ServiceConnection mServiceConnection = new ServiceConnection() {
                    // 服务连接建立之后的回调函数。
                    @Override
                    public void onServiceConnected(ComponentName componentName, IBinder service) {
                        Logger.d("--------onServiceConnected----------");
                        mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
                        // 初始化
                        if (!mBluetoothLeService.initialize()) {
                            Logger.e("Unable to initialize Bluetooth");
                            finish();
                        }
                        Logger.d("--------mdeviceAddress----------" + Device_Mac);
                        new Thread(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        Logger.d("--------子线程开启---------");
                                        if (Device_Mac != null && Device_Mac != "") {
                                            Logger.d("mDeviceAddress:" + Device_Mac);
                                            Connectted = mBluetoothLeService.connect(Device_Mac);
                                            while (!Connectted) {
                                                mBluetoothLeService.connect(Device_Mac);
                                                Connectted = mBluetoothLeService.connect(Device_Mac);
                                                Logger.d("--------connnected----------" + Connectted);
                                            }
                                        }
                                    }
                                }
                        ).start();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName componentName) {
                        mBluetoothLeService = null;
                    }
                };
                if (mBluetoothLeService != null) {
                    mBluetoothLeService.stopSelf();
                    mBluetoothLeService.disconnect();
                    mBluetoothLeService = null;
                    Constant.Device_Mac = null;
                    Constant.ISCONNECT = false;
                }
                final BluetoothDevice device = leDeviceListAdapter.getDevice(position);
                if (device == null) {
                    Logger.d("device 为null");
                } else {
                    Logger.d("device 不为null");
                }
                SharePreferenceUtil.deleteAttributeByKey(ScanBtActivity.this, "SP", "MAC");
                Constant.Device_Mac = device.getAddress();
                Logger.d("Mac地址：" + Constant.Device_Mac);
                /* Intent intent = new Intent(ScanBtActivity.this, MainActivity.class);
                  intent.putExtra("MAC", device.getAddress());*/
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                if (device.getName().substring(0, 4).equals("Milk") || device.getName().substring(0, 4).equals("PSI3")) {
                    SharePreferenceUtil.saveOrUpdateAttribute(ScanBtActivity.this, "SP", "CommPro", "0");
                    Constant.CommProtocol = "0";
                } else {
                    SharePreferenceUtil.saveOrUpdateAttribute(ScanBtActivity.this, "SP", "CommPro", "1");
                    Constant.CommProtocol = "1";
                }
                Intent gattServiceIntent = new Intent(ScanBtActivity.this, BluetoothLeService.class);
                IsBind = bindService(gattServiceIntent, mServiceConnection, ScanBtActivity.BIND_AUTO_CREATE);
                Logger.d("是否绑定：" + IsBind);
            }
        });
    }

    private void initData() {
        iv_bt_open.setOnClickListener(this);
        tv_not_open.setOnClickListener(this);
        pgbar.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leDeviceListAdapter.clear();
                scanLeDevice(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("==position==" + position);
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        });
        leDeviceListAdapter = new LeDeviceListAdapter(this);
        listView.setAdapter(leDeviceListAdapter);
        tv_startcalibration.setOnClickListener(this);
    }

    private void initView() {
        pgbar_stop = (ImageView) findViewById(R.id.pbar_stop);
        space = (Space) findViewById(R.id.space);
        tv_not_open = (TextView) findViewById(R.id.tv_do_not_open);
        tv_title = (TextView) findViewById(R.id.activity_scan_bt_tv_title);
        iv_bt_open = (ImageView) findViewById(R.id.iv_open_bt);
        pgbar = (ProgressBar) findViewById(R.id.pbar);
        tv_big = (TextView) findViewById(R.id.tv_big);
        tv_small = (TextView) findViewById(R.id.tv_small);
        rl_back = (RelativeLayout) findViewById(R.id.rl_do_not);
        listView = (ListView) findViewById(R.id.activity_device_scan_ll);
        ll_calibration_preparation = (LinearLayout) findViewById(R.id.ll_calibration_preparation);
        tv_startcalibration = (TextView) findViewById(R.id.scanbt_tv_calibration);
        ll_startcalibration = (LinearLayout) findViewById(R.id.ll_btn_calibration);
        ll_before = (LinearLayout) findViewById(R.id.ll_scan_before);
        ll_scan = (LinearLayout) findViewById(R.id.ll_scanning);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById
                (R.id.activity_device_scan_swipeRefreshLayout);
        mHandler = new Handler();
        leDeviceListAdapter = new LeDeviceListAdapter(this);
        listView.setAdapter(leDeviceListAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink,
                R.color.colorPrimaryDark
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
      /*  // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            *//*if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }*//*
        }*/
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode != Activity.RESULT_CANCELED) {
            Logger.d("收到蓝牙确认回复~~~~");
            tv_title.setText("MSI");
            tv_not_open.setText("暂不开启");
            iv_bt_open.setVisibility(View.GONE);
            pgbar.setVisibility(View.VISIBLE);
            return;
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Logger.d("取消蓝牙~~~~~");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_do_not_open:
                onBackPressed();
                break;
            case R.id.scanbt_tv_calibration:
                ToastUtil.showToast(getApplicationContext(), "少时诵诗书");
                pgbar.setVisibility(View.VISIBLE);
                pgbar_stop.setVisibility(View.GONE);
                tv_startcalibration.setText("正在校准");
                byte[] payload = {0x01};
                mBluetoothLeService.sendMessage(payload, (byte) 0x54, (byte) 0x00);
            default:
        }
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    if (leDeviceListAdapter.getCount() == 0) {

                    }
                }
            }, SCAN_PERIOD);
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (device.getName() != null) {
                        if (device.getName().substring(0, 4).equals("Milk") || device.getName().substring(0, 4).equals("MLK2")) {
                            leDeviceListAdapter.addDevice(device, rssi);
                            ll_before.setVisibility(View.GONE);
                            ll_scan.setVisibility(View.VISIBLE);
                        }
                    }
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    /**
     * 权限申请成功后进行操作
     *
     * @param requestCode
     */
    @Override
    public void permissionSuccess(int requestCode) {
        super.permissionSuccess(requestCode);
        switch (requestCode) {
            case 0x0002:
                scanLeDevice(true);
                break;
        }
    }

    @Override
    public void permissionFail(int requestCode) {
        super.permissionFail(requestCode);
    }

    /**
     * 广播接收
     */
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
           /*     mServiceList = mBluetoothLeService.getSupportedGattServices();
                checkOad();
                for (int i = 0; i < mServiceList.size(); i++) {
                    Logger.d("------获取的服务列表----" + i + ":" + mServiceList.get(i).getUuid());
                }*/
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                Logger.d("接收到设备可用的广播");
            } else if (BluetoothLeService.ACTION_TO_DETECTION.equals(action)) {

            } else if (BluetoothLeService.ACTION_PAIR.equals(action)) {

                Logger.d("接收到匹配的广播");
            } else if (BluetoothLeService.ACTION_COLLECT.equals(action)) {

                Logger.d("接收到采集消息 并且isclicked=true");
            } else if (BluetoothLeService.ACTION_CONN_AUTHORIZE.equals(action)) {
                Logger.d("接收到 身份认证的广播");
                deviceBindDialog.dimissSuc();
                ll_before.setVisibility(View.VISIBLE);
                ll_scan.setVisibility(View.GONE);
                ll_calibration_preparation.setVisibility(View.VISIBLE);
                ll_startcalibration.setVisibility(View.VISIBLE);
                rl_back.setVisibility(View.GONE);
                space.setVisibility(View.GONE);
                pgbar.setVisibility(View.GONE);
                pgbar_stop.setVisibility(View.VISIBLE);

            } else if (BluetoothLeService.ACTION_GET_WAVE_MAP.equals(action)) {

            } else if (BluetoothLeService.ACTION_SET_WAVE_MAP.equals(action)) {

            } else if (BluetoothLeService.ACTION_CALI_DARK_FINISHED.equals(action)) {
                Logger.d("接收到 dark结束 的广播");
                ToastUtil.showToast(getApplicationContext(), "dark结束。。。");
                tv_startcalibration.setText("校准完成");
                //建立定制list dialog
                CustomDialogNew.Builder builder = new CustomDialogNew.Builder(ScanBtActivity.this);
                customDialogNew =
                        builder.cancelTouchout(false)
                                .view(R.layout.list_dialog_layout)
                                .style(R.style.MyDialog)
                                .addViewOnclick(R.id.list2, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ToastUtil.showToast(getApplicationContext(), "重试");
                                        customDialogNew.dismiss();

                                    }
                                })
                                .addViewOnclick(R.id.list3, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ToastUtil.showToast(getApplicationContext(), "帮助");
                                        //跳转至帮助页面
                                        Intent intent = new Intent(ScanBtActivity.this, HelpActivity.class);
                                        startActivity(intent);
                                        customDialogNew.dismiss();
                                    }
                                })
                                .addViewOnclick(R.id.list4, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        ToastUtil.showToast(getApplicationContext(), "暂不校准");
                                        Intent intent = new Intent();
                                        intent.setClass(ScanBtActivity.this, CollectActivity.class);
                                        startActivity(intent);

                                        customDialogNew.dismiss();
                                    }
                                })
                                .build();
                customDialogNew.show();
                Window dialogWindow = customDialogNew.getWindow();
                WindowManager.LayoutParams p = dialogWindow.getAttributes();
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);
                p.width = (int) (dm.widthPixels) * 240 / 375;
                Logger.d("宽度" + dm.heightPixels + "长度：" + dm.widthPixels);
                p.height = p.width * 175 / 240;
                dialogWindow.setAttributes(p);
                //   deviceBindDialog.show();
                //  deviceBindDialog.dimissSucForCali();
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
 /*protected void checkOad() {
        // Check if OAD is supported (needs OAD and Connection Control service)
        mOadService = null;
        mConnControlService = null;
        // 查找OAD 服务
        for (int i = 0; i < mServiceList.size() && (mOadService == null || mConnControlService == null); i++) {
            BluetoothGattService srv = mServiceList.get(i);
            if (srv.getUuid().equals(Constant.OAD_SERVICE_UUID)) {
                mOadService = srv;
                Logger.d("--OadService--" + mOadService);
                mCharacteristic = mOadService.getCharacteristic(Constant.OAD_CHARACTER);// 获取可读写的特征值
                Logger.d("mCharacteristic:" + mCharacteristic);
                List<BluetoothGattCharacteristic> temp = mOadService.getCharacteristics();
                for (BluetoothGattCharacteristic bluetoothGattCharacteristic : temp) {
                    Logger.d(bluetoothGattCharacteristic.getUuid().toString());
                }
                mCharacteristicNotify = mOadService.getCharacteristic(Constant.OAD_CHARACTER);// 获取有通知特性的特征值
                System.out
                        .println("mCharacteristicNotify" + mCharacteristicNotify + "mCharacteristic" + mCharacteristic);
                mBluetoothLeService.setCharacteristicNotification(mCharacteristicNotify, true);
                Logger.d("准备进行OAD升级");
            } else if (srv.getUuid().equals(Constant.BLE_SERVICE_UUID)) {
                Logger.d("-----普通模式----");
                mnotyGattService = srv;
                mCharacteristic = mnotyGattService.getCharacteristic(Constant.BLE_READ_WRITE_UUID);// 获取可读写的特征值
                mCharacteristicNotify = mnotyGattService.getCharacteristic(Constant.BLE_NOTIFY_UUID);
                mBluetoothLeService.setCharacteristicNotification(mCharacteristicNotify, true);
                try {
                    Thread.sleep(1000);
                    mBluetoothLeService.sendMessage(null, (byte) 0x53, (byte) 0x00);
                    Logger.d("write执行了");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }*/

}
