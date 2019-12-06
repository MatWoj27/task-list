package com.mattech.task_list;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mattech.task_list.models.Task;
import com.mattech.task_list.models.TaskDao;

import java.util.concurrent.Executors;

@Database(entities = Task.class, version = 1)
public abstract class TaskDatabase extends RoomDatabase {
    private static volatile TaskDatabase database;

    public abstract TaskDao getTaskDao();

    public static TaskDatabase getInstance(final Context context) {
        if (database == null) {
            synchronized (TaskDatabase.class) {
                if (database == null) {
                    database = Room.databaseBuilder(context.getApplicationContext(), TaskDatabase.class, "task-database")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return database;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadScheduledExecutor().execute(() -> {
                Task[] initialTasks = new Task[20];
                for (int i = 0; i < 20; i++) {
                    initialTasks[i] = new Task("Task " + (i + 1));
                }
                database.getTaskDao().insertTasks(initialTasks);
            });
        }
    };
}
