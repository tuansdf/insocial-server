package com.example.sbt.module.sportevent.seregion.dto;

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
public class SERegionDTO {

    private UUID id;
    private UUID seasonId;
    private String seasonCode;
    private String code;
    private String name;
    private Instant createdAt;
    private Instant updatedAt;

}
