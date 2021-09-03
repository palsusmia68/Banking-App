package com.cmb_collector.model;

public class Fddata {
    private String investmentplacecodefdid;
    private String investmentplacecodee;
    private String investmentplacecodefdd;
    public Fddata(String investmentplacecodefdid,String investmentplacecodee,String investmentplacecodefdd){
        this.investmentplacecodefdid=investmentplacecodefdid;
        this.investmentplacecodee=investmentplacecodee;
        this.investmentplacecodefdd=investmentplacecodefdd;
    }

    public String getInvestmentplacecodefdid() {
        return investmentplacecodefdid;
    }

    public void setInvestmentplacecodefdid(String investmentplacecodefdid) {
        this.investmentplacecodefdid = investmentplacecodefdid;
    }

    public String getInvestmentplacecodee() {
        return investmentplacecodee;
    }

    public void setInvestmentplacecodee(String investmentplacecodee) {
        this.investmentplacecodee = investmentplacecodee;
    }

    public String getInvestmentplacecodefdd() {
        return investmentplacecodefdd;
    }

    public void setInvestmentplacecodefdd(String investmentplacecodefdd) {
        this.investmentplacecodefdd = investmentplacecodefdd;
    }
}
