package com.example.sbt.module.sportevent.seunit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SEUnitRepository extends JpaRepository<SEUnit, UUID> {
}
