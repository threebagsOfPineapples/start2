package com.tachyon5.kstart.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tachyon5.kstart.R;
import com.tachyon5.kstart.activity.AboutActivity;
import com.tachyon5.kstart.activity.FeedBackActivity;
import com.tachyon5.kstart.activity.HelpActivity;
import com.tachyon5.kstart.activity.MyDevice;
import com.tachyon5.kstart.activity.NotifyActivity;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentSet extends Fragment implements View.OnClickListener {
    private RelativeLayout rl_my_device, rl_device_calibration, rl_fw_version, rl_notify, rl_help,
            rl_feedback, rl_about;
    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_set, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        rl_my_device = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_myDevice);
        rl_device_calibration = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_device_calibration);
        rl_fw_version = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_fw_version);
        rl_notify = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_notice);
        rl_help = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_help);
        rl_feedback = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_feedBack);
        rl_about = (RelativeLayout) getActivity().findViewById(R.id.fragment_set_RL_about);
    }

    private void initData() {
        rl_my_device.setOnClickListener(this);
        rl_device_calibration.setOnClickListener(this);
        rl_fw_version.setOnClickListener(this);
        rl_notify.setOnClickListener(this);
        rl_help.setOnClickListener(this);
        rl_feedback.setOnClickListener(this);
        rl_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_set_RL_myDevice:
                intent = new Intent();
                intent.setClass(getContext(), MyDevice.class);
                startActivity(intent);
                break;
            case R.id.fragment_set_RL_device_calibration:

                break;
            case R.id.fragment_set_RL_fw_version:

                break;
            case R.id.fragment_set_RL_notice:
                intent = new Intent(getContext(), NotifyActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_set_RL_about:
                intent = new Intent();
                intent.setClass(getContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_set_RL_help:
                intent = new Intent();
                intent.setClass(getContext(), HelpActivity.class);
                startActivity(intent);

                break;
            case R.id.fragment_set_RL_feedBack:
                intent = new Intent();
                intent.setClass(getContext(), FeedBackActivity.class);
                startActivity(intent);
                break;
            default:
        }
    }
}
