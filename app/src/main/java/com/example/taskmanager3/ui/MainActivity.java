package com.example.taskmanager3.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskList.TaskRecyclerViewAdapter;
import com.example.taskmanager3.databinding.ActivityMainBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FloatingActionButton fabAddTask;
    RecyclerView recyclerViewTasks;
    TaskRecyclerViewAdapter taskAdapter;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Check if user is logged in
        if (auth.getCurrentUser() == null) {
            redirectToLogin();
            return;
        }

        fabAddTask = binding.buttonAddTask;
        recyclerViewTasks = binding.recyclerViewTasks;

        recyclerViewTasks.setHasFixedSize(true);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));

        fabAddTask.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTaskActivity.class));
        });

        taskAdapter = new TaskRecyclerViewAdapter();
        recyclerViewTasks.setAdapter(taskAdapter);

        observeFirestoreTasks();

        // Setup logout button
        binding.buttonLogout.setOnClickListener(v -> showLogoutConfirmationDialog());
    }

    private void redirectToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void observeFirestoreTasks() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("users").document(userId).collection("tasks")
                .addSnapshotListener((querySnapshot, error) -> {
                    if (error != null) {
                        // Handle error
                        return;
                    }

                    if (querySnapshot != null) {
                        List<Task> tasks = querySnapshot.toObjects(Task.class);
                        taskAdapter.setTasks(tasks);
                        updateUI(tasks.isEmpty());
                    }
                });
    }

    private void updateUI(boolean isEmpty) {
        if (isEmpty) {
            binding.textViewNoTasks.setVisibility(View.VISIBLE);
        } else {
            binding.textViewNoTasks.setVisibility(View.GONE);
        }
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.logout_confirmation_dialog, null);
        builder.setView(dialogView);

        // Initialize buttons from dialog layout
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);
        Button buttonConfirm = dialogView.findViewById(R.id.buttonConfirm);

        // Create and show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Set click listener for cancel button
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Dismiss the dialog if cancel is clicked
            }
        });

        // Set click listener for confirm (logout) button
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout operation here
                logout();
                dialog.dismiss(); // Dismiss the dialog after logout
            }
        });
    }

    private void logout() {
        auth.signOut();
        redirectToLogin();
    }
}
