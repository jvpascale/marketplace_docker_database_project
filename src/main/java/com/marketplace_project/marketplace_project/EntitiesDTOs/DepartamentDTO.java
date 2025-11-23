package com.marketplace_project.marketplace_project.EntitiesDTOs;

public class DepartamentDTO {
    private Integer number;
    private String localization;
    private String name;
    private Integer cpf_manager;

    public DepartamentDTO(Integer number, String name, String localization,Integer cpf_manager) {
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

    public Integer getCpf_manager() {
        return cpf_manager;
    }

    public void setCpf_manager(Integer cpf_manager) {
        this.cpf_manager = cpf_manager;
    }
}
