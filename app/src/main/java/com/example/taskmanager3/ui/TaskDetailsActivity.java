package com.example.taskmanager3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskDB.TaskDatabase;

public class TaskDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        TextView titleTextView = findViewById(R.id.textViewTitle);
        TextView descriptionTextView = findViewById(R.id.textViewDetails);
        ImageView checkMarkImage = findViewById(R.id.imageViewCheckMark);

        // Retrieve task data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            int taskId = intent.getIntExtra("TASK_ID", -1);
            TaskDatabase db = TaskDatabase.getInstance(this);
            Task task = db.taskDao().getTaskById(taskId);

            // Set task title and description

            titleTextView.setText(task.getTitle());
            descriptionTextView.setText(task.getDescription());

            if (task.isCompleted()) {
                // Set image to checked
                checkMarkImage.setImageResource(R.drawable.task_checked);
            }
            else {
                // Set image to unchecked
                checkMarkImage.setImageResource(R.drawable.task_unchecked);
            }

            Button deleteButton = findViewById(R.id.buttonDeleteTask);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    db.taskDao().deleteTaskById(taskId);
                    finish();
                }
            });
        }


    }
}