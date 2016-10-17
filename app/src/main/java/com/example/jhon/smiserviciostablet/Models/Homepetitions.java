package com.example.jhon.smiserviciostablet.Models;

/**
 * Created by jhon on 21/05/16.
 */
public class Homepetitions {
    public static final int PETITION_PENDING = 0;
    public static final int PETITION_TAKEN = 1;
    public static final int PETITION_REFUSED = 2 ;


    String id;
    String servicename;
    String address;
    String carid;
    String description;
    String insuranceid;
    String randomcode;
    int state;
    String userid;


    //region Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarid() {
        return carid;
    }

    public void setCarid(String carid) {
        this.carid = carid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInsuranceid() {
        return insuranceid;
    }

    public void setInsuranceid(String insuranceid) {
        this.insuranceid = insuranceid;
    }

    public String getRandomcode() {
        return randomcode;
    }

    public void setRandomcode(String randomcode) {
        this.randomcode = randomcode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    //endregion
}
