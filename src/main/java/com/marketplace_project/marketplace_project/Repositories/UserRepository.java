package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.EntitiesDTOs.UserDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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
    public UserDTO getUserById(String id) {
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