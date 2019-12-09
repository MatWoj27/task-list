package com.mattech.task_list.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mattech.task_list.R;
import com.mattech.task_list.adapters.TaskAdapter;
import com.mattech.task_list.models.Task;
import com.mattech.task_list.view_models.TaskViewModel;

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
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildViewHolder(view).getAdapterPosition();
                int itemCount = state.getItemCount();
                outRect.top = 16;
                outRect.bottom = position == itemCount - 1 ? 16 : 0;
            }
        });
        TaskAdapter taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        viewModel.getTasks().observe(this, taskAdapter::setTasks);
    }

    @Override
    public void taskStatusChanged(Task task) {
        viewModel.update(task);
    }
}
