package com.lumination.leadmeclassroom_companion.utilities;

import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.lumination.leadmeclassroom_companion.MainActivity;

public class TaskHelpers {
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
}
