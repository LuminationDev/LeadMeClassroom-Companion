package com.lumination.leadmeweb_companion.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<String> roomCode;
    private MutableLiveData<String> errorCode;

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