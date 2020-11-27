package com.mattech.task_list.type_converters;

import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.mattech.task_list.models.Task;

public class TaskStatusConverter {

    @TypeConverter
    public static String statusToString(@NonNull Task.TaskStatus taskStatus) {
        return taskStatus.name();
    }

    @TypeConverter
    public static Task.TaskStatus stringToStatus(String name) {
        return Task.TaskStatus.valueOf(name);
    }
}
