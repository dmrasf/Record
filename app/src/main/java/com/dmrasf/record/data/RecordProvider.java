package com.dmrasf.record.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class RecordProvider extends ContentProvider {

    public static final String LOG_TAG = RecordProvider.class.getSimpleName();

    private static final int RECORDS = 100;
    private static final int RECORD_ID = 101;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

//    static {
//        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, RecordAndDayContract.PATH_RECORDS, RECORDS);
//        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, RecordAndDayContract.PATH_RECORDS + "/#", RECORD_ID);
//    }

    private RecordDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
//        mDbHelper = new RecordDbHelper(getContext());
        return true;
    }

    public RecordProvider(Context context) {
        mDbHelper = new RecordDbHelper(context);
        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, RecordAndDayContract.PATH_RECORDS, RECORDS);
        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, RecordAndDayContract.PATH_RECORDS + "/#", RECORD_ID);
    }

    public RecordProvider() {
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                cursor = db.query(RecordAndDayContract.RecordEntry.TABLE_NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case RECORD_ID:
                selection = RecordAndDayContract.RecordEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(RecordAndDayContract.RecordEntry.TABLE_NAME, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return RecordAndDayContract.CONTENT_LIST_TYPE;
            case RECORD_ID:
                return RecordAndDayContract.CONTENT_ITEM_TYPE;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return insertRecord(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertRecord(Uri uri, ContentValues values) {
        String name = values.getAsString(RecordAndDayContract.RecordEntry.COLUMN_TITLE);
        // 判断Record名是否为空
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "不能为空", Toast.LENGTH_SHORT).show();
            return null;
        }
        // 判断Record 是否已经存在
        String[] projection = {
                RecordAndDayContract.RecordEntry.COLUMN_TITLE,
        };
        Cursor cursor = query(RecordAndDayContract.RecordEntry.CONTENT_URI, projection,
                RecordAndDayContract.RecordEntry.COLUMN_TITLE + "=?", new String[]{name}, null);
        if (cursor.getCount() != 0) {
            Toast.makeText(getContext(), "该Record已存在", Toast.LENGTH_SHORT).show();
            cursor.close();
            return null;
        }
        cursor.close();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long id = db.insert(RecordAndDayContract.RecordEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        // 新建day
        String SQL_CREATE_DAY_TABLE = "CREATE TABLE " + name + "("
                + RecordAndDayContract.DayEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + RecordAndDayContract.DayEntry.COLUMN_DATE + " INTEGER NOT NULL, "
                + RecordAndDayContract.DayEntry.COLUMN_TEXT + " TEXT, "
                + RecordAndDayContract.DayEntry.COLUMN_IMG + " BLOB NOT NULL, "
                + RecordAndDayContract.DayEntry.COLUMN_IMG_PATH + " TEXT NOT NULL);";
        Log.e("-----------", SQL_CREATE_DAY_TABLE);
        db.execSQL(SQL_CREATE_DAY_TABLE);
        return Uri.withAppendedPath(RecordAndDayContract.RecordEntry.CONTENT_URI, String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return db.delete(RecordAndDayContract.RecordEntry.TABLE_NAME, selection, selectionArgs);
            case RECORD_ID:
                selection = RecordAndDayContract.RecordEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(RecordAndDayContract.RecordEntry.TABLE_NAME, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case RECORDS:
                return updateRecord(uri, values, selection, selectionArgs);
            case RECORD_ID:
                selection = RecordAndDayContract.RecordEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateRecord(uri, values, selection, selectionArgs);
            default:
                return 0;
        }
    }

    private int updateRecord(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(RecordAndDayContract.RecordEntry.COLUMN_TITLE)) {
            String name = values.getAsString(RecordAndDayContract.RecordEntry.COLUMN_TITLE);
            if (name.isEmpty()) {
                Toast.makeText(getContext(), "该Record已存在", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        return db.update(RecordAndDayContract.RecordEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
