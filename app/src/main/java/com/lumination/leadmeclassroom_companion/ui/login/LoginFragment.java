package com.lumination.leadmeclassroom_companion.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;
import com.lumination.leadmeclassroom_companion.ui.login.username.UsernameFragment;

public class LoginFragment extends Fragment {
    public static FragmentManager childManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        childManager = getChildFragmentManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            //Determine if logging in or changing name
            Bundle bundle = getArguments();
            loadEntryFragment(bundle);
        }
    }

    /**
     * Load the initial entry point which is the class code entry screen.
     */
    private void loadEntryFragment(Bundle bundle) {
        String type = bundle != null ? bundle.getString("type") : null;

        childManager.beginTransaction()
                .replace(R.id.subpage,
                        type != null ? UsernameFragment.class : ClassCodeFragment.class,
                        type != null ? bundle : null)
                .commit();

        childManager.executePendingTransactions();
    }
}
