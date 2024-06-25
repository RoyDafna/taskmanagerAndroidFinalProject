package com.example.taskmanager3.taskList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskViewHolder> {
    private final List<Task> dataSet;

    public TaskRecyclerViewAdapter() { // Constructor without initial dataset
        this.dataSet = new ArrayList<>();
    }

    public TaskRecyclerViewAdapter(List<Task> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_task, parent, false);
        return new TaskViewHolder(view, dataSet);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = dataSet.get(position);
        ImageView imageView = holder.getImageViewCheckMark();

        holder.getTextViewTaskTitle().setText(task.getTitle());

        if(task.isCompleted()) {
            imageView.setImageResource(R.drawable.task_checked);
        } else {
            imageView.setImageResource(R.drawable.task_unchecked);
        }
    }

    public void setTasks(List<Task> newDataSet) {
        this.dataSet.clear();
        this.dataSet.addAll(newDataSet);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
