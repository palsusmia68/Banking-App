package com.cmb_collector.model;

import java.io.Serializable;

public class WCDueAndPaidData implements Serializable {

    private String LoanId;
    private String EmiNo;
    private String Date;
    private String Amount;
    private String principle;
    private String interest;

    public WCDueAndPaidData(String loanId, String emiNo, String date, String amount, String principle, String interest) {
        LoanId = loanId;
        EmiNo = emiNo;
        Date = date;
        Amount = amount;
        this.principle = principle;
        this.interest = interest;
    }

    public String getLoanId() {
        return LoanId;
    }

    public void setLoanId(String loanId) {
        LoanId = loanId;
    }

    public String getEmiNo() {
        return EmiNo;
    }

    public void setEmiNo(String emiNo) {
        EmiNo = emiNo;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
