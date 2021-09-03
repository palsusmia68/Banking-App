package com.cmb_collector.model;

import java.io.Serializable;

public class WCRegisAmount implements Serializable {

    private String value = "";

    public WCRegisAmount() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
