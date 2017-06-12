package com.tachyon5.kstart.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.activity.AddNewModelActivity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentHome extends Fragment implements View.OnClickListener {
    private TextView tv_model_sum, tv_today_dete, tv_dete_times, tv_dete_sum, tv_rem_model, tv_add_model;
    private LinearLayout ll_today_collect, ll_total_collect, ll_collect_times;
    private ListView lv_model;
    private ArrayList<HashMap<String, Object>> listItem_model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void initView() {
        tv_model_sum = (TextView) getActivity().findViewById(R.id.fragment_home_tv_model_sum);
        tv_today_dete = (TextView) getActivity().findViewById(R.id.fragment_home_tv_today_collect_sum);
        tv_dete_times = (TextView) getActivity().findViewById(R.id.fragment_home_tv_total_collect_times);
        tv_dete_sum = (TextView) getActivity().findViewById(R.id.fragment_home_tv_total_collect_sum);
        tv_rem_model = (TextView) getActivity().findViewById(R.id.fragment_home_tv_recommed_model);
        tv_add_model = (TextView) getActivity().findViewById(R.id.fragment_home_tv_add_model);
        ll_today_collect = (LinearLayout) getActivity().findViewById(R.id.fragment_home_ll_today_collect);
        ll_total_collect = (LinearLayout) getActivity().findViewById(R.id.fragment_home_ll_total_collect);
        ll_collect_times = (LinearLayout) getActivity().findViewById(R.id.fragment_home_ll_collect_times);
        lv_model = (ListView) getActivity().findViewById(R.id.fragment_home_lv_model);
    }

    private void initData() {
        ll_collect_times.setOnClickListener(this);
        ll_total_collect.setOnClickListener(this);
        ll_today_collect.setOnClickListener(this);
        tv_add_model.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        listItem_model = new ArrayList<HashMap<String, Object>>();
        HashMap hashMap = new HashMap();
        for (int i = 0; i < 4; i++) {
            hashMap.put("model_desc", "支持100余种奶粉品牌");
            hashMap.put("model_name", "奶粉卫士");
            listItem_model.add(hashMap);
        }
        MyAdapter myAdapter = new MyAdapter(getContext());
        lv_model.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_home_ll_today_collect:
                break;
            case R.id.fragment_home_iv_total_collect:
                break;
            case R.id.fragment_home_ll_collect_times:
                break;
            case R.id.fragment_home_tv_add_model:
                Intent intent = new Intent(getContext(), AddNewModelActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public class ViewHolder {
        public TextView model_name;
        public TextView model_desc;
    }

    //自定义适配器
    public class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return listItem_model.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup viewGroup) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_model_layout, null);
                viewHolder.model_name = (TextView) convertView.findViewById(R.id.fragment_home_lv_tv_model_name);
                viewHolder.model_desc = (TextView) convertView.findViewById(R.id.fragment_home_lv_tv_model_desc);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.model_desc.setText((String) listItem_model.get(position).get("model_desc"));
            viewHolder.model_name.setText((String) listItem_model.get(position).get("model_name"));
            return convertView;
        }
    }
}
