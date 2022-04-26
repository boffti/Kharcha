package com.example.kharcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BudgetActivity extends AppCompatActivity {

    private EditText incomeEditText;
    private EditText budgetEditText;
    private Button btnIncomeSave;
    private Button btnBudgetSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);

        incomeEditText = findViewById(R.id.incomeEditText);
        budgetEditText = findViewById(R.id.budgetEditText);
        btnIncomeSave = findViewById(R.id.btnIncomeSave);
        btnBudgetSave = findViewById(R.id.btnBudgetSave);

        budgetEditText.setText(sharedPreferences.getString("current_budget", "0"));
        DBHelper dbHelper = new DBHelper(this);
        btnBudgetSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String budget = budgetEditText.getText().toString();
                if(!sharedPreferences.contains("remaining_budget")) {
                    sharedPreferences.edit().putString("remaining_budget", budget).commit();
                }
                if(sharedPreferences.getString("current_budget", "0").equals(sharedPreferences.getString("remaining_budget", "0"))) {
                    sharedPreferences.edit().putString("remaining_budget", budget).commit();
                }
                sharedPreferences.edit().putString("current_budget", budget).commit();
                dbHelper.updateRemainingBudget();
                Log.i("Budget", sharedPreferences.getString("current_budget", "0"));
                Intent i = new Intent(BudgetActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

    }
}