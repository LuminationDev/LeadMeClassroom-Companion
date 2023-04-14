package com.lumination.leadmeclassroom_companion.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> loginCode = new MutableLiveData<>();
    private MutableLiveData<String> errorCode = new MutableLiveData<>();

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
     * Get the error code that was entered.
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