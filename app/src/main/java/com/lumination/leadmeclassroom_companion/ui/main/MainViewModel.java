package com.lumination.leadmeclassroom_companion.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> roomCode = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> currentPackage = new MutableLiveData<>("null");
    private MutableLiveData<List<String>> installedPackages = new MutableLiveData<>();

    /**
     * Get the username that was entered.
     */
    public LiveData<String> getUsername() {
        if (username == null) {
            username = new MutableLiveData<>();
        }

        return username;
    }

    /**
     * Set the username for the duration of the activity.
     */
    public void setUsername(String newValue) {
        username.setValue(newValue);
    }

    /**
     * Get the room code that was entered.
     */
    public LiveData<String> getRoomCode() {
        if (roomCode == null) {
            roomCode = new MutableLiveData<>();
        }

        return roomCode;
    }

    /**
     * Set the room code for the duration of the activity.
     */
    public void setRoomCode(String newValue) {
        roomCode.setValue(newValue);
    }

    /**
     * Get the username that was entered.
     */
    public LiveData<String> getCurrentPackage() {
        if (currentPackage == null) {
            currentPackage = new MutableLiveData<>("null");
        }

        return currentPackage;
    }

    /**
     * Set the username for the duration of the activity.
     */
    public void setCurrentPackage(String newValue) {
        currentPackage.setValue(newValue);
    }

    /**
     * Get the list of installed packages for the local device.
     */
    public LiveData<List<String>> getInstalledPackages() {
        if (installedPackages == null) {
            installedPackages = new MutableLiveData<>();
        }

        return installedPackages;
    }

    /**
     * Set the list of packages that are currently installed.
     */
    public void setInstalledPackages(List<String> newValue) {
        installedPackages.setValue(newValue);
    }
}