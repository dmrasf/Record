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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
