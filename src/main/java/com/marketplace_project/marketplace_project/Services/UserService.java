package com.marketplace_project.marketplace_project.Services;



import com.marketplace_project.marketplace_project.EntitiesDTOs.UserDTO;
import com.marketplace_project.marketplace_project.Repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDTO> getBuyerUsersByOrderCategoryAndDate(String category, String name, Date fromTime, Date toTime){
        return userRepository.getBuyerUsersByOrderCategoryAndDate(category,name,fromTime,toTime);
    }

    public List<UserDTO> getBuyerUsersByOrderPrice(Float min, Float max){
        return userRepository.getBuyerUsersByOrderPrice(min,max);
    }

    //Sem compras nem vendas
    public List<UserDTO> getInativeUsersByDate(Date from, Date to){
        return userRepository.getInativeUsersByDate(from, to);
    }
}
