package com.lumination.leadmeclassroom_companion.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHelpers {
    public interface NetworkStateListener {
        void onNetworkAvailable();
        void onNetworkUnavailable();
    }

    public interface WebsiteReachabilityListener {
        void onWebsiteReachable();
        void onWebsiteUnreachable();
    }

    /**
     * Checks the availability of network connectivity on the device.
     * This method uses the {@link ConnectivityManager} to monitor network changes and notifies the caller about
     * the network availability status asynchronously through the provided {@link NetworkStateListener}.
     * The listener should implement the methods {@link NetworkStateListener#onNetworkAvailable()}
     * and {@link NetworkStateListener#onNetworkUnavailable()} to handle the respective events.
     * The listener methods will be called when the network becomes available or unavailable, respectively.
     *
     * @param context  The context of the application or activity to access system services.
     * @param listener A {@link NetworkStateListener} that will be notified with the network availability status.
     *                 The listener should implement the methods {@link NetworkStateListener#onNetworkAvailable()}
     *                 and {@link NetworkStateListener#onNetworkUnavailable()} to handle the respective events.
     *
     * @see NetworkStateListener
     */
    public static void checkNetworkAvailability(Context context, final NetworkStateListener listener) {
        new Thread(() -> {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            if (connectivityManager != null && isInternetReachable()) {
                NetworkRequest networkRequest = new NetworkRequest.Builder()
                        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                        .build();

                ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(Network network) {
                        listener.onNetworkAvailable();
                    }

                    @Override
                    public void onLost(Network network) {
                        listener.onNetworkUnavailable();
                    }
                };

                connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
            } else {
                listener.onNetworkUnavailable();
            }
        }).start();
    }

    /**
     * Checks if the internet is reachable by making a simple HEAD request to a reliable website (e.g., "https://www.google.com").
     * This method performs a blocking network request and should be called from a background thread to avoid blocking the main thread.
     *
     * @return {@code true} if the internet is reachable and the HEAD request returns HTTP_OK (200), {@code false} otherwise.
     *         Note that exceptions, such as IOException, during the network request will also return {@code false}.
     */
    private static boolean isInternetReachable() {
        try {
            String urlToCheck = "https://www.google.com";
            URL url = new URL(urlToCheck);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // Timeout for the connection (in milliseconds)
            int responseCode = connection.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK);
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks the reachability of a website by making a simple HTTP GET request to the specified URL.
     * The method is asynchronous and will be executed on a separate thread to avoid blocking the main thread.
     *
     * @param urlToCheck The URL of the website to check for reachability.
     * @param listener   A {@link WebsiteReachabilityListener} that will be notified with the result of the reachability check.
     *                   The listener should implement the methods {@link WebsiteReachabilityListener#onWebsiteReachable()}
     *                   and {@link WebsiteReachabilityListener#onWebsiteUnreachable()} to handle the respective events.
     *                   The listener methods will be called when the website is reachable or unreachable, respectively.
     *
     * @see WebsiteReachabilityListener
     */
    public static void checkWebsiteReachability(String urlToCheck, final WebsiteReachabilityListener listener) {
        new Thread(() -> {
            try {
                URL url = new URL(urlToCheck + "/.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000); // Timeout for the connection (in milliseconds)
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    listener.onWebsiteReachable();
                } else {
                    listener.onWebsiteUnreachable();
                }
            } catch (IOException e) {
                listener.onWebsiteUnreachable();
            }
        }).start();
    }
}
