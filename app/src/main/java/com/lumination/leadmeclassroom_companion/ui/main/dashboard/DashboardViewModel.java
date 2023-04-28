package com.lumination.leadmeclassroom_companion.ui.main.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lumination.leadmeclassroom_companion.models.Task;

import java.util.ArrayList;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<String> roomCode = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<String> currentPackage = new MutableLiveData<>("null");
    private MutableLiveData<List<String>> installedPackages = new MutableLiveData<>();
    private MutableLiveData<List<Task>> pushedPackages = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<String> packageListType = new MutableLiveData<>("carousel");
    private MutableLiveData<Task> selectedTask = new MutableLiveData<>();

    /**
     * Reset all data fields within the ViewModel.
     */
    public void resetData() {
        roomCode = new MutableLiveData<>();
        username = new MutableLiveData<>();
        currentPackage = new MutableLiveData<>("null");
        installedPackages = new MutableLiveData<>();
        pushedPackages = new MutableLiveData<>();
    }

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

        //TODO Update firebase
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

    /**
     * Get the list of packages that have been pushed from a teacher to the student for the local
     * device to access.
     */
    public LiveData<List<Task>> getPushedPackages() {
        if (pushedPackages == null) {
            pushedPackages = new MutableLiveData<>(new ArrayList<>());
        }

        return pushedPackages;
    }

    /**
     * Set the list of packages that have been pushed from a teacher to the student.
     */
    public void setPushedPackages(List<Task> newValue) {
        pushedPackages.setValue(newValue);
    }

    /**
     * Get the room code that was entered.
     */
    public LiveData<String> getPackageListType() {
        if (packageListType == null) {
            packageListType = new MutableLiveData<>();
        }

        return packageListType;
    }

    /**
     * Set the room code for the duration of the activity.
     */
    public void setPackageListType(String newValue) {
        packageListType.setValue(newValue);
    }

    /**
     * Get the room code that was entered.
     */
    public LiveData<Task> getSelectedTask() {
        if (selectedTask == null) {
            selectedTask = new MutableLiveData<>();
        }

        return selectedTask;
    }

    /**
     * Set the room code for the duration of the activity.
     */
    public void setSelectedTask(Task newValue) {
        selectedTask.setValue(newValue);
    }
}