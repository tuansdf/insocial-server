package com.example.sbt.module.sportevent.sesport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SESportDisciplineRepository extends JpaRepository<SESportDiscipline, UUID> {
}
