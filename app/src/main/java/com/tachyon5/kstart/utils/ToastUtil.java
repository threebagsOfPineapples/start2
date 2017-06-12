package com.tachyon5.kstart.utils;

import android.content.Context;
import android.widget.Toast;

import com.tachyon5.kstart.view.XpToast;


public class ToastUtil {
    private static String oldMsg;
    protected static Toast toast = null;
    private static long oneTime = 0;
    private static long twoTime = 0;

    @SuppressWarnings("unused")
    public static void showToast(Context context, String s) {
        XpToast toast = new XpToast(context, s);
        if (toast == null) {
            toast.setText(s);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > Toast.LENGTH_SHORT) {
                    toast.setText(s);
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
