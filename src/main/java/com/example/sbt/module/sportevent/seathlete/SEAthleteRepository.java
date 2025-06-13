package com.example.sbt.module.sportevent.seathlete;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SEAthleteRepository extends JpaRepository<SEAthlete, UUID> {

    Optional<SEAthlete> findTopByUserIdAndSeasonId(UUID userId, UUID seasonId);

}
