package com.cmb_collector.model;

import java.io.Serializable;

public class WCEMIPaymentData implements Serializable {
    private String LoanId = "";
    private String AppName = "";
    private String LoanAmt = "";
    private String Emi = "";
    private String TotalPaid = "";

    public WCEMIPaymentData(String loanId, String appName, String loanAmt, String emi, String totalPaid) {
        LoanId = loanId;
        AppName = appName;
        LoanAmt = loanAmt;
        Emi = emi;
        TotalPaid = totalPaid;
    }

    public String getLoanId() {
        return LoanId;
    }

    public void setLoanId(String loanId) {
        LoanId = loanId;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getLoanAmt() {
        return LoanAmt;
    }

    public void setLoanAmt(String loanAmt) {
        LoanAmt = loanAmt;
    }

    public String getEmi() {
        return Emi;
    }

    public void setEmi(String emi) {
        Emi = emi;
    }

    public String getTotalPaid() {
        return TotalPaid;
    }

    public void setTotalPaid(String totalPaid) {
        TotalPaid = totalPaid;
    }
}
