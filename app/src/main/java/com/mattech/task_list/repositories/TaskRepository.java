package com.mattech.task_list.repositories;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.mattech.task_list.daos.TaskDao;
import com.mattech.task_list.databases.TaskDatabase;
import com.mattech.task_list.models.Task;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> tasks;

    public TaskRepository(Application application) {
        taskDao = TaskDatabase.getInstance(application).getTaskDao();
        tasks = taskDao.getAllTasks();
    }

    public LiveData<List<Task>> getTasks() {
        return tasks;
    }

    public void updateTask(Task task) {
        UpdateTask updateTask = new UpdateTask(taskDao);
        updateTask.execute(task);
    }

    public void deleteTask(Task task) {
        DeleteTask deleteTask = new DeleteTask(taskDao);
        deleteTask.execute(task);
    }

    private static class UpdateTask extends AsyncTask<Task, Void, Void> {
        TaskDao dao;

        UpdateTask(@NonNull TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            dao.updateTask(tasks[0]);
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Task, Void, Void> {
        TaskDao dao;

        DeleteTask(@NonNull TaskDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Task... tasks) {
            dao.deleteTask(tasks[0]);
            return null;
        }
    }
}
