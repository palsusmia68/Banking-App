package com.cmb_collector.model;

import java.io.Serializable;

public class WCLoanModeClass implements Serializable {

    private String LoanMode = "";
    private String LoanROI = "";
    private String InterestMode = "";


    public WCLoanModeClass() {
    }

    public String getLoanMode() {
        return LoanMode;
    }

    public void setLoanMode(String loanMode) {
        LoanMode = loanMode;
    }

    public String getLoanROI() {
        return LoanROI;
    }

    public void setLoanROI(String loanROI) {
        LoanROI = loanROI;
    }

    public String getInterestMode() {
        return InterestMode;
    }

    public void setInterestMode(String interestMode) {
        InterestMode = interestMode;
    }


}
