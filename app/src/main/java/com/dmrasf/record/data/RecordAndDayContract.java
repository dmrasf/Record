package com.dmrasf.record.data;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class RecordAndDayContract {
    // 为record数据库建立的方便操作的工具类

    public static abstract class RecordEntry implements BaseColumns {

        public static final String TABLE_NAME = "records";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        // 另一个days表
        public static final String COLUMN_DAY_TABLE = "day_table";
        public static final String COLUMN_DAYS = "days";

    }

    public static abstract class DayEntry implements BaseColumns {

        public static final String TABLE_NAME = "days";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMG = "img";

    }
}
