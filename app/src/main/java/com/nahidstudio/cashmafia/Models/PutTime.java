package com.nahidstudio.cashmafia.Models;

public class PutTime {

    private int Time;
    private String Task;

    public PutTime() {
    }

    public PutTime(int time, String task) {
        Time = time;
        Task = task;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public String getTask() {
        return Task;
    }

    public void setTask(String task) {
        Task = task;
    }
}
