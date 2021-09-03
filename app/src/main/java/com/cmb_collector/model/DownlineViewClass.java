package com.cmb_collector.model;

import java.io.Serializable;

public class DownlineViewClass implements Serializable {
    private String name;
    private String rank;
    private String doj;

    public DownlineViewClass() {
    }

    public DownlineViewClass(String name, String rank, String doj) {
        this.name = name;
        this.rank = rank;
        this.doj = doj;
    }

    public String getName() {
        return name;
    }

    public String getRank() {
        return rank;
    }

    public String getDoj() {
        return doj;
    }
}
