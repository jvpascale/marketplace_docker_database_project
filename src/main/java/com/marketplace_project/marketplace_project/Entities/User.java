package com.marketplace_project.marketplace_project.Entities;

public class User {

    private Integer id;
    private String address;
    private String first_name;
    private String last_name;

    public User(){}

    public User(Integer id, String last_name, String first_name, String address) {
        this.id = id;
        this.last_name = last_name;
        this.first_name = first_name;
        this.address = address;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
