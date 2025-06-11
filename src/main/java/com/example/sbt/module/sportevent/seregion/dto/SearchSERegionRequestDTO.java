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
public class SearchSERegionRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private UUID id;
    private UUID seasonId;
    private String code;
    private Instant createdAtFrom;
    private Instant createdAtTo;

}
