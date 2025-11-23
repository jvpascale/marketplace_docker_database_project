package com.marketplace_project.marketplace_project.Entities;

public class Dependent {
    private String name;
    private Integer age;
    private String kinship;
    private Integer cpf_employee;

    public Dependent(){}

    public Dependent(String name, Integer age, String kinship, Integer cpf_employee) {
        this.name = name;
        this.age = age;
        this.kinship = kinship;
        this.cpf_employee = cpf_employee;
    }

    public String getKinship() {
        return kinship;
    }

    public void setKinship(String kinship) {
        this.kinship = kinship;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getCpfEmployee() {
        return cpf_employee;
    }

    public void setCpfEmployee(Integer cpf_employee) {
        this.cpf_employee = cpf_employee;
    }
}
