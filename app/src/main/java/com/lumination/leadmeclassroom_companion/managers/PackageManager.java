package com.lumination.leadmeclassroom_companion.managers;

import android.content.Intent;
import android.util.Log;

import com.lumination.leadmeclassroom_companion.MainActivity;

/**
 * A class with the purpose of controlling which packages are currently active.
 */
public class PackageManager {
    private static final String TAG = "PackageManager";

    /**
     * Return the device back to the LeadMe companion main fragment.
     */
    public static void ReturnHome() {
        Log.e(TAG, "Returning home");

        Intent intent = new Intent(MainActivity.getInstance(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        MainActivity.getInstance().startActivity(intent);
    }

    /**
     * Change the currently active package on the device to the supplied one.
     * @param packageName A string representing the package name of the application to launch.
     */
    public static void ChangeActivePackage(String packageName) {
        Intent launchIntent = MainActivity.getInstance().getPackageManager()
                .getLaunchIntentForPackage(packageName);

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            MainActivity.getInstance().startActivity(launchIntent);
        }
    }
}
