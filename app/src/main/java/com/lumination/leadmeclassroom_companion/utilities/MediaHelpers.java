package com.lumination.leadmeclassroom_companion.utilities;

import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.models.Video;
import com.lumination.leadmeclassroom_companion.ui.main.dashboard.DashboardFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MediaHelpers {
    public static void collectVideoFiles() {
        List<Video> videoList = new ArrayList<>();

        Uri collection;
        collection = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);

        String[] projection = new String[] {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATA
        };

        //Add any specialty quires, currently looking at all videos with 0+ second duration
        String selection = MediaStore.Video.Media.DURATION +
                " >= ?";
        String[] selectionArgs = new String[] {
                String.valueOf(TimeUnit.MILLISECONDS.convert(0, TimeUnit.MINUTES))
        };

        String sortOrder = MediaStore.Video.Media.DISPLAY_NAME + " ASC";

        try (Cursor cursor = MainActivity.getInstance().getApplicationContext().getContentResolver().query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
        )) {
            // Cache column indices.
            int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            int nameColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
            int durationColumn =
                    cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
            int dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                int duration = cursor.getInt(durationColumn);
                int size = cursor.getInt(sizeColumn);
                String filePath = cursor.getString(dataColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                videoList.add(new Video(contentUri, name, duration, size, filePath));
            }

            DashboardFragment.mViewModel.setLocalVideos(videoList);
        }
    }
}
