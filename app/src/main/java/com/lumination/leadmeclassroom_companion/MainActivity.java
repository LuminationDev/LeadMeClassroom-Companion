package com.lumination.leadmeclassroom_companion;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.lumination.leadmeclassroom_companion.managers.PermissionManager;
import com.lumination.leadmeclassroom_companion.models.Application;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;
import com.lumination.leadmeclassroom_companion.services.LeadMeService;
import com.lumination.leadmeclassroom_companion.services.ScreenBlockService;
import com.lumination.leadmeclassroom_companion.services.PixelService;
import com.lumination.leadmeclassroom_companion.ui.login.LoginFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeViewModel;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardViewModel;
import com.lumination.leadmeclassroom_companion.utilities.Constants;
import com.lumination.leadmeclassroom_companion.utilities.MediaHelpers;
import com.lumination.leadmeclassroom_companion.vrplayer.VRPlayerBroadcastReceiver;

import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private static MainActivity instance;
    public static MainActivity getInstance() { return instance; }

    public static Handler UIHandler;
    static { UIHandler = new Handler(Looper.getMainLooper()); }
    public static FragmentManager fragmentManager;
    public ViewModelProvider viewModelProvider;
    private VRPlayerBroadcastReceiver vrBroadcastReceiver;

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
        preloadViewModels();

        if (savedInstanceState == null) {
            setupFragmentManager();
        }

        instance = this;

        PermissionManager.checkForPermissions();
        //Load up the installed packages and video files
        collectInstalledPackages();
        MediaHelpers.collectVideoFiles();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PermissionManager.checkForPermissions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        logout();

        if(vrBroadcastReceiver != null) {
            unregisterReceiver(vrBroadcastReceiver);
        }
    }

    // This function is called when user accept or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when user is prompt for permission.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PermissionManager.STORAGE_PERMISSION_CODE) {
            boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
            ClassCodeFragment.mViewModel.setStoragePermission(granted);
            
            if (granted) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
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
     * Stop the Firebase service, this will send the disconnect command to the leader if there is an
     * active class.
     */
    public void stopFirebaseService() {
        Intent network_intent = new Intent(getApplicationContext(), FirebaseService.class);
        stopService(network_intent);
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
    public void startScreenBlockService() {
        Intent screenBlockIntent = new Intent(getApplicationContext(), ScreenBlockService.class);
        startService(screenBlockIntent);
    }

    /**
     * Stop the Overlay service and unblock a learner's screen.
     */
    public void stopScreenBlockService() {
        Intent screenBlockIntent = new Intent(getApplicationContext(), ScreenBlockService.class);
        stopService(screenBlockIntent);
    }

    /**
     * Listen for broadcasts from the associated VR player.
     */
    public void registerBroadcastReceiver() {
        if(vrBroadcastReceiver != null) return;

        vrBroadcastReceiver = new VRPlayerBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.lumination.VRPlayer");
        registerReceiver(vrBroadcastReceiver, intentFilter);
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

        //Collect the default home package as this will not be in the install package list by
        //default.
        final Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        final ResolveInfo res = getPackageManager().resolveActivity(homeIntent, 0);
        packages.add(new Application("Home Screen", res.activityInfo.packageName));

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
        // Stop firebase service and remove from class
        stopFirebaseService();

        // Reset view models
        clearViewModels();

        // Return to the class code screen - attempt to pop the entire back stack
        try {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction()
                    .replace(R.id.container, LoginFragment.class, null)
                    .commitNow();
        }
        catch (Exception e) {
            Log.e("LOGOUT", e.toString());
        }
    }
}
