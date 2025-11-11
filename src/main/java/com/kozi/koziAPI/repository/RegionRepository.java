package com.kozi.koziAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kozi.koziAPI.model.Region;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long>{

} 
