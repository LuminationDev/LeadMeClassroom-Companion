package com.lumination.leadmeclassroom_companion.ui.main.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.CardSelectedTaskBinding;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;

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

        CardSelectedTaskBinding binding = DataBindingUtil.bind(view);
        binding.setTask(DashboardFragment.mViewModel.getSelectedTask().getValue());

        TextView back = view.findViewById(R.id.back_to_list);
        back.setOnClickListener(v -> {
            DashboardFragment.childManager.popBackStack();
            DashboardFragment.mViewModel.setSelectedTask(null);
        });

        //TODO add listener for launcher button?
        return view;
    }
}