package com.example.taskmanager3.taskDB;

import android.os.Debug;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreHelper {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    public FirestoreHelper() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public void addTask(Task task, OnTaskAddedListener listener) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("tasks")
                .add(task)
                .addOnSuccessListener(documentReference -> {
                    String taskId = documentReference.getId();
                    task.setId(taskId);
                    documentReference.set(task)
                            .addOnSuccessListener(aVoid -> listener.onSuccess(task))
                            .addOnFailureListener(e -> listener.onFailure(e));
                })
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public void updateTaskCompletion(String taskId, boolean isCompleted, OnTaskUpdateListener listener) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("tasks").document(taskId)
                .update("completed", isCompleted)
                .addOnSuccessListener(aVoid -> listener.onSuccess())
                .addOnFailureListener(e -> listener.onFailure(e));
    }

    public void getTaskById(String taskId, OnTaskFetchedListener listener) {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("tasks").document(taskId)
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
