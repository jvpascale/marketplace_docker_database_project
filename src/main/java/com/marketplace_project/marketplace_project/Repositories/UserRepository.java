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
     * Essa query é complexa: cruza Usuário -> Pedidos -> Produtos -> Categoria
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
                PR.categoria = ?
                AND U.p_nome ILIKE ?  -- ILIKE faz busca case-insensitive (joao acha Joao)
                AND P.data_de_criacao BETWEEN ? AND ?
        """;

        // Dica: Para o nome, geralmente queremos buscar "contém" ou "igual".
        // Vou assumir busca exata ou parcial. Se quiser parcial use "%" + name + "%"
        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                category,
                name,
                fromTime,
                toTime
        );
    }

    /**
     * 1) Buscar todos os usuários
     */
    public List<UserDTO> getAllUsers() {
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario AS U
        """;

        return jdbcTemplate.query(query_sql, this::mapRowToDto);
    }

    /**
     * 2) Buscar um usuário pelo ID
     * Retorna null se não encontrar (igual ao seu código original)
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

        // Estratégia: Buscamos uma lista. Se tiver algo, pegamos o primeiro.
        List<UserDTO> result = jdbcTemplate.query(query_sql, this::mapRowToDto, id);

        return result.isEmpty() ? null : result.get(0);
    }

    /**
     * 3) Buscar usuários por sobrenome
     */
    public List<UserDTO> getUsersByLastName(String sobrenome) {
        String query_sql = """
            SELECT 
                U.id,
                U.endereco,
                U.p_nome,
                U.sobrenome
            FROM Usuario AS U
            WHERE U.sobrenome = ?
        """;

        return jdbcTemplate.query(query_sql, this::mapRowToDto, sobrenome);
    }

    // ============================================================
    // Mapper Auxiliar (Centraliza a lógica de criar o DTO)
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