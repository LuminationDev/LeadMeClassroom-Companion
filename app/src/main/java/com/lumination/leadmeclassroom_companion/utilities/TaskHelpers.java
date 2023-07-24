package com.lumination.leadmeclassroom_companion.utilities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.vrplayer.VRPlayerManager;

import java.net.MalformedURLException;
import java.net.URL;

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
            case "Video_local":
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
     * Get the domain name of a website from a supplied link.
     * @param urlString A string that is the URL attempting to find a domain name for.
     * @return A string of the domain name.
     */
    public static String getWebsiteNameFromUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            String host = url.getHost();
            return host.startsWith("www.") ? host.substring(4) : host;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
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
