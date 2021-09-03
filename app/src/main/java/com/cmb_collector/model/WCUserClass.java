package com.cmb_collector.model;

import java.io.Serializable;

public class WCUserClass implements Serializable {

    private String GlobalUserName = "";
    private String GlobalUserCode = "";
    private String GlobalBCode = "";
    private String GlobalBranchName = "";
    private String UserType = "";
    private String CashLimit = "";
    private String ChequeLimit = "";

    public String getFlag() {
        return Flag;
    }

    public void setFlag(String flag) {
        Flag = flag;
    }

    public static WCUserClass getUserClass() {
        return userClass;
    }

    public static void setUserClass(WCUserClass userClass) {
        WCUserClass.userClass = userClass;
    }

    private String MPINsave = "";
    private String PhoneNumber = "";

    private String GlobalMemberCode = "";
    private String Flag = "";
    private static WCUserClass userClass;

    public static WCUserClass getInstance() {
        if (userClass == null) {
            synchronized (WCUserClass.class) {
                userClass = new WCUserClass();
            }
        }
        return userClass;
    }

    public String getGlobalMemberCode() {
        return GlobalMemberCode;
    }

    public void setGlobalMemberCode(String globalMemberCode) {
        GlobalMemberCode = globalMemberCode;
    }

    public String getPhoneNumber() {
        return GlobalMemberCode;
    }

    public void setPhoneNumber(String globalMemberCode) {
        GlobalMemberCode = globalMemberCode;
    }
    public String getMPINsave() {
        return MPINsave;
    }

    public void setMPINsave(String MPINsave) {
        this.MPINsave = MPINsave;
    }

    public String getGlobalUserName() {
        return GlobalUserName;
    }

    public void setGlobalUserName(String globalUserName) {
        GlobalUserName = globalUserName;
    }

    public String getGlobalUserCode() {
        return GlobalUserCode;
    }

    public void setGlobalUserCode(String globalUserCode) {
        GlobalUserCode = globalUserCode;
    }

    public String getGlobalBCode() {
        return GlobalBCode;
    }

    public void setGlobalBCode(String globalBCode) {
        GlobalBCode = globalBCode;
    }

    public String getGlobalBranchName() {
        return GlobalBranchName;
    }

    public void setGlobalBranchName(String globalBranchName) {
        GlobalBranchName = globalBranchName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getCashLimit() {
        return CashLimit;
    }

    public void setCashLimit(String cashLimit) {
        CashLimit = cashLimit;
    }

    public String getChequeLimit() {
        return ChequeLimit;
    }

    public void setChequeLimit(String chequeLimit) {
        ChequeLimit = chequeLimit;
    }
}
