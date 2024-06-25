package com.example.taskmanager3.taskDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import androidx.lifecycle.LiveData; // Import LiveData

@Dao
public interface TaskDao {
    @Query("SELECT * FROM tasks")
    LiveData<List<Task>> getAllTasks();

    @Insert
    void insertTask(Task task);

    @Delete
    void deleteTask(Task task);

    @Query("UPDATE tasks SET completed = :isCompleted WHERE id = :taskId")
    void updateCompletedStatus(int taskId, boolean isCompleted);

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);

    @Query("DELETE FROM tasks WHERE id = :taskId")
    void deleteTaskById(int taskId);
}