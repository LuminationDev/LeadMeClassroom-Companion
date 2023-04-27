package com.lumination.leadmeclassroom_companion.models;

import java.util.List;

public class Task {
    public String name;
    public String type;
    public String packageName;
    public List<String> tags;

    public Task(String name, String type, String packageName, List<String> tags) {
        this.name = name;
        this.type = type;
        this.packageName = packageName;
        this.tags = tags;
    }
}
