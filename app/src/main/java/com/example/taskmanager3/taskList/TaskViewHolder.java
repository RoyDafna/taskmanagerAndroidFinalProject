package com.example.taskmanager3.taskList;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskDB.TaskDatabase;
import com.example.taskmanager3.ui.TaskDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewCheckMark;
    private TextView textViewTaskTitle;

    public TaskViewHolder(View itemView, List<Task> dataSet) {
        super(itemView);
        imageViewCheckMark = itemView.findViewById(R.id.imageViewCheckMark);
        textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
        TaskDatabase db = TaskDatabase.getInstance(itemView.getContext());

        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task clickedTask = dataSet.get(position);

                clickedTask.setCompleted(!clickedTask.isCompleted(), db.taskDao());

                if (clickedTask.isCompleted()) {
                    getImageViewCheckMark().setImageResource(R.drawable.task_checked);
                } else {
                    getImageViewCheckMark().setImageResource(R.drawable.task_unchecked);
                }
            }
        });

        itemView.setOnLongClickListener(view -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task clickedTask = dataSet.get(position);

            // Create an Intent to start TaskDetailsActivity
            Intent intent = new Intent(view.getContext(), TaskDetailsActivity.class);

            // Pass task data as extras
            intent.putExtra("TASK_ID", clickedTask.getId());

            // Start the activity
            view.getContext().startActivity(intent);

            return true;
        }
            return false;
    });
    }

    public ImageView getImageViewCheckMark() {
        return imageViewCheckMark;
    }

    public void setImageViewCheckMark(ImageView imageViewCheckMark) {
        this.imageViewCheckMark = imageViewCheckMark;
    }

    public TextView getTextViewTaskTitle() {
        return textViewTaskTitle;
    }

    public void setTextViewTaskTitle(TextView textViewTaskTitle) {
        this.textViewTaskTitle = textViewTaskTitle;
    }
}
