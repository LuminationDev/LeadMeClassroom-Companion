package com.lumination.leadmeweb_companion.managers;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.lumination.leadmeweb_companion.MainActivity;
import com.lumination.leadmeweb_companion.R;
import com.lumination.leadmeweb_companion.interfaces.StringCallbackInterface;

public class DialogManager {
    /**
     * Create a basic dialog box with a custom title and content based on the strings that are
     * passed in.
     */
    public static void createBasicInputDialog(String titleText, String contentText, StringCallbackInterface stringCallbackInterface) {
        View basicDialogView = View.inflate(MainActivity.getInstance(), R.layout.dialog_basic_input, null);
        AlertDialog basicDialog = new AlertDialog.Builder(MainActivity.getInstance(), R.style.DialogTheme).setView(basicDialogView).create();

        TextView title = basicDialogView.findViewById(R.id.title);
        title.setText(titleText);

        TextView contentView = basicDialogView.findViewById(R.id.content_text);
        contentView.setText(contentText);

        //Show only if required (if there is a callback provided)
        EditText editText = basicDialogView.findViewById(R.id.text_entry);

        Button confirmButton = basicDialogView.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(w -> {
            basicDialog.dismiss();
        });

        Button cancelButton = basicDialogView.findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(w -> {
            basicDialog.dismiss();
        });

        basicDialog.show();
        basicDialog.getWindow().setLayout(680, 680);
    }
}
