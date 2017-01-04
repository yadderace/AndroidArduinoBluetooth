package com.example.yadder.arqui2car.Models;

import io.realm.RealmObject;

/**
 * Created by Yadder on 20/12/2016.
 */
public class Driver extends RealmObject{

    private String driverId;
    private String fullName;
    private int age;
    private int exercises;
    private int bestScore;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getExercises() {
        return exercises;
    }

    public void setExercises(int exercises) {
        this.exercises = exercises;
    }

    public int getBestScore() {
        return bestScore;
    }

    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
}
