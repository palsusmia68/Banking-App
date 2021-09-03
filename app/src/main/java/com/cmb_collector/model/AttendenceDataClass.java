package com.cmb_collector.model;

public class AttendenceDataClass {
    private String collectorcode;
    private String date;
    private String intime;
    private String outtime;
    private String runtime;
    private String type;
    private String hh;
    private String mi;
    public AttendenceDataClass(String collectorcode, String date, String intime, String outtime,String runtime, String type,String hh,String mi) {
        this.collectorcode = collectorcode;
        this.date = date;
        this.intime = intime;
        this.outtime = outtime;
        this.runtime=runtime;
        this.type = type;
        this.hh=hh;
        this.mi=mi;
    }

    public String getCollectorcode() {
        return collectorcode;
    }

    public void setCollectorcode(String collectorcode) {
        this.collectorcode = collectorcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHh() {
        return hh;
    }

    public void setHh(String hh) {
        this.hh = hh;
    }

    public String getMi() {
        return mi;
    }

    public void setMi(String mi) {
        this.mi = mi;
    }
}
