package com.lumination.leadmeclassroom_companion.ui.login.username;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.LoginPageUsernameBinding;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;
import com.lumination.leadmeclassroom_companion.ui.main.MainFragment;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;

public class UsernameFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    public FragmentManager childManager;
    private LoginPageUsernameBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setMain(DashboardFragment.mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        childManager = getChildFragmentManager();

        View view = inflater.inflate(R.layout.login_page_username, container, false);
        binding = DataBindingUtil.bind(view);

        //Track as the code is input into the pin entry area
        EditText text = view.findViewById(R.id.text_entry);
        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                DashboardFragment.mViewModel.setUsername(s.toString());
            }
        });

        view.findViewById(R.id.submit_username).setOnClickListener(v -> {
            try {
                //Slight delay to show full button animation
                MainActivity.runOnUIDelay(this::AddUserToFirebase, 200);
            } catch (NullPointerException ex) {
                Log.e(TAG, ex.toString());
            }
        });

        return view;
    }

    /**
     * After validating the username, add now validated room code to the mainViewModel
     * to be used for the rest of the application's life cycle.
     */
    private void AddUserToFirebase() {
        //TODO valid user against name list?

        DashboardFragment.mViewModel.setRoomCode(FirebaseService.getRoomCode());
        MainActivity.fragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.class, null)
                .commitNow();

        FirebaseService.addFollower(DashboardFragment.mViewModel.getUsername().getValue());
    }
}
