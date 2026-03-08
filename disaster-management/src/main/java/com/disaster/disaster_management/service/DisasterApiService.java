package com.disaster.disaster_management.service;

import com.disaster.disaster_management.model.Disaster;
import com.disaster.disaster_management.repository.DisasterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DisasterApiService {

    @Autowired
    private DisasterRepository disasterRepository;

    private final String API_URL =
    "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson";

    public String fetchAndSaveDisasters(){

        RestTemplate restTemplate = new RestTemplate();

        String response = restTemplate.getForObject(
            "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_day.geojson",
            String.class
        );

        Disaster disaster = new Disaster();

        disaster.setType("Earthquake");
        disaster.setLocation("Global");
        disaster.setMagnitude(5.0);
        disaster.setSeverity("High");

        disasterRepository.save(disaster);

        return "Disasters saved successfully";
    }

}