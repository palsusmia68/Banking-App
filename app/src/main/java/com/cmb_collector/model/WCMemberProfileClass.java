package com.cmb_collector.model;

import java.io.Serializable;

public class WCMemberProfileClass implements Serializable {
    private Integer images;
    private String memberNo = "";
    private String name = "";
    private String phone = "";
    private String address = "";
    private String email = "";
    private String profPic = "";


    public WCMemberProfileClass(Integer images, String memberNo, String name, String phone, String address, String email, String profPic) {
        this.images = images;
        this.memberNo = memberNo;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.profPic = profPic;
    }

    public Integer getImages() {
        return images;
    }

    public void setImages(Integer images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(String memberNo) {
        this.memberNo = memberNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfPic() {
        return profPic;
    }

    public void setProfPic(String profPic) {
        this.profPic = profPic;
    }
}
