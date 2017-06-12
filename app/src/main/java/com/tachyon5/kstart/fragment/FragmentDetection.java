package com.tachyon5.kstart.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.view.AVLoadingIndicatorView;
import com.tachyon5.kstart.view.LineView;
import com.tachyon5.kstart.view.RippleLayout;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentDetection extends Fragment implements View.OnClickListener {
    private LinearLayout fragmentDeteLlTest;
    private RippleLayout rippleLayout;
    private ImageView ivRip;
    private ImageView ivGreenRight;
    private LineView lv;
    private AVLoadingIndicatorView fragmentDeteCv;
    private TextView fragmentDeteTvModeName;
    private TextView fragmentDeteTvStartDete;
    private LinearLayout fragmentDeteLlResult;
    private TextView fragmentDeteTvTestAgain;
    private TextView fragmentDeteTvStopDete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_detection, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        fragmentDeteTvStartDete.setOnClickListener(this);
        fragmentDeteTvTestAgain.setOnClickListener(this);
        fragmentDeteTvStopDete.setOnClickListener(this);
    }

    private void initView() {
        fragmentDeteLlTest = (LinearLayout) getActivity().findViewById(R.id.fragment_dete_ll_test);
        rippleLayout = (RippleLayout) getActivity().findViewById(R.id.ripple_layout);
        ivRip = (ImageView) getActivity().findViewById(R.id.iv_rip);
        ivGreenRight = (ImageView) getActivity().findViewById(R.id.iv_green_right);
        lv = (LineView) getActivity().findViewById(R.id.lv);
        fragmentDeteCv = (AVLoadingIndicatorView) getActivity().findViewById(R.id.fragment_dete_cv);
        fragmentDeteTvModeName = (TextView) getActivity().findViewById(R.id.fragment_dete_tv_mode_name);
        fragmentDeteTvStartDete = (TextView) getActivity().findViewById(R.id.fragment_dete_tv_start_dete);
        fragmentDeteLlResult = (LinearLayout) getActivity().findViewById(R.id.fragment_dete_ll_result);
        fragmentDeteTvTestAgain = (TextView) getActivity().findViewById(R.id.fragment_dete_tv_testAgain);
        fragmentDeteTvStopDete = (TextView) getActivity().findViewById(R.id.fragment_dete_tv_stopDete);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_dete_tv_start_dete:
                //开始检测
                break;
            case R.id.fragment_dete_tv_stopDete:
                //停止检测
                break;
            case R.id.fragment_dete_tv_testAgain:
                //再次检测
                break;
            default:

        }
    }
}

