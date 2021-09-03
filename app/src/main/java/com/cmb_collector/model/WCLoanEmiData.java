package com.cmb_collector.model;

import java.io.Serializable;

public class WCLoanEmiData implements Serializable {

    private String LoanId = "";
    private String EmiNo = "";
    private String Date = "";
    private String PayAmount = "";
    private String Particulars = "";
    private String interest = "";

    public WCLoanEmiData(String loanId, String emiNo, String date, String payAmount, String particulars, String interest) {
        LoanId = loanId;
        EmiNo = emiNo;
        Date = date;
        PayAmount = payAmount;
        Particulars = particulars;
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

    public String getPayAmount() {
        return PayAmount;
    }

    public void setPayAmount(String payAmount) {
        PayAmount = payAmount;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
