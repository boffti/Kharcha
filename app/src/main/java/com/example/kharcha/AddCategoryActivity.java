package com.example.kharcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategoryActivity extends AppCompatActivity {

    private EditText categoryNameText;
    private Button btnAddCategory;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        sp = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);
        categoryNameText = findViewById(R.id.categoryName);
        btnAddCategory = findViewById(R.id.btnCategorySave);

        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_categories = sp.getString("user_categories", "");
                String new_category = categoryNameText.getText().toString();
                String serialize_categories = user_categories + ";" + new_category;
                sp.edit().putString("user_categories", serialize_categories).commit();
                Log.i("User Categories", user_categories);
                Intent i = new Intent(AddCategoryActivity.this, AddExpenseActivity.class);
                startActivity(i);
                Toast.makeText(AddCategoryActivity.this, "New category added", Toast.LENGTH_SHORT).show();
            }
        });

    }
}