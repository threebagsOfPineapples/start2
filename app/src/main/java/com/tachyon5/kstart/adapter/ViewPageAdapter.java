package com.tachyon5.kstart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guofe on 2017/1/17 0017.
 */
public class ViewPageAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<Fragment>();

    public ViewPageAdapter(FragmentManager fragmentManager,
                           List<Fragment> arrayList) {
        super(fragmentManager);
        this.fragmentList = arrayList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
