package com.lumination.leadmeclassroom_companion.ui.main.tasks;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.CardSelectedTaskBinding;
import com.lumination.leadmeclassroom_companion.models.Task;
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
        Task task = DashboardFragment.mViewModel.getSelectedTask().getValue();

        //TODO supply a backup view?
        if(task == null) return view;

        CardSelectedTaskBinding binding = DataBindingUtil.bind(view);
        binding.setTask(task);

        try
        {
            Drawable icon = MainActivity.getInstance().getApplicationContext().getPackageManager().getApplicationIcon(task.packageName);
            ImageView imageView = view.findViewById(R.id.application_icon);
            imageView.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        TextView back = view.findViewById(R.id.back_to_list);
        back.setOnClickListener(v -> {
            DashboardFragment.childManager.popBackStack();
            DashboardFragment.mViewModel.setSelectedTask(null);
        });

        //TODO add listener for launcher button?
        MaterialButton launcher = view.findViewById(R.id.launch_button);
        launcher.setOnClickListener(v -> {
            try {
                Intent launchIntent = MainActivity.getInstance().getPackageManager()
                        .getLaunchIntentForPackage(task.packageName);

                if (launchIntent != null) {
                    launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                    MainActivity.getInstance().startActivity(launchIntent);
                }
            } catch (ActivityNotFoundException e) {
                // Define what your app should do if no activity can handle the intent.
                Log.e(TAG, "Application not found: " + e);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        });

        return view;
    }
}