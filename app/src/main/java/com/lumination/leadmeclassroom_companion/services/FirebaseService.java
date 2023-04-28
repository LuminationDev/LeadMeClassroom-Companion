package com.lumination.leadmeclassroom_companion.services;

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
import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;
import com.lumination.leadmeclassroom_companion.models.Learner;
import com.lumination.leadmeclassroom_companion.ui.login.LoginFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;
import com.lumination.leadmeclassroom_companion.ui.login.username.UsernameFragment;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;

/**
 * A service class responsible for maintain listeners on firebase collections.
 */
public class FirebaseService extends Service {
    private static final String TAG = "FirebaseService";
    private static final String CHANNEL_ID = "firebase_communication";
    private static final String CHANNEL_NAME = "Firebase_Communication";

    private static final DatabaseReference database = getDatabase();
    private static DatabaseReference roomReference;
    private static DatabaseReference taskReference;
    private static DatabaseReference packageReference;
    private static String roomCode;
    private static String uuid = "1234";

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
        removeFollower();
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
        return FirebaseDatabase.getInstance("https://browserextension-bc94e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
    }

    /**
     * Get the valid room code.
     * @return A string of the entered room code.
     */
    public static String getRoomCode() {
        return roomCode;
    }

    /**
     * Attempt to add a listener to a leader's collection on firebase. The functions collects the
     * room code from the loginViewModel, any errors that occur will be set in the loginViewModel
     * and displayed to the user.
     */
    public static void connectToRoom()
    {
        //Check that the room code is available and not null
        roomCode = ClassCodeFragment.mViewModel.getLoginCode().getValue();
        if(roomCode == null) {
            ClassCodeFragment.mViewModel.setErrorCode("Room code not entered.");
            return;
        }

        roomReference = database.child("classCode").child(roomCode).child("classCode");
        roomReference.addListenerForSingleValueEvent(roomListener);
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
     * A listener function that is attached to an active room. This checks if the room exists.
     */
    private static final ValueEventListener roomListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                ClassCodeFragment.mViewModel.setErrorCode("");

                //If no error load the username entry
                LoginFragment.childManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.subpage, UsernameFragment.class, null)
                        .commit();

                LoginFragment.childManager.executePendingTransactions();
            } else {
                ClassCodeFragment.mViewModel.setErrorCode("Room not found. Try again");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
            ClassCodeFragment.mViewModel.setErrorCode("Connection issue. Try again later");
        }
    };

    /**
     * Add a user with their details to the firebase database.
     * @param username A string of the new user's name.
     */
    public static void addFollower(String username) {
        MainActivity.getInstance().startLeadMeService();

        //TODO finish this off
        //Create a UUID and check it does not exist on firebase for the student assignment.

        //Create an entry in firebase for the new Android user
        Learner test = new Learner(username, uuid, DashboardFragment.mViewModel.getInstalledPackages().getValue());
        database.child("androidFollowers").child(uuid).setValue(test);

        //Add the additional firebase listeners
        taskReference = database.child("androidFollowers").child(uuid).child("tasks");
        taskReference.addValueEventListener(taskListener);

        packageReference = database.child("androidFollowers").child(uuid).child("toLoadPackage");
        packageReference.addValueEventListener(pushedPackageListener);
    }

    /**
     * A listener function that is attached to the allowed task list. This forms the list of
     * packages that a student is allowed to visit.
     */
    private static final ValueEventListener taskListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                Log.e("Task", snapshot.getValue().toString());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
        }
    };

    /**
     * A listener function that is attached to the package that has been selected by a leader. The
     * package is what the device should now load.
     */
    private static final ValueEventListener pushedPackageListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                if(snapshot.getValue() != null) {
                    String packageName = snapshot.getValue().toString();

                    Log.e("Package", packageName);
                    PackageManager.ChangeActivePackage(packageName);
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
        }
    };

    /**
     * Clear the firebase data associated with the user that is currently logging out.
     */
    public static void removeFollower() {
        database.child("androidFollowers").child(uuid).removeValue();

        if(taskReference != null) {
            taskReference.removeEventListener(taskListener);
        }
        if (packageReference != null) {
            packageReference.removeEventListener(pushedPackageListener);
        }
        if (roomReference != null) {
            roomReference.removeEventListener(roomListener);
        }
    }

    /**
     * Update the android follow entry with what the current package is on the local device.
     * @param packageName A String of the currently active package.
     */
    public static void updateCurrentPackage(String packageName) {
        database.child("androidFollowers").child(uuid).child("currentPackage").setValue(packageName);
    }
}
