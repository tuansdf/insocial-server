package com.example.sbt.module.sportevent.seregion;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.seregion.dto.SERegionDTO;
import com.example.sbt.module.sportevent.seregion.dto.SearchSERegionRequestDTO;

import java.util.UUID;

public interface SERegionService {

    SERegionDTO save(SERegionDTO roleDTO);

    SERegionDTO findOneById(UUID id);

    SERegionDTO findOneByIdOrThrow(UUID id);

    SERegionDTO findOneByCode(String code);

    SERegionDTO findOneByCodeOrThrow(String code);

    PaginationData<SERegionDTO> search(SearchSERegionRequestDTO requestDTO, boolean isCount);

}
