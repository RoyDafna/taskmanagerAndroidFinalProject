package com.example.taskmanager3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskList.TaskRecyclerViewAdapter;
import com.example.taskmanager3.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FloatingActionButton fabAddTask;
    RecyclerView recyclerViewTasks;
    TaskRecyclerViewAdapter taskAdapter;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

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
    }

    private void observeFirestoreTasks() {
        db.collection("tasks")
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
}
