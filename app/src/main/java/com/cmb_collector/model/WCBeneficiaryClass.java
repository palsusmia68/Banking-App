package com.cmb_collector.model;

import java.io.Serializable;

public class WCBeneficiaryClass implements Serializable {
    private String acNo;
    private String name;
    private String limit;

    public WCBeneficiaryClass(String acNo, String name, String limit) {
        this.acNo = acNo;
        this.name = name;
        this.limit = limit;
    }

    public String getAcNo() {
        return acNo;
    }

    public String getName() {
        return name;
    }

    public String getLimit() {
        return limit;
    }
}
