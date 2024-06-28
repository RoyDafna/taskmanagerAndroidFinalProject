package com.example.taskmanager3.taskDB;

import com.google.firebase.firestore.FirebaseFirestore;


public class FirestoreHelper {

    private FirebaseFirestore db;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void addTask(Task task, OnTaskAddedListener listener) {
        db.collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> {
                    task.setId(documentReference.getId());
                    listener.onSuccess(task);
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public void updateTaskCompletion(String taskId, boolean isCompleted, OnTaskUpdateListener listener) {
        db.collection("tasks").document(taskId)
                .update("completed", isCompleted)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public void getTaskById(String taskId, OnTaskFetchedListener listener) {
        db.collection("tasks").document(taskId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    Task task = documentSnapshot.toObject(Task.class);
                    if (task != null) {
                        task.setId(documentSnapshot.getId());
                        listener.onSuccess(task);
                    } else {
                        listener.onFailure(new Exception("Task not found"));
                    }
                })
                .addOnFailureListener(listener::onFailure);
    }

    public interface OnTaskAddedListener {
        void onSuccess(Task task);
        void onFailure(Exception e);
    }

    public interface OnTaskUpdateListener {
        void onSuccess();
        void onFailure(Exception e);
    }

    public interface OnTaskFetchedListener {
        void onSuccess(Task task);
        void onFailure(Exception e);
    }
}
