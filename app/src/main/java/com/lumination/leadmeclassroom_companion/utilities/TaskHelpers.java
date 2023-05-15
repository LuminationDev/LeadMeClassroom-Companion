package com.lumination.leadmeclassroom_companion.utilities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.vrplayer.VRPlayerManager;

public class TaskHelpers {
    /**
     * Depending on the media type find and set a corresponding icon.
     * @param imageView An ImageView an icon will be set to.
     * @param mediaType A String of an tasks' media type.
     * @param packageName A String of an application package name.
     */
    public static void setIconOrDefault(ImageView imageView, String mediaType, String packageName) {
        switch (mediaType) {
            case "Application":
                setApplicationIconOrDefault(imageView, packageName);
                break;
            case "Website":
                setDefaultWebsiteIcon(imageView);
                break;
            case "Video":
                setVRPlayerIcon(imageView);
                break;
        }
    }

    /**
     * Query the local package manager for the icon associated with the supplied package name.
     * @param imageView An ImageView an icon will be set to.
     * @param packageName A String of an application package name.
     */
    public static void setApplicationIconOrDefault(ImageView imageView, String packageName) {
        try
        {
            Drawable icon = MainActivity.getInstance().getApplicationContext().getPackageManager().getApplicationIcon(packageName);
            if(icon != null) {
                imageView.setImageDrawable(icon);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Query the local package manager for the icon of default web browser.
     * @param imageView An ImageView an icon will be set to.
     */
    public static void setDefaultWebsiteIcon(ImageView imageView) {
        try
        {
            PackageManager pm = MainActivity.getInstance().getApplicationContext().getPackageManager();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
            ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            Drawable icon = resolveInfo.loadIcon(pm);

            if(icon != null) {
                imageView.setImageDrawable(icon);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Query the local package manager for the icon of the VR player.
     * @param imageView An ImageView an icon will be set to.
     */
    public static void setVRPlayerIcon(ImageView imageView) {
        try
        {
            Drawable icon = MainActivity.getInstance().getApplicationContext().getPackageManager().getApplicationIcon(VRPlayerManager.packageName);
            if(icon != null) {
                imageView.setImageDrawable(icon);
            }
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}
