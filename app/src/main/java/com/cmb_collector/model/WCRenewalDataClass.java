package com.cmb_collector.model;

import java.io.Serializable;

public class WCRenewalDataClass implements Serializable {

    private String PolicyNo = "";
    private String AppName = "";
    private String PolicyAmount = "";
    private String Plan = "";
    private String TotalDep = "";


    public WCRenewalDataClass(String policyNo, String appName, String policyAmount, String plan, String totalDep) {
        PolicyNo = policyNo;
        AppName = appName;
        PolicyAmount = policyAmount;
        Plan = plan;
        TotalDep = totalDep;

    }

    public String getPolicyNo() {
        return PolicyNo;
    }

    public void setPolicyNo(String policyNo) {
        PolicyNo = policyNo;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String appName) {
        AppName = appName;
    }

    public String getPolicyAmount() {
        return PolicyAmount;
    }

    public void setPolicyAmount(String policyAmount) {
        PolicyAmount = policyAmount;
    }

    public String getPlan() {
        return Plan;
    }

    public void setPlan(String plan) {
        Plan = plan;
    }

    public String getTotalDep() {
        return TotalDep;
    }

    public void setTotalDep(String totalDep) {
        TotalDep = totalDep;
    }

}
