package com.tachyon5.kstart.application;

import android.app.Application;
import android.util.DisplayMetrics;

import com.lzy.okgo.OkGo;
import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.utils.Constant;

import okio.Buffer;

/**
 * Created by xp on 2017/1/13 0013.
 */
public class BaseApplication extends Application {
    public static boolean a = false;
    private static BaseApplication context;
    private String CER = "-----BEGIN CERTIFICATE-----\n" +
            "MIICkTCCAfoCCQCOVljopdR53DANBgkqhkiG9w0BAQUFADCBjDELMAkGA1UEBhMCODYxCzAJBgNV\n" +
            "BAgMAkJKMQswCQYDVQQHDAJCSjERMA8GA1UECgwITWFnaXNwZWMxDDAKBgNVBAsMA0RldjEWMBQG\n" +
            "A1UEAwwNMTIzLjU2LjIyOS41MDEqMCgGCSqGSIb3DQEJARYbbWluZ3hpbmcuemhhbmdAbWFnaXNw\n" +
            "ZWMuY29tMB4XDTE2MDMxMTA4MzYxN1oXDTE3MDMxMTA4MzYxN1owgYwxCzAJBgNVBAYTAjg2MQsw\n" +
            "CQYDVQQIDAJCSjELMAkGA1UEBwwCQkoxETAPBgNVBAoMCE1hZ2lzcGVjMQwwCgYDVQQLDANEZXYx\n" +
            "FjAUBgNVBAMMDTEyMy41Ni4yMjkuNTAxKjAoBgkqhkiG9w0BCQEWG21pbmd4aW5nLnpoYW5nQG1h\n" +
            "Z2lzcGVjLmNvbTCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEA4ZtLhVDDVqXqBijQgpvfupv0\n" +
            "yqy9Vu9QYR2bJ/PfBpfdlxeyUnt7dLEDT+YsSPwEBydWvvTjtDPvDq+lzci1HK9m0Q453Uo1w8SN\n" +
            "KX9bDyxhf2Hdkt3udvuvy8x7qVyBGlHs2aLs40CFaZW9cX1s9DodJtPJQy10vXkl8LoKqt8CAwEA\n" +
            "ATANBgkqhkiG9w0BAQUFAAOBgQCzYbmZQTZfTdoOMj9kQyxa+MDBC9tJWo2VLSwuyxucT7xBQ8CQ\n" +
            "iBswDX7zwi+eZ9rSS/I1bXHcuseDMrP5koUk2NiunxU8Izz6L1RCrvXlqxm0M d1M6leY8c70W0wf\n" +
            "wRY2AAmWAEBXRCCBXUjbBVOZpDXTu5odnOYbTMjI+UYeKg==\n" +
            "-----END CERTIFICATE-----";

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        OkGo.getInstance().setCertificates(new Buffer()
                .writeUtf8(CER)
                .inputStream());
        Logger.init("XP")            // default tag : PRETTYLOGGER or use just init()
                .methodCount(0)
                .hideThreadInfo();   // default it is shown
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        Constant.widpx = dm.widthPixels;
        Constant.heidpx = dm.heightPixels;
        Logger.e(dm.heightPixels + "=======宽：" + dm.widthPixels);
    }

    public synchronized static BaseApplication getInstance() {
        return context;
    }

    public static BaseApplication getAppContext() {
        return context;
    }
}


