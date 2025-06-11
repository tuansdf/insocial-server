package com.example.sbt.module.sportevent.seseason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SESeasonRepository extends JpaRepository<SESeason, UUID> {
}
