package com.mnhmilu.app.bondmaker.entity;

import java.util.Date;

public class ContactModel {


    private int id;

    private String name, number,identity,accountType,callType,contact_tag;

    private String lastCallDate;

    private int dayElapsed;


  /*  public ContactModel(int id, String name, String number, String identity, String callType, String contact_tag, String lastCallDate, int dayElapsed) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.identity = identity;
        this.callType = callType;
        this.contact_tag = contact_tag;
        this.lastCallDate = lastCallDate;
        this.dayElapsed = dayElapsed;
    }*/

    public ContactModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContact_tag() {
        return contact_tag;
    }

    public void setContact_tag(String contact_tag) {
        this.contact_tag = contact_tag;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public int getDayElapsed() {
        return dayElapsed;
    }

    public void setDayElapsed(int dayElapsed) {
        this.dayElapsed = dayElapsed;
    }

    public String getLastCallDate() {
        return lastCallDate;
    }

    public void setLastCallDate(String lastCallDate) {
        this.lastCallDate = lastCallDate;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}