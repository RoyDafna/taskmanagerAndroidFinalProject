package com.example.taskmanager3.taskDB;

public class Task {
    private String id;
    private String title;
    private String details;
    private boolean completed;
    // Constructor, getters, and setters

    public Task() {
        // Default constructor required for calls to DataSnapshot.getValue(Task.class)
    }

    public Task(String title, String details, boolean completed) {
        this.title = title;
        this.details = details;
        this.completed = completed;
    }

    // Add getters and setters for all fields including userId

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

}
