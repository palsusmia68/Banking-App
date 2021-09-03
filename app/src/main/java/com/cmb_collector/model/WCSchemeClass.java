package com.cmb_collector.model;

import java.io.Serializable;

public class WCSchemeClass implements Serializable {

    private String SchemeName = "";
    private String SchemeID = "";

    public WCSchemeClass() {
    }

    public String getSchemeName() {
        return SchemeName;
    }

    public void setSchemeName(String schemeName) {
        SchemeName = schemeName;
    }

    public String getSchemeID() {
        return SchemeID;
    }

    public void setSchemeID(String schemeID) {
        SchemeID = schemeID;
    }


}
