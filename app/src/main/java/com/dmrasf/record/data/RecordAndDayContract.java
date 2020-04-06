package com.dmrasf.record.data;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

public class RecordAndDayContract {
    // 为record数据库建立的方便操作的工具类

    public static final String CONTENT_AUTHORITY = "com.dmrasf.record";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_RECORDS = "records";

    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RECORDS;

    public static abstract class RecordEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "records";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        // 另一个days表
        public static final String COLUMN_DAY_TABLE = "day_table";
        public static final String COLUMN_DAYS = "days";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_RECORDS);
    }

    public static abstract class DayEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
//        public static final String TABLE_NAME = "days";
        public static final String COLUMN_TEXT = "text";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_IMG = "img";

        public static final String CONTENT_DAY_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/";
        public static final String CONTENT_DAY_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/";
    }
}
