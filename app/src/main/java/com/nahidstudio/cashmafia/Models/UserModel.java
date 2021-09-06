package com.nahidstudio.cashmafia.Models;

public class UserModel {
    private String uid;
    private String name;
    private String mail;
    private String imageUrl;
    private String block;
    private int point;

    public UserModel() {
    }

    public UserModel(String uid, String name, String mail, String imageUrl, String block, int point) {
        this.uid = uid;
        this.name = name;
        this.mail = mail;
        this.imageUrl = imageUrl;
        this.block = block;
        this.point = point;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
