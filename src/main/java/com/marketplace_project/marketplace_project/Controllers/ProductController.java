package com.marketplace_project.marketplace_project.Controllers;

import com.marketplace_project.marketplace_project.Entities.User;
import com.marketplace_project.marketplace_project.EntitiesDTOs.ProductDTO;
// Importamos os Records específicos do FiltersDTO
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.SalesQuantityDateFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.PriceFilter;
import com.marketplace_project.marketplace_project.EntitiesDTOs.FiltersDTO.SellerFilter;

import com.marketplace_project.marketplace_project.Services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ============================================================
    // 1. Filtrar por Quantidade de Vendas
    // JSON Esperado: { "min": 10, "max": 50, "from": "2025-01-01", "to": "2025-12-31" }
    // ============================================================
    @PostMapping("/filter/sales")
    public ResponseEntity<List<ProductDTO>> getProductsBySaleQuantity(@RequestBody SalesQuantityDateFilter filter) {
        // O Spring já converteu o JSON e as datas automaticamente para o Record
        return ResponseEntity.ok(
                productService.getProductsBySaleQuantity(
                        filter.min(),
                        filter.max(),
                        filter.from(),
                        filter.to()
                )
        );
    }

    // ============================================================
    // 2. Filtrar por Preço
    // JSON Esperado: { "minPrice": 10.50, "maxPrice": 100.00 }
    // ============================================================
    @PostMapping("/filter/price")
    public ResponseEntity<List<ProductDTO>> getProductsByPrice(@RequestBody PriceFilter filter) {
        return ResponseEntity.ok(
                productService.getProductsByPrice(filter.minPrice(), filter.maxPrice())
        );
    }

    // ============================================================
    // 3. Filtrar por Vendedor
    // JSON Esperado: { "sellerId": "usuario_123" }
    // ============================================================
    @PostMapping("/seller")
    public ResponseEntity<List<ProductDTO>> getProductsBySeller(@RequestBody SellerFilter filter) {
        // O Service espera um objeto User, criamos um temporário
        User user = new User();
        user.setId(filter.sellerId());

        return ResponseEntity.ok(productService.getProductsBySellerUser(user));
    }
}