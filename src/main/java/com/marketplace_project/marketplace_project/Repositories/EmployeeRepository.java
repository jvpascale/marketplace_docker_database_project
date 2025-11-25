package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.Entities.Employee;
import com.marketplace_project.marketplace_project.EntitiesDTOs.EmployeeDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class EmployeeRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================================
    // Busca funcionários por Departamento (Localização)
    // ============================================================
    public List<EmployeeDTO> getEmployeesByDepartament(Departament departament) {
        // Alterado para ILIKE (Case Insensitive) e ordenado por nome
        String sql = """
            SELECT 
                F.cpf,
                F.salario,
                F.nome,
                F.cargo,
                F.unidade_localizacao,
                F.supervisor_cpf
            FROM Funcionario AS F
            WHERE F.unidade_localizacao ILIKE ?
            ORDER BY F.nome ASC
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                "%" + departament.getLocalization() + "%" // Adicionado % para busca parcial
        );
    }

    // ============================================================
    // Busca funcionários por Supervisor
    // ============================================================
    public List<EmployeeDTO> getEmployeesBySupervisior(Employee supervisior) {
        // Usamos LIKE aqui. Se o supervisor tiver CPF "123.456...",
        // buscar por "123" vai trazer os subordinados dele.
        String sql = """
            SELECT
                F.cpf,
                F.salario,
                F.nome,
                F.cargo,
                F.unidade_localizacao,
                F.supervisor_cpf
            FROM Funcionario AS F
            WHERE F.supervisor_cpf LIKE ?
            ORDER BY F.nome ASC
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                "%" + supervisior.getCpf() + "%" // Adicionado % para busca parcial
        );
    }

    // ============================================================
    // Busca funcionários por produtividade (pedidos entregues em data)
    // ============================================================
    public List<EmployeeDTO> getEmployeesByNumberOfOrdersDeliveredInDate(Integer minOrdersDelivered, Integer maxOrdersDelivered, Date fromTime, Date toTime) {
        // Aqui os filtros WHERE são de data (não usa LIKE),
        // mas adicionei a ordenação no final.
        String query_sql = """
            SELECT
                F.cpf,
                F.salario,
                F.nome,
                F.cargo,
                F.unidade_localizacao,
                F.supervisor_cpf,
                COUNT(P.codigo) AS quantidade_pedidos
            FROM Funcionario AS F
            LEFT JOIN Pedidos P
                ON F.cpf = P.funcionario_cpf
            WHERE P.data_de_entrega BETWEEN ? AND ?
            GROUP BY F.cpf, F.salario, F.nome, F.cargo, F.unidade_localizacao, F.supervisor_cpf
            HAVING count(P.codigo) BETWEEN ? AND ?
            ORDER BY F.nome ASC
        """;

        // Nota técnica: Adicionei os campos do SELECT no GROUP BY para garantir
        // compatibilidade com regras estritas de SQL (Full Group By), além de ordenar.

        return jdbcTemplate.query(
                query_sql,
                this::mapRowToDto,
                // Parâmetros na ordem dos (?)
                fromTime,
                toTime,
                minOrdersDelivered,
                maxOrdersDelivered
        );
    }

    // ============================================================
    // Método Auxiliar de Mapeamento (RowMapper)
    // ============================================================
    private EmployeeDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setCpf(rs.getString("cpf"));
        dto.setSalary(rs.getFloat("salario"));
        dto.setName(rs.getString("nome"));
        dto.setRole(rs.getString("cargo"));
        dto.setDepartament_localization(rs.getString("unidade_localizacao"));
        dto.setCpf_manager(rs.getString("supervisor_cpf"));
        return dto;
    }
}