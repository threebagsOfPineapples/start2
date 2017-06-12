/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tachyon5.kstart.ble;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.utils.Constant;
import com.tachyon5.kstart.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.tachyon5.kstart.ble.BTMsg.Id;
import static com.tachyon5.kstart.utils.Constant.CommProtocol;
import static com.tachyon5.kstart.utils.Constant.SERVICE_DATA_SEND_FOR_TWO;
import static com.tachyon5.kstart.utils.Constant.SERVICE_TIMEOUT;
import static com.tachyon5.kstart.utils.Constant.mBluetoothLeService;
import static com.tachyon5.kstart.utils.Constant.mCharacteristic;
import static com.tachyon5.kstart.utils.Constant.mCharacteristicNotify;
import static com.tachyon5.kstart.utils.Constant.mnotyGattService;

/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
    private final static String TAG = BluetoothLeService.class.getSimpleName();
    // BLE
    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBtAdapter = null;
    private static BluetoothGatt mBluetoothGatt = null;
    private static BluetoothLeService mThis = null;
    private volatile boolean mBusy = false; // Write/read pending response
    public static boolean MSG_TIMEOUT = false;
    private String mBluetoothDeviceAddress;
    private int state;
    private int length = 0;
    private byte cmd;
    public static boolean OK = false;
    public static byte[] Send;
    public static byte[] MSG_Send;
    public static boolean MSG_OK = false;
    MsgTimeOut msgTimeOut = new MsgTimeOut(1000, 100);
    private int mConnectionState = STATE_DISCONNECTED;
    private BTMsg mMsg = new BTMsg();
    private MsgDataProcessing dataProcessing = new MsgDataProcessing();
    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    public static byte[] data;
    private static byte[] msgData;
    private static int msgDataPos;
    StringBuffer a = new StringBuffer();
    private List<BluetoothGattService> mServiceList = null;
    public static BluetoothGattService mOadService = null;
    public static BluetoothGattService mConnControlService = null;
    public final static String ACTION_CALI_DARK_FINISHED = "com.example.bluetooth.le.CALI_DARK_FINISHED";
    public final static String ACTION_CALI_REF1_FINISHED = "com.example.bluetooth.le.CALI_REF1_FINISHED";
    public final static String ACTION_CALI_REF2_FINISHED = "com.example.bluetooth.le.CALI_REF2_FINISHED";
    public final static String ACTION_TO_DETECTION = "com.example.bluetooth.le.ACTION_TO_DETECTION";
    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String ACTION_COLLECT = "com.example.bluetooth.le.ACTION_COLLECT";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
    public final static String IMAGEB_DATA = "com.example.bluetooth.le.IMAGEB_DATA";
    public final static String TEST_TIME = "com.example.bluetooth.le.TEST_TIME";
    public final static String ACTION_PAIR = "com.example.bluetooth.le.ACTION_PAIR";
    public final static String ACTION_CONN_AUTHORIZE = "com.example.bluetooth.le.ACTION_CONN_AUTHORIZE";
    public final static String ACTION_GET_FW_VER = "com.example.bluetooth.le.ACTION_GET_FW_VER";
    public final static String ACTION_FONT = "com.example.bluetooth.le.ACTION_FONT";
    public final static String ACTION_FONT_Name = "com.example.bluetooth.le.ACTION_FONT_NAME";
    public final static String ACTION_UPGRADE = "com.example.bluetooth.le.ACTION_UPGRADE";
    public final static String ACTION_IMAGB = "com.example.bluetooth.le.ACTION_IMAGEB";
    public final static String EXTRA_STATUS = "com.example.bluetooth.le.EXTRA_STATUS";
    public final static String EXTRA_ADDRESS = "com.example.bluetooth.le.EXTRA_ADDRESS";
    public final static String ACTION_SET_DARK_SPECTRUM_DATA = "com.example.bluetooth.le.ACTION_DARK_SET";
    public final static String ACTION_SET_REF_SPECTRUM_DATA = "com.example.bluetooth.le.ACTION_REF_SET";
    public final static String ACTION_GET_CALIBRATE_TIME = "com.example.bluetooth.le.ACTION_GET_CALIBRATE_TIME";
    public final static String ACTION_SET_TEST_OBJ = "com.example.bluetooth.le.ACTION_SET_TEST_OBJ";
    public final static String ACTION_ERROR_INFO = "com.example.bluetooth.le.ACTION_ERROR_INFO";
    public final static String ACTION_SET_AUTO_CALIBRATE_TIME = "com.example.bluetooth.le.ACTION_SET_AUTO_CALIBRATE_TIME";
    public final static String ACTION_GET_WAVE_MAP = "com.example.bluetooth.le.ACTION_GET_WAVE_MAP";
    public final static String ACTION_SET_WAVE_MAP = "com.example.bluetooth.le.ACTION_SET_WAVE_MAP";
    public final static String ACTION_GET_BATTERY_VAL = "com.example.bluetooth.le.ACTION_GET_BATTERY_VAL";
    //	public final static String ACTION_
    // ble特征值UUID
    @SuppressWarnings("unused")
    private static final UUID UUID_READ_WRITE = UUID.fromString(Constant.BLE_READ_WRITE);
    public Handler serviceHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                switch (msg.what) {
                    case Constant.SERVICE_DATA_SEND:
                        Logger.d("开始传输" + msgData.length + "--msg.data--" + msgDataPos + "msgData长度" + msgData.length);
                        int rest = msgData.length - msgDataPos;
                        int sendByte = rest > 20 ? 20 : rest;
                        if (sendByte >= 0) {
                            byte[] send = new byte[sendByte];
                            for (int i = 0; i < send.length; i++) {
                                send[i] = msgData[msgDataPos + i];
                            }
                            Logger.d("Service中send的值------" + Arrays.toString(send));
                            if (send != null) {
                                writeCharacteristic(mCharacteristic, send);
                            }
                            msgDataPos += send.length;
                            Logger.d("开始传输----------debug" + "rest:" + rest + "sendByte" + sendByte + "send:"
                                    + send.length);
                            if (msgDataPos < msgData.length) {
                                serviceHandler.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
                            } else {
                                Logger.d("组装完毕");
                            }
                        } else {
                            Logger.d("消息");
                        }
                        break;
                    case SERVICE_DATA_SEND_FOR_TWO:
                        int rest1 = msgData.length - msgDataPos;
                        Logger.d("rest1的值为：" + rest1);
                        int sendByte1 = rest1 > 19 ? 19 : rest1;
                        if (sendByte1 >= 0) {
                            byte[] send = new byte[sendByte1 + 1];
                            Logger.d("send.length:" + send.length + "大小长度" + sendByte1 + "msgdatapos:" + msgDataPos);
                            send[0] = Id;
                            for (int i = 0; i < send.length - 1; i++) {
                                send[i + 1] = msgData[msgDataPos + i];
                            }
                            Logger.d("Service中send的值------" + Arrays.toString(send));
                            MSG_Send = send;
                            if (send.length > 2) {
                                writeCharacteristic(mCharacteristic, MSG_Send);
                                MSG_TIMEOUT = true;
                                // serviceHandler.sendEmptyMessageDelayed(SERVICE_TIMEOUT,100);
                                msgTimeOut.start();
                            }
                            msgDataPos += send.length - 1;
                            if (msgDataPos < msgData.length) {
                                MSG_OK = false;
                                Logger.d("个数小于消息总数+继续进行组装");
                            } else {
                                MSG_OK = true;
                                Logger.d("组装完毕");
                            }
                        } else {
                            Logger.d("传输出错 此时Id" + Id);
                            Id = 0;
                        }
                        break;
                    case SERVICE_TIMEOUT:
                        Logger.d("超时 重新发送----");
                        if (MSG_TIMEOUT) {
                        }
                        break;
                    default:
                }
            }
        }
    };
    /*
     * Implements callback methods for GATT events that the app cares about. For
	 * example, connection change and services discovered.
	 * 这里有9个要实现的回调方法，看情况要实现那些，用到那些就实现那些
	 * 如果只是在某个点接收(有客户端请求)，可以用读;如果要一直接收(无客户端请求)，要用notify
	 */
    /**
     * GATT client callbacks
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
        @Override
        // 连接或者断开蓝牙，方法一
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (mBluetoothGatt == null) {
                Logger.e(TAG, "mBluetoothGatt not created!");
                return;
            }
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                // MainActivity.ISCONNECT = true;
                broadcastUpdate(intentAction);
                Logger.i(TAG, "Connected to GATT server.");
                /*
                 * Attempts to discover services after successful connection.
				 * 函数调用之间存在先后关系。例如首先需要connect上才能discoverServices。
				 */
                Logger.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                //   MainActivity.ISCONNECT = false;
                //	ToastUtil.showToast(getBaseContext(),"与设备断开连接");
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        // 发现服务的回调，发送广播，方法二
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            Logger.d("服务回调");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
                mServiceList = mBluetoothLeService.getSupportedGattServices();
                checkOad();
                for (int i = 0; i < mServiceList.size(); i++) {
                    Logger.d("------获取的服务列表----" + i + ":" + mServiceList.get(i).getUuid());
                }
                getSupportedGattServices();
                Logger.d("服务回调===================================");
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        // write的回调,发送广播，方法三
        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            Log.e("WRITE", "onCharacteristicWrite() - status: " + status + "  - UUID: " + characteristic.getUuid());
            // write回调失败 status=128， read回调失败status=128. status=0,回调成功；=9，数组超长
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                Log.e("WRITE SUCCESS", "回调成功 " + status + "  - UUID: " + characteristic.getUuid());
            } else {
                Log.e("FAIL", "回调失败 " + status + "  - UUID: " + characteristic.getUuid());
            }
        }

        // read回调，方法四
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
                Log.e("READ SUCCESS",
                        "onCharacteristicRead() - status: " + status + "  - UUID: " + characteristic.getUuid());
            }
        }

        // notification回调，方法五
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            data = characteristic.getValue();
            Logger.d("data的长度" + data.length + "data的值：" + Arrays.toString(data));
            if (CommProtocol.equals("0")) {
                if (false) {
                   /* int value;
                    value = data[0] & 0xff;
                    value |= (data[1] & 0xff) << 8;
                    if (value < MainActivity.block.size()) {
                        byte[] send = new byte[18];
                        send[0] = data[0];
                        send[1] = data[1];
                        Logger.d("value的值是：" + value);
                        for (int i = 0; i < 16; i++) {
                            send[i + 2] = MainActivity.block.get(value)[i];
                        }
                        Send = send;
                    }
                    broadcastUpdate(ACTION_IMAGB, characteristic);*/
                } else {
                    int state = mMsg.assembleMsgByByte(data);
                    if (state == BTMsg.MSG_STATE_PROCESSING) {
                        dataProcessing.putMsg(mMsg);
                        Logger.d(mMsg.getmCmd() + "getCmd" + "++++++++++++++++++++++++++++++++++++++++++++++++");
                        dataProcessing.dataProcessing();
                        switch (mMsg.getmCmd()) {
                            case BTMsg.MSG_CMD_CONN_AUTHORIZE:
                                Logger.d("---身份校验-----");
                                broadcastUpdate(ACTION_CONN_AUTHORIZE);
                                break;
                            case BTMsg.MSG_CMD_SPECTRUM_NOTIFY:
                                Logger.d("++++++++" + OK);
                                if (OK) {
                                    broadcastUpdate(ACTION_TO_DETECTION);
                                }
                                Logger.d("我发送数据了");
                                OK = false;
                                break;
                            case BTMsg.MSG_CMD_DARKEVN_CALIB:
                                Logger.d("!!!MSG_CMD_DARKEVN_CALIB");
                                broadcastUpdate(ACTION_CALI_DARK_FINISHED);
                                break;
                            case BTMsg.MSG_CMD_REF1_CALIB:
                                Logger.d("!!!MSG_CMD_REF1_CALIB");
                                broadcastUpdate(ACTION_CALI_REF1_FINISHED);
                                break;
                            case BTMsg.MSG_CMD_SET_DARK_SPECTRUM_DATA:
                                Logger.d("!!!MSG_CMD_SET_DARK_CALIB");
                                broadcastUpdate(ACTION_SET_DARK_SPECTRUM_DATA);
                                break;
                            case BTMsg.MSG_CMD_SET_REF_SPECTRUM_DATA:
                                Logger.d("!!!MSG_CMD_SET_REF_CALIB");
                                broadcastUpdate(ACTION_SET_REF_SPECTRUM_DATA);
                                break;
                            case BTMsg.MSG_CMD_PAIR:
                                Logger.d("!!!MSG_CMD_PAIR");
                                broadcastUpdate(ACTION_PAIR);
                                break;
                            case BTMsg.MSG_CMD_GET_FW_VER:
                                Logger.d("MSG_CMD_GET_FW_VER-获取版本号-");
                                broadcastUpdate(ACTION_GET_FW_VER);
                                break;
                            case BTMsg.MSG_CMD_COLLECT:
                                Logger.d("MSG_CMD_COLLECT-收集");
                                broadcastUpdate(ACTION_COLLECT);
                                break;
                            case BTMsg.MSG_CMD_FONT:
                              /*  Logger.d("----------字库传输---------");
                                //	broadcastUpdate(ACTION_FONT);
                                try {
                                    WriteFontName();
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }*/
                                break;
                            case BTMsg.MSG_CMD_FONT_NAME:
                                Logger.d("----------字库名称传输---------");
                                break;
                            // TODO OAD升级
                            case BTMsg.MSG_CMD_UPGRADE:
                                Logger.d("----------OAD升级传输---------");
                                broadcastUpdate(ACTION_UPGRADE);
                                break;
                            case BTMsg.MSG_GET_CALIBRATE_TIME:
                                Logger.d("-----获取上次校准时间------");
                                broadcastUpdate(ACTION_GET_CALIBRATE_TIME);
                                break;
                            case BTMsg.MSG_SET_AUTO_CALIBRATE_TIME:
                                Logger.d("-----设置校准时间------");
                                broadcastUpdate(ACTION_SET_AUTO_CALIBRATE_TIME);
                                break;
                            case BTMsg.MSG_GET_WAVE_MAP:
                                Logger.d("---获取波长映射数据---");
                                broadcastUpdate(ACTION_GET_WAVE_MAP);
                                break;
                            case BTMsg.MSG_SET_WAVE_MAP:
                                Logger.d("---设置波长映射数据---");
                                broadcastUpdate(ACTION_SET_WAVE_MAP);
                                break;
                            case BTMsg.MS_TM_SET_TEST_OBJ:
                                Logger.d("--设置为未知物品--");
                                broadcastUpdate(ACTION_SET_TEST_OBJ);
                                break;
                            case BTMsg.MS_TM_ERROR_INFO:
                                Logger.d("--设置错误信息--");
                                broadcastUpdate(ACTION_ERROR_INFO);
                                break;
                            case BTMsg.MSG_CMD_GET_BATTERY_VAL:
                                Logger.d("--获得电量信息--");
                                broadcastUpdate(ACTION_GET_BATTERY_VAL);
                                break;
                            default:
                                break;
                        }
                        mMsg.clear();
                    }
                    Log.e("zmx", "--------onCharacteristicChanged-----" + "state:  " + state);
                }
            } else if (CommProtocol.equals("1")) {
                //添加延迟 尝试~
                if (data.length < 2) {
                    Logger.d("data的长度小于2第一位的值为" + data[0]);
                    int a = data[0];
                    Logger.d("int类型：" + a);

                    if (data[0] == Id && !MSG_OK) {
                        Id = (byte) (a + 1);
                        Logger.d("id增加 id为：" + Id);
                    }
                    msgTimeOut.cancel();
                    MSG_TIMEOUT = false;

                    if (!MSG_OK) {
                        Logger.d("消息未发送完毕 id：" + Id);
                        serviceHandler.sendEmptyMessage(SERVICE_DATA_SEND_FOR_TWO);
                    }
                } else if (false) {
                   /* int value;
                    value = data[0] & 0xff;
                    value |= (data[1] & 0xff) << 8;
                    if (value < MainActivity.block.size()) {
                        byte[] send = new byte[18];
                        send[0] = data[0];
                        send[1] = data[1];
                        Logger.d("value的值是：" + value);
                        for (int i = 0; i < 16; i++) {
                            send[i + 2] = MainActivity.block.get(value)[i];
                        }
                        Send = send;
                    }
                    broadcastUpdate(ACTION_IMAGB, characteristic);*/
                } else {
                    Logger.d("data的长度大于2--第一位的值为" + data[0] + "  Id:" + Id);
                    if (data[0] == 0 || data[0] != Id) {
                        int state = mMsg.assembleMsgByBytefortwo(data);
                        if (state == BTMsg.MSG_STATE_PROCESSING) {
                            dataProcessing.putMsg(mMsg);
                            Logger.d(mMsg.getmCmd() + "getCmd" + "++++++++++++++++++++++++++++++++++++++++++++++++");
                            dataProcessing.dataProcessing();
                            switch (mMsg.getmCmd()) {
                                case BTMsg.MSG_CMD_CONN_AUTHORIZE:
                                    Logger.d("---身份校验-----");
                                    broadcastUpdate(ACTION_CONN_AUTHORIZE);
                                    break;
                                case BTMsg.MSG_CMD_SPECTRUM_NOTIFY:
                                    Logger.d("++++++++" + OK);
                                    if (OK) {
                                        broadcastUpdate(ACTION_TO_DETECTION);
                                    }
                                    Logger.d("我发送数据了");
                                    OK = false;
                                    break;
                                case BTMsg.MSG_CMD_DARKEVN_CALIB:
                                    Logger.d("!!!MSG_CMD_DARKEVN_CALIB");
                                    broadcastUpdate(ACTION_CALI_DARK_FINISHED);
                                    break;
                                case BTMsg.MSG_CMD_REF1_CALIB:
                                    Logger.d("!!!MSG_CMD_REF1_CALIB");
                                    broadcastUpdate(ACTION_CALI_REF1_FINISHED);
                                    break;
                                case BTMsg.MSG_CMD_SET_DARK_SPECTRUM_DATA:
                                    Logger.d("!!!MSG_CMD_SET_DARK_CALIB");
                                    broadcastUpdate(ACTION_SET_DARK_SPECTRUM_DATA);
                                    break;
                                case BTMsg.MSG_CMD_SET_REF_SPECTRUM_DATA:
                                    Logger.d("!!!MSG_CMD_SET_REF_CALIB");
                                    broadcastUpdate(ACTION_SET_REF_SPECTRUM_DATA);
                                    break;
                                case BTMsg.MSG_CMD_PAIR:
                                    Logger.d("!!!MSG_CMD_PAIR");
                                    broadcastUpdate(ACTION_PAIR);
                                    break;
                                case BTMsg.MSG_CMD_GET_FW_VER:
                                    Logger.d("MSG_CMD_GET_FW_VER-获取版本号-");
                                    broadcastUpdate(ACTION_GET_FW_VER);
                                    break;
                                case BTMsg.MSG_CMD_COLLECT:
                                    Logger.d("MSG_CMD_COLLECT-收集");
                                    broadcastUpdate(ACTION_COLLECT);
                                    break;
                                case BTMsg.MSG_CMD_FONT:
                                  /*  Logger.d("----------字库传输---------");
                                    //	broadcastUpdate(ACTION_FONT);
                                    try {
                                        WriteFontName();
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    break;*/
                                case BTMsg.MSG_CMD_FONT_NAME:
                                    Logger.d("----------字库名称传输---------");
                                    //broadcastUpdate(ACTION_FONT_Name);
                                    // WRITE_FONT_BUSY = false;
                                    break;
                                // TODO OAD升级
                                case BTMsg.MSG_CMD_UPGRADE:
                                    Logger.d("----------OAD升级传输---------"
                                            + mMsg.getmPayloadLen() + "payload:" + mMsg.getmPayload()[0]);
                                    broadcastUpdate(ACTION_UPGRADE);
                                    break;
                                case BTMsg.MSG_GET_CALIBRATE_TIME:
                                    Logger.d("-----获取上次校准时间------");
                                    broadcastUpdate(ACTION_GET_CALIBRATE_TIME);
                                    break;
                                case BTMsg.MSG_SET_AUTO_CALIBRATE_TIME:
                                    Logger.d("-----设置校准时间------");
                                    broadcastUpdate(ACTION_SET_AUTO_CALIBRATE_TIME);
                                    break;
                                case BTMsg.MSG_GET_WAVE_MAP:
                                    Logger.d("---获取波长映射数据---");
                                    broadcastUpdate(ACTION_GET_WAVE_MAP);
                                    break;
                                case BTMsg.MSG_SET_WAVE_MAP:
                                    Logger.d("---设置波长映射数据---");
                                    broadcastUpdate(ACTION_SET_WAVE_MAP);
                                    break;
                                case BTMsg.MS_TM_SET_TEST_OBJ:
                                    Logger.d("--设置为未知物品--");
                                    broadcastUpdate(ACTION_SET_TEST_OBJ);
                                    break;
                                case BTMsg.MS_TM_ERROR_INFO:
                                    Logger.d("--设置错误信息--");
                                    broadcastUpdate(ACTION_ERROR_INFO);
                                    break;
                                case BTMsg.MSG_CMD_GET_BATTERY_VAL:
                                    Logger.d("--获得电量信息--");
                                    broadcastUpdate(ACTION_GET_BATTERY_VAL);
                                    break;
                                default:
                                    break;
                            }
                            mMsg.clear();
                        }
                        Log.e("zmx", "--------onCharacteristicChanged-----" + "state:  " + state);
                    } else {
                        Logger.d("固件尝试超时发送id:" + data[0]);
                    }
                }
            }
        }
    };

    /*  private void WriteFontName() throws UnsupportedEncodingException {
          byte[] payload = Font.FontLibraryGenerationName(s);
          byte[] payloadNew = new byte[payload.length + 2];
          payloadNew[0] = 0x03;
          payloadNew[1] = (byte) 0x00;
          for (int i = 0; i < payload.length; i++) {
              payloadNew[i + 2] = payload[i];
          }
          sendMessage(payloadNew, (byte) 0x52, (byte) 0x00);
      }*/
    public void sendMessage(byte[] send, byte cmd, byte mtype) {
        BTMsg msg = new BTMsg(cmd, mtype, send);
        msgData = msg.getRawData();
        msgDataPos = 0;
        Id = 0;
        if (CommProtocol.equals("0")) {
            Logger.d("0000000000000000000000000000000000000000000000");
            serviceHandler.sendEmptyMessage(Constant.SERVICE_DATA_SEND);
        } else if (CommProtocol.equals("1")) {
            Logger.d("1111111111111111111111111");
            serviceHandler.sendEmptyMessage(SERVICE_DATA_SEND_FOR_TWO);
        }
    }

    public void sendMessage(byte b) {
        Logger.d("执行发送ID" + b);
        msgDataPos = 0;
        byte[] a = {b};
        writeCharacteristic(mCharacteristic, a);
    }

    // 当一个特定的回调函数被触发，它会调用适当的broadcastUpdate()辅助方法并传递一个action。
    // 这个broadcastUpdate方法，实现蓝牙状态（即方法一、二）的广播
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // 复写broadcastUpdate方法，来实现蓝牙其他状态的广播（方法三、四、五的回调，蓝牙最重要的三个方法）
    // 注意,本节中的数据解析执行按照蓝牙心率测量概要文件规范
    // Parameters intent ： The Intent to broadcast;
    // all receivers matching this Intent will receive the broadcast.
    // an intent with a given action.
    private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
        Intent intent = new Intent(action);
        // 配置文件，用十六进制发送和接收数据。
        byte[] data = characteristic.getValue();
        StringBuilder stringBuilder = new StringBuilder(data.length);// StringBuilder非线程安全，执行速度最快
        if (data != null && data.length > 0) {
            for (byte byteChar : data)
                stringBuilder.append(String.format("%02X", byteChar));// "%02x "以FF FF形式解析数据(注意有无空格)
            intent.putExtra(EXTRA_DATA, stringBuilder.toString());
        }
        Log.e("广播发送", "特征值长度" + characteristic.getValue().length + "  " + stringBuilder.toString());
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.d("是否解除绑定");
        close();
        return super.onUnbind(intent);
    }

    private final IBinder mBinder = new LocalBinder();

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }
        mBtAdapter = mBluetoothManager.getAdapter();
        if (mBtAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }
        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     * @return Return true if the connection is initiated successfully. The
     * connection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public boolean connect(final String address) {
        if (mBtAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            Logger.d(address + "");
            return false;
        }
        // Previously connected device. Try to reconnect.
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            List<BluetoothGattService> a = getSupportedGattServices();
            if (a != null) {
                for (int i = 0; i < a.size(); i++) {
                    Logger.d("getUuid" + i + ":" + a.get(i).getUuid());
                }
            } else {
                Logger.d("a为null");
            }
            if (mBluetoothGatt.connect()) {
                Logger.d("connect 为true");
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        /**
         * We want to directly connect to the device, so we are setting the
         * autoConnect parameter to false. device.connectGatt连接到GATT
         * Server,并返回一个BluetoothGatt实例.
         * mGattCallback为回调函数BluetoothGattCallback（抽象类）的实例。
         */
        Logger.d("-----------Trying to create a new connection-------------" + mBluetoothGatt);
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        // 刷新缓存
        refreshDeviceCache(mBluetoothGatt);
        Logger.d("尝试清理缓存");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The
     * disconnection result is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBtAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    /**
     * 利用反射调用Gatt中的方法
     *
     * @param gatt
     * @return
     */
    private boolean refreshDeviceCache(BluetoothGatt gatt) {
        try {
            BluetoothGatt localBluetoothGatt = gatt;
            Method localMethod = localBluetoothGatt.getClass().getMethod("refresh");
            if (localMethod != null) {
                boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt)).booleanValue();
                return bool;
            }
        } catch (Exception localException) {
            Log.e(TAG, "An exception occured while refreshing device");
        }
        return false;
    }

    /**
     * 清理缓存
     */
    public static boolean refreshDeviceCachea(BluetoothGatt gatt) {
        Logger.d("------蓝牙缓存清理-----");
        /*
         * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
		 */
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null) {
                final boolean success = (Boolean) refresh.invoke(mBluetoothGatt);
                Log.i(TAG, "Refreshing result: " + success);
                return success;
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occured while refreshing device", e);
        }
        return false;
    }

    /**
     * After using a given BLE device, the app must call this method to ensure
     * resources are released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
        Logger.d("关闭服务");
    }

    /**
     * Request a read on a given {@code BluetoothGattCharacteristic}. The read
     * result is reported asynchronously through the
     * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt,
     * android.bluetooth.BluetoothGattCharacteristic, int)} callback.
     *
     * @param characteristic The characteristic to read from.     *                       <p>
     *                       为应用方便，复写了readCharacteristic()方法
     */
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBtAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    /**
     * Enables or disables notification on a give characteristic.
     *
     * @param characteristic Characteristic to act on.
     * @param enabled        If true, enable notification. False otherwise.
     *                       <p>复写setCharacteristicNotification()
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
        if (mBtAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
        BluetoothGattDescriptor clientConfig = characteristic
                .getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
        if (enabled) {
            clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        } else {
            clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
        }
        mBluetoothGatt.writeDescriptor(clientConfig);
        // 设置characteristic的描述值。
        // 所有的服务、特征值、描述值都用UUID来标识，先根据characteristic的UUID找到characteristic，再根据BluetoothGattDescriptor的
        // UUID找到BluetoothGattDescriptor，然后设定其值。
        // 关于descriptor，可以通过getDescriptor()方法的返回值来理解,
        // Returns a descriptor with a given UUID out of the list of descriptors
        // for this characteristic.5
    }

    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] test) {
        Logger.d("-----------------writeCharacteristic------------");
        boolean flag = false;
        characteristic.setValue(test);
        flag = mBluetoothGatt.writeCharacteristic(characteristic);
        return flag;
    }

    public BluetoothGattService getSupportedGattServices(UUID uuid) {
        BluetoothGattService mBluetoothGattService;
        if (mBluetoothGatt == null)
            return null;
        mBluetoothGattService = mBluetoothGatt.getService(uuid);
        return mBluetoothGattService;
    }

    /**
     * Retrieves a list of supported GATT services on the connected device. This
     * should be invoked only after {@code BluetoothGatt#discoverServices()}
     * completes successfully.
     *
     * @return A {@code List} of supported services.
     */
    public List<BluetoothGattService> getSupportedGattServices() {
        if (mBluetoothGatt == null)
            return null;
        return mBluetoothGatt.getServices();
    }

    // Utility functions
    public static BluetoothGatt getBtGatt() {
        return mBluetoothGatt;
    }

    public static BluetoothManager getBtManager() {
        return mThis.mBluetoothManager;
    }

    public static BluetoothLeService getInstance() {
        return mThis;
    }

    public void clear() {
        mMsg.clear();
    }

    class MsgTimeOut extends CountDownTimer {
        public MsgTimeOut(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }

        @Override
        public void onFinish() {
            //计时完毕时触发
            Logger.d("-----超时触发---：" + Arrays.toString(MSG_Send));
            writeCharacteristic(mCharacteristic, MSG_Send);
        }
    }

    protected void checkOad() {
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
                    Thread.sleep(2000);
                    mBluetoothLeService.sendMessage(null, (byte) 0x53, (byte) 0x00);
                    Logger.d("write执行了");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}
