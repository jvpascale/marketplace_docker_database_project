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
    public List<OrderDTO> getOrdersByUser(Integer userId) {
        // IDs são números exatos, então mantemos a comparação =,
        // mas adicionamos a ordenação.
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
           ORDER BY P.codigo ASC
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, userId, userId);
    }

    // ============================================================
    // Busca Pedidos por CPF do Funcionário responsável
    // ============================================================
    public List<OrderDTO> getOrdersByCpfEmployee(String cpf){
        // Alterado para LIKE para permitir busca parcial do CPF
        String sql = """
           SELECT 
            P.codigo, P.status, P.data_de_criacao, P.valor_total,
            P.comprador_id, P.vendedor_id, P.meio_de_pagamento,
            P.numero_de_parcelas, P.destino_localizacao, P.origem_localizacao,
            P.o_flag_chegada, P.o_data, P.d_flag_chegada, P.d_data,
            P.funcionario_cpf, P.placa_do_veiculo,
            P.data_de_entrega, P.previsao_de_entrega
           FROM Pedidos AS P
           WHERE P.funcionario_cpf LIKE ?
           ORDER BY P.codigo ASC
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, "%" + cpf + "%");
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
           ORDER BY P.codigo ASC
        """;

        return jdbcTemplate.query(sql, this::mapRowToDto, minPrice, maxPrice);
    }

    // ============================================================
    // Busca Pedidos por Departamento (Destino) e Data
    // ============================================================
    public List<OrderDTO> getOrdersByDepartamentAndDate(Departament departament, Date fromTime, Date toTime){
        // Alterado para ILIKE para ignorar Case Sensitive na Localização
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
             AND U.localizacao ILIKE ? 
           ORDER BY P.codigo ASC
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                fromTime,
                toTime,
                "%" + departament.getLocalization() + "%"
        );
    }

    // ============================================================
    // Busca Pedidos por Status e Data
    // ============================================================
    public List<OrderDTO> getOrdersByStatusAndDate(String orderStatus, Date fromTime, Date toTime){
        // Alterado para ILIKE (ex: busca 'entregue' acha 'ENTREGUE')
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
             AND P.status ILIKE ?
           ORDER BY P.codigo ASC
        """;

        return jdbcTemplate.query(
                sql,
                this::mapRowToDto,
                fromTime,
                toTime,
                "%" + orderStatus + "%"
        );
    }

    // ============================================================
    // MAPPER GERAL
    // ============================================================
    private OrderDTO mapRowToDto(ResultSet rs, int rowNum) throws SQLException {
        OrderDTO dto = new OrderDTO();
        dto.setCode(rs.getInt("codigo"));
        dto.setStatus(rs.getString("status"));
        dto.setCreationDate(rs.getDate("data_de_criacao"));
        dto.setTotalValue(rs.getFloat("valor_total"));
        dto.setBuyerId(rs.getInt("comprador_id"));
        dto.setSellerId(rs.getInt("vendedor_id"));
        dto.setPaymentMethod(rs.getString("meio_de_pagamento"));
        dto.setInstallments(rs.getInt("numero_de_parcelas"));
        dto.setDestinationLocalization(rs.getString("destino_localizacao"));
        dto.setOriginLocalization(rs.getString("origem_localizacao"));

        dto.setOriginArrivalFlag(rs.getBoolean("o_flag_chegada"));
        dto.setOriginDate(rs.getDate("o_data"));
        dto.setDestinationArrivalFlag(rs.getBoolean("d_flag_chegada"));
        dto.setDestinationDate(rs.getDate("d_data"));

        dto.setEmployeeCpf(rs.getString("funcionario_cpf"));
        dto.setVehiclePlate(rs.getString("placa_do_veiculo"));
        dto.setDeliveryDate(rs.getDate("data_de_entrega"));
        dto.setEstimatedDeliveryDate(rs.getDate("previsao_de_entrega"));

        return dto;
    }
}
