package com.example.ema.classs;

import java.io.Serializable;

public class Order implements Serializable {
    private String id;
    private String startDate,endDate;
    private String amounte;
    private String deatails;
    private String balance;
    private String balanceProcess;

    public Order(String id, String startDate, String endDate, String amounte, String deatails,String balance,String balanceProcess) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.amounte = amounte;
        this.deatails = deatails;
        this.balance = balance;
        this.balanceProcess = balanceProcess;
    }
    public Order(){}

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getBalanceProcess() {
        return balanceProcess;
    }

    public void setBalanceProcess(String balanceProcess) {
        this.balanceProcess = balanceProcess;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAmounte() {
        return amounte;
    }

    public void setAmounte(String amounte) {
        this.amounte = amounte;
    }

    public String getDeatails() {
        return deatails;
    }

    public void setDeatails(String deatails) {
        this.deatails = deatails;
    }
}
