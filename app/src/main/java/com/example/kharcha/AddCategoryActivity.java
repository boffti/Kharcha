package com.example.kharcha;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class AddCategoryActivity extends AppCompatActivity implements  CatRecyclerViewInterface{

    private EditText categoryNameText;
    private Button btnAddCategory;
    private RecyclerView rv_cat_list;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    SharedPreferences sp;
    List<String> categories;
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
//                Intent i = new Intent(AddCategoryActivity.this, AddExpenseActivity.class);
//                startActivity(i);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                Toast.makeText(AddCategoryActivity.this, "New category added", Toast.LENGTH_SHORT).show();
            }
        });

        rv_cat_list = (RecyclerView) findViewById(R.id.rv_cat_list);
        rv_cat_list.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        rv_cat_list.setLayoutManager(layoutManager);

        String categories_serialized = sp.getString("user_categories", "");
        categories = Arrays.asList(categories_serialized.split(";"));

        mAdapter = new CatListAdapter(categories, AddCategoryActivity.this, this  );
        rv_cat_list.setAdapter(mAdapter);

    }

    @Override
    public void onItemClick(int position) {
        Log.i("Cat pos clicked", categories.get(position));
        sp.edit().putString("current_category_selected", categories.get(position)).commit();
        Intent i = new Intent(AddCategoryActivity.this, AddExpenseActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemLongClick(int position) {
        Log.i("Long pressed", String.valueOf(position));
        String categories_serialized = sp.getString("user_categories", "");
        Log.i("Long pressed string", categories_serialized);
        List<String> categories_deserialized = Arrays.asList(categories_serialized.split(";"));
        categories_deserialized.remove(position);
        String new_categories_serialized = String.join(";", categories_deserialized);
        sp.edit().putString("user_categories", new_categories_serialized).apply();
        Log.i("Long pressed new string", new_categories_serialized);

//        Toast.makeText(AddCategoryActivity.this, "Category Deleted", Toast.LENGTH_SHORT).show();
//        finish();
//        overridePendingTransition(0, 0);
//        startActivity(getIntent());
//        overridePendingTransition(0, 0);
    }
}