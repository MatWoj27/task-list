package com.mattech.task_list.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mattech.task_list.repositories.TaskRepository;
import com.mattech.task_list.models.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        tasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void update(Task task) {
        taskRepository.updateTask(task);
    }
}