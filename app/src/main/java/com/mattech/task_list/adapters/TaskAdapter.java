package com.mattech.task_list.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mattech.task_list.R;
import com.mattech.task_list.ViewAnimator;
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
        holder.taskId.setTextColor(Color.WHITE);
        holder.taskName.setTextColor(Color.WHITE);
        holder.taskStatus.setTextColor(Color.WHITE);
        holder.taskActionBtn.setBackground(context.getDrawable(R.drawable.rounded_btn_background_white));
        holder.taskActionBtn.setVisibility(View.VISIBLE);
        switch (task.getStatus()) {
            case OPEN:
                holder.taskStatus.setText(context.getResources().getString(R.string.open));
                holder.itemView.setCardBackgroundColor(Color.WHITE);
                holder.taskId.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskName.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskStatus.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskActionBtn.setBackground(context.getDrawable(R.drawable.rounded_btn_background_color));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.start_travel));
                holder.taskActionBtn.setTextColor(Color.WHITE);
                if (!allTasksOpened) {
                    holder.taskActionBtn.setVisibility(View.INVISIBLE);
                }
                break;
            case TRAVELLING:
                holder.taskStatus.setText(context.getResources().getString(R.string.travelling));
                holder.itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStateTravelling, null));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.start_work));
                holder.taskActionBtn.setTextColor(context.getResources().getColor(R.color.colorStateTravelling, null));
                break;
            case WORKING:
                holder.taskStatus.setText(context.getResources().getString(R.string.working));
                holder.itemView.setCardBackgroundColor(context.getResources().getColor(R.color.colorStateWorking, null));
                holder.taskActionBtn.setText(context.getResources().getString(R.string.stop));
                holder.taskActionBtn.setTextColor(context.getResources().getColor(R.color.colorStateWorking, null));
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
    public void onBindViewHolder(TaskViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            Task.TaskStatus changedTaskStatus = (Task.TaskStatus) payloads.get(0);
            if (changedTaskStatus == Task.TaskStatus.OPEN) {
                ViewAnimator.animateViewAppearance(holder.taskActionBtn);
            } else {
                ViewAnimator.animateViewDisappearance(holder.taskActionBtn);
            }
        } else {
            onBindViewHolder(holder, position);
        }
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
        if (this.tasks.size() > 0) {
            int changePosition = getDifferencePosition(this.tasks, tasks);
            if (changePosition != -1) {
                allTasksOpened = tasks.get(changePosition).getStatus() == Task.TaskStatus.OPEN;
                this.tasks.set(changePosition, tasks.get(changePosition));
                for (int i = 0; i < this.tasks.size(); i++) {
                    if (i == changePosition) {
                        notifyItemChanged(changePosition);
                    } else {
                        notifyItemChanged(i, this.tasks.get(changePosition).getStatus());
                    }
                }
            }
        } else {
            this.tasks = tasks;
            allTasksOpened = tasks.stream().noneMatch(task -> task.getStatus() != Task.TaskStatus.OPEN);
            notifyDataSetChanged();
        }
    }

    private int getDifferencePosition(List<Task> original, List<Task> changed) {
        for (int i = 0; i < original.size() && i < changed.size(); i++) {
            if (original.get(i).getStatus() != changed.get(i).getStatus()) {
                return i;
            }
        }
        return -1;
    }
}
