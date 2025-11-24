package com.marketplace_project.marketplace_project.EntitiesDTOs;

public class DepartamentDTO {
    private Integer number;
    private String localization;
    private String name;
    private String cpf_manager;

    public DepartamentDTO(Integer number, String name, String localization,String cpf_manager) {
        this.number = number;
        this.name = name;
        this.localization = localization;
        this.cpf_manager = cpf_manager;
    }

    public DepartamentDTO(){}

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public String getCpf_manager() {
        return cpf_manager;
    }

    public void setCpf_manager(String cpf_manager) {
        this.cpf_manager = cpf_manager;
    }
}
