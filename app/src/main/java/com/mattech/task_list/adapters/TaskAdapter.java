package com.mattech.task_list.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mattech.task_list.R;
import com.mattech.task_list.utils.ViewAnimator;
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
        switch (task.getStatus()) {
            case OPEN:
                holder.changeState(R.string.open, R.color.white, R.string.start_travel, R.color.white);
                holder.taskId.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskName.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskStatus.setTextColor(context.getResources().getColor(R.color.colorStateOpened, null));
                holder.taskActionBtn.setBackground(context.getDrawable(R.drawable.rounded_btn_background_color));
                break;
            case TRAVELLING:
                holder.changeState(R.string.travelling, R.color.colorStateTravelling, R.string.start_work, R.color.colorStateTravelling);
                break;
            case WORKING:
                holder.changeState(R.string.working, R.color.colorStateWorking, R.string.stop, R.color.colorStateWorking);
                break;
            default:
                throw new IllegalArgumentException("Could not recognize status");
        }
        holder.taskActionBtn.setOnClickListener(v -> {
            taskClicked(task);
        });
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position, List<Object> payloads) {
        if (payloads != null && payloads.size() > 0) {
            int changedItemIndex = (int) payloads.get(0);
            if (position == changedItemIndex) {
                ViewAnimator.animateViewBounce(holder.taskActionBtn);
            } else if (tasks.get(changedItemIndex).getStatus() == Task.TaskStatus.OPEN) {
                ViewAnimator.animateViewAppearance(holder.taskActionBtn);
            } else if (tasks.get(changedItemIndex).getStatus() == Task.TaskStatus.TRAVELLING) {
                ViewAnimator.animateViewDisappearance(holder.taskActionBtn);
            }
        } else {
            if (tasks.get(position).getStatus() == Task.TaskStatus.OPEN && !allTasksOpened) {
                holder.taskActionBtn.setVisibility(View.INVISIBLE);
            } else {
                holder.taskActionBtn.setVisibility(View.VISIBLE);
            }
        }
        onBindViewHolder(holder, position);
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

        void changeState(@StringRes int taskStatusId, @ColorRes int itemBackgroundColorId, @StringRes int buttonTextId, @ColorRes int buttonTextColorId) {
            taskStatus.setText(context.getString(taskStatusId));
            itemView.setCardBackgroundColor(context.getResources().getColor(itemBackgroundColorId, null));
            taskActionBtn.setText(context.getString(buttonTextId));
            taskActionBtn.setTextColor(context.getResources().getColor(buttonTextColorId, null));
        }
    }

    public void setTasks(List<Task> tasks) {
        if (this.tasks.size() > 0) {
            int changedItemIndex = getDifferencePosition(this.tasks, tasks);
            if (changedItemIndex != -1) {
                allTasksOpened = tasks.get(changedItemIndex).getStatus() == Task.TaskStatus.OPEN;
                this.tasks.set(changedItemIndex, tasks.get(changedItemIndex));
                for (int i = 0; i < this.tasks.size(); i++) {
                    notifyItemChanged(i, changedItemIndex);
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

    private synchronized void taskClicked(Task task) {
        if (allTasksOpened || task.getStatus() != Task.TaskStatus.OPEN) {
            allTasksOpened = false;
            Task newTask = new Task(task.getId(), task.getName(), Task.nextStatus(task));
            listener.taskStatusChanged(newTask);
        }
    }
}
