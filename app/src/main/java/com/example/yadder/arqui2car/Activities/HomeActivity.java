package com.example.yadder.arqui2car.Activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.yadder.arqui2car.Dialogs.CreateDriverDialog;
import com.example.yadder.arqui2car.Dialogs.SelectDriverDialog;
import com.example.yadder.arqui2car.Interfaces.CreateDriverDialogListener;
import com.example.yadder.arqui2car.Interfaces.SelectDriverDialogListener;
import com.example.yadder.arqui2car.Models.Driver;
import com.example.yadder.arqui2car.R;
import com.example.yadder.arqui2car.Utilities.RealmOperations;

public class HomeActivity extends AppCompatActivity implements SelectDriverDialogListener, CreateDriverDialogListener{

    Activity        activity;
    Button          btn_start,
                    btn_stats,
                    btn_credits,
                    btn_exit;
    FloatingActionButton    floatingbutton_new_driver;
    RelativeLayout  relativelayout_home;

    public static String DRIVER_ID_KEY = "DRIVER_ID_KEY";
    public static String WEEK_NUMBER_KEY = "WEEK_NUMBER_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_home);

        btn_start = (Button) findViewById(R.id.btn_start);
        btn_stats = (Button) findViewById(R.id.btn_stats);
        btn_credits = (Button) findViewById(R.id.btn_credits);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        floatingbutton_new_driver = (FloatingActionButton) findViewById(R.id.floatingbutton_new_driver);

        relativelayout_home = (RelativeLayout) findViewById(R.id.relativelayout_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RealmOperations.context = getApplicationContext();
        RealmOperations.createDummyData();

        floatingbutton_new_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateDriverDialog();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDriverDialog();
            }
        });
        btn_stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, StaticsActivity.class);
                startActivity(intent);
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
        btn_credits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(activity)
                        .setTitle("Créditos")
                        .setMessage("Arquitectura 2 USAC \ngithub.com/yadderace/AndroidArduinoBluetooth")
                        .setPositiveButton("Regresar", null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void showSelectDriverDialog(){
        DialogFragment newFragment = new SelectDriverDialog();
        newFragment.show(getSupportFragmentManager(), "SELECT_DRIVER_DIALOG");
    }

    private void showCreateDriverDialog(){
        DialogFragment newFragment = new CreateDriverDialog();
        newFragment.show(getSupportFragmentManager(), "CREATE_DRIVER_DIALOG");
    }

    private void showStartExcersiceActivity(String driverId, int week){
        Intent intent = new Intent(activity, StartExcerciseActivity.class);
        intent.putExtra(DRIVER_ID_KEY, driverId);
        intent.putExtra(WEEK_NUMBER_KEY, week);
        startActivity(intent);
    }

    @Override
    public void onFinishSelectDriverDialog(String driverId, int week) {

        if(!RealmOperations.existsDriver(driverId)){
            Snackbar.make(relativelayout_home, "No se encontro el conductor", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }else{
            showStartExcersiceActivity(driverId, week);
        }
    }

    @Override
    public void onFinishCreateDriverDialog(Driver newDriver) {
        if(!RealmOperations.existsDriver(newDriver.getDriverId())){
            RealmOperations.saveDriver(newDriver);
            Snackbar.make(relativelayout_home, "Nuevo conductor guardado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else{
            Snackbar.make(relativelayout_home, "El id del conductor ya esta guardado", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
}
