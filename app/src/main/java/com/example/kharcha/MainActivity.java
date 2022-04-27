package com.example.kharcha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ExpenseRecyclerViewInterface {

    private CardView budgetCardView;
    private FloatingActionButton fabAddBudget;
    private TextView textUserName;
    private TextView textTotalBudget;
    private TextView textRemainingBudget;
    private TextView textXp;
    private TextView incomeTextView;
    private TextView textLevel;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private RecyclerView rv_expenses_list;    
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public SQLiteDatabase db;
    List<ExpenseModel> expenses;
    DBHelper dbHelper;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv_expenses_list = (RecyclerView) findViewById(R.id.rv_expenses_list);
        rv_expenses_list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rv_expenses_list.setLayoutManager(layoutManager);

        sharedPreferences = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);
        if(!sharedPreferences.contains("user_name")) {
            sharedPreferences.edit().putString("user_name", "User").apply();
        }

        if(!sharedPreferences.contains("user_categories")) {
            sharedPreferences.edit().putString("user_categories", "Food;Travel;Shopping;Entertainment;Investment;Bill").apply();
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

        incomeTextView = findViewById(R.id.incometextView);
        incomeTextView.setText(sharedPreferences.getString("income", "-"));


        textRemainingBudget = findViewById(R.id.textRemainingBudget);
        Float remainingBudget = Float.parseFloat(sharedPreferences.getString("current_budget", "0")) - Float.parseFloat(sharedPreferences.getString("total_spent", "0"));
        textRemainingBudget.setText(String.valueOf(remainingBudget));

        dbHelper = new DBHelper(MainActivity.this);
        expenses = dbHelper.getAllExpenses();

        textXp = findViewById(R.id.xp);
        textXp.setText(String.valueOf(dbHelper.calculateXP()));

        int level = (int) Math.floor(dbHelper.calculateXP() > 0 ? (dbHelper.calculateXP() * 1.0 /1000) : 0);
        textLevel = findViewById(R.id.level);
        textLevel.setText(String.valueOf(level + 1));

        mAdapter = new ExpenseListAdapter(expenses, MainActivity.this, this);
        rv_expenses_list.setAdapter(mAdapter);

        tabLayout = findViewById(R.id.graphTab);
        viewPager = findViewById(R.id.viewPager);

        tabLayout.setupWithViewPager(viewPager);

        GraphVPAdapter graphVPAdapter = new GraphVPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        graphVPAdapter.addFragment(new CategoricalGraph(), "Categorical");
        graphVPAdapter.addFragment(new HistoricalGraph(), "Historical");
        viewPager.setAdapter(graphVPAdapter);
    }

    @Override
    public void onItemClick(int position) {
//        ExpenseModel expenseToDelete = expenses.get(position);
//        dbHelper.deleteOne(expenseToDelete);
    }

    @Override
    public void onItemLongClick(int position) {
        ExpenseModel expenseToDelete = expenses.get(position);
        dbHelper.deleteOne(expenseToDelete);
        Float totalSpent = Float.parseFloat(sharedPreferences.getString("total_spent", "0")) - Float.parseFloat(expenseToDelete.getAmount().toString());
        sharedPreferences.edit().putString("total_spent", totalSpent.toString()).commit();
        Toast.makeText(MainActivity.this, "Expsense Deleted", Toast.LENGTH_SHORT).show();
                finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}