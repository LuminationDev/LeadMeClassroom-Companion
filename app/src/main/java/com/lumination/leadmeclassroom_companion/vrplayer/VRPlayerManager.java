package com.lumination.leadmeclassroom_companion.vrplayer;

import android.content.ComponentName;
import android.content.Intent;

import com.lumination.leadmeclassroom_companion.MainActivity;

public class VRPlayerManager {
    private final static String packageName = "com.lumination.VRPlayer";
    private final static String className = "com.lumination.receiver.ReceiverPlugin";

    //Create an intent based on the action supplied and send it to the external application
    private static void newIntent(String action) {
        // sendIntent is the object that will be broadcast outside our app
        Intent sendIntent = new Intent();

        // We add flags for example to work from background
        sendIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION|Intent.FLAG_FROM_BACKGROUND|Intent.FLAG_INCLUDE_STOPPED_PACKAGES);

        // SetAction uses a string which is an important name as it identifies the sender of the intent and that we will give to the receiver to know what to listen.
        // By convention, it's suggested to use the current package name
        sendIntent.setAction("com.lumination.leadmeclassroom_companion.IntentToUnity");

        // Set an explicit component and class to send the intent to, cannot use implicit anymore.
        sendIntent.setComponent(new ComponentName(packageName, className));

        // Here we fill the Intent with our data, here just a string with an incremented number in it.
        sendIntent.putExtra(Intent.EXTRA_TEXT, action);

        // And here it goes ! our message is sent to any other app that want to listen to it.
        MainActivity.getInstance().sendBroadcast(sendIntent);
    }
}
