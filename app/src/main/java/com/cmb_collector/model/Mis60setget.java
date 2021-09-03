package com.cmb_collector.model;

public class Mis60setget {
    private String mistwelvetableid;
    private String modemis;
    private String termmis;
    private String roimis;
    public Mis60setget(String mistwelvetableid,String modemis,String termmis,String roimis){
        this.mistwelvetableid=mistwelvetableid;
        this.modemis=modemis;
        this.termmis=termmis;
        this.roimis=roimis;
    }

    public String getMistwelvetableid() {
        return mistwelvetableid;
    }

    public void setMistwelvetableid(String mistwelvetableid) {
        this.mistwelvetableid = mistwelvetableid;
    }

    public String getModemis() {
        return modemis;
    }

    public void setModemis(String modemis) {
        this.modemis = modemis;
    }

    public String getTermmis() {
        return termmis;
    }

    public void setTermmis(String termmis) {
        this.termmis = termmis;
    }

    public String getRoimis() {
        return roimis;
    }

    public void setRoimis(String roimis) {
        this.roimis = roimis;
    }
}
