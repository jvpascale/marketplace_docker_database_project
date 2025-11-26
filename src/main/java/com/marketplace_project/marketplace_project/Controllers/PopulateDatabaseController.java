package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DepartamentDTO;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.OrderCodeFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.OrderFlowFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.RangeFilter;
import com.marketplace_project.marketplace_project.Services.DepartamentService;
import com.marketplace_project.marketplace_project.Services.PopulateDatabaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/database")
@CrossOrigin(origins = "http://localhost:3000")
public class PopulateDatabaseController {

    private final PopulateDatabaseService populateDatabaseService;

    public PopulateDatabaseController(PopulateDatabaseService populateDatabaseService) {
        this.populateDatabaseService = populateDatabaseService;
    }

    // ============================================================
    // 1. Filtrar Departamentos por Quantidade de Funcionários
    // O Swagger vai ler automaticamente: { "min": 0, "max": 0 }
    // ============================================================
    @PostMapping("/populate")
    public ResponseEntity<Void> populateDatabase() {
        populateDatabaseService.populateDatabase();
        return ResponseEntity.ok().build();
    }

}