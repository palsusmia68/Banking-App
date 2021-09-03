package com.cmb_collector.model;

public class RelationSetGet {
    private String relationid;
    private String relationlist;
    public RelationSetGet(String relationid, String relationlist) {
        this.relationid = relationid;
        this.relationlist = relationlist;
    }

    public String getRelationid() {
        return relationid;
    }

    public void setRelationid(String relationid) {
        this.relationid = relationid;
    }

    public String getRelationlist() {
        return relationlist;
    }

    public void setRelationlist(String relationlist) {
        this.relationlist = relationlist;
    }
}
