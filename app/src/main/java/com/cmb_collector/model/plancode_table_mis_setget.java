package com.cmb_collector.model;

public class plancode_table_mis_setget {
    private String plancodemisdid;
    private String plancodemiss;
    private String planmis;
    public plancode_table_mis_setget(String plancodemisdid,String plancodemiss,String planmis){
        this.plancodemisdid=plancodemisdid;
        this.plancodemiss=plancodemiss;
        this.planmis=planmis;
    }

    public String getPlancodemisdid() {
        return plancodemisdid;
    }

    public void setPlancodemisdid(String plancodemisdid) {
        this.plancodemisdid = plancodemisdid;
    }

    public String getPlancodemiss() {
        return plancodemiss;
    }

    public void setPlancodemiss(String plancodemiss) {
        this.plancodemiss = plancodemiss;
    }

    public String getPlanmis() {
        return planmis;
    }

    public void setPlanmis(String planmis) {
        this.planmis = planmis;
    }
}
