package com.marketplace_project.marketplace_project.Services;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DependentDTO;
import com.marketplace_project.marketplace_project.Repositories.DependentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependentService {

    private final DependentRepository dependentRepository;

    public DependentService(DependentRepository dependentRepository) {
        this.dependentRepository = dependentRepository;
    }

    // 1. Busca dependentes por CPF do funcionário
    public List<DependentDTO> getDependentsByEmployee(Integer employeeCpf) {
        return dependentRepository.getDependentsByEmployee(employeeCpf);
    }

    // 2. Busca filhos menores de idade (<18)
    public List<DependentDTO> getMinorChildren() {
        return dependentRepository.getMinorChildren();
    }

    // 3. Busca dependentes por unidade (localização)
    public List<DependentDTO> getDependentsByUnit(String unitLocalization) {
        return dependentRepository.getDependentsByUnit(unitLocalization);
    }
}