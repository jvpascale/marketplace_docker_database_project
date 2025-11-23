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
        String sql = """
            SELECT 
                F.cpf,
                F.salario,
                F.nome,
                F.cargo,
                F.unidade_localizacao,
                F.supervisor_cpf
            FROM Funcionario AS F
            WHERE F.unidade_localizacao = ?
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                departament.getLocalization() // Parâmetro 1
        );
    }

    // ============================================================
    // Busca funcionários por Supervisor
    // ============================================================
    public List<EmployeeDTO> getEmployeesBySupervisior(Employee supervisior) {
        String sql = """
            SELECT
                F.cpf,
                F.salario,
                F.nome,
                F.cargo,
                F.unidade_localizacao,
                F.supervisor_cpf
            FROM Funcionario AS F
            WHERE F.supervisor_cpf = ?
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                supervisior.getCpf() // Parâmetro 1
        );
    }

    // ============================================================
    // Busca funcionários por produtividade (pedidos entregues em data)
    // ============================================================
    public List<EmployeeDTO> getEmployeesByNumberOfOrdersDeliveredInDate(Integer minOrdersDelivered, Integer maxOrdersDelivered, Date fromTime, Date toTime) {
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
            WHERE P.data_entrega BETWEEN ? AND ?
            GROUP BY F.cpf
            HAVING count(P.codigo) BETWEEN ? AND ?
        """;

        // Nota: Mesmo que o SQL retorne 'quantidade_pedidos', seu DTO original
        // não mapeava esse campo, então mantive o mapeamento padrão do Employee.
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
    // Reaproveitado por todos os métodos acima
    // ============================================================
    private EmployeeDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setCpf(rs.getInt("cpf"));
        dto.setSalary(rs.getFloat("salario"));
        dto.setName(rs.getString("nome"));
        dto.setRole(rs.getString("cargo"));
        dto.setDepartament_localization(rs.getString("unidade_localizacao"));
        dto.setCpf_manager(rs.getInt("supervisor_cpf"));
        return dto;
    }
}