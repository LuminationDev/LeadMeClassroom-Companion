package com.lumination.leadmeweb_companion.ui.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {
    private MutableLiveData<String> username;

    /**
     * Get the room code that was entered.
     */
    public LiveData<String> getUsername() {
        if (username == null) {
            username = new MutableLiveData<>();
        }

        return username;
    }

    /**
     * Set the room code for the duration of the activity.
     */
    public void setUsername(String newValue) {
        username.setValue(newValue);
    }
}