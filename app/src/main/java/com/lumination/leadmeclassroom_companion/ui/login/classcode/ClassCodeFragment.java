package com.lumination.leadmeclassroom_companion.ui.login.classcode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.LoginPageClassCodeBinding;
import com.lumination.leadmeclassroom_companion.managers.PermissionManager;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;

public class ClassCodeFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    public FragmentManager childManager;
    public static ClassCodeViewModel mViewModel;
    private LoginPageClassCodeBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setLogin(mViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        childManager = getChildFragmentManager();

        View view = inflater.inflate(R.layout.login_page_class_code, container, false);
        binding = DataBindingUtil.bind(view);

        setupConnectionIcons(view);
        setupPermissionButtons(view);
        setupTextInput(view);

        return view;
    }

    /**
     * Start the spinner animation, displaying to users that something is happening while they wait.
     */
    private void setupConnectionIcons(View view) {
        Animation animation;
        animation = AnimationUtils.loadAnimation(MainActivity.getInstance().getApplicationContext(),
                R.anim.spinner_rotation);

        view.findViewById(R.id.internet_spinner).startAnimation(animation);
        view.findViewById(R.id.database_spinner).startAnimation(animation);
    }

    /**
     * Create listeners for the button images used to navigate to the required permissions
     */
    private void setupPermissionButtons(View view) {
        view.findViewById(R.id.usage_permission).setOnClickListener(v -> PermissionManager.askForUsageStatPermission());

        view.findViewById(R.id.overlay_permission).setOnClickListener(v -> PermissionManager.askForOverlayPermission());

        view.findViewById(R.id.storage_permission).setOnClickListener(v -> PermissionManager.askForStoragePermission());
    }

    /**
     * Setup a text watcher to update the view model whenever a character is entered and create a
     * listener to submit the code to firebase.
     */
    private void setupTextInput(View view) {
        //Track as the code is input into the pin entry area
        PinEntryEditText text = view.findViewById(R.id.room_code);

        text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setLoginCode(s.toString());
            }
        });

        view.findViewById(R.id.submit_room_code).setOnClickListener(v -> {
            try {
                //Slight delay to show full button animation
                MainActivity.runOnUIDelay(this::submitRoomCode, 200);
            } catch (NullPointerException ex) {
                Log.e(TAG, ex.toString());
            }
        });
    }

    /**
     * Submit the room code to the firebase service to check if there is a room with the supplied
     * value. If valid, listeners will be attached to that collection and the student will move to
     * the main page. If not, an error message will be resented under the Pin entry.
     */
    private void submitRoomCode() {
        Log.e("ROOM CODE", "Code: " + mViewModel.getLoginCode().getValue());
        FirebaseService.connectToRoom();
    }
}
