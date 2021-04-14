package com.example.simpletodo.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletodo.data.models.TaskItem;

import java.util.List;

import static com.example.simpletodo.util.Constant.TABLE_NAME_TASK;

@Dao
public interface TaskDAO {

    @Query("SELECT * FROM " + TABLE_NAME_TASK)
    List<TaskItem> fetchAllTaskItem();

    @Query("SELECT * FROM " + TABLE_NAME_TASK + " WHERE taskId = :taskId")
    TaskItem fetchTaskItemListById(int taskId);

    @Insert
    Long insertTaskItem(TaskItem taskItem);

    @Update
    int updateTaskItem(TaskItem taskItem);

    @Delete
    int deleteTaskItem(TaskItem taskItem);
}
