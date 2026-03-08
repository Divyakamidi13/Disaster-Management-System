package com.disaster.disaster_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.disaster.disaster_management.service.DisasterApiService;

@RestController
@RequestMapping("/api/disaster")
@CrossOrigin(origins = "http://localhost:4200")
public class DisasterApiController {

    @Autowired
    private DisasterApiService disasterApiService;

    @GetMapping("/fetch")
    public String fetchDisasters(){

        return disasterApiService.fetchAndSaveDisasters();

    }
}