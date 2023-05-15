package com.lumination.leadmeclassroom_companion.models;

import java.util.List;

public class Task {
    public String name;
    public String link; //either a packageName or website link
    public String type;
    public List<String> tags;

    public Task(String name, String link, String type, List<String> tags) {
        this.name = name;
        this.link = link;
        this.type = type;
        this.tags = tags;
    }
}
