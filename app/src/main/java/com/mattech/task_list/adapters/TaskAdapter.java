package com.mattech.task_list.adapters;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mattech.task_list.R;
import com.mattech.task_list.databinding.TaskItemBinding;
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
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TaskItemBinding binding = TaskItemBinding.inflate(inflater, parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
        holder.taskActionBtn.setBackground(context.getDrawable(R.drawable.rounded_btn_background_white));
        switch (task.getStatus()) {
            case OPEN:
                holder.changeState(R.string.open, R.color.white, R.string.start_travel, R.color.white);
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
        } else if (tasks.get(position).getStatus() == Task.TaskStatus.OPEN && !allTasksOpened) {
            holder.taskActionBtn.setVisibility(View.INVISIBLE);
        } else {
            holder.taskActionBtn.setVisibility(View.VISIBLE);
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

        private TaskItemBinding binding;

        TaskViewHolder(@NonNull TaskItemBinding binding) {
            super(binding.getRoot());
            this.itemView = (CardView) binding.getRoot();
            ButterKnife.bind(this, itemView);
            this.binding = binding;
        }

        void changeState(@StringRes int taskStatusId, @ColorRes int itemBackgroundColorId, @StringRes int buttonTextId, @ColorRes int buttonTextColorId) {
            taskStatus.setText(context.getString(taskStatusId));
            itemView.setCardBackgroundColor(context.getResources().getColor(itemBackgroundColorId, null));
            taskActionBtn.setText(context.getString(buttonTextId));
            taskActionBtn.setTextColor(context.getResources().getColor(buttonTextColorId, null));
        }

        void bind(Task task) {
            binding.setTask(task);
            binding.executePendingBindings();
        }
    }

    public void setTasks(List<Task> tasks) {
        if (this.tasks.size() == 0) {
            this.tasks = tasks;
            allTasksOpened = tasks.stream().noneMatch(task -> task.getStatus() != Task.TaskStatus.OPEN);
            notifyDataSetChanged();
        } else if (tasks.size() < this.tasks.size()) {
            int removedItemIndex = Task.getRemovedItemIndex(this.tasks, tasks);
            allTasksOpened = allTasksOpened || this.tasks.get(removedItemIndex).getStatus() != Task.TaskStatus.OPEN;
            this.tasks.remove(removedItemIndex);
            notifyItemRemoved(removedItemIndex);
        } else if (tasks.size() > this.tasks.size()) {
            this.tasks.add(tasks.get(tasks.size() - 1));
            notifyItemInserted(this.tasks.size() - 1);
        } else {
            int changedItemIndex = Task.getChangedItemIndex(this.tasks, tasks);
            if (changedItemIndex != -1) {
                allTasksOpened = tasks.get(changedItemIndex).getStatus() == Task.TaskStatus.OPEN;
                this.tasks.set(changedItemIndex, tasks.get(changedItemIndex));
                for (int i = 0; i < this.tasks.size(); i++) {
                    notifyItemChanged(i, changedItemIndex);
                }
            }
        }
    }

    private synchronized void taskClicked(Task task) {
        if (allTasksOpened || task.getStatus() != Task.TaskStatus.OPEN) {
            allTasksOpened = false;
            Task newTask = new Task(task.getId(), task.getName(), Task.nextStatus(task));
            listener.taskStatusChanged(newTask);
        }
    }
}
