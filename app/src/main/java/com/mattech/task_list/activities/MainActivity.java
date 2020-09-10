package com.mattech.task_list.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.mattech.task_list.R;
import com.mattech.task_list.adapters.TaskAdapter;
import com.mattech.task_list.databinding.ActivityMainBinding;
import com.mattech.task_list.models.Task;
import com.mattech.task_list.view_models.TaskViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskActionListener {
    private TaskViewModel viewModel;

    private ItemTouchHelper mainListItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            viewModel.deleteTask(viewHolder.getAdapterPosition());
        }
    });

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ButterKnife.bind(this);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        presetMainList();
    }

    private void presetMainList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                int position = parent.getChildViewHolder(view).getAdapterPosition();
                outRect.top = position == 0 ? 16 : 0;
                outRect.bottom = 16;
            }
        });
        mainListItemTouchHelper.attachToRecyclerView(recyclerView);
        TaskAdapter taskAdapter = new TaskAdapter(this);
        recyclerView.setAdapter(taskAdapter);
        viewModel.getTasks().observe(this, taskAdapter::setTasks);
    }

    @Override
    public void taskStatusChanged(Task task) {
        viewModel.update(task);
    }
}
