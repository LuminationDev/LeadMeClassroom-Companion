package com.lumination.leadmeclassroom_companion.vrplayer;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.models.Video;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;

import java.util.Optional;

public class VRPlayerManager {
    public final static String packageName = "com.lumination.VRPlayer";
    private final static String className = "com.lumination.receiver.ReceiverPlugin";

    /**
     * Determine what type of media the VR player has to load, this will be either a Link or a URI.
     * @param path A url or name of the video location or name to play
     * @param startTime A string of the time to start the video at
     * @param mediaType A string describing how to treat the action
     */
    public static void determineMediaType(String path, String startTime, String mediaType) {
        switch(mediaType) {
            case "Link":
                newIntent("File path:" + path + ":" + startTime + ":" + mediaType);
                break;
            case "Video":
                String filePath = findLocalVideo(path);
                if(filePath != null) {
                    newIntent("File path:" + filePath + ":" + startTime + ":" + mediaType);
                }
                break;
        }
    }

    /**
     * Based on the supplied name, find the local video and attempt to load it through the VR player.
     */
    private static String findLocalVideo(String name) {
        if(DashboardFragment.mViewModel.getLocalVideos().getValue() != null) {
            Optional<Video> videoOptional = DashboardFragment.mViewModel.getLocalVideos().getValue()
                    .stream()
                    .filter(video -> video.getName().equals(name))
                    .findFirst();

            if (videoOptional.isPresent()) {
                Video video = videoOptional.get();
                return video.getFilePath();
            }
        }

        // Video not found
        return null;
    }

    /**
     * Send a controlling action to the VR player, this may be stop, play or pause for example.
     * @param videoAction A string detailing what VR player's internal video player should do.
     */
    public static void videoAction(String videoAction) {
        newIntent(videoAction);
    }

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
