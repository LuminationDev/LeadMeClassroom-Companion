package com.lumination.leadmeclassroom_companion.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;

public class OverlayService extends Service {
    private WindowManager mWindowManager;
    private View mOverlayView;

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a new overlay view
        mOverlayView = new View(this);

        // Inflate the custom XML layout file
        LayoutInflater inflater = LayoutInflater.from(this);
        mOverlayView = inflater.inflate(R.layout.overlay_page, null);

        mOverlayView.findViewById(R.id.return_button).setOnClickListener(v -> {
            PackageManager.ReturnHome();
        });

        // Get the WindowManager service
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // Set layout parameters for the overlay view
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Add the overlay view to the WindowManager
        mWindowManager.addView(mOverlayView, layoutParams);

        MainActivity.runOnUIDelay(() -> mOverlayView.findViewById(R.id.return_button), 2000);
        //TODO DELETE
        //WindowManager.removeView(mOverlayView);
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
