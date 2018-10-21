package com.mnhmilu.app.bondmaker;

import java.util.Date;

public class ContactModel {

    private String name, number,identity,accountType;

    private Date lastCallDate;

    private int dayElapsed;

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

    public Date getLastCallDate() {
        return lastCallDate;
    }

    public void setLastCallDate(Date lastCallDate) {
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