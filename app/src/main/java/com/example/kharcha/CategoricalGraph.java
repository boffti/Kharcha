package com.example.kharcha;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class CategoricalGraph extends Fragment {

    private PieChart pieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categorical_graph, container, false);

        this.pieChart = view.findViewById(R.id.pieChart);
//        onResume();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DBHelper dbHelper = new DBHelper(getContext());
        TreeMap<String, ExpensesByCategoryAndPeriod> data = dbHelper.getByCategoryAndPeriod();

//        System.out.println("PIECHART: " + pieChart);
        if(data.lastEntry() != null) {
            ExpensesByCategoryAndPeriod currentMonth = data.lastEntry().getValue();
            List<PieEntry> pieEntryList = new ArrayList<>();

            for(Map.Entry<String, Float> entry: currentMonth.expensesByCategory.entrySet()) {
                pieEntryList.add(new PieEntry(entry.getValue(), entry.getKey()));
            }

            System.out.println("PIE DATA: " + pieEntryList + pieChart);
            PieDataSet set = new PieDataSet(pieEntryList, "Monthly Expenses");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            set.setSliceSpace(5f);
            PieData pd = new PieData(set);
            pieChart.setData(pd);
            pieChart.invalidate(); // refresh

        }
    }
}