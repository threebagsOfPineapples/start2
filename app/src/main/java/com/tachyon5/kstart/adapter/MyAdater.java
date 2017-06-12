package com.tachyon5.kstart.adapter;

/**
 * Created by guofe on 2017/4/6 0006.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;
import com.tachyon5.kstart.R;
import com.tachyon5.kstart.application.BaseApplication;
import com.tachyon5.kstart.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义适配器
 *
 * @author xp
 */
public class MyAdater extends BaseSwipeAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    public MyAdater(Context mContext) {
        this.mContext = mContext;
        this.inflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout_datalist;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return LayoutInflater.from(mContext).inflate(R.layout.swipe_datalist, null);
    }

    @Override
    public void fillValues(int position, View convertView) {
        TextView list_number = (TextView) convertView.findViewById(R.id.datalist_number);
        TextView list_name = (TextView) convertView.findViewById(R.id.datalist_name);
        TextView list_date = (TextView) convertView.findViewById(R.id.datalist_date);
        LinearLayout ll_list_delete = (LinearLayout) convertView.findViewById(R.id.bottom_wrapper);
        ll_list_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtil.showToast(BaseApplication.getAppContext(), "删除");
            }
        });
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    /*    private LayoutInflater mInflater;
        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return 3;
        }
        @Override
        public Object getItem(int arg0) {
            return null;
        }
        @Override
        public long getItemId(int arg0) {
            return 0;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.listview_datalist, null);
                holder.list_number = (TextView) convertView.findViewById(R.id.datalist_number);
                holder.list_name = (TextView) convertView.findViewById(R.id.datalist_name);
                holder.list_date= (TextView) convertView.findViewById(R.id.datalist_date);
                holder.ll_list_delete= (LinearLayout) convertView.findViewById(R.id.ll_delete);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.list_number.setText("1");
            holder.list_name.setText("测试测试 测试测试");
            holder.list_date.setText("2017/04/06   10:38");
            return convertView;
        }*/
}
