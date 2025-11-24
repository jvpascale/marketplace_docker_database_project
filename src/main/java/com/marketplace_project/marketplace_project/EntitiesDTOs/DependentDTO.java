package com.marketplace_project.marketplace_project.EntitiesDTOs;

public class DependentDTO {
    private String name;
    private Integer age;
    private String kinship;
    private String cpf_employee;

    public DependentDTO(){}

    public DependentDTO(String name, Integer age, String kinship, String cpf_employee) {
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

    public String getCpfEmployee() {
        return cpf_employee;
    }

    public void setCpfEmployee(String cpf_employee) {
        this.cpf_employee = cpf_employee;
    }
}
