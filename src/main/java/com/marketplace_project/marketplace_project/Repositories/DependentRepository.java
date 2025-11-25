package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DependentDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class DependentRepository {

    private final JdbcTemplate jdbcTemplate;

    public DependentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================================
    // Query 1: Dependentes de um funcionário pelo CPF
    // ============================================================
    public List<DependentDTO> getDependentsByEmployee(String employeeCpf) {
        String sql = """
            SELECT 
                nome, 
                idade, 
                parentesco, 
                funcionario_cpf
            FROM Dependente
            WHERE funcionario_cpf LIKE ?
            ORDER BY nome ASC
        """;

        // Adicionei os "%" para permitir busca parcial no CPF (ex: digitar o final e achar)
        return jdbcTemplate.query(sql, this::mapRowToDto, "%" + employeeCpf + "%");
    }

    // ============================================================
    // Query 2: Filhos menores de idade (<18)
    // ============================================================
    public List<DependentDTO> getMinorChildren() {
        String sql = """
            SELECT 
                nome, 
                idade, 
                parentesco,
                funcionario_cpf
            FROM Dependente
            WHERE parentesco IN ('Filho', 'Filha') 
              AND idade < 18
            ORDER BY nome ASC
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto);
    }

    // ============================================================
    // Query 3: Dependentes por unidade via JOIN com Funcionário
    // ============================================================
    public List<DependentDTO> getDependentsByUnit(String unitLocalization) {
        String sql = """
            SELECT 
                D.nome, 
                D.idade, 
                D.parentesco, 
                D.funcionario_cpf,
                F.nome AS nome_responsavel,
                F.unidade_localizacao
            FROM Dependente AS D
            INNER JOIN Funcionario AS F 
                ON D.funcionario_cpf = F.cpf
            WHERE F.unidade_localizacao ILIKE ?
            ORDER BY D.nome ASC
        """;

        // Adicionei os "%" para filtrar partes do nome da localização (ex: "sao" acha "São Paulo")
        return jdbcTemplate.query(sql, this::mapRowToDto, "%" + unitLocalization + "%");
    }

    // ============================================================
    // Método Auxiliar (RowMapper)
    // ============================================================
    private DependentDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        DependentDTO dep = new DependentDTO();
        dep.setName(rs.getString("nome"));
        dep.setAge(rs.getInt("idade"));
        dep.setKinship(rs.getString("parentesco"));
        dep.setCpfEmployee(rs.getString("funcionario_cpf"));
        return dep;
    }
}