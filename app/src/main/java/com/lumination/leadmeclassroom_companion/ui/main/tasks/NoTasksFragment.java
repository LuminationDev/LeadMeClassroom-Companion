package com.lumination.leadmeclassroom_companion.ui.main.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lumination.leadmeclassroom_companion.R;

public class NoTasksFragment extends Fragment {
    public final static String TAG = "NO_TASK_CARD";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.card_no_tasks, container, false);
        //TODO add listener for library button?
        return view;
    }
}
