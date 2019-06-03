package com.example.sqlitetomsexcel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Your database name
    static final String DATABASE_NAME = "employee.db";
    //Your table name
    static final String TABLE_NAME = "tbl_employee";

    //Column Names of your table
    public static final String COL_ID = "ID";
    private static final String COL_FNAME = "FNAME";
    private static final String COL_LNAME = "LNAME";
    private static final String COL_CITY = "CITY";
    private static final String COL_SALARY = "SALARY";


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT,FNAME TEXT,LNAME TEXT,CITY TEXT,SALARY NUMBER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Inserting record in SQLite database
    public boolean insertData(String fname, String lname, String city, String salary) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FNAME, fname);
        contentValues.put(COL_LNAME, lname);
        contentValues.put(COL_CITY, city);
        contentValues.put(COL_SALARY, salary);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
