package com.example.simpletodo.data.local;

import androidx.room.TypeConverter;

import com.example.simpletodo.data.models.Priority;

public class Converter {

    @TypeConverter
    public static String fromPriority(Priority priority) {
        return priority.name();
    }

    @TypeConverter
    public static Priority toPriority(String priority) {
        return Priority.valueOf(priority);
    }
}
