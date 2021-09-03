package com.cmb_collector.model;

public class Misdatasetget {
    private String investmentplacecodemisid;
    private String investmentplacecodee;
    private String investmentplacecodemis;

    public Misdatasetget(String investmentplacecodemisid,String investmentplacecodee,String investmentplacecodemis){
        this.investmentplacecodemisid=investmentplacecodemisid;
        this.investmentplacecodee=investmentplacecodee;
        this.investmentplacecodemis=investmentplacecodemis;
    }

    public String getInvestmentplacecodemisid() {
        return investmentplacecodemisid;
    }

    public void setInvestmentplacecodemisid(String investmentplacecodemisid) {
        this.investmentplacecodemisid = investmentplacecodemisid;
    }

    public String getInvestmentplacecodee() {
        return investmentplacecodee;
    }

    public void setInvestmentplacecodee(String investmentplacecodee) {
        this.investmentplacecodee = investmentplacecodee;
    }

    public String getInvestmentplacecodemis() {
        return investmentplacecodemis;
    }

    public void setInvestmentplacecodemis(String investmentplacecodemis) {
        this.investmentplacecodemis = investmentplacecodemis;
    }
}
