package com.lumination.leadmeclassroom_companion.ui.login.classcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassCodeViewModel extends ViewModel {
    private MutableLiveData<String> loginCode = new MutableLiveData<>();
    private MutableLiveData<String> errorCode = new MutableLiveData<>();

    private MutableLiveData<Boolean> overlayPermission = new MutableLiveData<>();
    private MutableLiveData<Boolean> usageStatPermission = new MutableLiveData<>();
    private MutableLiveData<Boolean> storagePermission = new MutableLiveData<>();

    private MutableLiveData<Boolean> internetConnection = new MutableLiveData<>(null);
    private MutableLiveData<Boolean> databaseConnection = new MutableLiveData<>(null);

    /**
     * Reset all data fields within the ViewModel.
     */
    public void resetData() {
        loginCode = new MutableLiveData<>();
        errorCode = new MutableLiveData<>();
    }

    /**
     * Get the login code that was entered for an attempted connection.
     */
    public LiveData<String> getLoginCode() {
        if (loginCode == null) {
            loginCode = new MutableLiveData<>();
        }

        return loginCode;
    }

    /**
     * Set the login code for a connection attempt.
     */
    public void setLoginCode(String newValue) {
        loginCode.setValue(newValue);
    }

    /**
     * Get the latest error code that has been sent from firebase or another party.
     */
    public LiveData<String> getErrorCode() {
        if (errorCode == null) {
            errorCode = new MutableLiveData<>();
        }

        return errorCode;
    }

    /**
     * Set an error code that relates to the pin input.
     */
    public void setErrorCode(String newValue) {
        errorCode.setValue(newValue);
    }

    /**
     * Return a boolean of whether the overlay permission has been granted or not.
     */
    public LiveData<Boolean> getOverlayPermission() {
        if (overlayPermission == null) {
            overlayPermission = new MutableLiveData<>();
        }

        return overlayPermission;
    }

    /**
     * Set whether the overlay permission has been granted
     */
    public void setOverlayPermission(Boolean newValue) {
        overlayPermission.setValue(newValue);
    }

    /**
     * Return a boolean of whether the usage stat permission has been granted or not.
     */
    public LiveData<Boolean> getUsageStatPermission() {
        if (usageStatPermission == null) {
            usageStatPermission = new MutableLiveData<>();
        }

        return usageStatPermission;
    }

    /**
     * Set whether the usage stat permission has been granted
     */
    public void setUsageStatPermission(Boolean newValue) {
        usageStatPermission.setValue(newValue);
    }

    /**
     * Return a boolean of whether the storage permission has been granted or not.
     */
    public LiveData<Boolean> getStoragePermission() {
        if (storagePermission == null) {
            storagePermission = new MutableLiveData<>();
        }

        return storagePermission;
    }

    /**
     * Set whether the storage permission has been granted
     */
    public void setStoragePermission(Boolean newValue) {
        storagePermission.setValue(newValue);
    }

    /**
     * Return a boolean of whether there is an active internet connection or not.
     */
    public LiveData<Boolean> getInternetConnection() {
        if (internetConnection == null) {
            internetConnection = new MutableLiveData<>();
        }

        return internetConnection;
    }

    /**
     * Set whether the internet connection has been established
     */
    public void setInternetConnection(Boolean newValue) {
        internetConnection.setValue(newValue);
    }

    /**
     * Return a boolean of whether there is a database connection or not.
     */
    public LiveData<Boolean> getDatabaseConnection() {
        if (databaseConnection == null) {
            databaseConnection = new MutableLiveData<>();
        }

        return databaseConnection;
    }

    /**
     * Set whether a connection to the database can be established
     */
    public void setDatabaseConnection(Boolean newValue) {
        databaseConnection.setValue(newValue);
    }
}