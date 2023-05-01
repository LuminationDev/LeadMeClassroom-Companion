package com.lumination.leadmeclassroom_companion.ui.main.tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.CardCarouselTaskBinding;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;
import com.lumination.leadmeclassroom_companion.models.Task;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.utilities.TaskHelpers;

import java.util.ArrayList;
import java.util.List;

public class TaskCarouselAdapter extends RecyclerView.Adapter<TaskCarouselAdapter.TaskViewHolder> {
    public List<CardCarouselTaskBinding> taskBindings = new ArrayList<>();
    public List<Task> taskList = DashboardFragment.mViewModel.getPushedPackages().getValue();

    @NonNull
    @Override
    public TaskCarouselAdapter.TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        CardCarouselTaskBinding binding = CardCarouselTaskBinding.inflate(layoutInflater, parent, false);
        return new TaskCarouselAdapter.TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskCarouselAdapter.TaskViewHolder holder, int position) {
        Task task = getItem(position);
        ((TaskCarouselAdapter.TaskViewHolder) holder).bind(task, this);
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
        private final CardCarouselTaskBinding binding;
        public TaskViewHolder(@NonNull CardCarouselTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Task task, TaskCarouselAdapter listAdapter) {
            binding.setTask(task);

            View finalResult = binding.getRoot();

            ImageView imageView = finalResult.findViewById(R.id.application_icon);
            TaskHelpers.setApplicationIconOrDefault(imageView, task.packageName);

            finalResult.findViewById(R.id.launch_button).setOnClickListener(v -> {
                PackageManager.ChangeActivePackage(task.packageName);
            });

            taskBindings.add(binding);
        }
    }
}
