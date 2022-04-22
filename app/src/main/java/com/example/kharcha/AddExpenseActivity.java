package com.example.kharcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddExpenseActivity extends AppCompatActivity {
    private Spinner tagDropdownSpinner;
    private EditText expenseName, expenseAmount;
    private Button btnExpenseSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);

        expenseName = findViewById(R.id.expenseName);
        expenseAmount = findViewById(R.id.expenseAmount);
        btnExpenseSave = findViewById(R.id.btnExpenseSave);

        tagDropdownSpinner = findViewById(R.id.tagDropdownSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_tags, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        tagDropdownSpinner.setAdapter(adapter);

        btnExpenseSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExpenseModel expenseModel;
               try {
                   Date date = Calendar.getInstance().getTime();
                   SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                   String formattedDate = df.format(date);

                   Float remaining = Float.parseFloat(sharedPreferences.getString("remaining_budget", "0")) - Float.parseFloat(expenseAmount.getText().toString());
                   sharedPreferences.edit().putString("remaining_budget", remaining.toString()).commit();

                   expenseModel = new ExpenseModel(1, expenseName.getText().toString(), Float.parseFloat(expenseAmount.getText().toString()), tagDropdownSpinner.getSelectedItem().toString(), formattedDate);
                   Toast.makeText(AddExpenseActivity.this, expenseModel.toString(), Toast.LENGTH_SHORT).show();
               } catch (Exception e) {
                    Toast.makeText(AddExpenseActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    expenseModel = new ExpenseModel(-1, "error", Float.parseFloat("0"), "error", "error");
                }

               DBHelper DBHelper = new DBHelper(AddExpenseActivity.this);
               boolean success = DBHelper.addOne(expenseModel);
               if(success) {
                   Intent i = new Intent(AddExpenseActivity.this, MainActivity.class);
                   startActivity(i);
                   Toast.makeText(AddExpenseActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
               } else {
                   Toast.makeText(AddExpenseActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}