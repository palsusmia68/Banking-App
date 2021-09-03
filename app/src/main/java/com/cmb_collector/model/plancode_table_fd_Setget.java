package com.cmb_collector.model;

public class plancode_table_fd_Setget {
    private String plancodetablefdidd;
    private String plancodefdd;
    private String plancodef;

    public plancode_table_fd_Setget(String plancodetablefdidd,String plancodefdd,String plancodef){
        this.plancodetablefdidd=plancodetablefdidd;
        this.plancodefdd=plancodefdd;
        this.plancodef=plancodef;
    }

    public String getPlancodetablefdidd() {
        return plancodetablefdidd;
    }

    public void setPlancodetablefdidd(String plancodetablefdidd) {
        this.plancodetablefdidd = plancodetablefdidd;
    }

    public String getPlancodefdd() {
        return plancodefdd;
    }

    public void setPlancodefdd(String plancodefdd) {
        this.plancodefdd = plancodefdd;
    }

    public String getPlancodef() {
        return plancodef;
    }

    public void setPlancodef(String plancodef) {
        this.plancodef = plancodef;
    }
}
