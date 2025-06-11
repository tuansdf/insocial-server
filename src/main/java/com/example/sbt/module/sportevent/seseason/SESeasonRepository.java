package com.example.sbt.module.sportevent.seseason;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SESeasonRepository extends JpaRepository<SESeason, UUID> {

    boolean existsByCode(String code);

    Optional<SESeason> findTopByCode(String code);

}
