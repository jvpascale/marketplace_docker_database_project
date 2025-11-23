package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DependentDTO;
// Importamos os Records específicos definidos no FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.EmployeeCpfFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.UnitLocalizationFilter;

import com.marketplace_project.marketplace_project.Services.DependentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dependents")
public class DependentController {

    private final DependentService dependentService;

    public DependentController(DependentService dependentService) {
        this.dependentService = dependentService;
    }

    // ============================================================
    // 1. Busca dependentes por CPF do funcionário
    // JSON Esperado: { "employeeCpf": 12345678900 }
    // ============================================================
    @PostMapping("/employee")
    public ResponseEntity<List<DependentDTO>> getDependentsByEmployee(@RequestBody EmployeeCpfFilter filter) {
        return ResponseEntity.ok(
                dependentService.getDependentsByEmployee(filter.employeeCpf())
        );
    }

    // ============================================================
    // 2. Busca filhos menores de idade (<18)
    // JSON Esperado: {}
    // ============================================================
    @PostMapping("/minors")
    public ResponseEntity<List<DependentDTO>> getMinorChildren() {
        return ResponseEntity.ok(
                dependentService.getMinorChildren()
        );
    }

    // ============================================================
    // 3. Busca dependentes por unidade (localização)
    // JSON Esperado: { "unitLocalization": "Zona Norte" }
    // ============================================================
    @PostMapping("/unit")
    public ResponseEntity<List<DependentDTO>> getDependentsByUnit(@RequestBody UnitLocalizationFilter filter) {
        return ResponseEntity.ok(
                dependentService.getDependentsByUnit(filter.unitLocalization())
        );
    }
}