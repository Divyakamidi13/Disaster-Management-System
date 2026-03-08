package com.disaster.disaster_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.disaster.disaster_management.model.Disaster;

public interface DisasterRepository extends JpaRepository<Disaster, Long> {
}