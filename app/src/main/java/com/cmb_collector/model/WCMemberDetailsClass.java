package com.cmb_collector.model;

import java.io.Serializable;

public class WCMemberDetailsClass implements Serializable {
    private String memberNo;
    private String memberName;
    private String fatherName;
    private String address;
    private String phno;
    private String profilePic;
    private String signPic;

    private static WCMemberDetailsClass memberDetails;

    public static WCMemberDetailsClass getInstance() {
        if (memberDetails == null) {
            synchronized (WCMemberDetailsClass.class) {
                memberDetails = new WCMemberDetailsClass();
            }
        }
        return memberDetails;
    }


    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhno() {
        return phno;
    }

    public void setPhno(String phno) {
        this.phno = phno;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getSignPic() {
        return signPic;
    }

    public void setSignPic(String signPic) {
        this.signPic = signPic;
    }
}
