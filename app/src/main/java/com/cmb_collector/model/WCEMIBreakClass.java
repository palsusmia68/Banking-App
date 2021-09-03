package com.cmb_collector.model;

import java.io.Serializable;

public class WCEMIBreakClass implements Serializable {

    private String LoanID = "";
    private String EMINo = "";
    private String emi = "";
    private String interest = "";
    private String principle = "";
    private String current_balance = "";
    private String dueDate = "";

    public WCEMIBreakClass() {
    }

    public String getLoanID() {
        return LoanID;
    }

    public void setLoanID(String loanID) {
        LoanID = loanID;
    }

    public String getEMINo() {
        return EMINo;
    }

    public void setEMINo(String EMINo) {
        this.EMINo = EMINo;
    }

    public String getEmi() {
        return emi;
    }

    public void setEmi(String emi) {
        this.emi = emi;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getPrinciple() {
        return principle;
    }

    public void setPrinciple(String principle) {
        this.principle = principle;
    }

    public String getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = current_balance;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }


}
