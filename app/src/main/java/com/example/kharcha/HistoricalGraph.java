package com.example.kharcha;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class HistoricalGraph extends Fragment {

    private BarChart barChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historical_graph, container, false);

        this.barChart = view.findViewById(R.id.barChart);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper dbHelper = new DBHelper(getContext());
        TreeMap<String, ExpensesByCategoryAndPeriod> data = dbHelper.getByCategoryAndPeriod();

        List<BarEntry> barEntryList = new ArrayList<>();
        List<String> xLabels = new ArrayList<>();

        int index = 0;
        for(Map.Entry<String, ExpensesByCategoryAndPeriod> d: data.entrySet()) {
            float total = 0f;
            for(Map.Entry<String, Float> exp: d.getValue().expensesByCategory.entrySet()) {
                total += exp.getValue();
            }
            xLabels.add(d.getKey());
            barEntryList.add(new BarEntry(0f, total));
            index++;
            System.out.println(d.getKey() + " : " + total);
        }


        barEntryList.add(new BarEntry(1f, 333f));
        xLabels.add("Temp");
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xLabels));
        BarDataSet set = new BarDataSet(barEntryList, "");
        BarData barData = new BarData(set);
        set.setValueTextSize(16f);
        set.setColors(ColorTemplate.MATERIAL_COLORS);
//        barData.setBarWidth(0.1f); // set custom bar width
        barChart.setData(barData);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.setTouchEnabled(true);
        barChart.setClickable(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);

//        barChart.setDrawBorders(false);

        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        // Left Axis numbers and line
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawLabels(false);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        barChart.getXAxis().setDrawLabels(false);
//        barChart.getXAxis().setDrawAxisLine(false);

        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setDrawAxisLine(false);
        barChart.setFitBars(true); // make the x-axis fit exactly all bars

        barChart.getXAxis().setGranularity(1f);
        barChart.invalidate(); // refresh



    }
}