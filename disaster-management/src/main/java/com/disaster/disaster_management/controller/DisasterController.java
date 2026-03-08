package com.disaster.disaster_management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.disaster.disaster_management.model.Disaster;
import com.disaster.disaster_management.repository.DisasterRepository;
import com.disaster.disaster_management.service.DisasterApiService;

@RestController
@RequestMapping("/api/disasters")
@CrossOrigin(origins = "http://localhost:4200")
public class DisasterController {

    @Autowired
    private DisasterRepository disasterRepository;

    @Autowired
    private DisasterApiService disasterApiService;

    @GetMapping("/fetch")
    public String fetchDisasters(){

        disasterApiService.fetchAndSaveDisasters();

        return "Disasters fetched and saved";

    }

    @GetMapping("/all")
    public List<Disaster> getAllDisasters(){

        return disasterRepository.findAll();

    }

}