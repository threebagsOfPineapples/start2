package com.tachyon5.kstart.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.db.DBManage;
import com.tachyon5.kstart.db.NotificationBean;
import com.tachyon5.kstart.utils.Constant;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NotifyActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private LinearLayout ll_back;
    private SwipeRefreshLayout swlayout;
    public static ArrayList<HashMap<String, Object>> listItem;
    public static List<NotificationBean> list;
    ViewHolder holder;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        initWindow();
        initView();
        listItem = new ArrayList<>();
        list = new ArrayList<>();
        adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        //new GetListTask().execute();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              /*  Intent intent = new Intent(NotifyActivity.this, NoticeItemActivty.class);
                Bundle bundle = new Bundle();
                DBManage dbManage = new DBManage(NotifyActivity.this);
                dbManage.updateNotification(list.get(position).getTime(), "已知");
                adapter.notifyDataSetInvalidated();
                bundle.putString("title", list.get(position).getTitle());
                bundle.putString("time", list.get(position).getTime());
                bundle.putString("content", list.get(position).getDimension());
                intent.putExtras(bundle);
                startActivity(intent);*/
            }
        });
        swlayout.setColorSchemeResources(R.color.pink,
                R.color.colorPrimaryDark
        );
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    getData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

    }

    private class GetListTask extends AsyncTask<Void, Void, HashMap<String, Object>> {
        @Override
        protected HashMap<String, Object> doInBackground(Void... params) {
            try {
                getData();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }
    }

    private void getData() throws JSONException {
        if (Constant.NotifyCount.equals("0")) {
            swlayout.setRefreshing(false);
            DBManage dbManage = new DBManage(NotifyActivity.this);
            list = dbManage.queryNotification();
        } else {
            getNotify();
        }
        Logger.d("------notify记录---------");
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.notice_activty_lv_listview);
        swlayout = (SwipeRefreshLayout) findViewById(R.id.actvity_notice_SwipeRefreshLayout);
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4 全透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);        // (int) alphaValue)
        }
    }

    public final class ViewHolder {
        public TextView title;
        public ImageView iv_new;
    }

    /**
     * 自定义适配器
     *
     * @author xp
     */
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listitem_notice, null);
                holder.title = (TextView) convertView.findViewById(R.id.activity_notice_tv_title);
                holder.iv_new = (ImageView) convertView
                        .findViewById(R.id.activity_notice_iv_new);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.title.setText(list.get(position).getTitle());
            if (list.get(position).getTab().equals("未知")) {
                holder.iv_new.setVisibility(View.VISIBLE);
            } else {
                holder.iv_new.setVisibility(View.GONE);
            }
            return convertView;
        }
    }

    private void getNotify() throws JSONException {
        /*JSONObject json = new JSONObject();
        json.put(Constant.JSON_KEY_SESSIONID, Constant.SESSIONID);
        json.put(Constant.JSON_KEY_OPENID, Constant.OPENID);
        json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_NOTIFY);
        json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
        json.put(Constant.JSON_KEY_DATA, "");
        Logger.d("----json---" + json.toString());
        OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
                .execute(new StringCallback() {
                             @Override
                             public void onError(Call call, Exception e) {
                                 swlayout.setRefreshing(false);
                             }

                             @Override
                             public void onResponse(String s) {
                                 swlayout.setRefreshing(false);
                                 Logger.d("----notify---" + s);
                                 JSONObject json_resp = null;
                                 JSONObject data = null;
                                 if (s != null) {
                                     try {
                                         json_resp = new JSONObject(s);
                                     } catch (Exception e) {

                                         e.printStackTrace();
                                     }
                                     Logger.d("string:" + s + "json:" + json_resp);
                                     try {
                                         if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
                                                 && json_resp.get(Constant.JSON_KEY_DATA) != null) {
                                             Logger.d("解析data数据");
                                             data = new JSONObject(json_resp.getString(Constant.JSON_KEY_DATA));
                                             if (data != null && data.get("count") != null) {
                                                 listItem.clear();
                                                 Constant.NotifyCount = null;
                                                 int i = (int) data.get("count");
                                                 Logger.d("listitem增加数据" + i);

                                                 for (int j = 0; j < i; j++) {
                                                     DBManage dbManage = new DBManage(NotifyActivity.this);
                                                     HashMap<String, Object> map = new HashMap<String, Object>();
                                                     map.put("title", data.get("notify" + j + "_title"));
                                                     map.put("time", data.get("notify" + j + "_time"));
                                                     map.put("type", data.get("notify" + j + "_type"));
                                                     map.put("content", data.get("notify" + j + "_content"));
                                                     listItem.add(map);
                                                 }
                                                 DBManage dbManage = new DBManage(NotifyActivity.this);

                                                 list = dbManage.queryNotification();
                                                 Logger.d("----填充数据----------listitem大小" + listItem.size());
                                                 adapter.notifyDataSetChanged();
                                             } else {
                                                 Logger.d("出错----");
                                             }
                                         } else {
                                             swlayout.setRefreshing(false);
                                             Logger.d("data为null");
                                         }
                                     } catch (Exception e) {
                                         swlayout.setRefreshing(false);
                                         // TODO Auto-generated catch block
                                         e.printStackTrace();
                                     }
                                 }

                             }
                         }
                );*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBManage dbManage = new DBManage(NotifyActivity.this);
        list = dbManage.queryNotification();
    }
}
