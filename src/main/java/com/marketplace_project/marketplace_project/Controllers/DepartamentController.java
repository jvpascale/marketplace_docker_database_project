package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DepartamentDTO;
// Importamos os Records definidos dentro da classe FilterDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.RangeFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.OrderCodeFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.OrderFlowFilter;

import com.marketplace_project.marketplace_project.Services.DepartamentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/departments")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartamentController {

    private final DepartamentService departamentService;

    public DepartamentController(DepartamentService departamentService) {
        this.departamentService = departamentService;
    }

    // ============================================================
    // 1. Filtrar Departamentos por Quantidade de Funcionários
    // O Swagger vai ler automaticamente: { "min": 0, "max": 0 }
    // ============================================================
    @PostMapping("/filter/employee-quantity")
    public ResponseEntity<List<DepartamentDTO>> getDepartamentByEmployeeQuantity(@RequestBody RangeFilter filter) {
        return ResponseEntity.ok(
                departamentService.getDepartamentByEmployeeQuantity(filter.min(), filter.max())
        );
    }

    // ============================================================
    // 2. Buscar Origem e Destino de um Pedido
    // O Swagger vai ler automaticamente: { "orderCode": 0 }
    // ============================================================
    @PostMapping("/order-route")
    public ResponseEntity<List<DepartamentDTO>> getOriginDestinationByOrder(@RequestBody OrderCodeFilter filter) {
        return ResponseEntity.ok(
                departamentService.getOriginDestinationByOrder(filter.orderCode())
        );
    }

    // ============================================================
    // 3. Filtrar Departamentos por Fluxo de Pedidos em uma Data
    // O Swagger vai ler automaticamente: { "minOrders": 0, "maxOrders": 0, "from": "yyyy-MM-dd", "to": "yyyy-MM-dd" }
    // ============================================================
    @PostMapping("/filter/order-quantity")
    public ResponseEntity<List<DepartamentDTO>> getDepartamentByOrderQuantity(@RequestBody OrderFlowFilter filter) {
        return ResponseEntity.ok(
                departamentService.getDepartamentByOrderQuantity(
                        filter.minOrders(),
                        filter.maxOrders(),
                        filter.from(),
                        filter.to()
                )
        );
    }
}