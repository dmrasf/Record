package com.dmrasf.record.home;

// 不同的记录
public class Record {
    private String mTitle;
    private long mDate;
    private int mRecordImage;

    public Record(String title, int image, long date) {
        mTitle = title;
        mRecordImage = image;
        mDate = date;
    }

    public Record(String title, int image) {
        mTitle = title;
        mRecordImage = image;
        mDate = -1;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getRecordImage() {
        return mRecordImage;
    }

    public long getDate() {
        return mDate;
    }
}
