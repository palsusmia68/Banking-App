package com.cmb_collector.model;

import java.io.Serializable;

public class WCRelationData implements Serializable {

    private String Relation = "";

    public WCRelationData() {
    }

    public String getRelation() {
        return Relation;
    }

    public void setRelation(String relation) {
        Relation = relation;
    }

}
