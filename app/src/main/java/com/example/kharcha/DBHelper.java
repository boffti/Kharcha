package com.example.kharcha;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

class ExpensesByCategoryAndPeriod implements Comparable<ExpensesByCategoryAndPeriod> {
    public String monthText;
    public int monthNumber;
    public int year;
    public Map<String, Float> expensesByCategory;

    public ExpensesByCategoryAndPeriod(String monthText, int monthNumber, int year, Map<String, Float> expensesByCategory) {
        this.monthText = monthText;
        this.monthNumber = monthNumber;
        this.year = year;
        this.expensesByCategory = expensesByCategory;
    }


    @Override
    public int compareTo(ExpensesByCategoryAndPeriod o) {
        if(this.year == o.year)
            return Integer.compare(this.monthNumber, o.monthNumber);
        return Integer.compare(this.year, o.year);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpensesByCategoryAndPeriod that = (ExpensesByCategoryAndPeriod) o;
        return monthNumber == that.monthNumber && year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hash(monthNumber, year);
    }

    @Override
    public String toString() {
        return "{" +
                "monthText='" + monthText + '\'' +
                ", monthNumber=" + monthNumber +
                ", year=" + year +
                ", expensesByCategory=" + expensesByCategory +
                '}';
    }
}

public class DBHelper extends SQLiteOpenHelper {
    public static final String EXPENSE_TABLE = "EXPENSE_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_AMOUNT = "AMOUNT";
    public static final String COLUMN_TAG = "TAG";
    public static final String COLUMN_CREATED_AT = "CREATED_AT";
    public static final String COLUMN_ID = "ID";

    public static final String PERIOD_TABLE = "PERIOD_TABLE";
    public static final String P_COLUMN_PERIOD = "PERIOD";
    public static final String P_COLUMN_REMAINING_BUDGET = "REMAINING_BUDGET";
    public static final String P_COLUMN_CREATED_AT = "CREATED_AT";

    private Context context;

    public DBHelper(@Nullable Context context) {
        super(context, "expenses.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStmt = "CREATE TABLE IF NOT EXISTS " + EXPENSE_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME + " TEXT, " + COLUMN_AMOUNT + " FLOAT, " + COLUMN_TAG + " TEXT, " + COLUMN_CREATED_AT + " TEXT)";
        String createPeriodStmt = "CREATE TABLE IF NOT EXISTS " + PERIOD_TABLE + " (" + P_COLUMN_PERIOD + " TEXT PRIMARY KEY, " + P_COLUMN_REMAINING_BUDGET + " FLOAT, " + P_COLUMN_CREATED_AT + " TEXT)";
        db.execSQL(createTableStmt);
        db.execSQL(createPeriodStmt);
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
        System.out.println("EXPENSE: " + expenseModel.getAddedOn());
        db.close();
        if(insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    // Call this whenever "remaining_budget" is updated
    public boolean updateRemainingBudget() {
        SharedPreferences sharedPreferences = this.context.getSharedPreferences("com.example.kharcha", Context.MODE_PRIVATE);
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        String createdOn = df.format(date);
        String period = new SimpleDateFormat("MMMM-yyyy", Locale.getDefault()).format(new Date());
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(P_COLUMN_REMAINING_BUDGET, Float.parseFloat(sharedPreferences.getString("remaining_budget", "0")));
        cv.put(P_COLUMN_PERIOD, period);
        cv.put(P_COLUMN_CREATED_AT, createdOn);
        long insertPeriod = db.insertWithOnConflict(PERIOD_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return insertPeriod != -1;
    }

    // This is called whenever MainActivity is displayed
    // NOTE:  Already added this method in the MainActivity
    public int calculateXP() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT COUNT(`" + COLUMN_ID + "`) * 100 FROM " + EXPENSE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        int xp = cursor.getInt(0);

//      To find out budget is more
        String query1 = "SELECT SUM( CASE WHEN " + P_COLUMN_REMAINING_BUDGET + " > 0 THEN 0 ELSE 500 END) FROM " + PERIOD_TABLE;
        cursor = db.rawQuery(query1, null);
        cursor.moveToFirst();

        System.out.println("Calculating XP: " + xp + " - " + cursor.getInt(0));
        int netXP = xp - cursor.getInt(0);

        cursor.close();
        db.close();

        return netXP;
    }



    // Use this method to get data grouped by Tag and Year. Ordered by Created On Year and Month
    public TreeMap<String, ExpensesByCategoryAndPeriod> getByCategoryAndPeriod() {
        TreeMap<String, ExpensesByCategoryAndPeriod> map = new TreeMap<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT SUM(AMOUNT), TAG, SUBSTR(CREATED_AT, 4), CREATED_AT FROM EXPENSE_TABLE GROUP BY SUBSTR(CREATED_AT, 4), TAG;";
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                Float amount = cursor.getFloat(0);
                String tag = cursor.getString(1);
                String period = cursor.getString(2);
                String createdAt = cursor.getString(3);
                System.out.println("OOO: === " + createdAt);

                int monthNumber = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date(createdAt)));
                int year = Integer.parseInt(new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date(createdAt)));

                if(!map.containsKey(period))
                    map.put(period, new ExpensesByCategoryAndPeriod(period, monthNumber, year, new HashMap<>()));

                map.get(period).expensesByCategory.put(tag, amount);
            } while(cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();

        return map;
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
