package com.mattech.task_list;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mattech.task_list.adapters.TaskAdapter;
import com.mattech.task_list.models.Task;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskActionListener {
    private TaskViewModel viewModel;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        TaskAdapter taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        viewModel.getTasks().observe(this, taskAdapter::setTasks);
    }

    @Override
    public void taskStatusChanged(Task task) {
        viewModel.update(task);
    }
}
