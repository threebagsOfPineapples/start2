package com.tachyon5.kstart.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tachyon5.kstart.R;

/**
 * Created by guofe on 2017/1/13 0013.
 */
public class FragmentRemModel extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_rem_model, container, false);
    }
}
