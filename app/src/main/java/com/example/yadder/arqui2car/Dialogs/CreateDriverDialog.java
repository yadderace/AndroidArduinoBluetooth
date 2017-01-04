package com.example.yadder.arqui2car.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yadder.arqui2car.Interfaces.CreateDriverDialogListener;
import com.example.yadder.arqui2car.Interfaces.SelectDriverDialogListener;
import com.example.yadder.arqui2car.Models.Driver;
import com.example.yadder.arqui2car.R;
import com.example.yadder.arqui2car.Utilities.RealmOperations;

/**
 * Created by Yadder on 25/12/2016.
 */
public class CreateDriverDialog extends DialogFragment implements TextView.OnEditorActionListener{
    EditText                edittext_driver_id,
                            edittext_driver_fullname,
                            edittext_driver_age;
    String                  driverId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_create_driver, null);
        builder.setView(v);
        builder.setTitle(R.string.title_create_driver);

        edittext_driver_id = (EditText)v.findViewById(R.id.edittext_driver_id);
        edittext_driver_fullname = (EditText)v.findViewById(R.id.edittext_driver_fullname);
        edittext_driver_age = (EditText)v.findViewById(R.id.edittext_driver_age);

        driverId = RealmOperations.getNextDriverId();
        edittext_driver_id.setText(driverId);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        int age = Integer.parseInt(edittext_driver_age.getText().toString());
        String driverId = edittext_driver_id.getText().toString();
        String driverFullName = edittext_driver_fullname.getText().toString();
        Driver newDriver = new Driver();

        CreateDriverDialogListener activity = (CreateDriverDialogListener)getActivity();
        this.dismiss();

        newDriver.setDriverId(driverId);
        newDriver.setAge(age);
        newDriver.setFullName(driverFullName);

        activity.onFinishCreateDriverDialog(newDriver);
    }

    private boolean validateInputs(){
        String driverId = edittext_driver_id.getText().toString();
        String driverFullName = edittext_driver_fullname.getText().toString();
        String age = edittext_driver_age.getText().toString();
        return !(driverId == null || driverId.length() == 0 || driverFullName.length() == 0 || !isNumeric(age));
    }

    private boolean isNumeric(String s) {
        return s.matches("\\d+");
    }
}
