package com.cmb_collector.model;

import java.io.Serializable;

public class WCSBAccountData implements Serializable {
    private String Date = "";
    private String TrnNo = "";
    private String Particulars = "";
    private String Deposit = "";
    private String Withdrawal = "";
    private String Balance = "";

    public WCSBAccountData(String date, String trnNo, String particulars, String deposit, String withdrawal, String balance) {
        Date = date;
        TrnNo = trnNo;
        Particulars = particulars;
        Deposit = deposit;
        Withdrawal = withdrawal;
        Balance = balance;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTrnNo() {
        return TrnNo;
    }

    public void setTrnNo(String trnNo) {
        TrnNo = trnNo;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public String getDeposit() {
        return Deposit;
    }

    public void setDeposit(String deposit) {
        Deposit = deposit;
    }

    public String getWithdrawal() {
        return Withdrawal;
    }

    public void setWithdrawal(String withdrawal) {
        Withdrawal = withdrawal;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
