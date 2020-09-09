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
        switch (Task.TaskStatus.valueOf(name)) {
            case OPEN:
                return Task.TaskStatus.OPEN;
            case TRAVELLING:
                return Task.TaskStatus.TRAVELLING;
            case WORKING:
                return Task.TaskStatus.WORKING;
            default:
                throw new IllegalArgumentException("Could not recognize status");
        }
    }
}
