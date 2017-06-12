package com.tachyon5.kstart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.activity.AddNewModelActivity;
import com.tachyon5.kstart.adapter.ViewPageAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentModel extends Fragment implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private RelativeLayout rl_model_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_model, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViewPager();
        initView();
        initData();
    }

    private void initData() {
        rl_model_add.setOnClickListener(this);
    }

    private void initView() {
        rl_model_add = (RelativeLayout) getActivity().findViewById(R.id.fragment_model_rl_add_model);
    }

    private void initViewPager() {
        tabLayout = (TabLayout) getActivity().findViewById(R.id.fragment_model_tablayout);
        viewPager = (ViewPager) getActivity().findViewById(R.id.fragment_model_viewPager);
        List<Fragment> data = new ArrayList<>();
        data.add(new FragmentMyModel());
        data.add(new FragmentRemModel());
        ViewPageAdapter pageAdater = new ViewPageAdapter(getActivity().getSupportFragmentManager(), data);
        viewPager.setAdapter(pageAdater);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.getTabAt(0).setText(R.string.my_model);
        tabLayout.getTabAt(1).setText(R.string.recommended_model);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_model_rl_add_model:
                Intent intent = new Intent(getContext(), AddNewModelActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
