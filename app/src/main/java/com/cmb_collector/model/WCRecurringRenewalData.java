package com.cmb_collector.model;

import java.io.Serializable;

public class WCRecurringRenewalData implements Serializable {
    private String PolicyNo = "";
    private String InstNo = "";
    private String DueDate = "";
    private String PayDate = "";
    private String Amount = "";
    private String Balance = "";

    public WCRecurringRenewalData(String policyNo, String instNo, String dueDate, String payDate, String amount, String balance) {
        PolicyNo = policyNo;
        InstNo = instNo;
        DueDate = dueDate;
        PayDate = payDate;
        Amount = amount;
        Balance = balance;
    }

    public String getPolicyNo() {
        return PolicyNo;
    }

    public void setPolicyNo(String policyNo) {
        PolicyNo = policyNo;
    }

    public String getInstNo() {
        return InstNo;
    }

    public void setInstNo(String instNo) {
        InstNo = instNo;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getPayDate() {
        return PayDate;
    }

    public void setPayDate(String payDate) {
        PayDate = payDate;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}
