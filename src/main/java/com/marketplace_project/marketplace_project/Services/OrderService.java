package com.marketplace_project.marketplace_project.Services;

import com.marketplace_project.marketplace_project.Entities.Departament;
import com.marketplace_project.marketplace_project.EntitiesDTOs.OrderDTO;
import com.marketplace_project.marketplace_project.Repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderDTO> getOrdersByUser(Integer userId){
        return orderRepository.getOrdersByUser(userId);
    }

    public List<OrderDTO> getOrdersByCpfEmployee(String cpf){
        return orderRepository.getOrdersByCpfEmployee(cpf);
    }

    public List<OrderDTO> getOrderByPrice(Float minPrice, Float maxPrice){
        return orderRepository.getOrderByPrice(minPrice, maxPrice);
    }

    public List<OrderDTO> getOrdersByDepartamentAndDate(Departament departament, Date fromTime, Date toTime){
        return orderRepository.getOrdersByDepartamentAndDate(departament, fromTime, toTime);
    }

    public List<OrderDTO> getOrdersByStatusAndDate(String orderStatus, Date fromTime, Date toTime){
        return orderRepository.getOrdersByStatusAndDate(orderStatus, fromTime, toTime);
    }
}
