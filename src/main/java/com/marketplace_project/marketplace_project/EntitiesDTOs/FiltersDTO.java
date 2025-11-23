package com.marketplace_project.marketplace_project.EntitiesDTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

public class FiltersDTO {

    // ==========================================
    // Filtros para USER e BUYERS
    // ==========================================

    public record BuyerFilter(
            String category,
            String name,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date from,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date to
    ) {}

    public record IdFilter(Integer id) {}

    public record LastNameFilter(String lastname) {}

    public record UserIdFilter(Integer userId) {}


    // ==========================================
    // Filtros para PRODUCT e SALES
    // ==========================================

    public record SalesQuantityDateFilter(
            Integer min,
            Integer max,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date from,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date to
    ) {}

    public record PriceFilter(Float minPrice, Float maxPrice) {}

    public record SellerFilter(String sellerId) {}


    // ==========================================
    // Filtros para ORDERS
    // ==========================================

    public record CpfFilter(Integer cpf) {}

    public record LocalizationDateFilter(
            String localization,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date from,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date to
    ) {}

    public record StatusDateFilter(
            String status,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date from,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date to
    ) {}


    // ==========================================
    // Filtros para EMPLOYEES
    // ==========================================

    public record LocalizationFilter(String localization) {}

    public record SupervisorCpfFilter(Integer supervisorCpf) {}


    // ==========================================
    // Filtros para DEPENDENTS
    // ==========================================

    public record EmployeeCpfFilter(Integer employeeCpf) {}

    public record UnitLocalizationFilter(String unitLocalization) {}


    // ==========================================
    // Filtros para DEPARTMENT
    // ==========================================

    public record RangeFilter(Integer min, Integer max) {}

    public record OrderCodeFilter(Integer orderCode) {}

    // CORREÇÃO: Renomeado de OrderQuantityDateFilter para OrderFlowFilter
    // para bater com o DepartamentController
    public record OrderFlowFilter(
            Integer minOrders,
            Integer maxOrders,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date from,
            @JsonFormat(pattern = "yyyy-MM-dd", timezone = "UTC") Date to
    ) {}
}