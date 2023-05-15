package com.lumination.leadmeclassroom_companion.managers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.vrplayer.VRPlayerManager;

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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
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

    /**
     * Open a website with the local web browser to the supplied website page.
     * @param website A string of the URL to load.
     */
    public static void ChangeActiveWebsite(String website) {
        //Make sure the website starts with http:// or https:// otherwise device does not know what to do.
        String temp = website;
        if(!website.startsWith("http")) {
            temp = "https://" + temp;
        }

        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(temp));
            browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
            browserIntent.addCategory(Intent.CATEGORY_DEFAULT);
            MainActivity.getInstance().startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            // Define what your app should do if no activity can handle the intent.
            Log.e(TAG, "Application not found: " + e);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    /**
     * Open the VR player, after an initial 3 seconds delay send through the source link.
     * @param link A string of the URL to load in the video player.
     */
    public static void ChangeActiveVideo(String link) {
        PackageManager.ChangeActivePackage(VRPlayerManager.packageName);
        //Change url to a safe link (no ':' otherwise cannot split properly)
        String safeLink = link.replaceAll(":", "|");

        MainActivity.runOnUIDelay(() -> {
            String action = "File path:" + safeLink + ":" + "1" + ":" + "Link";
            VRPlayerManager.newIntent(action);
        }, 3000);
    }

    /**
     * Change the current activity based on the media type supplied.
     * @param mediaType A string describing what application will handle the supplied value.
     * @param value A string of the package name, website url or video link.
     */
    public static void ChangeActivity(String mediaType, String value) {
        switch(mediaType) {
            case "Application":
                ChangeActivePackage(value);
                break;
            case "Website":
                ChangeActiveWebsite(value);
                break;
            case "Video":
                ChangeActiveVideo(value);
                break;
        }
    }
}
