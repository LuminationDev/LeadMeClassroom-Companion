package com.lumination.leadmeclassroom_companion.vrplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lumination.leadmeclassroom_companion.services.FirebaseService;

public class VRPlayerBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "VRPlayer Broadcast Receiver";
    private static String CurrentAction = "";
    private static String CurrentSource = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Code to handle the broadcast
        String text = intent.getStringExtra(Intent.EXTRA_TEXT);
        Log.i(TAG, "Message is: " + text);

        String[] split = text.split("::::");

        switch (split[0]) {
            case "action":
                setCurrentAction(split[1]);
                break;
            case "source":
                setCurrentSource(split[1]);
                break;
        }
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
    private void setCurrentAction(String newAction) {
        CurrentAction = newAction;
        FirebaseService.changeVideoStatus("action", newAction);
    }

    /**
     * Get the latest source that the VR player has taken.
     * @return A String detailing what is current playing.
     */
    public static String getCurrentSource() {
        return CurrentSource;
    }

    /**
     * Set the current source of that the VR
     * @param newSource A String of the received current source.
     */
    private void setCurrentSource(String newSource) {
        CurrentSource = newSource;
        FirebaseService.changeVideoStatus("source", newSource);
    }
}
