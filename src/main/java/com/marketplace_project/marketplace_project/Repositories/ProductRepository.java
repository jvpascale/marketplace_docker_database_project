package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.Entities.User;
import com.marketplace_project.marketplace_project.EntitiesDTOs.ProductDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================================
    // Busca produtos por quantidade de vendas (Via Joins e Group By)
    // ============================================================
    public List<ProductDTO> getProductsBySaleQuantity(Integer minSaleQuantity, Integer maxSaleQuantity, Date fromTime, Date toTime){
        String query_sql = """
            SELECT 
                P.id,
                P.nome,
                P.descricao,
                P.categoria,
                P.preco,
                P.estoque,
                P.status,
                P.usuario_id,
                P.texto_anunciado,
                SUM(PCP.quantidade) AS saleQuantity
            FROM Produto as P
            INNER JOIN Produto_Contem_Pedidos as PCP 
                ON PCP.produto_id = P.id
            INNER JOIN Pedidos as PE 
                ON PCP.pedido_codigo = PE.codigo
            WHERE PE.data_de_criacao > ? 
              AND PE.data_de_criacao < ?  
            GROUP BY
                P.id, P.nome, P.descricao, P.categoria, P.preco, P.estoque,
                P.status, P.usuario_id, P.texto_anunciado
            HAVING
                SUM(PCP.quantidade) > ?
                AND SUM(PCP.quantidade) < ?
        """;
        // Nota: Ajustei o HAVING para usar SUM(...) em vez do alias 'saleQuantity'
        // para garantir compatibilidade total com o padrão SQL, embora Postgres aceite alias.

        return jdbcTemplate.query(
                query_sql,
                this::mapRowToDto,
                // Ordem dos parâmetros (?):
                fromTime,
                toTime,
                minSaleQuantity,
                maxSaleQuantity
        );
    }

    // ============================================================
    // Busca produtos por faixa de preço
    // ============================================================
    public List<ProductDTO> getProductsByPrice(Float minPrice, Float maxPrice){
        String query_sql = """
            SELECT 
                P.id,
                P.nome,
                P.descricao,
                P.categoria,
                P.preco,
                P.estoque,
                P.status,
                P.usuario_id,
                P.texto_anunciado
            FROM Produto as P
            WHERE P.preco > ? 
              AND P.preco < ?
        """;

        return jdbcTemplate.query(
                query_sql,
                this::mapRowToDto,
                minPrice,
                maxPrice
        );
    }

    // ============================================================
    // Busca produtos de um vendedor específico
    // ============================================================
    public List<ProductDTO> getProductsBySellerUser(User user){
        String query_sql = """
            SELECT 
                P.id,
                P.nome,
                P.descricao,
                P.categoria,
                P.preco,
                P.estoque,
                P.status,
                P.usuario_id,
                P.texto_anunciado
            FROM Produto as P
            WHERE P.usuario_id = ?
        """;

        return jdbcTemplate.query(
                query_sql,
                this::mapRowToDto,
                user.getId()
        );
    }

    // ============================================================
    // Mapper Auxiliar
    // ============================================================
    private ProductDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        ProductDTO dto = new ProductDTO();
        dto.setId(rs.getString("id"));
        dto.setName(rs.getString("nome"));
        dto.setDescription(rs.getString("descricao"));
        dto.setCategory(rs.getString("categoria"));
        dto.setPrice(rs.getFloat("preco"));
        dto.setStock(rs.getInt("estoque"));
        dto.setStatus(rs.getString("status"));
        dto.setUser_id(rs.getString("usuario_id"));
        return dto;
    }
}