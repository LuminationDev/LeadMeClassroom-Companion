package com.lumination.leadmeclassroom_companion.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.ui.main.sidemenu.SideMenuFragment;

public class MainFragment extends Fragment {
    public static FragmentManager childManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        childManager = getChildFragmentManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            loadDashboardEntry();
        }
    }

    /**
     * Load the initial entry point after successfully logging into a class.
     */
    private void loadDashboardEntry() {
        childManager.beginTransaction()
                .replace(R.id.subpage, DashboardFragment.class, null)
                .commit();

        childManager.executePendingTransactions();
    }

    /**
     * Show or hide the side menu fragment.
     */
    public static void openSideMenu() {
        childManager.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_right)
                .replace(R.id.sidemenu, SideMenuFragment.class, null, SideMenuFragment.TAG)
                .addToBackStack(SideMenuFragment.TAG)
                .commit();

        childManager.executePendingTransactions();
    }

    /**
     * Remove the side menu if it is present.
     */
    public static void removeMenu() {
        Fragment fragment = childManager.findFragmentByTag(SideMenuFragment.TAG);

        if(fragment != null) {
            //The side menu should be the latest fragment added to the backstack
            childManager.popBackStack();
        }
    }
}
