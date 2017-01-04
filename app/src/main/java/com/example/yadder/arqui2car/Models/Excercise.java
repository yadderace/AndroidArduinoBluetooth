package com.example.yadder.arqui2car.Models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Yadder on 20/12/2016.
 */
public class Excercise extends RealmObject {
    private String driverId;
    private int week;
    private int upperShocks;
    private int downShocks;
    private int rightShocks;
    private int leftShocks;

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getUpperShocks() {
        return upperShocks;
    }

    public void setUpperShocks(int upperShocks) {
        this.upperShocks = upperShocks;
    }

    public int getDownShocks() {
        return downShocks;
    }

    public void setDownShocks(int downShocks) {
        this.downShocks = downShocks;
    }

    public int getRightShocks() {
        return rightShocks;
    }

    public void setRightShocks(int rightShocks) {
        this.rightShocks = rightShocks;
    }

    public int getLeftShocks() {
        return leftShocks;
    }

    public void setLeftShocks(int leftShocks) {
        this.leftShocks = leftShocks;
    }

    public int getTotalShocks() {
        return upperShocks + downShocks + leftShocks + rightShocks;
    }
}
