package com.lumination.leadmeclassroom_companion;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.lumination.leadmeclassroom_companion.models.Application;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;
import com.lumination.leadmeclassroom_companion.services.LeadMeService;
import com.lumination.leadmeclassroom_companion.services.OverlayService;
import com.lumination.leadmeclassroom_companion.services.PixelService;
import com.lumination.leadmeclassroom_companion.ui.login.LoginFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeViewModel;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardViewModel;
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

        hideAndroidBars();

        startFirebaseService();
        startPixelService();
        preloadViewModels();

        if (savedInstanceState == null) {
            setupFragmentManager();
        }

        //Load up the installed packages
        collectInstalledPackages();

        checkForUsageStatPermission();
        checkForOverlayPermission();

        instance = this;
    }

    /**
     * Hide the navigation and status bars.
     */
    private void hideAndroidBars() {
        WindowInsetsController controller = getWindow().getInsetsController();
        if (controller != null) {
            controller.hide(WindowInsets.Type.statusBars());
            controller.hide(WindowInsets.Type.navigationBars());

            controller.setSystemBarsAppearance(
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS |
                            WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
            );
            controller.setSystemBarsBehavior(
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
        }
    }

    /**
     * Check if the PACKAGE_USAGE_STATS permission has been granted for this application. Prompt the
     * user to accept them if not.
     */
    private void checkForUsageStatPermission() {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);

        AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        if (granted) {
            checkForOverlayPermission();
            return;
        }

        startActivity(intent);
    }

    /**
     * Check if the ACTION_MANAGE_OVERLAY_PERMISSION permission has been granted for this application.
     * Prompt the user to accept them if not.
     */
    private void checkForOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            return;
        }

        // The user has not granted permission yet, so ask for it
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    /**
     * Populate any static ViewModels so that information is available as soon as the
     * fragment is loaded.
     */
    private void preloadViewModels() {
        // Store a reference to the ViewModelProvider
        viewModelProvider = new ViewModelProvider(this);

        ClassCodeFragment.mViewModel = viewModelProvider.get(ClassCodeViewModel.class);
        DashboardFragment.mViewModel = viewModelProvider.get(DashboardViewModel.class);
    }

    /**
     * Reset the view model data fields back to the initial load in state.
     */
    private void clearViewModels() {
        ClassCodeFragment.mViewModel.resetData();
        DashboardFragment.mViewModel.resetData();
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
    public void startLeadMeService() {
        Intent leadMe_intent = new Intent(getApplicationContext(), LeadMeService.class);
        leadMe_intent.setAction(Constants.ACTION_FOREGROUND);
        startService(leadMe_intent);
    }

    /**
     * Start the Firebase service.
     */
    private void startFirebaseService() {
        Intent network_intent = new Intent(getApplicationContext(), FirebaseService.class);
        startForegroundService(network_intent);
    }

    /**
     * Start the Pixel service
     */
    public void startPixelService() {
        Intent pixelIntent = new Intent(getApplicationContext(), PixelService.class);
        startService(pixelIntent);
    }

    /**
     * Start the Overlay service and block a learner's screen.
     */
    public void startOverlayService() {
        Intent overlayIntent = new Intent(getApplicationContext(), OverlayService.class);
        startService(overlayIntent);
    }

    /**
     * Stop the Overlay service and unblock a learner's screen.
     */
    public void stopOverlayService() {
        Intent overlayIntent = new Intent(getApplicationContext(), OverlayService.class);
        stopService(overlayIntent);
    }

    /**
     * Query the device for the currently installed packages. Extract the packages names so they
     * can be sent to the teacher.
     */
    private void collectInstalledPackages() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> rawAppList = getApplicationContext()
                .getPackageManager()
                .queryIntentActivities(intent, 0);

        List<Application> packages = rawAppList.stream()
                .map(info -> new Application(
                        info.loadLabel(getPackageManager()).toString(),
                        info.activityInfo.packageName))
                .collect(Collectors.toList());

        DashboardFragment.mViewModel.setInstalledPackages(packages);
    }

    /**
     * Change the current audio settings on a device. This will either mute or un-mute the device
     * @param mute A boolean representing whether to mute(true) or unmute(false) the device.
     */
    public void changeAudioSettings(boolean mute) {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null) {
            int flag = mute ? AudioManager.ADJUST_MUTE : AudioManager.ADJUST_UNMUTE;

            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, flag, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_RING, flag, 0);
            audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, flag, 0);
            // mute other audio streams as needed?
        }
    }

    /**
     * Remove the user from the Firebase collection, reset all view model data fields and then
     * return to the login screen.
     */
    public void logout() {
        // Remove from firebase
        FirebaseService.removeFollower();

        // Reset view models
        clearViewModels();

        // Return to the class code screen - attempt to pop the entire back stack
        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        catch (Exception e) {
            Log.e("LOGOUT", e.toString());
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.class, null)
                .commitNow();
    }
}
