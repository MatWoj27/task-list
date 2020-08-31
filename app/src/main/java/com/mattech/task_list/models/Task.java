package com.mattech.task_list.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.mattech.task_list.type_converters.TaskStatusConverter;

import java.util.List;

@Entity(tableName = "tasks")
public class Task {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    @TypeConverters(TaskStatusConverter.class)
    private TaskStatus status = TaskStatus.OPEN;

    public enum TaskStatus {
        OPEN,
        TRAVELLING,
        WORKING;
    }

    @Ignore
    public Task(String name) {
        this.name = name;
    }

    public Task(int id, String name, TaskStatus status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public static TaskStatus nextStatus(@NonNull Task task) {
        switch (task.getStatus()) {
            case OPEN:
                return TaskStatus.TRAVELLING;
            case TRAVELLING:
                return TaskStatus.WORKING;
            case WORKING:
                return TaskStatus.OPEN;
            default:
                throw new IllegalArgumentException("Could not recognize status");
        }
    }

    public static int getRemovedItem(@NonNull List<Task> original, List<Task> changed) {
        for (int i = 0; i < original.size() && i < changed.size(); i++) {
            if (original.get(i).getId() != changed.get(i).getId()) {
                return i;
            }
        }
        return -1;
    }
}
