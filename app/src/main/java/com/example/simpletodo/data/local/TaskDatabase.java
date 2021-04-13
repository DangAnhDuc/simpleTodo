package com.example.simpletodo.data.local;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.simpletodo.data.models.TaskItem;

import static com.example.simpletodo.util.Constant.DB_VERSION;

@Database(entities = {TaskItem.class},version = DB_VERSION,exportSchema = false)
@TypeConverters(Converter.class)
public  abstract class TaskDatabase extends RoomDatabase {
    public abstract TaskDAO taskDAO();
}
