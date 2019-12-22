package com.allvideodownloader.socialmedia.videodownloader.models;

public class Video {
    private String mRes;
    private String mSize;
    private String mUrl;
    private String mFormat;

    public Video(String res, String format, String size, String url) {
        mRes = res;
        mSize = size;
        mUrl = url;
        mFormat = format;
    }

    public String getRes() {
        return mRes;
    }

    public String getSize() {
        return mSize;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getFormat() {
        return mFormat;
    }
}
