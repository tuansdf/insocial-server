package com.example.sbt.module.sportevent.seathlete.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SEAthleteDTO {

    private UUID id;
    private UUID userId;
    private UUID seasonId;
    private UUID regionId;
    private UUID unitId;
    private Instant confirmedAt;
    private Instant createdAt;
    private Instant updatedAt;

    private String seasonCode;
    private String regionCode;
    private String unitCode;
    private String userUsername;

}
