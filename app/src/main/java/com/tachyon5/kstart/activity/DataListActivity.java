package com.tachyon5.kstart.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.adapter.MyAdater;
import com.tachyon5.kstart.chart.LineChartActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by guofe on 2017/3/31 0031.
 */
public class DataListActivity extends AppCompatActivity {
    private ListView listView;
    public static ArrayList<HashMap<String, Object>> listItem;
    private SwipeRefreshLayout swlayout;
    public static List<Object> list;
    ViewHolder holder;
    MyAdater adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        initData();
        adapter = new MyAdater(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(DataListActivity.this, LineChartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.data_list);
        swlayout = (SwipeRefreshLayout) findViewById(R.id.swprlayout);
    }

    private void initData() {
        swlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                swlayout.setEnabled(false);
            }
        });
    }

    public final class ViewHolder {
        public TextView list_number, list_name, list_date;
        public LinearLayout ll_list_delete;
    }
}
