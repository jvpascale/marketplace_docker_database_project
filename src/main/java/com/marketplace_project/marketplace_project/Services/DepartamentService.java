package com.marketplace_project.marketplace_project.Services;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DepartamentDTO;
import com.marketplace_project.marketplace_project.Repositories.DepartamentRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DepartamentService {

    private final DepartamentRepository departamentRepository;

    public DepartamentService(DepartamentRepository departamentRepository) {
        this.departamentRepository = departamentRepository;
    }

    public List<DepartamentDTO> getDepartamentByEmployeeQuantity(Integer minEmployeesQuantity, Integer maxEmployeesQuantity){
        return departamentRepository.getDepartamentByEmployeeQuantity(minEmployeesQuantity, maxEmployeesQuantity);
    }

    public List<DepartamentDTO> getOriginDestinationByOrder(Integer code){
        return departamentRepository.getOriginDestinationByOrder(code);
    }

    public List<DepartamentDTO> getDepartamentByOrderQuantity(Integer minOrders, Integer maxOrders, Date fromTime, Date toTime){
        return departamentRepository.getDepartamentByOrderQuantity( minOrders,  maxOrders, fromTime, toTime);
    }
}
