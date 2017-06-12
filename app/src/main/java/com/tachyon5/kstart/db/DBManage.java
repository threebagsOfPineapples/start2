package com.tachyon5.kstart.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManage {
    private MySQLiteOpenHelper helper;
    private SQLiteDatabase db;

    //通知   表
    private static final String TABLE_NAME = "notification";

    public DBManage(Context context) {

        helper = new MySQLiteOpenHelper(context);
    }

    /*
     * 往数据表中  增加数据 ，
     * number 通知 标号
     * title 通知标题
     * time 时间
     * dimension 内容
     * tab 已读或 未读标记
     * return  void
     */
    public void addNotification(String number, String title, String time, String dimension, String tab) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("number", number);
        values.put("title", title);
        values.put("time", time);
        values.put("dimension", dimension);
        values.put("tab", tab);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    /*
     *  查询 所有通知 信息
     *  return :  list(通知)
     */
    public List<NotificationBean> queryNotification() {
        db = helper.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        List<NotificationBean> listbean = new ArrayList<>();
        while (cursor.moveToNext()) {
            NotificationBean notificationBean = new NotificationBean();
            notificationBean.setNumber(cursor.getString(cursor.getColumnIndex("number")));
            notificationBean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            notificationBean.setTime(cursor.getString(cursor.getColumnIndex("time")));
            notificationBean.setDimension(cursor.getString(cursor.getColumnIndex("dimension")));
            notificationBean.setTab(cursor.getString(cursor.getColumnIndex("tab")));
            listbean.add(notificationBean);
        }
        cursor.close();
        db.close();
        return listbean;
    }

    /*
     * 修改通知 标记
     * number 通知 标号
     * tab  要修改的 标记
     * return : void
     */
    public void updateNotification(String number, String tab) {
        db = helper.getWritableDatabase();
        ContentValues valus = new ContentValues();
        valus.put("tab", tab);
        db.update(TABLE_NAME, valus, "time= ?", new String[]{number});
        db.close();
    }

    /*
     * 查询 通知状态 是否有未读 消息
     * return： true  有未读消息   false  无 未读消息
     */
    public boolean queryNotificationState() {
        db = helper.getWritableDatabase();
        boolean bool = false;
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String tab = cursor.getString(cursor.getColumnIndex("tab"));
            if (tab.equals("已读")) {
                bool = true;
            }
        }
        cursor.close();
        db.close();
        return bool;
    }

}
