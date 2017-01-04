package com.example.yadder.arqui2car.Activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yadder.arqui2car.Models.Driver;
import com.example.yadder.arqui2car.Models.Excercise;
import com.example.yadder.arqui2car.R;
import com.example.yadder.arqui2car.Utilities.RealmOperations;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import io.realm.Realm;

/**
 * Created by Yadder on 23/12/2016.
 */
public class StartExcerciseActivity extends AppCompatActivity {

    private Activity            activity;
    private int                 week;
    private Driver              driver;
    private TextView            textview_driver_fullname,
                                textview_week_number,
                                textview_driver_id,
                                textview_driver_age,
                                textview_up_side,
                                textview_down_side,
                                textview_left_side,
                                textview_right_side,
                                textview_up_shocks,
                                textview_down_shocks,
                                textview_left_shocks,
                                textview_right_shocks,
                                textview_total_shocks;
    private Toolbar             toolbar;
    private Button              button_stop_excercise,
                                button_start_excercise;
    private ImageView           imageview_bluetooth,
                                imageview_excercise_state;
    private CoordinatorLayout   linearlayout_content;

    private BluetoothAdapter    bluetoothAdapter;
    private BluetoothSocket     bluetoothSocket;
    private Handler             bluetoothIn;

    private int                 handlerState = 0;
    private int                 upShock = 0,
                                downShock = 0,
                                rightShock = 0,
                                leftShock = 0;
    private boolean             excersiceStarted = false;
    private String              address;
    private StringBuilder       recDataString = new StringBuilder();
    private ConnectedThread     connectedThread;

    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final int PICK_PAIRED_DEVICE = 500;
    public static final String PAIRED_DEVICE_KEY = "PAIRED_DEVICE_KEY";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_start_excercise);

        textview_driver_fullname = (TextView) findViewById(R.id.textview_driver_fullname);
        textview_driver_id = (TextView) findViewById(R.id.textview_driver_id);
        textview_driver_age = (TextView) findViewById(R.id.textview_driver_age);
        textview_week_number = (TextView) findViewById(R.id.textview_week_number);
        textview_up_side = (TextView) findViewById(R.id.textview_up_side);
        textview_up_shocks = (TextView) findViewById(R.id.textview_up_shocks);
        textview_down_side = (TextView) findViewById(R.id.textview_down_side);
        textview_down_shocks = (TextView) findViewById(R.id.textview_down_shocks);
        textview_left_side = (TextView) findViewById(R.id.textview_left_side);
        textview_left_shocks = (TextView) findViewById(R.id.textview_left_shocks);
        textview_right_side = (TextView) findViewById(R.id.textview_right_side);
        textview_right_shocks = (TextView) findViewById(R.id.textview_right_shocks);
        textview_total_shocks = (TextView) findViewById(R.id.textview_total_shocks);
        button_start_excercise = (Button) findViewById(R.id.button_start_excercise);
        button_stop_excercise = (Button) findViewById(R.id.button_stop_excercise);
        linearlayout_content = (CoordinatorLayout) findViewById(R.id.linearlayout_content);
        imageview_bluetooth = (ImageView) findViewById(R.id.imageview_bluetooth);
        imageview_excercise_state = (ImageView) findViewById(R.id.imageview_excercise_state);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        week = getIntent().getIntExtra(HomeActivity.WEEK_NUMBER_KEY, -1);
        String driverId = getIntent().getStringExtra(HomeActivity.DRIVER_ID_KEY);
        driver = RealmOperations.getDriver(driverId);

        textview_driver_fullname.setText(driver.getFullName());
        textview_week_number.setText("Semana: " + String.valueOf(week));
        textview_driver_id.setText(driver.getDriverId());
        textview_driver_age.setText("Edad: " + String.valueOf(driver.getAge()));

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null){
            Toast.makeText(activity, "El dispositivo no soporta Bluetooh", Toast.LENGTH_SHORT);
            finish();
            return;
        }
        checkBluetoothEnabled();

        bluetoothIn = new Handler(){
            public void handleMessage(Message msg){
                if(msg.what == handlerState){
                    String readMessage = (String) msg.obj;
                    recDataString.append(readMessage);
                    //int idxEndOfLine = recDataString.indexOf("~");
                    //if(idxEndOfLine > 0){
                        //String dataInPrint = recDataString.substring(0, idxEndOfLine);

                    String  dataInPrint = recDataString.substring(0,1).toUpperCase();
                        if(dataInPrint.compareTo("A") == 0){
                            upShock();
                        }else if(dataInPrint.compareTo("B") == 0){
                            downShock();
                        }else if(dataInPrint.compareTo("C") == 0){
                            leftShock();
                        }else if(dataInPrint.compareTo("D") == 0){
                            rightShock();
                        }
                        recDataString.delete(0, recDataString.length());
                    //}
                }
            }
        };

        button_start_excercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothSocket != null && bluetoothSocket.isConnected()) {
                    Toast.makeText(activity, "Ejercicio Iniciado", Toast.LENGTH_SHORT).show();
                    excersiceStarted = true;
                    imageview_excercise_state.setImageResource(R.drawable.ic_started);
                    button_stop_excercise.setEnabled(true);
                    button_start_excercise.setEnabled(false);
                }else{
                    Toast.makeText(activity, "No hay conexión Bluetooth", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button_stop_excercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(excersiceStarted){
                    excersiceStarted = false;
                    button_start_excercise.setEnabled(false);
                    button_stop_excercise.setEnabled(false);

                    try {
                        bluetoothSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageview_bluetooth.setVisibility(View.GONE);
                    imageview_excercise_state.setImageResource(R.drawable.ic_stoped);

                    askSaveExcercise();

                }
            }
        });

        updateUI();

    }

    private void askSaveExcercise(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:

                        Excercise excercise = new Excercise();
                        excercise.setDriverId(driver.getDriverId());
                        excercise.setWeek(week);
                        excercise.setUpperShocks(upShock);
                        excercise.setDownShocks(downShock);
                        excercise.setLeftShocks(leftShock);
                        excercise.setRightShocks(rightShock);

                        RealmOperations.saveExcercise(excercise);

                        Toast.makeText(activity, "Ejercicio guardado", Toast.LENGTH_LONG);
                        activity.finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(activity, "Ejercicio invalidado", Toast.LENGTH_LONG);
                        activity.finish();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Desea guardar el ejercicio?").setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }


    /**
     * Try to connect the bluetooth device with the address param
     * @param address
     */
    private void connectBluetoothDevice(String address){
        this.address = address;
        imageview_bluetooth.setVisibility(View.GONE);

        //create device and set the MAC address
        BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);

        try {
            bluetoothSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Creacion de Socket ha fallado", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            bluetoothSocket.connect();
        } catch (IOException e) {
            try
            {
                bluetoothSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        connectedThread = new ConnectedThread(bluetoothSocket);
        connectedThread.start();

        if(bluetoothSocket.isConnected()){
            excersiceStarted = false;
            button_start_excercise.setEnabled(true);
            Toast.makeText(activity, "Conexión exitosa", Toast.LENGTH_SHORT).show();
            imageview_bluetooth.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(activity, "Error en conexión", Toast.LENGTH_LONG).show();
            button_start_excercise.setEnabled(true);
        }
        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        //connectedThread.write("x");
    }


    /**
     * Create the options menu for the start excercise activity
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
    }

    /**
     * Executed when selected the item
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.option_pair) {
            Intent intent = new Intent(activity, DeviceListActivity.class);
            startActivityForResult(intent, PICK_PAIRED_DEVICE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Executed when the activity of paired devices return the address
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_PAIRED_DEVICE && resultCode == RESULT_OK){
            String address = data.getStringExtra(PAIRED_DEVICE_KEY);
            Toast.makeText(activity, "La direccion es "+address, Toast.LENGTH_LONG);
            connectBluetoothDevice(address);
        }
    }

    /**
     * Check if bluetooth adapter is enabled
     */
    private void checkBluetoothEnabled(){
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, 1);
        }
    }

    /**
     * Crea a bluetooth socket
     * @param device
     * @return
     * @throws IOException
     */
    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
    }

    /**
     * Update the counters of the user interface
     */
    private void updateUI(){
        textview_up_side.setText(String.valueOf(upShock));
        textview_up_shocks.setText(String.valueOf(upShock));
        textview_down_side.setText(String.valueOf(downShock));
        textview_down_shocks.setText(String.valueOf(downShock));
        textview_left_side.setText(String.valueOf(leftShock));
        textview_left_shocks.setText(String.valueOf(leftShock));
        textview_right_side.setText(String.valueOf(rightShock));
        textview_right_shocks.setText(String.valueOf(rightShock));
        textview_total_shocks.setText(String.valueOf(upShock + downShock + leftShock + rightShock));
    }


    /**
     * Update the up shocks
     */
    private void upShock(){
        if(excersiceStarted){
            upShock++;
            updateUI();
        }
    }

    /**
     * Update the down shocks
     */
    private void downShock(){
        if(excersiceStarted){
            downShock++;
            updateUI();
        }
    }

    /**
     * Update the left shocks
     */
    private void leftShock(){
        if(excersiceStarted){
            leftShock++;
            updateUI();
        }
    }

    /**
     * Update the left shocks
     */
    private void rightShock(){
        if(excersiceStarted){
            rightShock++;
            updateUI();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(bluetoothSocket!= null && bluetoothSocket.isConnected()){
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream inputStream;
        private final OutputStream outputStream;

        /**
         * Constructor for class, need a bluetooth socket
         * @param socket
         */
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        /**
         * Run the thread
         */
        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            while (true) {
                try {
                    bytes = inputStream.read(buffer);
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }

        /**
         * Write streaming to bluetooth device
         * @param input
         */
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                outputStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Fallo de Conexion", Toast.LENGTH_LONG).show();
                finish();
            }
        }


    }
}

