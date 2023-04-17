package com.lumination.leadmeclassroom_companion.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.FragmentMainBinding;

public class MainFragment extends Fragment {

    private FragmentMainBinding binding;
    public static MainViewModel mViewModel;

    private static MainFragment instance;
    public static MainFragment getInstance() { return instance; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setMain(mViewModel);

        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        binding = DataBindingUtil.bind(view);

        return view;
    }
}