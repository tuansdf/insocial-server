package com.example.sbt.module.sportevent.seseason.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchSESeasonRequestDTO {

    private Long pageNumber;
    private Long pageSize;
    private String code;
    private Integer year;
    private Instant createdAtFrom;
    private Instant createdAtTo;

}
