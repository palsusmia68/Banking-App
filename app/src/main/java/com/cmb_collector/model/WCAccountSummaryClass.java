package com.cmb_collector.model;

import java.io.Serializable;

public class WCAccountSummaryClass implements Serializable {
    private String accNo;
    private String date;
    private String narration;
    private String transactionNo;
    private String debit;
    private String credit;
    private String amount;
    private String purpose;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }



    public WCAccountSummaryClass() {
    }

    public WCAccountSummaryClass(String accNo, String date, String narration, String transactionNo, String debit, String credit, String amount,String purpose,String type) {
        this.accNo = accNo;
        this.date = date;
        this.narration = narration;
        this.transactionNo = transactionNo;
        this.debit = debit;
        this.credit = credit;
        this.amount = amount;
        this.purpose = purpose;
        this.type = type;
    }

    public String getAccNo() {
        return accNo;
    }

    public void setAccNo(String accNo) {
        this.accNo = accNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getTransactionNo() {
        return transactionNo;
    }

    public void setTransactionNo(String transactionNo) {
        this.transactionNo = transactionNo;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
