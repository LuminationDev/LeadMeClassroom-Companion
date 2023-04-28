package com.lumination.leadmeclassroom_companion.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Learner {
    public String username;
    public String uuid;
    public String currentPackage = "com.lumination.leadmeclassroom_companion";
    public String toLoadPackage = "com.lumination.leadmeclassroom_companion";
    public List<String> applications;
    public List<String> tasks = new ArrayList<>(Collections.singletonList("Empty"));

    public Learner() {
        // Default constructor required for calls to DataSnapshot.getValue(Learner.class)
    }

    public Learner(String username, String uuid, List<String> applications) {
        this.username = username;
        this.uuid = uuid;
        this.applications = applications;
    }
}
