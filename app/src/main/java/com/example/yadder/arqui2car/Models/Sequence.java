package com.example.yadder.arqui2car.Models;

import io.realm.RealmObject;

/**
 * Created by Yadder on 03/01/2017.
 */
public class Sequence extends RealmObject {
    private String name;
    private String prefix;
    private int current;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
