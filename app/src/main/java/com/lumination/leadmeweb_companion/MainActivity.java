package com.lumination.leadmeweb_companion;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.lumination.leadmeweb_companion.services.FirebaseService;
import com.lumination.leadmeweb_companion.ui.login.LoginFragment;
import com.lumination.leadmeweb_companion.ui.login.LoginViewModel;

public class MainActivity extends AppCompatActivity {
    public static MainActivity instance;
    public static MainActivity getInstance() { return instance; }

    public static Handler UIHandler;
    static { UIHandler = new Handler(Looper.getMainLooper()); }
    public static FragmentManager fragmentManager;

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

        //TODO needs to have google-services.json added
        //startFirebaseService();
        preloadViewModels();

        if (savedInstanceState == null) {
            setupFragmentManager();
        }

        instance = this;
    }

    /**
     * Populate any static ViewModels so that information is available as soon as the
     * fragment is loaded.
     */
    private void preloadViewModels() {
        LoginFragment.mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
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
     * Start the firebase service.
     */
    private void startFirebaseService() {
        Intent network_intent = new Intent(getApplicationContext(), FirebaseService.class);
        startForegroundService(network_intent);
    }

    /**
     * Detect if a service is currently running, this would be used in case of a restart or non-fatal
     * crash.
     * @param serviceClass The type of service class that is being queried.
     * @return A boolean for if the supplied class is running.
     */
    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}