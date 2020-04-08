package com.dmrasf.record.home.item_day;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Day {
    private String mText;
    private long mDate;
    //缩略图
    private Bitmap mBitmap;
    //内容 可以为空
    private String mImagePath;

    public Day(String text, long date, byte[] bitmap, String imagePath) {
        mText = text;
        mDate = date;
        // byte[] to bitmap
        mBitmap = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
        mImagePath = imagePath;
    }

    public String getText() {
        return mText;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public long getDate() {
        return mDate;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
