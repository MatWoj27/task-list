package com.mattech.task_list;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.mattech.task_list.models.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private LiveData<List<Task>> tasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        tasks = TaskDatabase.getInstance(application).getTaskDao().getAllTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

}
