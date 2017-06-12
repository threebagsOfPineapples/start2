package com.tachyon5.kstart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tachyon5.kstart.R;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentMyModel extends Fragment implements View.OnClickListener {
    private ListView listView;
    private SwipeRefreshLayout swp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_my_model, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        listView = (ListView) getActivity().findViewById(R.id.fragment_mymodel_listview);
        swp = (SwipeRefreshLayout) getActivity().findViewById(R.id.fragment_mymodel_swip_refresh);
    }

    private void initData() {
        listView.setOnClickListener(this);
        swp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
    }
}
