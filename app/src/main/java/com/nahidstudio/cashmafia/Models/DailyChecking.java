package com.nahidstudio.cashmafia.Models;

public class DailyChecking {
    private String date;

    public DailyChecking() {
    }

    public DailyChecking(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}