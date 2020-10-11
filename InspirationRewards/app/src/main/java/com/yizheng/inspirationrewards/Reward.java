package com.yizheng.inspirationrewards;

import java.io.Serializable;

public class Reward implements Serializable {
    private String username, name, date, notes;
    private int value;

//    public Reward(String username, String name, String date, String notes, int value) {
//        this.username = username;
//        this.name = name;
//        this.date = date;
//        this.notes = notes;
//        this.value = value;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
