package com.lumination.leadmeclassroom_companion.ui.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.lumination.leadmeclassroom_companion.MainActivity;
import com.lumination.leadmeclassroom_companion.R;
import com.lumination.leadmeclassroom_companion.databinding.FragmentLoginBinding;
import com.lumination.leadmeclassroom_companion.interfaces.StringCallbackInterface;
import com.lumination.leadmeclassroom_companion.managers.DialogManager;
import com.lumination.leadmeclassroom_companion.services.FirebaseService;
import com.lumination.leadmeclassroom_companion.ui.main.MainFragment;

public class LoginFragment extends Fragment {
    private static final String TAG = "LoginFragment";

    public FragmentManager childManager;
    public static LoginViewModel mViewModel;
    private FragmentLoginBinding binding;

    public static LoginFragment instance;
    public static LoginFragment getInstance() { return instance; }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setLifecycleOwner(getViewLifecycleOwner());
        binding.setLogin(mViewModel);

        instance = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        childManager = getChildFragmentManager();

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        binding = DataBindingUtil.bind(view);

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

        return view;
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
