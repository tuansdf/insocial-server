package com.example.sbt.module.sportevent.seregion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SERegionSportCategoryRepository extends JpaRepository<SERegionSportCategory, UUID> {
}
