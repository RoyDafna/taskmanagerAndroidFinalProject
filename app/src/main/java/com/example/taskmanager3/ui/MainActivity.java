package com.example.taskmanager3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskDB.TaskDatabase;
import com.example.taskmanager3.taskList.TaskRecyclerViewAdapter;
import com.example.taskmanager3.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FloatingActionButton fabAddTask;
    RecyclerView recyclerViewTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fabAddTask = binding.buttonAddTask;
        recyclerViewTasks = binding.recyclerViewTasks;

        recyclerViewTasks.setHasFixedSize(true);
        recyclerViewTasks.setLayoutManager(
                new LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        );

        fabAddTask.setOnClickListener(v -> {
            startActivity(new Intent(this, AddTaskActivity.class));
        });

        TaskDatabase db = TaskDatabase.getInstance(this);

        LiveData<List<Task>> taskListLiveData = db.taskDao().getAllTasks();

        TaskRecyclerViewAdapter taskAdapter = new TaskRecyclerViewAdapter();
        recyclerViewTasks.setAdapter(taskAdapter); // Set the adapter to your RecyclerView

        taskListLiveData.observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                taskAdapter.setTasks(tasks); // Update the adapter's dataset
                if (tasks.isEmpty()) {
                    binding.textViewNoTasks.setVisibility(View.VISIBLE);
                } else {
                    binding.textViewNoTasks.setVisibility(View.GONE);
                }
            }
        });
    }
}