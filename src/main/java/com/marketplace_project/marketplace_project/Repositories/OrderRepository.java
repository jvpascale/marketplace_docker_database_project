package com.marketplace_project.marketplace_project.Repositories;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.EntitiesDTOs.OrderDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Repository
public class OrderRepository {

    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ============================================================
    // Busca Pedidos onde o usuário é Comprador OU Vendedor
    // ============================================================
    public List<OrderDTO> getOrdersByUser(String userId) {
        // Nota: Removi as flags booleanas pois o seu SQL já verifica os dois casos com OR.
        // Se o ID aparecer na coluna vendedor OU na coluna comprador, ele traz.
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
           WHERE P.vendedor_id = ? OR P.comprador_id = ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, userId, userId);
    }

    // ============================================================
    // Busca Pedidos por CPF do Funcionário responsável
    // ============================================================
    public List<OrderDTO> getOrdersByCpfEmployee(Integer cpf){
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
           WHERE P.funcionario_cpf = ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, cpf);
    }

    // ============================================================
    // Busca Pedidos por faixa de preço
    // ============================================================
    public List<OrderDTO> getOrderByPrice(Float minPrice, Float maxPrice){
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
           WHERE P.valor_total > ? AND P.valor_total < ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, minPrice, maxPrice);
    }

    // ============================================================
    // Busca Pedidos por Departamento (Destino) e Data
    // ============================================================
    public List<OrderDTO> getOrdersByDepartamentAndDate(Departament departament, Date fromTime, Date toTime){
        // CORREÇÃO: Adicionei "AND U.localizacao = ?" ao final,
        // senão ele pegaria pedidos de todos os departamentos.
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
            INNER JOIN Unidade AS U
                ON U.localizacao = P.destino_localizacao
           WHERE P.data_de_entrega > ? 
             AND P.data_de_entrega < ?
             AND U.localizacao = ? 
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, fromTime, toTime, departament.getLocalization());
    }

    // ============================================================
    // Busca Pedidos por Status e Data
    // ============================================================
    public List<OrderDTO> getOrdersByStatusAndDate(String orderStatus, Date fromTime, Date toTime){
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
           WHERE P.data_de_entrega > ? 
             AND P.data_de_entrega < ?
             AND P.status = ?
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, fromTime, toTime, orderStatus);
    }

    // ============================================================
    // MAPPER GERAL (Salva centenas de linhas de código repetido)
    // ============================================================
    private OrderDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        OrderDTO dto = new OrderDTO();
        dto.setCode(rs.getInt("codigo"));
        dto.setStatus(rs.getString("status"));
        dto.setCreationDate(rs.getDate("data_de_criacao"));
        dto.setTotalValue(rs.getFloat("valor_total"));
        dto.setBuyerId(rs.getString("comprador_id"));
        dto.setSellerId(rs.getString("vendedor_id"));
        dto.setPaymentMethod(rs.getString("meio_de_pagamento"));
        dto.setInstallments(rs.getInt("numero_de_parcelas"));
        dto.setDestinationLocalization(rs.getString("destino_localizacao"));
        dto.setOriginLocalization(rs.getString("origem_localizacao"));

        // Flags booleanas (assumindo que no banco sejam BOOLEAN ou BIT)
        dto.setOriginArrivalFlag(rs.getBoolean("o_flag_chegada"));
        dto.setOriginDate(rs.getDate("o_data"));
        dto.setDestinationArrivalFlag(rs.getBoolean("d_flag_chegada"));
        dto.setDestinationDate(rs.getDate("d_data"));

        dto.setEmployeeCpf(rs.getInt("funcionario_cpf"));
        dto.setVehiclePlate(rs.getString("placa_do_veiculo"));
        dto.setDeliveryDate(rs.getDate("data_de_entrega"));
        dto.setEstimatedDeliveryDate(rs.getDate("previsao_de_entrega"));

        return dto;
    }
}