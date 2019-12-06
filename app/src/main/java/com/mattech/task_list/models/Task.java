package com.mattech.task_list.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.mattech.task_list.TaskStatusConverter;

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

    public Task(String name) {
        this.name = name;
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
}