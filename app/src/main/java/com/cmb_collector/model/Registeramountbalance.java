package com.cmb_collector.model;

public class Registeramountbalance {
    private String registeramountid;
    private String registeramountbalnce;
    public Registeramountbalance(String registeramountid,String registeramountbalnce){
        this.registeramountid = registeramountid;
        this.registeramountbalnce = registeramountbalnce;
    }

    public String getRegisteramountid() {
        return registeramountid;
    }

    public void setRegisteramountid(String registeramountid) {
        this.registeramountid = registeramountid;
    }

    public String getRegisteramountbalnce() {
        return registeramountbalnce;
    }

    public void setRegisteramountbalnce(String registeramountbalnce) {
        this.registeramountbalnce = registeramountbalnce;
    }
}
