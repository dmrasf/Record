package com.dmrasf.record.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;
import com.dmrasf.record.data.RecordAndDayContract;

public class RecordDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "all.db";
    private static final int DATABASE_VERSION = 1;

    public RecordDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 当不存在数据库时才会调用此函数

        String SQL_CREATE_RECORD_TABLE = "CREATE TABLE " + RecordAndDayContract.RecordEntry.TABLE_NAME + "("
                + RecordAndDayContract.RecordEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordAndDayContract.RecordEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + RecordAndDayContract.RecordEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + RecordAndDayContract.RecordEntry.COLUMN_DAY_TABLE + " TEXT NOT NULL, "
                + RecordAndDayContract.RecordEntry.COLUMN_DAYS + " INTEGER NOT NULL);";
        Log.e("-----------", SQL_CREATE_RECORD_TABLE);
        db.execSQL(SQL_CREATE_RECORD_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
