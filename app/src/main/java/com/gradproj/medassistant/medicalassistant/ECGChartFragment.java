package com.gradproj.medassistant.medicalassistant;


import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.gradproj.medassistant.medicalassistant.Enums.SensorType;
import com.gradproj.medassistant.medicalassistant.database.DBAdapter;

import java.util.ArrayList;

public class ECGChartFragment extends ReadingFragment {

    private Typeface tf;

    private LineChart mChart;
//    private DBAdapter mDBAdapter;

    private Handler handler;
    private Runnable myRunnable;


    public ECGChartFragment() {

    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_ecgchart, container, false);
//
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ecgchart, container, false);


        try {
            mDBAdapter = new DBAdapter(getContext());
            mDBAdapter.open();
        } catch (Exception e) {
            e.printStackTrace();
        }


        mChart = (LineChart) v.findViewById(R.id.lineChart1);

        drawChart();

        return v;
    }

    private void drawChart() {

        mChart.getDescription().setEnabled(false);

        mChart.setDrawGridBackground(false);

        mChart.setData(generateLineData());
//        mChart.animateX(3000);

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        Legend l = mChart.getLegend();
        l.setTypeface(tf);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTypeface(tf);

        leftAxis.setAxisMaximum(1000f);
        leftAxis.setAxisMinimum(0f);

        mChart.getAxisRight().setEnabled(false);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setEnabled(false);
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ECGChartFragment newInstance(int columnCount) {
        ECGChartFragment fragment = new ECGChartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    protected LineData generateLineData() {

        ArrayList<ILineDataSet> sets = new ArrayList<ILineDataSet>();
        ArrayList<Entry> myEntries = new ArrayList<>();
        Cursor result = mDBAdapter.fetchAllSensorReadings(SensorType.SPO2_SENSOR);
        if (result.moveToFirst()) {
            int i = 0;
            do {
                String reading = result.getString(result.getColumnIndex(DBAdapter.KEY_READING_VALUE));
                Entry e = new Entry();
                e.setX(i);
                e.setY(Float.parseFloat(reading) * 100);
                myEntries.add(e);
                i++;
            } while (result.moveToNext() && i < 11);
        }

        LineDataSet ds1 = new LineDataSet(myEntries, "Tes");
        ds1.setLineWidth(2f);

        ds1.setDrawCircles(false);

        ds1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);

        // load DataSets from textfiles in assets folders
        sets.add(ds1);
        result.close();

        LineData d = new LineData(sets);
        d.setValueTypeface(tf);
        return d;
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(myRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        handler = new Handler();
        myRunnable = new Runnable() {

            @Override
            public void run() {
                drawChart();
                handler.postDelayed(this, 1 * 500);
            }
        };
        handler.postDelayed(myRunnable, 1 * 500);
    }
}