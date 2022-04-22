package com.example.kharcha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddUserNameActivity extends AppCompatActivity {

    private EditText editUserName;
    private Button btnSaveUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_name);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);

        editUserName = findViewById(R.id.editUserName);
        btnSaveUserName = findViewById(R.id.btnSaveUserName);

        editUserName.setText(sharedPreferences.getString("user_name", "User"));

        btnSaveUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("user_name", editUserName.getText().toString()).commit();
                Intent i = new Intent(AddUserNameActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }
}