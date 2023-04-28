package com.lumination.leadmeclassroom_companion.ui.login.classcode;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ClassCodeViewModel extends ViewModel {
    private MutableLiveData<String> loginCode = new MutableLiveData<>();
    private MutableLiveData<String> errorCode = new MutableLiveData<>();

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
}