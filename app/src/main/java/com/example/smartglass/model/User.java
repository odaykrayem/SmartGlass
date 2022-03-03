package com.example.smartglass.model;

public class User {

    //all users have:
    private int id;
//    private int smartGlassId;
    private String name;
    private String email;


    public User(int id, int smartGlassId, String name, String email) {
        this.id = id;
//        this.smartGlassId = smartGlassId;
        this.name = name;
        this.email = email;
    }

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getSmartGlassId() {
//        return smartGlassId;
//    }
//
//    public void setSmartGlassId(int smartGlassId) {
//        this.smartGlassId = smartGlassId;
//    }


}
