package com.lumination.leadmeclassroom_companion.services;

import static android.app.PendingIntent.FLAG_IMMUTABLE;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;
import com.lumination.leadmeclassroom_companion.utilities.Constants;

/**
 * A service class responsible for maintain listeners on firebase collections.
 */
public class LeadMeService extends Service {
    private static final String TAG = "LeadMeService";
    private static final String CHANNEL_ID = "LeadMe";
    private static final String CHANNEL_NAME = "LeadMe";

    private Intent lastIntent;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder.  We know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public LeadMeService getService() {
            // Return this instance of FirebaseService so clients can call public methods
            return LeadMeService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        //If there is a saved room code, automatically connect?
    }

    @Override
    public void onDestroy() {
        endForeground();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        lastIntent = intent;
        startForeground();
        return START_STICKY;
    }

    public void startForeground() {
        if (lastIntent.getAction().equals(Constants.ACTION_FOREGROUND)) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_NONE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(channel);
            final int notificationId = (int) System.currentTimeMillis();

            PendingIntent pReturnIntent = createPendingIntent(Constants.ACTION_RETURN);
            PendingIntent pDisconnectIntent = createPendingIntent(Constants.ACTION_DISCONNECT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
            Notification notification = notificationBuilder
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("LeadMe Companion Connected")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .addAction(android.R.drawable.ic_menu_revert,
                            "Return", pReturnIntent)
                    .addAction(android.R.drawable.ic_menu_revert,
                            "Disconnect", pDisconnectIntent)
                    .build();

            startForeground(notificationId, notification);
        } else {
            Log.e(TAG, "Action: " + lastIntent.getAction());

            String action = lastIntent.getAction();
            if(action.equals(Constants.ACTION_RETURN)) {
                try {
                    PackageManager.ReturnHome();
                }
                catch(Exception e) {
                    Log.e(TAG, e.toString());
                }
            } else if(action.equals(Constants.ACTION_DISCONNECT)) {
                Log.e(TAG, "Disconnect clicked: REMOVE USER FROM FIREBASE");
            }
        }
    }

    /**
     * Create a Pending Intent that will be attached to a button within the foreground service.
     * @param action An action to be passed to when the intent is triggered.
     * @return A pending intent that will trigger a result.
     */
    private PendingIntent createPendingIntent(String action) {
        Intent intent = new Intent(this, LeadMeService.class);
        intent.setAction(action);
        return PendingIntent.getService(this, 0,
                intent, FLAG_IMMUTABLE);
    }

    public void endForeground() {
        stopForeground(true);
        stopSelf();
    }
}
