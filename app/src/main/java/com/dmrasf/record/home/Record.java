package com.dmrasf.record.home;

// 不同的记录
public class Record {
    private String mTitle;
    private int mRecordImage;

    public Record(String title, int image) {
        mTitle = title;
        mRecordImage = image;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getRecordImage() {
        return mRecordImage;
    }
}
