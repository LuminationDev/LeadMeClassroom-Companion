package com.lumination.leadmeclassroom_companion.models;

import java.util.List;
import java.util.Map;

public class Learner {
    public String name;
    public String classcode;
    public String action = "none";
    public String currentPackage = "com.lumination.leadmeclassroom_companion";
    public List<Application> applications;
    public List<Map<String, Object>> videos;

    public Learner() {
        // Default constructor required for calls to DataSnapshot.getValue(Learner.class)
    }

    public Learner(String name, String uuid, List<Application> applications, List<Map<String, Object>> videos) {
        this.name = name;
        this.classcode = uuid;
        this.applications = applications;
        this.videos = videos;
    }
}
