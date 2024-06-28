package com.example.taskmanager3.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class TaskDetailsActivity extends AppCompatActivity {

    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_task_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView titleTextView = findViewById(R.id.textViewTitle);
        TextView descriptionTextView = findViewById(R.id.textViewDetails);
        ImageView checkMarkImage = findViewById(R.id.imageViewCheckMark);
        Button deleteButton = findViewById(R.id.buttonDeleteTask);

        // Retrieve task data from intent extras
        Intent intent = getIntent();
        if (intent != null) {
            String taskId = intent.getStringExtra("TASK_ID");
            if (taskId != null) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference taskRef = db.collection("tasks").document(taskId);

                registration = taskRef.addSnapshotListener((documentSnapshot, e) -> {
                    if (e != null) {
                        // Handle error
                        return;
                    }

                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Task task = documentSnapshot.toObject(Task.class);
                        if (task != null) {
                            task.setId(documentSnapshot.getId());  // Ensure the task ID is set

                            titleTextView.setText(task.getTitle());
                            descriptionTextView.setText(task.getDetails());

                            if (task.isCompleted()) {
                                checkMarkImage.setImageResource(R.drawable.task_checked);
                            } else {
                                checkMarkImage.setImageResource(R.drawable.task_unchecked);
                            }
                        }
                    }
                });

                deleteButton.setOnClickListener(v -> {
                    taskRef.delete()
                            .addOnSuccessListener(aVoid -> finish())
                            .addOnFailureListener(exception -> {
                                // Handle failure
                            });
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (registration != null) {
            registration.remove();
        }
    }
}
