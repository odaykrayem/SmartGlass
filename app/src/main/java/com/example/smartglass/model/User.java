package com.example.smartglass.model;

public class User {

    //all users have:
    private int id;
    private int smartGlassId;
    private String fname;
    private String lname;


    //manager register result
    public User(int id, int smartGlassId, String fname, String lname) {
        this.id = id;
        this.smartGlassId = smartGlassId;
        this.fname = fname;
        this.lname = lname;

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSmartGlassId() {
        return smartGlassId;
    }

    public void setSmartGlassId(int smartGlassId) {
        this.smartGlassId = smartGlassId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }


    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

}
