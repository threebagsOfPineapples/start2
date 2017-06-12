package com.tachyon5.kstart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tachyon5.kstart.R;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by guofe on 2017/4/7 0007.
 * 模型相关的listview adapter
 */
public class MyListViewAdater extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, Object>> listItem;
    private LayoutInflater mInflater;

    public MyListViewAdater(Context mContext, ArrayList listItem) {
        this.mContext = mContext;
        this.listItem = listItem;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return listItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listview_model_layout, null);
            // viewHolder.milkcode = (TextView) convertView.findViewById(R.id.activty_scan_record_tv_milkscancode);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    public class ViewHolder {
        public TextView milkname;
        public TextView milkcode;
        public TextView date;
        public ImageView iv_delete;
        public ImageView iv_milk;
    }
}
