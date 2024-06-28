package com.example.taskmanager3.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class TaskDetailsActivity extends AppCompatActivity {

    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

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
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userId = auth.getCurrentUser().getUid();
                DocumentReference taskRef = db.collection("users").document(userId).collection("tasks").document(taskId);

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
