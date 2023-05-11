package com.lumination.leadmeclassroom_companion.ui.main.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.CardSelectedTaskBinding;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;
import com.lumination.leadmeclassroom_companion.models.Task;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.utilities.TaskHelpers;

public class TaskSingleFragment extends Fragment {
    public final static String TAG = "SINGLE_TASK_CARD";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.card_selected_task, container, false);
        Task task = DashboardFragment.mViewModel.getSelectedTask().getValue();

        //TODO supply a backup view?
        if(task == null) return view;

        CardSelectedTaskBinding binding = DataBindingUtil.bind(view);
        if(binding == null) return view;
        binding.setTask(task);

        View finalResult = binding.getRoot();

        ImageView imageView = finalResult.findViewById(R.id.application_icon);
        TaskHelpers.setApplicationIconOrDefault(imageView, task.link);

        TextView back = view.findViewById(R.id.back_to_list);
        back.setOnClickListener(v -> {
            DashboardFragment.childManager.popBackStack();
            DashboardFragment.mViewModel.setSelectedTask(null);
        });

        finalResult.findViewById(R.id.launch_button).setOnClickListener(v -> {
            PackageManager.ChangeActivePackage(task.link);
        });

        return view;
    }
}