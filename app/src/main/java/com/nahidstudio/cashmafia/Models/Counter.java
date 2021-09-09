package com.nahidstudio.cashmafia.Models;

public class Counter {
    private int count;
    private String uid;

    public Counter() {
    }

    public Counter(int count, String uid) {
        this.count = count;
        this.uid = uid;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}