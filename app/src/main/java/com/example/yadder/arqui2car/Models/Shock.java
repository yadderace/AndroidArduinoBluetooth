package com.example.yadder.arqui2car.Models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by Yadder on 20/12/2016.
 */
public class Shock extends RealmObject{
    private Date doDate;
    private int side;
    private String excersiseId;

    public Date getDoDate() {
        return doDate;
    }

    public void setDoDate(Date doDate) {
        this.doDate = doDate;
    }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

    public String getExcersiseId() {
        return excersiseId;
    }

    public void setExcersiseId(String excersiseId) {
        this.excersiseId = excersiseId;
    }
}
