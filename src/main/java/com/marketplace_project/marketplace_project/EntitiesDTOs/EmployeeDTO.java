package com.marketplace_project.marketplace_project.EntitiesDTOs;

public class EmployeeDTO {

    private String role;
    private String name;
    private Float salary;
    private Integer cpf;
    private String departament_localization;
    private Integer cpf_manager;

    public EmployeeDTO(){}

    public EmployeeDTO(String role, Integer cpf, Float salary, String name,String departament_localization, Integer cpf_manager) {
        this.role = role;
        this.cpf = cpf;
        this.salary = salary;
        this.name = name;
        this.cpf_manager = cpf_manager;
        this.departament_localization = departament_localization;
    }

    public Integer getCpf_manager() {
        return cpf_manager;
    }

    public void setCpf_manager(Integer cpf_manager) {
        this.cpf_manager = cpf_manager;
    }

    public String getDepartament_localization() {
        return departament_localization;
    }

    public void setDepartament_localization(String departament_localization) {
        this.departament_localization = departament_localization;
    }

    public Integer getCpf() {
        return cpf;
    }

    public void setCpf(Integer cpf) {
        this.cpf = cpf;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
