package com.example.simpletodo.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "task")
public class TaskItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TaskId")
    public int taskId;

    @ColumnInfo(name = "Title")
    public String title;

    @ColumnInfo(name = "Description")
    public String description;

    @ColumnInfo(name = "DueDate")
    public String dueDate;

    @ColumnInfo(name = "Priority")
    public Priority priority;

    @ColumnInfo(name = "Completed")
    public Boolean isCompleted;

    public TaskItem() {
        super();
    }

    public void setCompleted(Boolean completed) {
        isCompleted = completed;
    }
}
