package com.example.yadder.arqui2car.Models;

import java.util.Date;

/**
 * Created by Yadder on 26/12/2016.
 */
public class Static {
    private String driverId;
    private String fullName;
    private int     totalShock;
    private Date    excerciseDate;
    private int upperShocks;
    private int downShocks;
    private int leftShocks;
    private int rightShocks;
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getTotalShock() {
        return totalShock;
    }

    public void setTotalShock(int totalShock) {
        this.totalShock = totalShock;
    }

    public Date getExcerciseDate() {
        return excerciseDate;
    }

    public void setExcerciseDate(Date excerciseDate) {
        this.excerciseDate = excerciseDate;
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

    public int getLeftShocks() {
        return leftShocks;
    }

    public void setLeftShocks(int leftShocks) {
        this.leftShocks = leftShocks;
    }

    public int getRightShocks() {
        return rightShocks;
    }

    public void setRightShocks(int rightShocks) {
        this.rightShocks = rightShocks;
    }
}
