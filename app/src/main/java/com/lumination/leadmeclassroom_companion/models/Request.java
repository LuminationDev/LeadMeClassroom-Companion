package com.lumination.leadmeclassroom_companion.models;

public class Request {
    private String action;
    private String type;

    public Request() {
    }

    public Request(String action, String type) {
        this.action = action;
        this.type = type;
    }

    public String getAction() {
        return this.action;
    }

    public String getType() {
        return this.type;
    }
}
