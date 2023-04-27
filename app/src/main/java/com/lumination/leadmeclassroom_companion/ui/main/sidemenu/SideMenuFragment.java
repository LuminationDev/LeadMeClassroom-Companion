package com.lumination.leadmeclassroom_companion.ui.main.sidemenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.ui.main.MainFragment;

public class SideMenuFragment extends Fragment {
    public final static String TAG = "SIDE_MENU";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.main_page_sidemenu, container, false);
        view.findViewById(R.id.non_menu_space).setOnClickListener(v -> MainFragment.removeMenu());

        //TODO add listener for name change

        //Log the student out
        view.findViewById(R.id.logout).setOnClickListener(v ->
                MainActivity.runOnUIDelay(() -> MainActivity.getInstance().logout(), 200)
        );
        return view;
    }
}