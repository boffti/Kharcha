package com.example.kharcha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CardView budgetCardView;
    private FloatingActionButton fabAddBudget;
    private TextView textUserName;
    private TextView textTotalBudget;
    private TextView textRemainingBudget;

    private RecyclerView rv_expenses_list;    
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_expenses_list = (RecyclerView) findViewById(R.id.rv_expenses_list);
        rv_expenses_list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rv_expenses_list.setLayoutManager(layoutManager);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);
        if(!sharedPreferences.contains("user_name")) {
            sharedPreferences.edit().putString("user_name", "User").apply();
        }

        budgetCardView = findViewById(R.id.budgetCardView);
        budgetCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BudgetActivity.class);
                startActivity(i);
            }
        });

        fabAddBudget = findViewById(R.id.fabAddExpense);
        fabAddBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddExpenseActivity.class);
                startActivity(i);
            }
        });

        textUserName = findViewById(R.id.textUserName);
        textUserName.setText(sharedPreferences.getString("user_name", "User"));

        textUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddUserNameActivity.class);
                startActivity(i);
            }
        });

        textTotalBudget = findViewById(R.id.textTotalBudget);
        textTotalBudget.setText(sharedPreferences.getString("current_budget", "0"));

        textRemainingBudget = findViewById(R.id.textRemainingBudget);
        textRemainingBudget.setText(sharedPreferences.getString("remaining_budget", "0"));

        DBHelper dbHelper = new DBHelper(MainActivity.this);
        List<ExpenseModel> expenses = dbHelper.getAllExpenses();

        mAdapter = new ExpenseListAdapter(expenses, MainActivity.this);
        rv_expenses_list.setAdapter(mAdapter);
    }
}