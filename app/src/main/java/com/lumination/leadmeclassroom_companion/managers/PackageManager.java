package com.lumination.leadmeclassroom_companion.managers;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;

import java.io.ByteArrayOutputStream;
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

    public static void loadApplicationIcons(String apps) {
        String[] appList = apps.split(":");
        for (int i = 0; i < appList.length; i++) {
            try {
                /*
                 * We want to save as a jpeg so we can actually compress it, but
                 * unfortunately the transparent background will default to black
                 * in the jpeg. So we have to draw a white background on a bitmap
                 * and then draw the icon over the top.
                 */
                Drawable drawable = MainActivity.getInstance().getPackageManager().getApplicationIcon(appList[i]); // get app icon
                Bitmap icon = drawableToBitmap(drawable);
                Bitmap newBitmap = Bitmap.createBitmap(icon.getWidth(), // create new bitmap/canvas same size as icon
                        icon.getWidth(), icon.getConfig());
                Canvas canvas = new Canvas(newBitmap);
                canvas.drawColor(Color.WHITE); // white background for any transparent
                Rect dest = new Rect(0, 0, icon.getWidth(), icon.getHeight());
                Rect src = new Rect(0, 0, icon.getWidth(), icon.getHeight());
                canvas.drawBitmap(icon, src, dest, null); // draw the app icon over the top
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                newBitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream); // compress to 10% quality
                byte[] bitmapdata = stream.toByteArray();
                FirebaseService.uploadFile(appList[i] + ".JPG", bitmapdata);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
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
