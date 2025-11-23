package com.marketplace_project.marketplace_project.EntitiesDTOs;

import java.util.Date;

public class OrderDTO {

    private Integer code;
    private Float totalValue;
    private String status;
    private Date creationDate;

    private String buyerId;
    private String sellerId;
    private Integer employeeCpf;


    private String paymentMethod;
    private Integer installments;


    private String destinationLocalization;
    private String originLocalization;


    private Boolean originArrivalFlag;
    private Date originDate;

    private Boolean destinationArrivalFlag;
    private Date destinationDate;


    private String vehiclePlate;
    private Date deliveryDate;
    private Date estimatedDeliveryDate;

    public OrderDTO() {}

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Float getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Float totalValue) {
        this.totalValue = totalValue;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public Integer getEmployeeCpf() {
        return employeeCpf;
    }

    public void setEmployeeCpf(Integer employeeCpf) {
        this.employeeCpf = employeeCpf;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getInstallments() {
        return installments;
    }

    public void setInstallments(Integer installments) {
        this.installments = installments;
    }

    public String getDestinationLocalization() {
        return destinationLocalization;
    }

    public void setDestinationLocalization(String destinationLocalization) {
        this.destinationLocalization = destinationLocalization;
    }

    public String getOriginLocalization() {
        return originLocalization;
    }

    public void setOriginLocalization(String originLocalization) {
        this.originLocalization = originLocalization;
    }

    public Boolean getOriginArrivalFlag() {
        return originArrivalFlag;
    }

    public void setOriginArrivalFlag(Boolean originArrivalFlag) {
        this.originArrivalFlag = originArrivalFlag;
    }

    public Date getOriginDate() {
        return originDate;
    }

    public void setOriginDate(Date originDate) {
        this.originDate = originDate;
    }

    public Boolean getDestinationArrivalFlag() {
        return destinationArrivalFlag;
    }

    public void setDestinationArrivalFlag(Boolean destinationArrivalFlag) {
        this.destinationArrivalFlag = destinationArrivalFlag;
    }

    public Date getDestinationDate() {
        return destinationDate;
    }

    public void setDestinationDate(Date destinationDate) {
        this.destinationDate = destinationDate;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public void setEstimatedDeliveryDate(Date estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }
}