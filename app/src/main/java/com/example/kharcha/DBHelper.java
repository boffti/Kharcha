package com.example.kharcha;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String EXPENSE_TABLE = "EXPENSE_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_TAG = "TAG";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_ID = "ID";

    public DBHelper(@Nullable Context context) {
        super(context, "expenses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStmt = "CREATE TABLE " + EXPENSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_AMOUNT + " FLOAT, " + COLUMN_TAG + " TEXT, " + COLUMN_CREATED_AT + " TEXT)";
        db.execSQL(createTableStmt);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(ExpenseModel expenseModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, expenseModel.getName());
        cv.put(COLUMN_AMOUNT, expenseModel.getAmount());
        cv.put(COLUMN_TAG, expenseModel.getCategory());
        cv.put(COLUMN_CREATED_AT, expenseModel.getAddedOn());
        long insert = db.insert(EXPENSE_TABLE, null, cv);
        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<ExpenseModel> getAllExpenses() {
        List<ExpenseModel> returnList = new ArrayList<>();
        String query = "SELECT * FROM " + EXPENSE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                Float amount = cursor.getFloat(2);
                String tag = cursor.getString(3);
                String created_on = cursor.getString(4);
                ExpenseModel newExpense = new ExpenseModel(id, name, amount, tag, created_on);
                returnList.add(newExpense);
            } while(cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return returnList;
    }
}
