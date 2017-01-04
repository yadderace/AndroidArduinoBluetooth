package com.example.yadder.arqui2car.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yadder.arqui2car.Interfaces.SelectDriverDialogListener;
import com.example.yadder.arqui2car.R;

/**
 * Created by Yadder on 25/12/2016.
 */
public class SelectDriverDialog extends DialogFragment implements TextView.OnEditorActionListener{
    EditText                edittext_driver_id,
                            edittext_week;
    Button                  button_confirm;
    private int             week;
    private String          driverId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_select_driver, null);
        builder.setView(v);
        builder.setTitle(R.string.title_select_driver);

        edittext_driver_id = (EditText)v.findViewById(R.id.edittext_driver_id);
        edittext_week = (EditText)v.findViewById(R.id.edittext_week);
        button_confirm = (Button)v.findViewById(R.id.button_confirm);

        button_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInputs())
                    closeDialog();
            }
        });

        return builder.create();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            if(validateInputs())
                closeDialog();
            return true;
        }
        return false;
    }

    private void closeDialog(){
        week = Integer.parseInt(edittext_week.getText().toString());
        driverId = edittext_driver_id.getText().toString();
        SelectDriverDialogListener activity = (SelectDriverDialogListener)getActivity();
        this.dismiss();
        activity.onFinishSelectDriverDialog(driverId, week);
    }

    private boolean validateInputs(){
        String driverId = edittext_driver_id.getText().toString();
        String week = edittext_week.getText().toString();
        return !(driverId == null || driverId.length() == 0 || !isNumeric(week));
    }

    private boolean isNumeric(String s) {
        return s.matches("\\d+");
    }
}
