package com.example.sbt.module.sportevent.selocation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SELocationRepository extends JpaRepository<SELocation, UUID> {
}
