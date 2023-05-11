package com.lumination.leadmeclassroom_companion.vrplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lumination.leadmeclassroom_companion.services.FirebaseService;

public class VRPlayerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "VRPlayer Broadcast Receiver";
    private static String CurrentAction = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Code to handle the broadcast
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i(TAG, "Message is: " + text);

        setCurrentAction(text);
    }

    /**
     * Get the latest action that the VR player has taken.
     * @return A String detailing what is current happening.
     */
    public static String getCurrentAction() {
        return CurrentAction;
    }

    /**
     * Set the latest action that has been received from the VR player
     * broadcaster.
     * @param newAction A String of the received action.
     */
    public static void setCurrentAction(String newAction) {
        CurrentAction = newAction;
        FirebaseService.changeCurrentAction(newAction);
    }
}
