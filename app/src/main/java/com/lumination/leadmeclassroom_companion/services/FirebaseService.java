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
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.managers.PackageManager;
import com.lumination.leadmeclassroom_companion.models.Learner;
import com.lumination.leadmeclassroom_companion.models.Request;
import com.lumination.leadmeclassroom_companion.models.Task;
import com.lumination.leadmeclassroom_companion.ui.login.LoginFragment;
import com.lumination.leadmeclassroom_companion.ui.login.classcode.ClassCodeFragment;
import com.lumination.leadmeclassroom_companion.ui.login.username.UsernameFragment;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;
import com.lumination.leadmeclassroom_companion.vrplayer.VRPlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A service class responsible for maintain listeners on firebase collections.
 */
public class FirebaseService extends Service {
    private static final String TAG = "FirebaseService";
    private static final String CHANNEL_ID = "firebase_communication";
    private static final String CHANNEL_NAME = "Firebase_Communication";

    private static final DatabaseReference database = getDatabase();
    private static DatabaseReference roomReference;
    private static DatabaseReference nameReference;
    private static DatabaseReference taskReference;
    private static DatabaseReference allPackageReference;
    private static DatabaseReference individualPackageReference;

    private static final String followerRef = "mobileFollowers";
    private static final String messageRef = "mobileMessages";

    // Values for connecting to a class
    private static final String uuid = UUID.randomUUID().toString();
    private static String roomCode;

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
        MainActivity.getInstance().registerBroadcastReceiver();

        //Create an entry in firebase for the new Android user
        Learner test = new Learner(username, roomCode, DashboardFragment.mViewModel.getInstalledPackages().getValue());
        database.child(followerRef).child(roomCode).child(uuid).setValue(test);

        //Listen for remote name changes
        nameReference = database.child(followerRef).child(roomCode).child(uuid).child("name");
        nameReference.addValueEventListener(nameListener);

        //Listen for tasks (groups of applications) being set by a leader
        taskReference = database.child(followerRef).child(roomCode).child(uuid).child("tasks");
        taskReference.addValueEventListener(taskListener);

        //Listen for both individual actions and group wide actions
        allPackageReference = database.child("classCode").child(roomCode).child("request").child(messageRef);
        individualPackageReference = database.child(followerRef).child(roomCode).child(uuid).child("request");
        allPackageReference.addChildEventListener(requestListener);
        individualPackageReference.addChildEventListener(requestListener);
    }

    /**
     * A listener function that is attached to the allowed task list. This forms the list of
     * packages that a student is allowed to visit.
     */
    private static final ValueEventListener taskListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                if(snapshot.getValue() != null) {
                    Log.e("Tasks", snapshot.getValue().toString());

                    List<String> pushedTasks = (List<String>) snapshot.getValue();

                    List<Task> tasks = pushedTasks.stream()
                            .map(pushedTask -> {
                                String[] split = pushedTask.split("\\|");
                                return new Task(split[0], split[1], split[2], null);
                            })
                            .collect(Collectors.toList());

                    DashboardFragment.mViewModel.setPushedPackages(tasks.isEmpty() ? Collections.emptyList() : tasks);
                } else {
                    DashboardFragment.mViewModel.setPushedPackages(new ArrayList<>());
                }
            } else {
                Log.e("Tasks", "No tasks");
                DashboardFragment.mViewModel.setPushedPackages(new ArrayList<>());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
        }
    };

    /**
     * A listener function that is attached to the learners name field.
     */
    private static final ValueEventListener nameListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()) {
                if(snapshot.getValue() == null) return;

                Log.e("Task", snapshot.getValue().toString());
                DashboardFragment.mViewModel.setUsername(snapshot.getValue().toString());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            Log.e(TAG, "Database connection cancelled.");
        }
    };

    /**
     * A listener function that is attached to the latest request that has been made by a leader. The
     * action is what the device should now do.
     */
    private static final ChildEventListener requestListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if(snapshot.exists()) {
                if(snapshot.getValue() != null) {
                    Request request = snapshot.getValue(Request.class);

                    if(request == null) return;

                    switch(request.getType()) {
                        case "application":
                            if(request.getAction().equals(MainActivity.getInstance().getPackageName())) {
                                PackageManager.ReturnHome();
                            } else {
                                PackageManager.ChangeActivePackage(request.getAction());
                            }
                            break;

                        case "website":
                            PackageManager.ChangeActiveWebsite(request.getAction());
                            break;

                        case "video":
                            PackageManager.ChangeActivePackage(VRPlayerManager.packageName);
                            //Change url to a safe link (no ':' otherwise cannot split properly)
                            String safeLink = request.getAction().replaceAll(":", "|");

                            MainActivity.runOnUIDelay(() -> {
                                String action = "File path:" + safeLink + ":" + "1" + ":" + "Link";
                                VRPlayerManager.newIntent(action);
                            }, 3000);
                            break;

                        case "screenControl":
                            if (request.getAction().equals("block")) {
                                MainActivity.getInstance().startScreenBlockService();
                            } else {
                                MainActivity.getInstance().stopScreenBlockService();
                            }
                            break;

                        case "device_audio":
                            MainActivity.getInstance().changeAudioSettings(request.getAction().equals("mute"));
                            break;

                        case "end_session":
                        case "removedByLeader":
                            MainActivity.getInstance().logout();
                            break;

                        default:
                            Log.e(TAG, "Unknown request type: " + request.getType());
                            break;
                    }
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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
        database.child(followerRef).child(roomCode).child(uuid).removeValue();

        if(nameReference != null) {
            nameReference.removeEventListener(nameListener);
        }
        if(taskReference != null) {
            taskReference.removeEventListener(taskListener);
        }
        if (allPackageReference != null) {
            allPackageReference.removeEventListener(requestListener);
        }
        if (individualPackageReference != null) {
            individualPackageReference.removeEventListener(requestListener);
        }
        if (roomReference != null) {
            roomReference.removeEventListener(roomListener);
        }
    }

    /**
     * Update the android follower entry with what the current package is on the local device.
     * @param packageName A String of the currently active package.
     */
    public static void updateCurrentPackage(String packageName) {
        database.child(followerRef).child(roomCode).child(uuid).child("currentPackage").setValue(packageName);
    }

    /**
     * Update the android follower entry with the new name a learner has entered.
     * @param newName A String of the new name to be submitted.
     */
    public static void changeUsername(String newName) {
        database.child(followerRef).child(roomCode).child(uuid).child("name").setValue(newName);
    }

    /**
     * Update the android follower entry with the new action receieved from the VR player.
     * @param newName A String of the new action to be submitted.
     */
    public static void changeCurrentAction(String newName) {
        database.child(followerRef).child(roomCode).child(uuid).child("action").setValue(newName);
    }
}
