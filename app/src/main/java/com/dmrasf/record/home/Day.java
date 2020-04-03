package com.dmrasf.record.home;

public class Day {
    private String mTitle;
    private long mDate;
    private int mDayImage;

    public Day(String title, int dayImage) {
        mDayImage = dayImage;
        mTitle = title;
    }

    public Day(String title, int dayImage, long date) {
        mDayImage = dayImage;
        mTitle = title;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDayImage() {
        return mDayImage;
    }

    public long getDate() {
        return mDate;
    }
}
