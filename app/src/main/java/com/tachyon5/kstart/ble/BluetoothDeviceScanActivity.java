package com.tachyon5.kstart.ble;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.activity.MainActivity;
import com.tachyon5.kstart.utils.Constant;
import com.tachyon5.kstart.utils.SharePreferenceUtil;
import com.tachyon5.kstart.utils.ToastUtil;

public class BluetoothDeviceScanActivity extends Activity {
    private ListView listView;
    private Handler mHandler;
    private boolean mScanning;
    private RelativeLayout rl_back;
    private BluetoothAdapter mBluetoothAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LeDeviceListAdapter leDeviceListAdapter;
    private static final int REQUEST_ENABLE_BT = 1;
    // 10秒后停止查找搜索.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bluetooth_device_scan);
        initWindow();
        rl_back = (RelativeLayout) findViewById(R.id.activty_device_scan_rl_title_back);
        listView = (ListView) findViewById(R.id.activity_device_scan_ll);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById
                (R.id.activity_device_scan_swipeRefreshLayout);
        mHandler = new Handler();
        leDeviceListAdapter = new LeDeviceListAdapter(this);
        listView.setAdapter(leDeviceListAdapter);
        swipeRefreshLayout.setColorSchemeResources(R.color.pink,
                R.color.colorPrimaryDark
        );
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                leDeviceListAdapter.clear();
                scanLeDevice(true);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            ToastUtil.showToast(this, R.string.ble_not_supported);
            finish();
        }
        // 初始化 Bluetooth adapter, 通过蓝牙管理器得到一个参考蓝牙适配器(API必须在以上android4.3或以上和版本)
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // 检查设备上是否支持蓝牙
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("==position==" + position);
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                final BluetoothDevice device = leDeviceListAdapter.getDevice(position);
                if (device == null)
                    return;
                SharePreferenceUtil.deleteAttributeByKey(BluetoothDeviceScanActivity.this, "SP", "MAC");
                // SharePreferenceUtil.saveOrUpdateAttribute(BluetoothDeviceScanActivity.this, "SP",device.getAddress(), "MAC");
                Intent intent = new Intent(BluetoothDeviceScanActivity.this, MainActivity.class);
                intent.putExtra("MAC", device.getAddress());
                if (mScanning) {
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mScanning = false;
                }
                if (device.getName().substring(0, 4).equals("Milk") || device.getName().substring(0, 4).equals("PSI3")) {
                    SharePreferenceUtil.saveOrUpdateAttribute(BluetoothDeviceScanActivity.this, "SP", "CommPro", "0");
                    Constant.CommProtocol = "0";
                } else {
                    SharePreferenceUtil.saveOrUpdateAttribute(BluetoothDeviceScanActivity.this, "SP", "CommPro", "1");
                    Constant.CommProtocol = "1";
                }
                startActivity(intent);
                finish();
            }
        });
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
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
                        }
                    }
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    protected void onResume() {
        super.onResume();
        // 为了确保设备上蓝牙能使用, 如果当前蓝牙设备没启用,弹出对话框向用户要求授予权限来启用
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
        leDeviceListAdapter = new LeDeviceListAdapter(this);
        listView.setAdapter(leDeviceListAdapter);
        scanLeDevice(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initWindow() {
        // TODO Auto-generated method stub
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
        }
    }
}
