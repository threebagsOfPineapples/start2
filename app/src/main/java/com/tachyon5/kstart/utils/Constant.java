package com.tachyon5.kstart.utils;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.ble.BluetoothLeService;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Constant {
    public static int widpx = 0;
    public static int heidpx = 0;
    public static BluetoothLeService mBluetoothLeService = null;
    public static boolean ISCONNECT;
    public static boolean IsClick = false;
    public static byte ID = 0;
    public static byte TimeoutId = 0;
    public static final int SERVICE_DATA_SEND = 0x001;
    public static final int SERVICE_DATA_SEND_FOR_TWO = 0x002;
    public static final int SERVICE_TIMEOUT = 0x003;
    public static final int DATA_SEND = 0x01;
    public static final int SCAN_OVER = 0x02;
    public static final int STRAT_COLLECT = 0x03;
    public static final int CONN_AUTHORIZE = 0x04;
    public static final int OUT_TIME = 0x05;
    public static final int BT_CONNECT = 0x06;
    public static final int BT_DISCONNECTED = 0x07;
    public static final int BT_CONNECTING = 0x08;
    public static final int MilkName = 0x09;
    public static final int BTPAIR = 0x10;
    public static final int TEST_DARK_DETE = 0x101;
    public static final int TEST_DARK_AUTH = 0x102;
    public static final int OAD = 0x11;
    public static final int FW_CHECK = 0x12;
    public static final int CHECKGOOD = 0x13;
    public static final int CALIBRATE_TIME = 0x14;
    public static final int DISCONNECT = 0x15;
    public static final String APP_ID = "wxbcd44698935af441";
    public static final String App_SECRET = "d4624c36b6795d1d99dcf0547af5443d";
    public static String SESSIONID = null;
    public static String OPENID = null;
    public static String IMEI = null;
    public static String WXOPENID = null;
    public static String NotifyCount = null;
    public static String DARKREFID = null;
    public static String MILKRESULT = null;
    public static String RECORDSCOUNT = null;
    public static String LOCATION_PROVICE = "";
    public static String LOCATION_CITY = "";
    public static String LOCATION_DISTRICT = "";
    public static String FW_VERSION;
    public static String resultMilkId = null;
    public static String matchingDegree = null;
    public static String similarity = null;
    public static String milkNames = null;
    public static String recordId = "";
    public static String tt = "我刚刚用了奶粉卫士检测了宝宝奶粉的真假，快来看看吧";
    public static String ps;
    public static String tm;
    public static String byps;
    public static String bypc;
    public static String tmn;
    public static String tmpu;
    public static String rmd;
    public static String cs;
    public static String desc;
    public static String Location;
    public static boolean upDating;
    public static String Device_Sn = "";
    public static String Device_Mac = "";
    public static String Device_Power = "";
    public static String Device_Fw = "";
    public static String CommProtocol = null;
    //6.0动态权限
    public static final int REQUECT_CODE_CAMERA = 1;
    public static final int REQUECT_CODE_SDCARD = 2;
    public static final int REQUECT_CODE_LOACTION = 3;
    public static final int REQUECT_CODE_DIAGL = 4;
    public static final int MILLINFO = 0x100;
    public static int mInit = 0;
    public static BluetoothGattCharacteristic mCharacteristic, mCharacteristicNotify;
    public static BluetoothGattService mnotyGattService;// 三个长得很像，由大到小的对象BluetoothGatt、
    public static BluetoothGattService mOadService = null;
    public static BluetoothGattService mConnControlService = null;

    // 获取第一步的code后，请求以下链接获取access_token
    public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    // 获取用户个人信息
    public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
    // 利用refresh刷新access_token
    public static String RefreshAccess_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
    public static final String HTTPS_URL_HEAD = "https://123.56.229.50/";
    public static final String HTTPS_URL_LOGIN = HTTPS_URL_HEAD + "magispec-1.0/login.php";
    public static final String HTTPS_URL_MSG_CENTRAL = HTTPS_URL_HEAD + "magispec-1.0/msg_central.php";
    public static final String HTTPS_URL_MSG_AppVersion = HTTPS_URL_HEAD + "magispec-1.0/msg_central_nosession.php";
    public static final String JSON_KEY_SESSIONID = "SESSIONID";
    public static final String JSON_KEY_OPENID = "OPENID";
    public static final String JSON_KEY_ACTION = "ACTION";
    public static final String JSON_KEY_TYPE = "TYPE";
    public static final String JSON_KEY_DATA = "DATA";
    public static final String JSON_KEY_RESULT = "RESULT";
    public static final String JSON_KEY_DATA_resultMilkId = "resultMilkId";
    public static final String JSON_KEY_DATA_matchingDegree = "matchingDegree";
    public static final String JSON_KEY_DATA_similarity = "similarity";
    public static final String JSON_KEY_DATA_milksName = "milksName";
    public static final String JSON_KEY_DATA_ifDarkRefValid = "ifDarkRefValid";
    public static boolean ISBATTERY = false;
    public static final short ACTION_CREATE_SESSION = 0x0001;
    public static final short ACTION_DESTROY_SESSION = 0x0002;
    public static final short ACTION_UPDATE_SESSION = 0x0003;
    public static final short ACTION_MILKDEFENDER_GET_USER_INFO = 0x0100;
    public static final short ACTION_MILKDEFENDER_UPLOAD_DARKREF_DATA = 0x0101;
    public static final short ACTION_MILKDEFENDER_GET_MILK_INFO = 0x0102;
    public static final short ACTION_MILKDEFENDER_RECOGNIZE_MILK = 0x0103;
    public static final short ACTION_MILKDEFENDER_GET_RECORDS_COUNT = 0x0104;
    public static final short ACTION_MILKDEFENDER_GET_RECORDS = 0x0105;
    public static final short ACTION_MILKDEFENDER_GET_FW_LATEST_VERSION = 0x0106;
    public static final short ACTION_MILKDEFENDER_GET_APP_LATEST_VERSION = 0x0107;
    public static final short ACTION_MILKDEFENDER_SET_USER_NICKNAME = 0x0108;
    public static final short ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_ROLLING = 0x0109;
    public static final short ACTION_MILKDEFENDER_GET_DISCOVER_COTENT_LIST = 0x010A;
    public static final short ACTION_MILKDEFENDER_SUGGESTION = 0x010B;
    public static final short ACTION_MILKDEFENDER_GET_DAMAP = 0x010C;
    public static final short ACTION_MILKDEFRNDER_NOTIFY_DEVICE_DAMAP_INVALID = 0x010d;
    public static final short ACTION_MILKDEFENDER_BIND_WX = 0x010E;
    public static final short ACTION_MILKDEFENDER_ADD_NEW_MILK = 0x010F;
    public static final short ACTION_MILKDEFENDER_GET_BARCODE_HISTORY = 0x0110;
    public static final short ACTION_MILKDEFENDER_DEL_BARCODE_HISTROY = 0x0111;
    public static final short ACTION_MILKDEFENDER_GET_NOTIFY = 0x0112;
    public static final short ACTION_MILKDEFENDER_UPLOAD_PHOTO = 0x0113;
    public static final short ACTION_MILKDEFENDER_DEL_RECORDS = 0x0114;
    public static final byte MSG_REQ = 0x00;
    public static final byte MSG_RES = 0x01;
    //消息组装
    public static final int MSG_STATE_IDLE = 0;
    public static final int MSG_STATE_RECV_HDR = 1;
    public static final int MSG_STATE_RECV_PAYLOAD = 2;
    public static final int MSG_STATE_PROCESSING = 3;
    public static final byte MSG_MAGISPEC_ID_LSB = (byte) 0xF4;
    public static final byte MSG_MAGISPEC_ID_MSB = 0x52;
    public static final byte MSG_MAGISPEC_TYPE = 0x00;
    public static final byte MSG_CMD_COLLECT = 0x09;
    public static final byte MSG_CMD_SPECTRUM_NOTIFY = (byte) 0xa0;
    public static final byte MSG_TM_GET_BATTERY_VAL = (byte) 0x50;
    public static final byte MSG_CMD_FONT = (byte) 0x51;
    public static final byte MSG_CMD_FONT_NAME = (byte) 0x52;
    public static final byte MSG_CMD_CONN_AUTHORIZE = (byte) 0x53;
    public static final byte MSG_CMD_DARKEVN_CALIB = (byte) 0x54;
    public static final byte MSG_CMD_REF1_CALIB = (byte) 0x55;
    public static final byte MSG_CMD_SET_DARK_SPECTRUM_DATA = (byte) 0x56;
    public static final byte MSG_CMD_SET_REF_SPECTRUM_DATA = (byte) 0x57;
    public static final byte MSG_CMD_PAIR = (byte) 0x58;
    //获取固件版本
    public static final byte MSG_CMD_GET_FW_VER = (byte) 0x59;
    public static final byte MSG_CMD_UPGRADE = (byte) 0x60;
    //获取上次校准的时间
    public static final byte MSG_GET_CALIBRATE_TIME = (byte) 0x62;
    //设置自动校准的时间
    public static final byte MSG_SET_AUTO_CALIBRATE_TIME = (byte) 0x63;
    //获取波长步进
    public static final byte MSG_GET_WAVE_STEP = (byte) 0x64;
    //设置起始波长
    public static final byte MSG_SET_WAVE_START = (byte) 0x65;
    //8获取波长映射数据 (0x66)-MS_TM_GET_WAVE_MAP
    public static final byte MSG_GET_WAVE_MAP = (byte) 0x66;
    //设置测试目标
    public static final byte MS_TM_SET_TEST_OBJ = (byte) 0x67;
    //测试字体专用
    public static int MilkCount = 1;
    //记录条目
    public static int ResultCountInt;
    //记录
    public static ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
    public static HashMap<String, Object> map = new HashMap<String, Object>();
    public static ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    //发现
    public static String[] imageDescriptions;
    public static String[] imageUrls;
    public static String[] webUrls;
    public static ArrayList<String> labels;
    public static ArrayList<HashMap<String, Object>> listItem_discovery_list = new ArrayList<HashMap<String, Object>>();
    public static HashMap<String, Object> map_discovery = new HashMap<String, Object>();
    public static ArrayList<HashMap<String, Object>> list_discovery = new ArrayList<HashMap<String, Object>>();
    //记录测量次数
    public static int MeasuringTimes = 0;
    //记录 最优测试
    public static int MatchingDegree = 0;
    //dark ref 是否合格
    public static String ifDarkRefValid;
    //dark ref
    public static boolean IsAppNew = false;
    //是否是在检测页面测试Dark
    public static boolean IsTesting = false;
    //是否在个人页面测试
    public static boolean IsAuthTesting = false;
    //服务是否onbind
    public static boolean IsBind = false;
    //是否在首页
    public static boolean IsHomePage = false;
    //分享界面

    public static String shareUrl = "http://123.56.229.50/magispec-1.0/sharing.php?TT=tt&PS=ps&TM=tm&BYPS=byps&BYPC=bypc&TMN=tmn&TMPU=tmpu&RMD=rmd&CS=cs";
    public static String shareUrlHead = "http://eincloud.vicp.io//magispec-1.0/sharing.php?";
    public static String shareUrlBranch = "TT=tt&PS=pos&TM=tim&BYPS=byps&BYPC=bypc&TMN=tmn&TMPU=tmpu&RMD=rmd";
    //蓝牙部分
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    //这样写只是赋了一个常量值
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";
    //the descriptor of battery characteristic(battery service)
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    public static String BLE_Service = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String BLE_READ_WRITE = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String BLE_NOTIFY = "0000fff6-0000-1000-8000-00805f9b34fb";
    public static final String OAD_SERVICE = "f000ffc0-0000-1000-8000-00805f9b34fb";
    public static final String OAD_WRITE_READ = "0000ffc2-0000-1000-8000-00805f9b34fb";
    public static final UUID OAD_SERVICE_UUID = UUID.fromString("f000ffc0-0451-4000-b000-000000000000");
    public static final UUID OAD_CHARACTER = UUID.fromString(OAD_WRITE_READ);
    public static final UUID BLE_SERVICE_UUID = UUID.fromString(BLE_Service);
    public static final UUID BLE_READ_WRITE_UUID = UUID.fromString(BLE_READ_WRITE);
    public static final UUID BLE_NOTIFY_UUID = UUID.fromString(BLE_NOTIFY);

    static {
        // Sample Services.给自己用到的服务命名
        attributes.put("0000fff0-0000-1000-8000-00805f9b34fb", "奶粉卫士");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "设备信息");
        attributes.put(OAD_SERVICE, "OAD");
        //Sample Characteristics.给自己用到的特征值命名
        attributes.put(BLE_READ_WRITE, "READ_WRITE");
        attributes.put("00002a37-0000-1000-8000-00805f9b34fb", "YJ Name");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }

    /**
     * 设置监听
     *
     * @return intentFilter
     */
    public static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_TO_DETECTION);
        intentFilter.addAction(BluetoothLeService.ACTION_PAIR);
        intentFilter.addAction(BluetoothLeService.ACTION_FONT);
        intentFilter.addAction(BluetoothLeService.ACTION_FONT_Name);
        intentFilter.addAction(BluetoothLeService.ACTION_CONN_AUTHORIZE);
        intentFilter.addAction(BluetoothLeService.ACTION_CALI_DARK_FINISHED);
        intentFilter.addAction(BluetoothLeService.ACTION_CALI_REF1_FINISHED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BluetoothLeService.ACTION_COLLECT);
        intentFilter.addAction(BluetoothLeService.ACTION_IMAGB);
        intentFilter.addAction(BluetoothLeService.ACTION_GET_CALIBRATE_TIME);
        intentFilter.addAction(BluetoothLeService.ACTION_SET_AUTO_CALIBRATE_TIME);
        intentFilter.addAction(BluetoothLeService.ACTION_GET_FW_VER);
        intentFilter.addAction(BluetoothLeService.ACTION_SET_DARK_SPECTRUM_DATA);
        intentFilter.addAction(BluetoothLeService.ACTION_SET_REF_SPECTRUM_DATA);
        intentFilter.addAction(BluetoothLeService.ACTION_UPGRADE);
        intentFilter.addAction(BluetoothLeService.ACTION_SET_TEST_OBJ);
        intentFilter.addAction(BluetoothLeService.ACTION_ERROR_INFO);
        intentFilter.addAction(BluetoothLeService.ACTION_GET_WAVE_MAP);
        intentFilter.addAction(BluetoothLeService.ACTION_SET_WAVE_MAP);
        intentFilter.addAction(BluetoothLeService.ACTION_GET_BATTERY_VAL);
        return intentFilter;
    }


}
