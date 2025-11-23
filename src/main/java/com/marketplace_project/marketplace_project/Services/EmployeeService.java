package com.marketplace_project.marketplace_project.Services;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.Entities.Employee;
import com.marketplace_project.marketplace_project.EntitiesDTOs.EmployeeDTO;
import com.marketplace_project.marketplace_project.Repositories.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<EmployeeDTO> getEmployeesByDepartament(Departament departament){
        return employeeRepository.getEmployeesByDepartament(departament);
    }

    public List<EmployeeDTO> getEmployeesBySupervisior(Employee supervisior) {
        return employeeRepository.getEmployeesBySupervisior(supervisior);
    }

    public List<EmployeeDTO> getEmployeesByNumberOfOrdersDeliveredInDate(Integer minOrdersDelivered, Integer maxOrdersDelivered, Date fromTime, Date toTime) {
        return employeeRepository.getEmployeesByNumberOfOrdersDeliveredInDate(minOrdersDelivered, maxOrdersDelivered, fromTime, toTime);
    }
}