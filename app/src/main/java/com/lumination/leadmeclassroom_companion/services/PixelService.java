package com.lumination.leadmeclassroom_companion.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.lumination.leadmeclassroom_companion.R;

public class PixelService extends Service {
    private WindowManager mWindowManager;
    private View mOverlayView;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a new overlay view
        mOverlayView = new View(this);

        // Inflate the custom XML layout file
        LayoutInflater inflater = LayoutInflater.from(this);
        mOverlayView = inflater.inflate(R.layout.overlay_pixel_page, null);

        // Get the WindowManager service
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Set layout parameters for the overlay view
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                1,
                1,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Set the gravity to BOTTOM
        layoutParams.gravity = Gravity.BOTTOM | Gravity.START;

        // Add the overlay view to the WindowManager
        mWindowManager.addView(mOverlayView, layoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove the overlay view from the WindowManager
        mWindowManager.removeView(mOverlayView);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
