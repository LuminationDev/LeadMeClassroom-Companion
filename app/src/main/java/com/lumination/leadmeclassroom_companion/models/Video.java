package com.lumination.leadmeclassroom_companion.models;

import android.net.Uri;

import java.util.HashMap;
import java.util.Map;

// Container for information about each local video file.
public class Video {
    private final Uri uri;
    private final String name;
    private final int duration;
    private final int size;
    private final String filePath;

    public Video(Uri uri, String name, int duration, int size, String filePath) {
        this.uri = uri;
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.filePath = filePath;
    }

    public Map<String, Object> getVideoInfo() {
        Map<String, Object> videoInfo = new HashMap<>();
        videoInfo.put("name", this.name);
        videoInfo.put("duration", this.duration);
        return videoInfo;
    }

    public String getName() {
        return this.name;
    }

    public Uri getVideoURI() {
        return this.uri;
    }

    public int getSize() {
        return this.size;
    }

    public String getFilePath() { return this.filePath; }
}
