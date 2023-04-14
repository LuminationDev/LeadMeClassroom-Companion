package com.lumination.leadmeclassroom_companion;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.lumination.leadmeclassroom_companion.services.FirebaseService;
import com.lumination.leadmeclassroom_companion.services.LeadMeService;
import com.lumination.leadmeclassroom_companion.ui.login.LoginFragment;
import com.lumination.leadmeclassroom_companion.ui.login.LoginViewModel;
import com.lumination.leadmeclassroom_companion.ui.main.MainFragment;
import com.lumination.leadmeclassroom_companion.ui.main.MainViewModel;
import com.lumination.leadmeclassroom_companion.utilities.Constants;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    public static MainActivity getInstance() { return instance; }

    public static Handler UIHandler;
    static { UIHandler = new Handler(Looper.getMainLooper()); }
    public static FragmentManager fragmentManager;
    public ViewModelProvider viewModelProvider;

    /**
     * un a function on the main UI thread.
     */
    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    /**
     * Run a function on the main UI thread with a set delay.
     * @param delay A long representing milliseconds to delay the function for.
     */
    public static void runOnUIDelay(Runnable runnable, long delay)  {
        UIHandler.postDelayed(runnable, delay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startFirebaseService();
        startLeadMeService();
        preloadViewModels();

        if (savedInstanceState == null) {
            setupFragmentManager();
        }

        //Load up the installed packages
        CollectInstalledPackages();

        instance = this;
    }

    /**
     * Populate any static ViewModels so that information is available as soon as the
     * fragment is loaded.
     */
    private void preloadViewModels() {
        // Store a reference to the ViewModelProvider
        viewModelProvider = new ViewModelProvider(this);

        LoginFragment.mViewModel = viewModelProvider.get(LoginViewModel.class);
        MainFragment.mViewModel = viewModelProvider.get(MainViewModel.class);
    }

    /**
     * Setup the static fragment manager, this means the application's screens can be controlled
     * from anywhere inside the application.
     */
    private void setupFragmentManager() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.class, null)
                .commitNow();
    }

    /**
     * Start the LeadMe service.
     */
    private void startLeadMeService() {
        Intent leadMe_intent = new Intent(getApplicationContext(), LeadMeService.class);
        leadMe_intent.setAction(Constants.ACTION_FOREGROUND);
        startService(leadMe_intent);
    }

    /**
     * Start the firebase service.
     */
    private void startFirebaseService() {
        Intent network_intent = new Intent(getApplicationContext(), FirebaseService.class);
        startForegroundService(network_intent);
    }

    /**
     * Query the device for the currently installed packages. Extract the packages names so they
     * can be sent to the teacher.
     */
    private void CollectInstalledPackages() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> rawAppList = getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, 0);

        List<String> packages = rawAppList.stream()
                .map(info -> info.activityInfo.packageName).collect(Collectors.toList());

        MainFragment.mViewModel.setInstalledPackages(packages);
    }
}
