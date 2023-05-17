package com.lumination.leadmeclassroom_companion.managers;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class PermissionManager {
    public static final int STORAGE_PERMISSION_CODE = 100;
    private static final String STORAGE_PERMISSION = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ?
            android.Manifest.permission.READ_MEDIA_VIDEO : Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();

    public static void checkForPermissions() {
        checkForOverlayPermission();
        checkForUsageStatPermission();
        checkForStoragePermission();
    }

    /**
     * Check if the ACTION_MANAGE_OVERLAY_PERMISSION permission has been granted for this application.
     * Prompt the user to accept them if not.
     */
    private static boolean checkForOverlayPermission() {
        boolean granted = Settings.canDrawOverlays(MainActivity.getInstance());
        ClassCodeFragment.mViewModel.setOverlayPermission(granted);

        return granted;
    }

    /**
     * Prompt the user to accept the overlay permission.
     */
    public static void askForOverlayPermission() {
        pullUserBack(PermissionManager::checkForOverlayPermission);

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + MainActivity.getInstance().getPackageName()));
        MainActivity.getInstance().startActivity(intent);
    }

    /**
     * Check if the PACKAGE_USAGE_STATS permission has been granted for this application.
     */
    private static boolean checkForUsageStatPermission() {
        AppOpsManager appOpsManager = (AppOpsManager) MainActivity.getInstance().getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOpsManager.unsafeCheckOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), MainActivity.getInstance().getPackageName());
        boolean granted = mode == AppOpsManager.MODE_ALLOWED;

        ClassCodeFragment.mViewModel.setUsageStatPermission(granted);

        return granted;
    }

    /**
     * Prompt the user to accept the usage stat permission.
     */
    public static void askForUsageStatPermission() {
        pullUserBack(PermissionManager::checkForUsageStatPermission);

        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        MainActivity.getInstance().startActivity(intent);
    }

    /**
     * Ask the user for storage permission if it has not already been granted. For older devices the
     * READ_EXTERNAL_STORAGE is required whilst SDK 33 and above includes the new READ_MEDIA_VIDEO.
     */
    private static void checkForStoragePermission() {
        boolean granted = ContextCompat.checkSelfPermission(MainActivity.getInstance(), STORAGE_PERMISSION)
                == android.content.pm.PackageManager.PERMISSION_GRANTED;

        ClassCodeFragment.mViewModel.setStoragePermission(granted);
    }

    /**
     * Prompt the user to accept the storage permission.
     */
    public static void askForStoragePermission() {
        ActivityCompat.requestPermissions(MainActivity.getInstance(),
                new String[]{ STORAGE_PERMISSION },
                STORAGE_PERMISSION_CODE);
    }

    /**
     * After a permission has been granted pull the user back to the application if it is done within
     * a set period of time, otherwise stop the execution from running in the background forever.
     */
    private static void pullUserBack(Callable<Boolean> methodParam) {
        AtomicInteger attemptLimit = new AtomicInteger(30);
        AtomicInteger attempt = new AtomicInteger();

        scheduler.scheduleAtFixedRate
                (() -> MainActivity.runOnUI(() -> {
                    if(attempt.get() >= attemptLimit.get()) {
                        scheduler.shutdownNow();
                        return;
                    }
                    attempt.getAndIncrement();

                    try {
                        if(methodParam.call()) {
                            PackageManager.ReturnHome();
                            scheduler.shutdownNow();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }), 0, 1, TimeUnit.SECONDS);
    }
}
