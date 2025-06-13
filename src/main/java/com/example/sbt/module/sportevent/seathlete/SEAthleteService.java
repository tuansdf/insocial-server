package com.example.sbt.module.sportevent.seathlete;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.seathlete.dto.SEAthleteDTO;
import com.example.sbt.module.sportevent.seathlete.dto.SearchSEAthleteRequestDTO;

import java.util.UUID;

public interface SEAthleteService {

    SEAthleteDTO findOneOrInitByUserAndSeason(UUID userId, UUID seasonId);

    PaginationData<SEAthleteDTO> search(SearchSEAthleteRequestDTO requestDTO, boolean isCount);

}
