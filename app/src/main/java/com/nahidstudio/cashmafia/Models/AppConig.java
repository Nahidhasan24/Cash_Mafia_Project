package com.nahidstudio.cashmafia.Models;

public class AppConig {

    private String websitemassage;
    private String watchadsmassage;
    private int webvisitpoint;
    private int adsshowpoin;
    private String server;
    private String websielink;
    private String withdrawmsg;

    public AppConig() {
    }

    public AppConig(String websitemassage, String watchadsmassage, int webvisitpoint, int adsshowpoin, String server, String websielink, String withdrawmsg) {
        this.websitemassage = websitemassage;
        this.watchadsmassage = watchadsmassage;
        this.webvisitpoint = webvisitpoint;
        this.adsshowpoin = adsshowpoin;
        this.server = server;
        this.websielink = websielink;
        this.withdrawmsg = withdrawmsg;
    }

    public String getWebsitemassage() {
        return websitemassage;
    }

    public void setWebsitemassage(String websitemassage) {
        this.websitemassage = websitemassage;
    }

    public String getWatchadsmassage() {
        return watchadsmassage;
    }

    public void setWatchadsmassage(String watchadsmassage) {
        this.watchadsmassage = watchadsmassage;
    }

    public int getWebvisitpoint() {
        return webvisitpoint;
    }

    public void setWebvisitpoint(int webvisitpoint) {
        this.webvisitpoint = webvisitpoint;
    }

    public int getAdsshowpoin() {
        return adsshowpoin;
    }

    public void setAdsshowpoin(int adsshowpoin) {
        this.adsshowpoin = adsshowpoin;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getWebsielink() {
        return websielink;
    }

    public void setWebsielink(String websielink) {
        this.websielink = websielink;
    }

    public String getWithdrawmsg() {
        return withdrawmsg;
    }

    public void setWithdrawmsg(String withdrawmsg) {
        this.withdrawmsg = withdrawmsg;
    }
}
