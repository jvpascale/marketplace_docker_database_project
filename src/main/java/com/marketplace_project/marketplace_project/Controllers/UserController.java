package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO;
import com.marketplace_project.marketplace_project.EntitiesDTOs.UserDTO;
// Importamos os Records específicos do FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.BuyerFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.PriceFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.LastNameFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.IdFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.DateFilter;


import com.marketplace_project.marketplace_project.Services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    // ============================================================
    // 2. Buscar compradores por "Preço" em um intervalo de valor.
    // JSON Esperado: { "minPrice": 10.0, "maxPrice": 500.0 }
    // ============================================================
    @PostMapping("/buyers/price")
    public ResponseEntity<List<UserDTO>> getBuyerUsersByOrderPrice(@RequestBody PriceFilter filter) {
        // Chama o service passando min e max
        List<UserDTO> users = userService.getBuyerUsersByOrderPrice(filter.minPrice(), filter.maxPrice());

        // Retorna a lista (vazia ou preenchida) com status 200 OK
        return ResponseEntity.ok(users);
    }

    // ============================================================
    // 1. Buscar todos compradores que compraram determinados produtos por categoria do produto ou nome em um periodo de tempo especifico.
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
    // 2. Buscar usuarios inativos durante um periodo especifico, sem compra nem vendas.
    // ============================================================
    @PostMapping("/inative")
    public ResponseEntity<List<UserDTO>> getInativeUsersByDate(@RequestBody DateFilter filter) {
        List<UserDTO> users = userService.getInativeUsersByDate(filter.from(), filter.to());

        // Retorna 200 OK com a lista (vazia ou cheia)
        return ResponseEntity.ok(users);
    }

}