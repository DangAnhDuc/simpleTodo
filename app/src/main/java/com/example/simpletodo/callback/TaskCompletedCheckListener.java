package com.example.simpletodo.callback;

import com.example.simpletodo.data.models.TaskItem;

public interface TaskCompletedCheckListener {
    void onTaskCompletedCheck(boolean isChecked,int position, TaskItem taskItem);
}
