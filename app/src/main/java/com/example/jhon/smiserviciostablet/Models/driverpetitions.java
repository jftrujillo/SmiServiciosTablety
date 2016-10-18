package com.example.jhon.smiserviciostablet.Models;

/**
 * Created by jhon on 21/05/16.
 */
public class driverpetitions {
    public static final int PETITION_PENDING = 0;
    public static final int PETITION_TAKEN = 1;
    public static final int PETITION_REFUSED = 2 ;

    String actualaddress;
    String futureaddress;
    String id;
    String insuranceid;
    String servicename;
    int state;
    String time;
    String userid;
    String code;
    String date;
    String support_person;

    public String getSupport_person() {
        return support_person;
    }

    public void setSupport_person(String support_person) {
        this.support_person = support_person;
    }

    //region Getters and Setters


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    public String getActualaddress() {
        return actualaddress;
    }

    public void setActualaddress(String actualaddress) {
        this.actualaddress = actualaddress;
    }

    public String getFutureaddress() {
        return futureaddress;
    }

    public void setFutureaddress(String futureaddress) {
        this.futureaddress = futureaddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsuranceid() {
        return insuranceid;
    }

    public void setInsuranceid(String insuranceid) {
        this.insuranceid = insuranceid;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    //endregion
}
