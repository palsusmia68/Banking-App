package com.cmb_collector.model;

import java.io.Serializable;

public class WCFDDataClass implements Serializable {
    private String policyNo;
    private String payDate;
    private String amount;
    private String mtAmount;
    private String matDate;

    public WCFDDataClass(String policyNo, String payDate, String amount, String mtAmount, String matDate) {
        this.policyNo = policyNo;
        this.payDate = payDate;
        this.amount = amount;
        this.mtAmount = mtAmount;
        this.matDate = matDate;
    }


    public String getPolicyNo() {
        return policyNo;
    }

    public String getPayDate() {
        return payDate;
    }

    public String getAmount() {
        return amount;
    }

    public String getMtAmount() {
        return mtAmount;
    }

    public String getMatDate() {
        return matDate;
    }
}
