package com.tachyon5.kstart.ble;


import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.activity.MainActivity;
import com.tachyon5.kstart.utils.DigitalTrans;

import java.util.Arrays;

public class MsgDataProcessing {
    BTMsg mMsg;
    int choose;
    int[] data;
    String fwVersion;
    int[] Sample;
    int[] sample1, sample2, sample3, sample4, sample5;

    public void putMsg(BTMsg mMsg) {
        this.mMsg = mMsg;
    }

    public int[] dataProcessing() {
        Logger.d("+++" + mMsg.getmCmd() + "+++");
        switch (mMsg.getmCmd()) {
            case BTMsg.MSG_CMD_SPECTRUM_NOTIFY: {
                int sampleCount = mMsg.getmPayloadLen() / 2;
                byte[] payload = mMsg.getmPayload();
                int[] samples = new int[sampleCount];
                for (int i = 0; i < sampleCount; i++) {
                    samples[i] = payload[i * 2] & 0xff;
                    samples[i] |= (payload[i * 2 + 1] & 0xff) << 8;
                }
                Logger.d("sample+++++++" + Arrays.toString(samples));
                // MainActivity.Sample = samples;
                Logger.d("---得到Sample");
                BluetoothLeService.OK = true;
                Logger.d(BluetoothLeService.OK);
            }
            break;
            case BTMsg.MSG_CMD_FONT:
                Logger.d("MSG_CMD_FONT-----");
                break;
            case BTMsg.MSG_CMD_DARKEVN_CALIB: {
                int darkCount = (mMsg.getmPayloadLen() - 4) / 2;
                byte[] payload = mMsg.getmPayload();
                int[] darks = new int[darkCount];
                //  MainActivity.getcalibrateDark = payload;
                for (int i = 0; i < darkCount; i++) {
                    darks[i] = payload[i * 2 + 2] & 0xff;
                    darks[i] |= (payload[i * 2 + 3] & 0xff) << 8;
                    Logger.d("drak+++++++" + darks[i] + "---" + i);
                }
                // MainActivity.Dark = darks;
            }
            break;
            case BTMsg.MSG_CMD_REF1_CALIB: {
                int ref1Count = (mMsg.getmPayloadLen() - 4) / 2;
                byte[] payload = mMsg.getmPayload();
                //  MainActivity.getcalibrateRef = payload;
                int[] ref1 = new int[ref1Count];
                for (int i = 0; i < ref1Count; i++) {
                    ref1[i] = payload[i * 2 + 2] & 0xff;
                    ref1[i] |= (payload[i * 2 + 3] & 0xff) << 8;

                }
                Logger.d("ref1+++++++" + Arrays.toString(ref1));
                //  MainActivity.HalfRef = ref1;
            }
            break;
            case BTMsg.MSG_CMD_GET_FW_VER:
                String temp = "";
                Logger.d("MSG_CMD_GET_FW_VER" + "ssssss");
                byte[] payload = mMsg.getmPayload();
                for (int i = payload.length - 1; i > 0; i--) {
                    temp += payload[i] + ".";
                }
                temp += payload[0];
                //   Constant.FW_VERSION = temp;
                break;
            case BTMsg.MSG_GET_CALIBRATE_TIME:
                Logger.d("MSG_CMD_GET_CALIBRATE_TIME-----");
                int Count = (mMsg.getmPayloadLen()) / 4;
                byte[] payload1 = mMsg.getmPayload();
                Logger.d("payload第一个值：" + payload1[0] + "--" + DigitalTrans.algorismToHEXString(payload1[0]));
                if (payload1[0] == 0) {
                    Logger.d("开始读取time-----");
                    int[] time = new int[Count];
                    for (int i = 0; i < Count; i++) {
                        time[i] = payload1[i * 2 + 1] & 0xff;
                        time[i] |= (payload1[i * 2 + 2] & 0xff) << 8;
                        time[i] |= (payload1[i * 2 + 3] & 0xff) << 16;
                        time[i] |= (payload1[i * 2 + 4] & 0xff) << 24;
                        Logger.d("time+++++++" + time[i] + "---" + i);
                    }
                    //   MainActivity.Time = time;
                    Logger.d("开始读取time-----" + time[0]);
                } else {
                    Logger.d("读取time错误");
                    int time[] = {0x01};
                    // MainActivity.Time = time;
                }
                break;
            case BTMsg.MSG_GET_WAVE_MAP:
                Logger.d("得到Map type：" + mMsg.getType() + "长度：" + mMsg.getmPayloadLen());
                int sampleCount = mMsg.getmPayloadLen() / 2;
                byte[] mapPayload = mMsg.getmPayload();
                short[] map = new short[sampleCount];
                for (int i = 0; i < sampleCount; i++) {
                    map[i] = (short) (mapPayload[i * 2] & 0xff);
                    map[i] |= (mapPayload[i * 2 + 1] & 0xff) << 8;
                }
                Logger.d("map+++++++" + Arrays.toString(map));
                //   MainActivity.Map = map;
                Logger.d("---得到map");
                break;
            case BTMsg.MSG_CMD_GET_BATTERY_VAL: {
                Logger.d("测试=========================");
                byte[] BATTERY_VAL = mMsg.getmPayload();
                for (int i = 0; i < BATTERY_VAL.length; i++) {
                    Logger.d("电量的值" + BATTERY_VAL[i]);
                }
                int a;
                a = (BATTERY_VAL[1]) & 0xff;
                a |= (BATTERY_VAL[2] & 0xff) << 8;
              /*  if (a > 1650) {
                    Constant.Device_Power = 100 + "";
                } else if (a < 1650 && a >= 1598) {
                    Constant.Device_Power = 75 + (a - 1598) * 25 / (1650 - 1598) + "";

                } else if (a < 1598 && a >= 1572) {
                    Constant.Device_Power = 50 + (a - 1572) * 25 / (1598 - 1572) + "";

                } else if (a < 1572 && a >= 1546) {
                    Constant.Device_Power = 25 + (a - 1546) * 25 / (1598 - 1546) + "";

                } else if (a < 1546 && a >= 1517) {
                    Constant.Device_Power = (a - 1517) * 25 / (1546 - 1517) + "";
                } else if (a < 1517) {
                    Constant.Device_Power = "电量低";
                }
                Logger.d("电量值为" + Constant.Device_Power + a);*/
            }
            break;
            default:
                data = null;
                break;
        }
        return data;
    }

}
