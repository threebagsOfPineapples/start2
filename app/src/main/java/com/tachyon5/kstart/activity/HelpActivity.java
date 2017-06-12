package com.tachyon5.kstart.activity;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.adapter.ViewPageAdapter;
import com.tachyon5.kstart.fragment.FragmentHelp1;
import com.tachyon5.kstart.fragment.FragmentHelp2;
import com.tachyon5.kstart.fragment.FragmentHelp3;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {
    private ViewPager viewPage;
    private FragmentHelp1 mFragment1;
    private FragmentHelp2 mFragment2;
    private FragmentHelp3 mFragment3;
    private ImageView btnGo, back_btn;
    private SharedPreferences sp;
    private PagerAdapter mPgAdapter;
    private RadioGroup dotLayout;
    private List<Fragment> mListFragment = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initView();
        viewPage.addOnPageChangeListener(new MyPagerChangeListener());
    }

    private void initView() {
        dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
        viewPage = (ViewPager) findViewById(R.id.viewpager);
        mFragment1 = new FragmentHelp1();
        mFragment2 = new FragmentHelp2();
        mFragment3 = new FragmentHelp3();
        mListFragment.add(mFragment1);
        mListFragment.add(mFragment2);
        mListFragment.add(mFragment3);
        mPgAdapter = new ViewPageAdapter(getSupportFragmentManager(), mListFragment);
        viewPage.setAdapter(mPgAdapter);
        back_btn = (ImageView) findViewById(R.id.title_back_imageButton);
        btnGo = (ImageView) findViewById(R.id.btn_go);
        btnGo.setVisibility(View.GONE);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 结束当前页面，跳转至主页面 否则，进入主页面
                finish();
            }
        });
    }

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {
        public void onPageSelected(int position) {
            // 如果是最后一个页面，就显示按钮
            if (position == mPgAdapter.getCount() - 1) {
                btnGo.setVisibility(View.VISIBLE);
            } else {
                // 如果不是最后一个页面，那么就，隐藏按钮
                btnGo.setVisibility(View.GONE);
            }
        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
        }
    }
}
