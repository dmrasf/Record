package com.dmrasf.record.home;

public class Day {
    private String mTitle;
    private int mDayImage;

    public Day(String title, int dayImage) {
        mDayImage = dayImage;
        mTitle = title;
    }

    public String getTitle() {
        return mTitle;
    }

    public int getDayImage() {
        return mDayImage;
    }
}
