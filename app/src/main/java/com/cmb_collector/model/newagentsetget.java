package com.cmb_collector.model;

public class newagentsetget {
    private String agentregamountlistid;
    private String regvalue;
    public newagentsetget(String agentregamountlistid, String regvalue) {
        this.agentregamountlistid = agentregamountlistid;
        this.regvalue = regvalue;
    }

    public String getAgentregamountlistid() {
        return agentregamountlistid;
    }

    public void setAgentregamountlistid(String agentregamountlistid) {
        this.agentregamountlistid = agentregamountlistid;
    }

    public String getRegvalue() {
        return regvalue;
    }

    public void setRegvalue(String regvalue) {
        this.regvalue = regvalue;
    }
}
