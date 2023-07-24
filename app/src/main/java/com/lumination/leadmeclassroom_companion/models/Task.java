package com.lumination.leadmeclassroom_companion.models;

import com.lumination.leadmeclassroom_companion.utilities.TaskHelpers;

import java.util.List;
import java.util.Objects;

public class Task {
    public String name;
    public String link; //either a packageName or website link
    public String displayType;
    public String type;
    public List<String> tags;

    public Task(String name, String link, String type, List<String> tags) {
        this.name = Objects.equals(name, "Website") ? TaskHelpers.getWebsiteNameFromUrl(link) : name;
        this.link = link;
        this.displayType = Objects.equals(type.toLowerCase(), "video_local") ? "Local Video": type;
        this.type = type;
        this.tags = tags;
    }
}
