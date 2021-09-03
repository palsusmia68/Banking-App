package com.cmb_collector.model;

import java.io.Serializable;

public class WCAccountTransactionClass implements Serializable {

    private String InstNo = "";
    private String Date = "";
    private String Deposit = "";
    private String Withdrawl = "";
    private String Balance = "";

    public WCAccountTransactionClass(String instNo, String date, String deposit, String withdrawl, String balance) {
        InstNo = instNo;
        Date = date;
        Deposit = deposit;
        Withdrawl = withdrawl;
        Balance = balance;
    }

    public String getInstNo() {
        return InstNo;
    }

    public void setInstNo(String instNo) {
        InstNo = instNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDeposit() {
        return Deposit;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }

    public String getWithdrawl() {
        return Withdrawl;
    }

    public void setWithdrawl(String withdrawl) {
        Withdrawl = withdrawl;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
