package com.mattech.task_list.view_models;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import com.mattech.task_list.repositories.TaskRepository;
import com.mattech.task_list.models.Task;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private TaskRepository taskRepository;
    private LiveData<List<Task>> tasks;
    private MutableLiveData<Integer> taskCount;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        taskRepository = new TaskRepository(application);
        tasks = taskRepository.getTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public LiveData<Integer> getTaskCount() {
        return Transformations.switchMap(tasks, t -> {
            if (taskCount == null) {
                taskCount = new MutableLiveData<>();
            }
            taskCount.setValue(t.size());
            return taskCount;
        });
    }

    public void insertTask() {
        List<Task> tasks = this.tasks.getValue();
        if (tasks != null) {
            int taskNum = tasks.size() > 0 ? tasks.get(tasks.size() - 1).getId() + 1 : 1;
            Task task = new Task("Task" + taskNum);
            taskRepository.insertTask(task);
        }
    }

    public void update(Task task) {
        taskRepository.updateTask(task);
    }

    public void deleteTask(int adapterPosition) {
        if (tasks.getValue() != null) {
            taskRepository.deleteTask(tasks.getValue().get(adapterPosition));
        }
    }
}
