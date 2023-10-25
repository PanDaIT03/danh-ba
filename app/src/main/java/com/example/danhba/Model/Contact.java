package com.example.danhba.Model;

public class Contact {
    private int id;
    private int isMale;
    private String mName;
    private String mNumber;

    public Contact(int id, int isMale, String mName, String mNumber) {
        this.id = id;
        this.isMale = isMale;
        this.mName = mName;
        this.mNumber = mNumber;
    }

    public Contact(int isMale, String mName, String mNumber) {
        this.isMale = isMale;
        this.mName = mName;
        this.mNumber = mNumber;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIsMale() {
        return isMale;
    }

    public void setIsMale(int isMale) {
        this.isMale = isMale;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmNumber() {
        return mNumber;
    }

    public void setmNumber(String mNumber) {
        this.mNumber = mNumber;
    }
}