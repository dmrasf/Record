package com.dmrasf.record.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DayDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "all.db";
    private static final int DATABASE_VERSION = 1;
    public String Table_name;

    public DayDbHelper(@Nullable Context context, String table_name) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Table_name = table_name;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 当不存在数据库时才会调用此函数

        String SQL_CREATE_DAY_TABLE = "CREATE TABLE " + Table_name + "("
                + RecordAndDayContract.DayEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordAndDayContract.DayEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + RecordAndDayContract.DayEntry.COLUMN_TEXT + " TEXT, "
                + RecordAndDayContract.DayEntry.COLUMN_IMG + " TEXT NOT NULL);";
        Log.e("-----------", SQL_CREATE_DAY_TABLE);
        db.execSQL(SQL_CREATE_DAY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
