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
            loadClassCodeEntry();
        }
    }

    /**
     * Load the initial entry point which is the class code entry screen.
     */
    private void loadClassCodeEntry() {
        childManager.beginTransaction()
                .replace(R.id.subpage, ClassCodeFragment.class, null)
                .commit();

        childManager.executePendingTransactions();
    }
}
