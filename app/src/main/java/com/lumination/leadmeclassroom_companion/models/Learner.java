package com.lumination.leadmeclassroom_companion.models;

import java.util.List;

public class Learner {
    public String username;
    public String uuid;
    public List<String> applications;

    public Learner() {
        // Default constructor required for calls to DataSnapshot.getValue(Learner.class)
    }

    public Learner(String username, String uuid, List<String> applications) {
        this.username = username;
        this.uuid = uuid;
        this.applications = applications;
    }
}
