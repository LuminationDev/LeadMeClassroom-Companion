package com.lumination.leadmeclassroom_companion.managers;

import android.content.ActivityNotFoundException;
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT );
        MainActivity.getInstance().startActivity(intent);
    }

    /**
     * Change the currently active package on the device to the supplied one.
     * @param packageName A string representing the package name of the application to launch.
     */
    public static void ChangeActivePackage(String packageName) {
        try {
            Intent launchIntent = MainActivity.getInstance().getPackageManager()
                    .getLaunchIntentForPackage(packageName);

            if (launchIntent != null) {
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                MainActivity.getInstance().startActivity(launchIntent);
            }
        } catch (ActivityNotFoundException e) {
            // Define what your app should do if no activity can handle the intent.
            Log.e(TAG, "Application not found: " + e);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
}
