package com.lumination.leadmeclassroom_companion.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Learner {
    public String name;
    public String classcode;
    public String action = "None"; //FOR VR TESTING ONLY
    public String currentPackage = "com.lumination.leadmeclassroom_companion";
    public List<Application> applications;
    //public List<String> tasks = new ArrayList<>(Collections.singletonList("Empty"));

    public Learner() {
        // Default constructor required for calls to DataSnapshot.getValue(Learner.class)
    }

    public Learner(String name, String uuid, List<Application> applications) {
        this.name = name;
        this.classcode = uuid;
        this.applications = applications;
    }
}
