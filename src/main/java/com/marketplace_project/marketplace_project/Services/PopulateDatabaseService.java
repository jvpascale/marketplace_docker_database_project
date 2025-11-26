package com.marketplace_project.marketplace_project.Services;

import com.marketplace_project.marketplace_project.EntitiesDTOs.DepartamentDTO;
import com.marketplace_project.marketplace_project.Repositories.DepartamentRepository;
import com.marketplace_project.marketplace_project.Repositories.PopulateDatabaseRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PopulateDatabaseService {

    private final PopulateDatabaseRepository populateDatabaseRepository;

    public PopulateDatabaseService(PopulateDatabaseRepository populateDatabaseRepository) {
        this.populateDatabaseRepository = populateDatabaseRepository;
    }

    public void populateDatabase(){
        populateDatabaseRepository.populateDatabase();
    }

}
