package com.tachyon5.kstart.chart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.tachyon5.kstart.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by guofe on 2017/3/24 0024.
 */
public class LineChartActivity extends AppCompatActivity {
    private LineChart lineChart;
    Map hashMap;
    XAxis xAxis;
    YAxis axisLeft, axisRight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        lineChart = (LineChart) findViewById(R.id.linechart);
        LineData lineData = makeLineData(300);
        setChartStyle(lineChart, lineData, Color.rgb(255, 255, 255));
    }

    // 设置chart显示的样式
    private void setChartStyle(LineChart mLineChart, LineData lineData,
                               int color) {
        // 是否在折线图上添加边框
        mLineChart.setDrawBorders(false);
        mLineChart.setDescription(null);
        //  mLineChart.setDescription("测试0o0");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        mLineChart.setNoDataTextDescription("如果传给MPAndroidChart的数据为空，那么你将看到这段文字。");
        // 是否绘制背景颜色。
        // 如果mLineChart.setDrawGridBackground(false)，
        // 那么mLineChart.setGridBackgroundColor(Color.CYAN)将失效;
        // mLineChart.setDrawGridBackground(false);
        // mLineChart.setGridBackgroundColor(R.color.yellow);
        // 触摸
        mLineChart.setTouchEnabled(false);
        // 拖拽
        mLineChart.setDragEnabled(false);
        // 缩放
        mLineChart.setScaleEnabled(false);
        mLineChart.setPinchZoom(false);
        mLineChart.setBackgroundColor(color);
        mLineChart.setDrawGridBackground(false);

        //x轴
        xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelsToSkip(100);
        xAxis.setAxisLineColor(Color.BLUE);
        xAxis.setAxisLineWidth(2);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);
        //左边y
        axisLeft = lineChart.getAxisLeft();
        axisLeft.setLabelCount(5, true);
        axisLeft.setDrawLimitLinesBehindData(false);
        axisLeft.setAxisLineWidth(2);
        axisLeft.setAxisLineColor(Color.BLUE);
        axisLeft.setGridColor(Color.GRAY);
        axisLeft.setDrawGridLines(true);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRight = lineChart.getAxisRight();
        axisRight.setEnabled(false);
        // 设置x,y轴的数据
        mLineChart.setData(lineData);
        // 设置比例图标示，就是那个一组y的value的
        Legend mLegend = mLineChart.getLegend();
        mLegend.setEnabled(false);
        mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(15.0f);// 字体*/
        //  mLegend.setTextColor(Color.YELLOW);// 颜色
        // 沿x轴动画，时间2000毫秒。
        //   mLineChart.animateX(2000);
    }

    /**
     * @param count 数据点的数量。
     * @return
     */
    private LineData makeLineData(int count) {
        ArrayList<String> x = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            // x轴显示的数据
            x.add("x:" + i);
        }
        // y轴的数据
        ArrayList<Entry> y = new ArrayList<Entry>();
        for (int i = 0; i < count; i++) {
            float val = (float) (Math.random() * 100);
            Entry entry = new Entry(val, i);
            y.add(entry);
        }
        // y轴数据集
        LineDataSet mLineDataSet = new LineDataSet(y, null);
        // 用y轴的集合来设置参数
        // 线宽
        mLineDataSet.setLineWidth(1.0f);
        // 显示的圆形大小
        mLineDataSet.setCircleSize(1.5f);
        // 折线的颜色
        mLineDataSet.setColor(R.color.line_chart_blue);
        // 圆球的颜色
        //  mLineDataSet.setCircleColor(R.color.white);
        // 设置mLineDataSet.setDrawHighlightIndicators(false)后，
        // Highlight的十字交叉的纵横线将不会显示，
        // 同时，mLineDataSet.setHighLightColor(Color.CYAN)失效。
        mLineDataSet.setDrawHighlightIndicators(false);
        // 按击后，十字交叉线的颜色
        // mLineDataSet.setHighLightColor(Color.CYAN);
        mLineDataSet.setDrawValues(false);
        // 设置这项上显示的数据点的字体大小。
        // mLineDataSet.setValueTextSize(10.0f);
        // mLineDataSet.setDrawCircleHole(true);
        // 改变折线样式，用曲线。
        mLineDataSet.setDrawCubic(true);
        // 默认是直线
        // 曲线的平滑度，值越大越平滑。
        mLineDataSet.setCubicIntensity(0.2f);
        // 填充曲线下方的区域，红色，半透明。
        // mLineDataSet.setDrawFilled(true);
        // mLineDataSet.setFillAlpha(128);
        // mLineDataSet.setFillColor(Color.RED);
        // 填充折线上数据点、圆球里面包裹的中心空白处的颜色。
        //  mLineDataSet.setCircleColorHole(Color.WHITE);
        /*  设置折线上显示数据的格式。如果不设置，将默认显示float数据格式。
        mLineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                int n = (int) v;
                String s = "y:" + n;
                return s;
            }
        });*/
        ArrayList<LineDataSet> mLineDataSets = new ArrayList<LineDataSet>();
        mLineDataSets.add(mLineDataSet);
        LineData mLineData = new LineData(x, mLineDataSets);
        return mLineData;
    }
}
