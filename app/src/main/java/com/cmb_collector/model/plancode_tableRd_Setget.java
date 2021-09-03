package com.cmb_collector.model;

public class plancode_tableRd_Setget {
    private String plancodetablerdid;
    private String plancoderdd;
    private String planrd;
    public plancode_tableRd_Setget(String plancodetablerdid,String plancoderdd,String planrd){
        this.plancodetablerdid=plancodetablerdid;
        this.plancoderdd=plancoderdd;
        this.planrd=planrd;
    }

    public String getPlancodetablerdid() {
        return plancodetablerdid;
    }

    public void setPlancodetablerdid(String plancodetablerdid) {
        this.plancodetablerdid = plancodetablerdid;
    }

    public String getPlancoderdd() {
        return plancoderdd;
    }

    public void setPlancoderdd(String plancoderdd) {
        this.plancoderdd = plancoderdd;
    }

    public String getPlanrd() {
        return planrd;
    }

    public void setPlanrd(String planrd) {
        this.planrd = planrd;
    }
}
