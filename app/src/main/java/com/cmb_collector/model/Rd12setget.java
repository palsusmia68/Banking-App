package com.cmb_collector.model;

public class Rd12setget {
    private String rdtwelvetableid;
    private String mode;
    private String term;
    private String roi;
    public Rd12setget(String rdtwelvetableid,String mode,String term,String roi){
        this.rdtwelvetableid=rdtwelvetableid;
        this.mode=mode;
        this.term=term;
        this.roi=roi;
    }

    public String getRdtwelvetableid() {
        return rdtwelvetableid;
    }

    public void setRdtwelvetableid(String rdtwelvetableid) {
        this.rdtwelvetableid = rdtwelvetableid;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getRoi() {
        return roi;
    }

    public void setRoi(String roi) {
        this.roi = roi;
    }
}
