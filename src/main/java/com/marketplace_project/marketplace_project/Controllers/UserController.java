package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.EntitiesDTOs.UserDTO;
// Importamos os Records específicos do FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.BuyerFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.LastNameFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.IdFilter;


import com.marketplace_project.marketplace_project.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ============================================================
    // 1. Buscar compradores por Categoria, Nome e Data
    // JSON Esperado:
    // {
    //   "category": "Eletronicos",
    //   "name": "Joao",
    //   "from": "2025-01-01",
    //   "to": "2025-12-31"
    // }
    // ============================================================
    @PostMapping("/buyers/filter")
    public ResponseEntity<List<UserDTO>> getBuyerUsersByOrderCategoryAndDate(@RequestBody BuyerFilter filter) {
        // As datas já vêm convertidas no filter.from() e filter.to()
        return ResponseEntity.ok(
                userService.getBuyerUsersByOrderCategoryAndDate(
                        filter.category(),
                        filter.name(),
                        filter.from(),
                        filter.to()
                )
        );
    }

    // ============================================================
    // 2. Buscar compradores por "Preço" (ID)
    // JSON Esperado: { "id": "123" }
    // ============================================================
    @PostMapping("/buyers/price")
    public ResponseEntity<UserDTO> getBuyerUsersByOrderPrice(@RequestBody IdFilter filter) {
        UserDTO user = userService.getBuyerUsersByOrderPrice(filter.id());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    // ============================================================
    // 3. Buscar usuários por sobrenome
    // JSON Esperado: { "lastname": "Silva" }
    // ============================================================
    @PostMapping("/search")
    public ResponseEntity<List<UserDTO>> getUsersByLastName(@RequestBody LastNameFilter filter) {
        return ResponseEntity.ok(
                userService.getUsersByLastName(filter.lastname())
        );
    }
}