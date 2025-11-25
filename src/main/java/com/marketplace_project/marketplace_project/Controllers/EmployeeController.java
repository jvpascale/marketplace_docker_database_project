package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.Entities.Employee;
import com.marketplace_project.marketplace_project.EntitiesDTOs.EmployeeDTO;
// Importamos os Records específicos do FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.LocalizationFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.SupervisorCpfFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.SalesQuantityDateFilter;

import com.marketplace_project.marketplace_project.Services.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/employees")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ============================================================
    // 1. Buscar Funcionários por Departamento
    // JSON Esperado: { "localization": "Centro-Sul" }
    // ============================================================
    @PostMapping("/filter/department")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartament(@RequestBody LocalizationFilter filter) {
        // O Service espera um objeto Departament, criamos um temporário
        Departament dep = new Departament();
        dep.setLocalization(filter.localization());

        return ResponseEntity.ok(employeeService.getEmployeesByDepartament(dep));
    }

    // ============================================================
    // 2. Buscar Funcionários por Supervisor
    // JSON Esperado: { "supervisorCpf": 12345678900 }
    // ============================================================
    @PostMapping("/filter/supervisor")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesBySupervisior(@RequestBody SupervisorCpfFilter filter) {
        // O Service espera um objeto Employee, criamos um temporário
        Employee supervisor = new Employee();
        supervisor.setCpf(filter.supervisorCpf());

        return ResponseEntity.ok(employeeService.getEmployeesBySupervisior(supervisor));
    }

    // ============================================================
    // 3. Buscar Funcionários por Produtividade (Entregas em Data)
    // JSON Esperado: { "min": 10, "max": 50, "from": "2025-01-01", "to": "2025-12-31" }
    // ============================================================
    @PostMapping("/filter/productivity")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByNumberOfOrdersDeliveredInDate(@RequestBody SalesQuantityDateFilter filter) {
        // Reutilizamos o SalesQuantityDateFilter aqui pois ele já tem min, max, from e to.
        return ResponseEntity.ok(
                employeeService.getEmployeesByNumberOfOrdersDeliveredInDate(
                        filter.min(),
                        filter.max(),
                        filter.from(),
                        filter.to()
                )
        );
    }
}