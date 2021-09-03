package com.cmb_collector.model;

public class LoadCollectorSBAccount {
    private String accountno;
    private String balance;
    public LoadCollectorSBAccount(String accountno, String balance) {
        this.accountno = accountno;
        this.balance = balance;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
