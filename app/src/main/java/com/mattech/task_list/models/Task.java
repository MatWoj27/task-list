package com.mattech.task_list.models;

public class Task {
    private int id;
    private String name;
    private TaskStatus status;

    public enum TaskStatus {
        OPEN,
        TRAVELLING,
        WORKING;
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
}
