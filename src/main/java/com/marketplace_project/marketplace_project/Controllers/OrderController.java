package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO;
import com.marketplace_project.marketplace_project.EntitiesDTOs.OrderDTO;
// Importamos os Records específicos do FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.IdFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.CpfFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.PriceFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.LocalizationDateFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.StatusDateFilter;

import com.marketplace_project.marketplace_project.Services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // ============================================================
    // 1. Buscar Pedidos por Usuário (Comprador ou Vendedor)
    // JSON: { "userId": 123 }
    // ============================================================
    @PostMapping("/user")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(@RequestBody IdFilter filter) {
        return ResponseEntity.ok(
                orderService.getOrdersByUser(filter.id())
        );
    }

    // ============================================================
    // 2. Buscar Pedidos por CPF do Funcionário
    // JSON: { "cpf": 12345678900 }
    // ============================================================
    @PostMapping("/employee")
    public ResponseEntity<List<OrderDTO>> getOrdersByCpfEmployee(@RequestBody CpfFilter filter) {
        return ResponseEntity.ok(
                orderService.getOrdersByCpfEmployee(filter.cpf())
        );
    }

    // ============================================================
    // 3. Buscar Pedidos por Faixa de Preço
    // JSON: { "minPrice": 10.50, "maxPrice": 200.00 }
    // ============================================================
    @PostMapping("/filter/price")
    public ResponseEntity<List<OrderDTO>> getOrderByPrice(@RequestBody PriceFilter filter) {
        return ResponseEntity.ok(
                orderService.getOrderByPrice(filter.minPrice(), filter.maxPrice())
        );
    }

    // ============================================================
    // 4. Buscar Pedidos por Departamento e Data
    // JSON: { "localization": "Centro-Sul", "from": "2025-01-01", "to": "2025-12-31" }
    // ============================================================
    @PostMapping("/filter/department")
    public ResponseEntity<List<OrderDTO>> getOrdersByDepartamentAndDate(@RequestBody LocalizationDateFilter filter) {
        // O Service espera um objeto Departament, criamos um temporário
        Departament dep = new Departament();
        dep.setLocalization(filter.localization());

        return ResponseEntity.ok(
                orderService.getOrdersByDepartamentAndDate(dep, filter.from(), filter.to())
        );
    }

    // ============================================================
    // 5. Buscar Pedidos por Status e Data
    // JSON: { "status": "ENTREGUE", "from": "2025-01-01", "to": "2025-12-31" }
    // ============================================================
    @PostMapping("/filter/status")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatusAndDate(@RequestBody StatusDateFilter filter) {
        return ResponseEntity.ok(
                orderService.getOrdersByStatusAndDate(filter.status(), filter.from(), filter.to())
        );
    }
}