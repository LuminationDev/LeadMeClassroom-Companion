package com.lumination.leadmeweb_companion.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lumination.leadmeweb_companion.R;
import com.lumination.leadmeweb_companion.ui.login.LoginFragment;

/**
 * A service class responsible for maintain listeners on firebase collections.
 */
public class FirebaseService extends Service {
    private static final String TAG = "FirebaseService";
    private static final String CHANNEL_ID = "firebase_communication";
    private static final String CHANNEL_NAME = "Firebase_Communication";

    private static final DatabaseReference database = getDatabase();
    private static DatabaseReference roomReference;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder.  We know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public FirebaseService getService() {
            // Return this instance of FirebaseService so clients can call public methods
            return FirebaseService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground();

        //If there is a saved room code, automatically connect?
    }

    @Override
    public void onDestroy() {
        endForeground();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startForeground() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_NONE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(channel);

        final int notificationId = (int) System.currentTimeMillis();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("FirebaseService is running in the foreground")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(notificationId, notification);
    }

    public void endForeground() {
        disconnectFromLeader();
        stopForeground(true);
        stopSelf();
    }

    /**
     * Collect the reference to the Classroom database.
     * @return A DatabaseReference of the connected Firebase database.
     */
    private static DatabaseReference getDatabase()
    {
        return FirebaseDatabase.getInstance("https://leafy-rope-301003-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    /**
     * Attempt to add a listener to a leader's collection on firebase. The functions collects the
     * room code from the loginViewModel, any errors that occur will be set in the loginViewModel
     * and displayed to the user.
     */
    public static void connectToRoom()
    {
        //Check that the room code is available and not null
        String roomCode = LoginFragment.mViewModel.getRoomCode().getValue();
        if(roomCode == null) {
            LoginFragment.mViewModel.setErrorCode("Room code not entered.");
            return;
        }

        roomReference = database.child(roomCode).child("room");
        roomReference.addListenerForSingleValueEvent(roomListener);

        //Add other listeners below
    }

    /**
     * If there is a current room reference, remove the listener to avoid any future commands being
     * received by mistake.
     */
    public static void disconnectFromLeader() {
        if(roomReference != null) {
            roomReference.removeEventListener(roomListener);
        }
    }

    /**
     * A listener function that is attached to an active room. This listens for
     */
    static ValueEventListener roomListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                LoginFragment.mViewModel.setErrorCode("");

                //Do something here

                //Move to the main fragment

            } else {
                LoginFragment.mViewModel.setErrorCode("Room not found. Try again");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
        }
    };
}
