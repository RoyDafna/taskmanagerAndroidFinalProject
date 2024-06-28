package com.example.taskmanager3.taskList;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.taskmanager3.R;
import com.example.taskmanager3.taskDB.Task;
import com.example.taskmanager3.taskDB.FirestoreHelper;
import com.example.taskmanager3.ui.TaskDetailsActivity;

import java.util.List;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    private ImageView imageViewCheckMark;
    private TextView textViewTaskTitle;
    private FirestoreHelper firestoreHelper;

    public TaskViewHolder(View itemView, List<Task> dataSet) {
        super(itemView);
        imageViewCheckMark = itemView.findViewById(R.id.imageViewCheckMark);
        textViewTaskTitle = itemView.findViewById(R.id.textViewTaskTitle);
        firestoreHelper = new FirestoreHelper();

        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task clickedTask = dataSet.get(position);

                clickedTask.setCompleted(!clickedTask.isCompleted());

                // Update the task in Firestore
                firestoreHelper.updateTaskCompletion(clickedTask.getId(), clickedTask.isCompleted(), new FirestoreHelper.OnTaskUpdateListener() {
                    @Override
                    public void
                    onSuccess() {
                        if (clickedTask.isCompleted()) {
                            getImageViewCheckMark().setImageResource(R.drawable.task_checked);
                        } else {
                            getImageViewCheckMark().setImageResource(R.drawable.task_unchecked);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        // Handle failure
                    }
                });
            }
        });

        itemView.setOnLongClickListener(view -> {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Task clickedTask = dataSet.get(position);
                Intent intent = new Intent(view.getContext(), TaskDetailsActivity.class);
                intent.putExtra("TASK_ID", clickedTask.getId());
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
