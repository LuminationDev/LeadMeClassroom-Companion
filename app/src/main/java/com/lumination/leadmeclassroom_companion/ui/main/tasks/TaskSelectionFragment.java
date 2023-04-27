package com.lumination.leadmeclassroom_companion.ui.main.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.ui.main.tasks.adapters.TaskCarouselAdapter;
import com.lumination.leadmeclassroom_companion.ui.main.tasks.adapters.TaskListAdapter;

public class TaskSelectionFragment extends Fragment {
    public final static String TAG = "TASK_SELECTION";
    public static FragmentManager childManager;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        childManager = getChildFragmentManager();

        View view;

        //Determine if loading the carousel or list adapter (default to carousel)
        Bundle bundle = getArguments();
        String type = bundle != null ? bundle.getString("type") : "carousel";

        if (type.equals("list")) {
            view = inflater.inflate(R.layout.adapter_task_list, container, false);
            setupRecyclerView(view);
        } else {
            view = inflater.inflate(R.layout.adapter_task_carousel, container, false);
            setupCarouselView(view);
        }

        return view;
    }

    /**
     * Load up the list view of the currently allowed packages.
     * @param view The current view associated with the fragment.
     */
    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.task_recycler_view);

        // Set the layout manager for the RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        TaskListAdapter adapter = new TaskListAdapter();
        recyclerView.setAdapter(adapter);
    }

    /**
     * Load up the carousel view of the currently allowed packages.
     * @param view The current view associated with the fragment.
     */
    private void setupCarouselView(View view) {
        ViewPager2 viewPager = view.findViewById(R.id.task_view_pager);

        TaskCarouselAdapter adapter = new TaskCarouselAdapter();
        viewPager.setAdapter(adapter);

        // Set the carousel to loop infinitely
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(Integer.MAX_VALUE / 2);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == 0 || currentItem == adapter.getItemCount() - 1) {
                    viewPager.setCurrentItem(currentItem == 0 ? Integer.MAX_VALUE / 2 : Integer.MAX_VALUE / 2 - 1, false);
                }
            }
        });
    }
}
