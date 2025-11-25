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
        // Alterado PR.categoria para ILIKE
        // Adicionado ORDER BY pelo nome do usuário
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
                "%" + category + "%", // Busca categoria parcial (ex: "eletron" acha "Eletrônicos")
                "%" + name + "%",     // Busca nome parcial
                fromTime,
                toTime
        );
    }

    /**
     * 1) Buscar todos os usuários
     */
    public List<UserDTO> getAllUsers() {
        // Adicionado ORDER BY ID para manter a lista consistente
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario AS U
            ORDER BY U.id ASC
        """;

        return jdbcTemplate.query(query_sql, this::mapRowToDto);
    }

    /**
     * 2) Buscar um usuário pelo ID
     */
    public UserDTO getUserById(Integer id) {
        // IDs são exatos, mantemos o =. Não precisa de ORDER BY pois retorna 1 linha.
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
     * 3) Buscar usuários por sobrenome
     */
    public List<UserDTO> getUsersByLastName(String sobrenome) {
        // Alterado para ILIKE e ordenado pelo primeiro nome
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario AS U
            WHERE U.sobrenome ILIKE ?
            ORDER BY U.p_nome ASC
        """;

        return jdbcTemplate.query(query_sql, this::mapRowToDto, "%" + sobrenome + "%");
    }

    // ============================================================
    // Mapper Auxiliar
    // ============================================================
    private UserDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        UserDTO dto = new UserDTO();
        dto.setId(rs.getString("id"));
        dto.setAddress(rs.getString("endereco"));
        dto.setFirst_name(rs.getString("p_nome"));
        dto.setLast_name(rs.getString("sobrenome"));
        return dto;
    }
}