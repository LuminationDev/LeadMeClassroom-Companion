package com.lumination.leadmeclassroom_companion.ui.main.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.MainPageDashboardBinding;
import com.lumination.leadmeclassroom_companion.ui.main.MainFragment;
import com.lumination.leadmeclassroom_companion.ui.main.tasks.NoTasksFragment;
import com.lumination.leadmeclassroom_companion.ui.main.tasks.TaskSelectionFragment;

public class DashboardFragment extends Fragment {
    public final static String TAG = "DASHBOARD_FRAGMENT";

    public static FragmentManager childManager;

    private MainPageDashboardBinding binding;
    public static DashboardViewModel mViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setMain(mViewModel);

        setupTasks();

        view.findViewById(R.id.task_carousel_menu).setOnClickListener(v -> mViewModel.setPackageListType("carousel"));
        view.findViewById(R.id.task_list_menu).setOnClickListener(v -> mViewModel.setPackageListType("list"));

        mViewModel.getPushedPackages().observe(getViewLifecycleOwner(), packages -> {
            Log.e("Num of packages", String.valueOf(packages.size()));
            setupTasks();
        });

        mViewModel.getPackageListType().observe(getViewLifecycleOwner(), listType -> {
            Log.e("List Type", listType);
            setupTasks();
        });

        //TODO used for testing
//        if(Objects.requireNonNull(mViewModel.getPushedPackages().getValue()).size() == 0) {
//            List<Task> test = new ArrayList<>();
//            Task task = new Task("Settings", "Media type1", "com.android.settings", null);
//            Task task2 = new Task("Gallery", "Media type2", "com.miui.gallery", null);
//            Task task3 = new Task("Compass", "Media type3", "com.miui.compass", null);
//            test.add(task);
//            test.add(task2);
//            test.add(task3);
//
//            MainActivity.runOnUIDelay(() -> mViewModel.setPushedPackages(test), 4000);
//        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_page_dashboard, container, false);
        childManager = getChildFragmentManager();
        binding = DataBindingUtil.bind(view);

        view.findViewById(R.id.side_menu_button).setOnClickListener(v -> MainFragment.openSideMenu());
        return view;
    }

    /**
     * Detect how many tasks may already be present, i.e. a student joining the class late, and present
     * the correct task layout.
     */
    private void setupTasks() {
        if(mViewModel.getPushedPackages().getValue() == null) {
            childManager.beginTransaction()
                    .replace(R.id.task_container, NoTasksFragment.class, null, NoTasksFragment.TAG)
                    .commitNow();
        } else if (mViewModel.getPushedPackages().getValue().size() == 0) {
            childManager.beginTransaction()
                    .replace(R.id.task_container, NoTasksFragment.class, null, NoTasksFragment.TAG)
                    .commitNow();
        } else if (mViewModel.getPackageListType().getValue().equals("list")) {
            Bundle args = new Bundle();
            args.putString("type", "list");
            childManager.beginTransaction()
                    .replace(R.id.task_container, TaskSelectionFragment.class, args, TaskSelectionFragment.TAG)
                    .commitNow();
        } else {
            Bundle args = new Bundle();
            args.putString("type", "carousel");
            childManager.beginTransaction()
                    .replace(R.id.task_container, TaskSelectionFragment.class, args, TaskSelectionFragment.TAG)
                    .commitNow();
        }
    }
}
