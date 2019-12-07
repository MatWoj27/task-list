package com.mattech.task_list.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mattech.task_list.R;
import com.mattech.task_list.models.Task;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
    private Context context;
    private List<Task> tasks = new ArrayList<>();
    private TaskActionListener listener;
    private boolean allTasksOpened = true;

    public interface TaskActionListener {
        void taskStatusChanged(Task task);
    }

    public TaskAdapter(Context context) {
        if (context instanceof TaskActionListener) {
            this.context = context;
            listener = (TaskActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskId.setText(String.valueOf(task.getId()));
        holder.taskName.setText(task.getName());
        holder.taskActionBtn.setVisibility(View.VISIBLE);
        switch (task.getStatus()) {
            case OPEN:
                holder.taskStatus.setText(context.getResources().getString(R.string.open));
                holder.itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.start_travel));
                if (!allTasksOpened) {
                    holder.taskActionBtn.setVisibility(View.INVISIBLE);
                }
                break;
            case TRAVELLING:
                holder.taskStatus.setText(context.getResources().getString(R.string.travelling));
                holder.itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStateTravelling, null));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.start_work));
                break;
            case WORKING:
                holder.taskStatus.setText(context.getResources().getString(R.string.working));
                holder.itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStateWorking, null));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.stop));
                break;
            default:
                throw new IllegalArgumentException("Could not recognize status");
        }
        holder.taskActionBtn.setOnClickListener(v -> {
            Task newTask = new Task(task.getId(), task.getName(), Task.nextStatus(task));
            listener.taskStatusChanged(newTask);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_id)
        TextView taskId;

        @BindView(R.id.task_name)
        TextView taskName;

        @BindView(R.id.task_status)
        TextView taskStatus;

        @BindView(R.id.task_action_btn)
        Button taskActionBtn;

        CardView itemView;

        TaskViewHolder(View itemView) {
            super(itemView);
            this.itemView = (CardView) itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        allTasksOpened = tasks.stream().noneMatch(task -> task.getStatus() != Task.TaskStatus.OPEN);
        notifyDataSetChanged();
    }
}
