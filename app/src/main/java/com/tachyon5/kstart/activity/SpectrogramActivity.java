package com.tachyon5.kstart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.tachyon5.kstart.R;

public class SpectrogramActivity extends AppCompatActivity {
    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spectrogram);
        lineChart = (LineChart) findViewById(R.id.linechart);
    }
}
