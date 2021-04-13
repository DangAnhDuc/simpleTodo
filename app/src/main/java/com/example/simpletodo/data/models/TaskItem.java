package com.example.simpletodo.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "task")
public class TaskItem {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TaskId")
    public int taskId;

    @ColumnInfo(name = "Subject")
    public String subject;

    @ColumnInfo(name = "Description")
    public String description;

//    @ColumnInfo(name = "DueDate")
//    public Date dueDate;

    @ColumnInfo(name = "Priority")
    public Priority priority;

    @SuppressWarnings("unused")
    public TaskItem(){
        super();
    }

    public TaskItem(String subject){
        super();
        this.subject = subject;
    }

//    public int compareTo(TaskItem item) {
//
//        if(item.dueDate != null && this.dueDate == null) {
//            return 1;
//        }
//        if(item.dueDate == null && this.dueDate != null) {
//            return -1;
//        }
//
//        if(item.dueDate != null && this.dueDate != null && item.dueDate.compareTo(this.dueDate) > 0) {
//            return -1;
//        } else if(item.dueDate != null && this.dueDate != null && item.dueDate.compareTo(this.dueDate) < 0) {
//            return 1;
//        } else {
//            return this.priority.getValue() - item.priority.getValue();
//        }
//    }
}
