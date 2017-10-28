package com.michael.bitcoinapp;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

import utilities.DailyData;
import utilities.HistoryData;


public class DailyChartFragment extends Fragment {


    public DailyChartFragment() {

    }

    LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.daily_chart_fragment, container, false);
        lineChart = (LineChart) view.findViewById(R.id.daily_line_chart);

        List<String> xAXES = new ArrayList<String>();
        List<Float> yValue = new ArrayList<Float>();
        List<Entry> yAXES = new ArrayList<Entry>();

        String TAG = "bitcoin app log test";

        xAXES = DailyData.getTime();
        yValue = DailyData.getPrice();

        for (int i=0; i<DailyData.getPrice().size(); i++){
            yAXES.add(new Entry(yValue.get(i), i));
        }

        ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(yAXES, "Bitcoin Price");
        lineDataSet.setDrawCircles(false);
        if (yValue.get(0) < yValue.get(yValue.size()-1)){
            lineDataSet.setColor(Color.GREEN);
        } else {
            lineDataSet.setColor(Color.RED);
        }
        lineDataSet.setLineWidth(2);

        lineDataSets.add(lineDataSet);

        lineChart.setData(new LineData(xAXES, lineDataSets));

        return view;
    }

}
