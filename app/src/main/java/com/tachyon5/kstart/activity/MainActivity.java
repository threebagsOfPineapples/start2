package com.tachyon5.kstart.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.fragment.FragmentDetection;
import com.tachyon5.kstart.fragment.FragmentHome;
import com.tachyon5.kstart.fragment.FragmentModel;
import com.tachyon5.kstart.fragment.FragmentRecord;
import com.tachyon5.kstart.fragment.FragmentSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

import okhttp3.Call;
import okhttp3.Response;

import static com.tachyon5.kstart.application.BaseApplication.a;

public class MainActivity extends MPermissionsActivity implements View.OnClickListener {
    private FragmentSet fragmentSet;
    private FragmentHome fragmentHome;
    private FragmentModel fragmentModel;
    private FragmentRecord fragmentRecord;
    private FragmentDetection fragmentDetection;
    private LinearLayout tab_ll_home, tab_ll_model, tab_ll_detection, tab_ll_record, tab_ll_set;
    Locale locale;
    private TextView tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        // supportInvalidateOptionsMenu();
        initView();
        initData();
        //默认加载首页
        initFragment(0);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!a) {
                    a = true;
                    locale = new Locale("en");
                } else {
                    a = false;
                    locale = new Locale("ch");
                }
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity,all of the other activities on top of it will be closed and this Intent will be delivered to the (now on top) old activity as a new Intent.
                startActivity(intent);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.main_activity_tv_title);
        tab_ll_home = (LinearLayout) findViewById(R.id.ll_home);
        tab_ll_model = (LinearLayout) findViewById(R.id.ll_model);
        tab_ll_detection = (LinearLayout) findViewById(R.id.ll_detection);
        tab_ll_record = (LinearLayout) findViewById(R.id.ll_record);
        tab_ll_set = (LinearLayout) findViewById(R.id.ll_set);
    }

    private void initData() {
        tab_ll_home.setOnClickListener(this);
        tab_ll_model.setOnClickListener(this);
        tab_ll_detection.setOnClickListener(this);
        tab_ll_record.setOnClickListener(this);
        tab_ll_set.setOnClickListener(this);
    }

    public void createSession(final String openId, String type, String logintype, String approle, String protocol) {
        JSONObject json = new JSONObject();
        JSONObject json_resp = null;
        try {
            json.put("SESSIONID", null);
            json.put("OPENID", openId);
            json.put("ACTION", (short) 0x0001);
            json.put("TYPE", 0x00);
            JSONObject data = new JSONObject();
            data.put("apptype", type);
            data.put("approle", approle);
            data.put("logintype", logintype);
            data.put("username", "usermagispec");// need to hide ...???
            data.put("password", "11qqaazZ");
            data.put("protocol", protocol);
            json.put("DATA", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Logger.d("json" + json);
        OkGo.post("https://123.56.229.50/" + "magispec-1.0/login.php")
                .upJson(json.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_home:
                restartAll();
                enableAll();
                initFragment(0);
                break;
            case R.id.ll_model:
                restartAll();
                enableAll();
                initFragment(1);
                break;
            case R.id.ll_detection:
                restartAll();
                enableAll();
                initFragment(2);
                break;
            case R.id.ll_record:
                restartAll();
                enableAll();
                initFragment(3);
                break;
            case R.id.ll_set:
                restartAll();
                enableAll();
                initFragment(4);
                break;
            default:
                break;
        }
    }

    // 初始化并设置当前Fragment
    private void initFragment(int i) {
        // 得到管理器
        FragmentManager fragmentManager = getSupportFragmentManager();
        // 开启事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (fragmentHome == null) {
                    fragmentHome = new FragmentHome();
                    transaction.add(R.id.frame_content, fragmentHome);
                } else {
                    transaction.show(fragmentHome);
                }
                transaction.commitAllowingStateLoss();
                // 改变选中状态
                tv_title.setText(R.string.app_name);
                tab_ll_home.setSelected(true);
                tab_ll_home.setEnabled(false);
                break;
            case 1:
                if (fragmentModel == null) {
                    fragmentModel = new FragmentModel();
                    transaction.add(R.id.frame_content, fragmentModel);
                } else {
                    transaction.show(fragmentModel);
                }
                tv_title.setText(R.string.tab_model);
                transaction.commitAllowingStateLoss();
                // 改变选中状态
                tab_ll_model.setSelected(true);
                tab_ll_model.setEnabled(false);
                break;
            case 2:
                if (fragmentDetection == null) {
                    fragmentDetection = new FragmentDetection();
                    transaction.add(R.id.frame_content, fragmentDetection);
                } else {
                    transaction.show(fragmentDetection);
                }
                transaction.commitAllowingStateLoss();
                // 改变选中状态
                tv_title.setText(R.string.tab_detection);
                tab_ll_detection.setSelected(true);
                tab_ll_detection.setEnabled(false);
                break;
            case 3:
                if (fragmentRecord == null) {
                    fragmentRecord = new FragmentRecord();
                    transaction.add(R.id.frame_content, fragmentRecord);
                } else {
                    transaction.show(fragmentRecord);
                }
                transaction.commitAllowingStateLoss();
                // 改变选中状态
                tv_title.setText(R.string.tab_record);
                tab_ll_record.setSelected(true);
                tab_ll_record.setEnabled(false);
                break;
            case 4:
                if (fragmentSet == null) {
                    fragmentSet = new FragmentSet();
                    transaction.add(R.id.frame_content, fragmentSet);
                } else {
                    transaction.show(fragmentSet);
                }
                tv_title.setText(R.string.tab_set);
                transaction.commitAllowingStateLoss();
                // 改变选中状态
                tab_ll_set.setSelected(true);
                tab_ll_set.setEnabled(false);
                break;
            default:
                break;
        }
    }

    //tab按钮全部不被选
    public void restartAll() {
        tab_ll_home.setSelected(false);
        tab_ll_model.setSelected(false);
        tab_ll_detection.setSelected(false);
        tab_ll_record.setSelected(false);
        tab_ll_set.setSelected(false);
    }

    //tab按钮全部可用
    public void enableAll() {
        tab_ll_home.setEnabled(true);
        tab_ll_model.setEnabled(true);
        tab_ll_detection.setEnabled(true);
        tab_ll_record.setEnabled(true);
        tab_ll_set.setEnabled(true);
    }

    // 隐藏Fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (fragmentHome != null) {
            transaction.hide(fragmentHome);
        }
        if (fragmentModel != null) {
            transaction.hide(fragmentModel);
        }
        if (fragmentRecord != null) {
            transaction.hide(fragmentRecord);
        }
        if (fragmentSet != null) {
            transaction.hide(fragmentSet);
        }
        if (fragmentDetection != null) {
            transaction.hide(fragmentDetection);
        }
    }
}
