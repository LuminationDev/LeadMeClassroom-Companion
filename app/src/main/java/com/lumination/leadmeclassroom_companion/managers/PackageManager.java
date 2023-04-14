package com.lumination.leadmeclassroom_companion.managers;

import android.util.Log;

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
        ChangeActivePackage("com.lumination.leadmeweb_companion");
    }

    /**
     * Change the currently active package on the device to the supplied one.
     * @param packageName A string representing the package name of the application to launch.
     */
    public static void ChangeActivePackage(String packageName) {
//        Intent launchIntent = MainActivity.getInstance().getPackageManager()
//                .getLaunchIntentForPackage(packageName);
//
//        if (launchIntent != null) {
//            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            MainActivity.getInstance().startActivity(launchIntent);
//        }

//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//        intent.setComponent(new ComponentName(packageName, "com.lumination.leadmeweb_companion.MainActivity"));
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//
//        RoleManager roleManager = MainActivity.getInstance().getSystemService(RoleManager.class);
//        if (roleManager.isRoleAvailable(RoleManager.ROLE_ASSISTANT)) {
//            Log.e(TAG, "ROLE REQUIRED");
//            Intent roleIntent = roleManager.createRequestRoleIntent(RoleManager.ROLE_ASSISTANT);
//            MainActivity.getInstance().startActivityForResult(roleIntent, 999);
//        } else {
//            MainActivity.getInstance().startActivity(intent);
//        }
    }
}
