package com.lumination.leadmeclassroom_companion.models;

import java.util.List;

public class Task {
    public String name;
    public String type;
    public String link; //either a packageName or website link
    public List<String> tags;

    public Task(String name, String type, String link, List<String> tags) {
        this.name = name;
        this.type = type;
        this.link = link;
        this.tags = tags;
    }
}
