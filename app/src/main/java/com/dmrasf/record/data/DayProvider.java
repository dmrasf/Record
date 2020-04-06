package com.dmrasf.record.data;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dmrasf.record.MainActivity;

import static android.icu.text.DateTimePatternGenerator.DAY;

public class DayProvider extends ContentProvider {

    public static final String LOG_TAG = DayProvider.class.getSimpleName();

    private static final int DAYS = 200;
    private static final int DAYS_ID = 201;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private DayDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
//        mDbHelper = new DayDbHelper(getContext(), ((MainActivity) getContext()).recordTitle);
//        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, mDbHelper.Table_name, DAYS);
//        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, mDbHelper.Table_name + "/#", DAYS_ID);
        return true;
    }

    public DayProvider(Context context) {
        mDbHelper = new DayDbHelper(context, ((MainActivity) context).recordTitle);
        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, mDbHelper.Table_name, DAYS);
        sUriMatcher.addURI(RecordAndDayContract.CONTENT_AUTHORITY, mDbHelper.Table_name + "/#", DAYS_ID);
    }

    public DayProvider() {
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                cursor = db.query(mDbHelper.Table_name, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case DAYS_ID:
                selection = RecordAndDayContract.DayEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(mDbHelper.Table_name, projection,
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
            case DAYS:
                return RecordAndDayContract.DayEntry.CONTENT_DAY_LIST_TYPE + mDbHelper.Table_name;
            case DAYS_ID:
                return RecordAndDayContract.DayEntry.CONTENT_DAY_ITEM_TYPE + mDbHelper.Table_name;
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                return insertDay(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertDay(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        long id = db.insert(mDbHelper.Table_name, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        return Uri.withAppendedPath(Uri.withAppendedPath(RecordAndDayContract.BASE_CONTENT_URI, mDbHelper.Table_name), String.valueOf(id));
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DAYS:
                //删除实际文件
                return db.delete(mDbHelper.Table_name, selection, selectionArgs);
            case DAYS_ID:
                selection = RecordAndDayContract.DayEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return db.delete(mDbHelper.Table_name, selection, selectionArgs);
            default:
                return 0;
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
