package com.cmb_collector.model;

public class Plancode_Setget {
    private String investmentplacecodeid;
    private String investmentplacecodecode;
    private String investmentplacecoderd;
    private String investmentplacecodedrd;
    public Plancode_Setget(String investmentplacecodeid,String investmentplacecodecode,String investmentplacecoderd,String investmentplacecodedrd){
        this.investmentplacecodeid=investmentplacecodeid;
        this.investmentplacecodecode=investmentplacecodecode;
        this.investmentplacecoderd=investmentplacecoderd;
        this.investmentplacecodedrd =investmentplacecodedrd;
    }

    public String getInvestmentplacecodeid() {
        return investmentplacecodeid;
    }

    public void setInvestmentplacecodeid(String investmentplacecodeid) {
        this.investmentplacecodeid = investmentplacecodeid;
    }

    public String getInvestmentplacecodecode() {
        return investmentplacecodecode;
    }

    public void setInvestmentplacecodecode(String investmentplacecodecode) {
        this.investmentplacecodecode = investmentplacecodecode;
    }

    public String getInvestmentplacecoderd() {
        return investmentplacecoderd;
    }

    public void setInvestmentplacecoderd(String investmentplacecoderd) {
        this.investmentplacecoderd = investmentplacecoderd;
    }

    public String getInvestmentplacecodedrd() {
        return investmentplacecodedrd;
    }

    public void setInvestmentplacecodedrd(String investmentplacecodedrd) {
        this.investmentplacecodedrd = investmentplacecodedrd;
    }
}
