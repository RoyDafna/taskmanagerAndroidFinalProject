package com.example.taskmanager3.taskDB;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;

public class Task {
    @DocumentId
    private String id;
    private String title;
    private String details;
    private boolean completed;

    public Task() {}

    public Task(String title, String details, boolean completed) {
        this.title = title;
        this.details = details;
        this.completed = completed;
    }

    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    @PropertyName("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("details")
    public String getDetails() {
        return details;
    }

    @PropertyName("details")
    public void setDetails(String details) {
        this.details = details;
    }

    @PropertyName("completed")
    public boolean isCompleted() {
        return completed;
    }

    @PropertyName("completed")
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
