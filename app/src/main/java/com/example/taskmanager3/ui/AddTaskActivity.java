package com.example.taskmanager3.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskDB.TaskDatabase;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_task);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextTaskTitle = findViewById(R.id.editTextTaskTitle);
        EditText editTextTaskDetails = findViewById(R.id.editTextTaskDetails);
        Button buttonSave = findViewById(R.id.buttonSaveTask);

        buttonSave.setEnabled(false); // Initially disable the save button
        buttonSave.setAlpha(0.5f);

        editTextTaskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed for this specific case
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Enable the save button if the title is not empty
                buttonSave.setEnabled(!s.toString().trim().isEmpty());

                if (buttonSave.isEnabled()) {
                    buttonSave.setAlpha(1f);
                }
                else {
                    buttonSave.setAlpha(0.5f);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Not needed for this specific case
            }
        });

        buttonSave.setOnClickListener(v -> {
            String taskTitle = editTextTaskTitle.getText().toString();
            String taskDetails = editTextTaskDetails.getText().toString();

            Task newTask = new Task(taskTitle, taskDetails, false);
            TaskDatabase db = TaskDatabase.getInstance(this);
            db.taskDao().insertTask(newTask);

            finish();
        });
    }
}