package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.EntitiesDTOs.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 4) Buscar compradores por Categoria, Nome e Data
     */
    public List<UserDTO> getBuyerUsersByOrderCategoryAndDate(String category, String name, Date fromTime, Date toTime) {
        String sql = """
            SELECT DISTINCT
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario U
            INNER JOIN Pedidos P ON U.id = P.comprador_id
            INNER JOIN Produto_Contem_Pedidos PCP ON P.codigo = PCP.pedido_codigo
            INNER JOIN Produto PR ON PCP.produto_id = PR.id
            WHERE 
                PR.categoria ILIKE ?
                AND U.p_nome ILIKE ?
                AND P.data_de_criacao BETWEEN ? AND ?
            ORDER BY U.p_nome ASC
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                "%" + category + "%",
                "%" + name + "%",
                fromTime,
                toTime
        );
    }

    /**
     * 1) Pegar usuarios pelo preço do pedidos
     */
    public List<UserDTO> getBuyerUsersByOrderPrice(Float minPrice, Float maxPrice) {
        // SQL Check: Tabela 'Pedidos' usa a coluna 'valor_total' (não 'preco')
        String query_sql = """
            SELECT DISTINCT
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario U
            INNER JOIN Pedidos P ON U.id = P.comprador_id
            WHERE P.valor_total BETWEEN ? AND ?
            ORDER BY U.p_nome ASC
        """;

        return jdbcTemplate.query(query_sql, this::mapRowToDto, minPrice, maxPrice);
    }

    /**
     * 2) Buscar um usuário pelo ID
     */
    public UserDTO getUserById(Integer id) {
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario AS U
            WHERE U.id = ?
        """;

        List<UserDTO> result = jdbcTemplate.query(query_sql, this::mapRowToDto, id);

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 3) Buscar usuários inativos pela data (Sem compras E sem vendas).
     */
    public List<UserDTO> getInativeUsersByDate(Date from, Date to) {
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario U
            WHERE 
                -- 1. Filtra quem NÃO COMPROU nada no período (comprador_id na tabela Pedidos)
                NOT EXISTS (
                    SELECT 1 
                    FROM Pedidos P 
                    WHERE P.comprador_id = U.id 
                    AND P.data_de_criacao BETWEEN ? AND ?
                )
                AND
                -- 2. Filtra quem NÃO VENDEU nada no período
                -- SQL Check: Na tabela 'Produto', o dono é 'usuario_id', NÃO 'vendedor_id'
                NOT EXISTS (
                    SELECT 1 
                    FROM Pedidos P2
                    INNER JOIN Produto_Contem_Pedidos PCP ON P2.codigo = PCP.pedido_codigo
                    INNER JOIN Produto PR ON PCP.produto_id = PR.id
                    WHERE PR.usuario_id = U.id  -- CORREÇÃO CRÍTICA AQUI
                    AND P2.data_de_criacao BETWEEN ? AND ?
                )
            ORDER BY U.p_nome ASC
        """;

        return jdbcTemplate.query(
                query_sql,
                this::mapRowToDto,
                from, to, from, to
        );
    }

    // ============================================================
    // Mapper Auxiliar
    // ============================================================
    private UserDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        UserDTO dto = new UserDTO();
        // SQL Check: Tabela Usuario usa 'id' (Integer), 'endereco', 'p_nome', 'sobrenome'
        dto.setId(rs.getString("id"));
        dto.setAddress(rs.getString("endereco"));
        dto.setFirst_name(rs.getString("p_nome"));
        dto.setLast_name(rs.getString("sobrenome"));
        return dto;
    }
}