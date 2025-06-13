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
public class SearchSEAthleteRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private UUID userId;
    private UUID seasonId;
    private UUID regionId;
    private UUID unitId;
    private Instant confirmedAtFrom;
    private Instant confirmedAtTo;
    private Instant createdAtFrom;
    private Instant createdAtTo;

}
