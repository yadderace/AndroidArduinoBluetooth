package com.example.yadder.arqui2car.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yadder.arqui2car.Adapters.StaticsAdapter;
import com.example.yadder.arqui2car.Models.Static;
import com.example.yadder.arqui2car.R;
import com.example.yadder.arqui2car.Utilities.RealmOperations;

import java.util.ArrayList;

/**
 * Created by Yadder on 26/12/2016.
 */
public class StaticsActivity extends AppCompatActivity {

    Activity            activity;
    EditText            edittext_week;
    RecyclerView        recyclerview_statics;
    Button              button_find_statics;
    Toolbar             toolbar;

    StaticsAdapter      adapter;
    ArrayList<Static>   stats;

    int                 currentWeek = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        setContentView(R.layout.activity_statics);

        edittext_week = (EditText)findViewById(R.id.edittext_week);
        recyclerview_statics = (RecyclerView) findViewById(R.id.recyclerview_statics);
        button_find_statics = (Button) findViewById(R.id.button_find_statics);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        recyclerview_statics.setLayoutManager(new LinearLayoutManager(this));
        edittext_week.setText(String.valueOf(currentWeek));

        toolbar.setTitle("Estadísticas");
        stats = RealmOperations.getWeekStats(currentWeek);
        adapter = new StaticsAdapter(stats, activity);
        recyclerview_statics.setAdapter(adapter);

        button_find_statics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edittext_week.getText().toString().length() > 0 && isNumeric(edittext_week.getText().toString())){
                    currentWeek = Integer.parseInt(edittext_week.getText().toString());
                    stats = RealmOperations.getWeekStats(currentWeek);
                    adapter = new StaticsAdapter(stats, activity);
                    recyclerview_statics.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        super.onCreate(savedInstanceState);
    }

    private boolean isNumeric(String s) {
        return s.matches("\\d+");
    }
}
