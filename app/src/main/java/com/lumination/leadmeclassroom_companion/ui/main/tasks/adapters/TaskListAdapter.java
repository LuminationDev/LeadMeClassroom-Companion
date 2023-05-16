package com.lumination.leadmeclassroom_companion.ui.main.tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.CardListTaskBinding;
import com.lumination.leadmeclassroom_companion.models.Task;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.ui.main.tasks.TaskSingleFragment;
import com.lumination.leadmeclassroom_companion.utilities.TaskHelpers;

import java.util.ArrayList;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    public List<CardListTaskBinding> taskBindings = new ArrayList<>();
    public List<Task> taskList = DashboardFragment.mViewModel.getPushedPackages().getValue();

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CardListTaskBinding binding = CardListTaskBinding.inflate(layoutInflater, parent, false);
        return new TaskListAdapter.TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = getItem(position);
        ((TaskViewHolder) holder).bind(task, this);
    }

    @Override
    public int getItemCount() {
        return taskList != null ? taskList.size() : 0;
    }

    public Task getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public long getItemId(int position) {
        //return taskList.get(position).id;
        return 0;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {
        private final CardListTaskBinding binding;
        public TaskViewHolder(@NonNull CardListTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task, TaskListAdapter listAdapter) {
            binding.setTask(task);

            View finalResult = binding.getRoot();

            ImageView imageView = finalResult.findViewById(R.id.application_icon);
            TaskHelpers.setIconOrDefault(imageView, task.type, task.link);

            finalResult.setOnClickListener(v -> {
                DashboardFragment.mViewModel.setSelectedTask(task);
                DashboardFragment.childManager.beginTransaction()
                        .replace(R.id.task_container, TaskSingleFragment.class, null, TaskSingleFragment.TAG)
                        .addToBackStack(TaskSingleFragment.TAG)
                        .commit();
                DashboardFragment.childManager.executePendingTransactions();
            });

            taskBindings.add(binding);
        }
    }
}
