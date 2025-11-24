package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DepartamentDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class DepartamentRepository {

    // O Spring injeta essa ferramenta automaticamente
    private final JdbcTemplate jdbcTemplate;

    public DepartamentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DepartamentDTO> getDepartamentByEmployeeQuantity(Integer minEmployeesQuantity, Integer maxEmployeesQuantity) {
        String query_sql = """
            SELECT
                U.localizacao,
                U.numero,
                U.nome,
                U.gerente_cpf,
                COUNT(F.cpf) AS quantidade_funcionarios
            FROM Unidade AS U
            LEFT JOIN Funcionario F
                ON F.unidade_localizacao = U.localizacao
            GROUP BY U.localizacao, U.numero, U.nome, U.gerente_cpf
            HAVING COUNT(F.cpf) BETWEEN ? AND ?
        """;

        // O jdbcTemplate.query executa o SQL, passa os parametros e mapeia o resultado linha por linha
        return jdbcTemplate.query(
                query_sql,
                (rs, rowNum) -> {
                    DepartamentDTO dto = new DepartamentDTO();
                    dto.setLocalization(rs.getString("localizacao"));
                    dto.setNumber(rs.getInt("numero"));
                    dto.setName(rs.getString("nome"));
                    dto.setCpf_manager(rs.getString("gerente_cpf"));
                    return dto;
                },
                minEmployeesQuantity, // Parametro 1 (?)
                maxEmployeesQuantity  // Parametro 2 (?)
        );
    }

    public List<DepartamentDTO> getOriginDestinationByOrder(Integer code) {
        String query_sql = """
            SELECT
                O.localizacao AS origem_localizacao,
                O.numero      AS origem_numero,
                O.nome        AS origem_nome,
                O.gerente_cpf AS origem_gerente_cpf,
                D.localizacao AS destino_localizacao,
                D.numero      AS destino_numero,
                D.gerente_cpf AS destino_gerente_cpf,
                D.nome        AS destino_nome
            FROM Pedidos AS P
            INNER JOIN Unidade AS O ON P.origem_localizacao = O.localizacao
            INNER JOIN Unidade AS D ON P.destino_localizacao = D.localizacao
            WHERE P.codigo = ?
        """;

        // Aqui usamos 'query' com ResultSetExtractor, pois uma única linha do banco
        // vai gerar DOIS objetos na lista (Origem e Destino)
        return jdbcTemplate.query(query_sql, rs -> {
            List<DepartamentDTO> results = new ArrayList<>();

            if (rs.next()) { // Verifica se achou o pedido
                // Mapeia a Unidade de Origem
                DepartamentDTO origem = new DepartamentDTO();
                origem.setLocalization(rs.getString("origem_localizacao"));
                origem.setNumber(rs.getInt("origem_numero"));
                origem.setName(rs.getString("origem_nome"));
                origem.setCpf_manager(rs.getString("origem_gerente_cpf"));
                results.add(origem);

                // Mapeia a Unidade de Destino
                DepartamentDTO destino = new DepartamentDTO();
                destino.setLocalization(rs.getString("destino_localizacao"));
                destino.setNumber(rs.getInt("destino_numero"));
                destino.setName(rs.getString("destino_nome"));
                destino.setCpf_manager(rs.getString("destino_gerente_cpf"));
                results.add(destino);
            }
            return results;
        }, code);
    }

    public List<DepartamentDTO> getDepartamentByOrderQuantity(Integer minOrders, Integer maxOrders, Date fromTime, Date toTime) {
        String query_sql = """
            SELECT
                U.localizacao,
                U.numero,
                U.nome,
                U.gerente_cpf,
                COUNT(P.codigo) AS quantidade_pedidos
            FROM Unidade AS U
            LEFT JOIN Pedidos P
                ON U.localizacao = P.origem_localizacao
                OR U.localizacao = P.destino_localizacao
            WHERE P.data_de_entrega BETWEEN ? AND ?
            GROUP BY U.localizacao, U.numero, U.nome, U.gerente_cpf
            HAVING COUNT(P.codigo) BETWEEN ? AND ?
        """;

        return jdbcTemplate.query(
                query_sql,
                (rs, rowNum) -> {
                    DepartamentDTO dto = new DepartamentDTO();
                    dto.setLocalization(rs.getString("localizacao"));
                    dto.setNumber(rs.getInt("numero"));
                    dto.setName(rs.getString("nome"));
                    dto.setCpf_manager(rs.getString("gerente_cpf"));
                    return dto;
                },
                // Parametros na ordem dos (?)
                fromTime,
                toTime,
                minOrders,
                maxOrders
        );
    }
}