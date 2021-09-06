package com.nahidstudio.cashmafia.Models;

public class Withdraw {
    private int image;
    private String titel,point;

    public Withdraw(int image, String titel, String point) {
        this.image = image;
        this.titel = titel;
        this.point = point;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
