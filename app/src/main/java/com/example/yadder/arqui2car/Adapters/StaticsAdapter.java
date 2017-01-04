package com.example.yadder.arqui2car.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yadder.arqui2car.Models.Static;
import com.example.yadder.arqui2car.R;

import java.util.ArrayList;

/**
 * Created by Yadder on 26/12/2016.
 */
public class StaticsAdapter extends RecyclerView.Adapter<StaticsAdapter.ViewHolder> {

    ArrayList<Static>       data;
    Activity                activity;

    public StaticsAdapter(ArrayList<Static> data, Activity activity){
        this.data = data;
        this.activity = activity;
    }

    @Override
    public StaticsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_static, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(StaticsAdapter.ViewHolder holder, int position) {
        Static stat = data.get(position);
        holder.textview_down_shocks.setText(String.valueOf(stat.getDownShocks()));
        holder.textview_up_shocks.setText(String.valueOf(stat.getUpperShocks()));
        holder.textview_left_shocks.setText(String.valueOf(stat.getLeftShocks()));
        holder.textview_right_shocks.setText(String.valueOf(stat.getRightShocks()));
        holder.textview_total_shocks.setText(String.valueOf(stat.getTotalShock()));
        holder.textview_driver_fullname.setText(stat.getFullName());
        holder.textview_driver_id.setText(stat.getDriverId());
        holder.textview_driver_position.setText(String.valueOf(stat.getPosition()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView    textview_driver_position,
                textview_driver_fullname,
                textview_driver_id,
                textview_up_shocks,
                textview_down_shocks,
                textview_left_shocks,
                textview_right_shocks,
                textview_total_shocks;

        public ViewHolder(View v) {
            super(v);
            textview_down_shocks = (TextView)v.findViewById(R.id.textview_down_shocks);
            textview_up_shocks = (TextView)v.findViewById(R.id.textview_up_shocks);
            textview_left_shocks = (TextView)v.findViewById(R.id.textview_left_shocks);
            textview_right_shocks = (TextView)v.findViewById(R.id.textview_right_shocks);
            textview_total_shocks = (TextView)v.findViewById(R.id.textview_total_shocks);
            textview_driver_id = (TextView)v.findViewById(R.id.textview_driver_id);
            textview_driver_fullname = (TextView)v.findViewById(R.id.textview_driver_fullname);
            textview_driver_position = (TextView)v.findViewById(R.id.textview_driver_position);
        }
    }
}
