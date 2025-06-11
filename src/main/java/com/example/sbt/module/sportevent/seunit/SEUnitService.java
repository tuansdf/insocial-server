package com.example.sbt.module.sportevent.seunit;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.seunit.dto.SEUnitDTO;
import com.example.sbt.module.sportevent.seunit.dto.SearchSEUnitRequestDTO;

import java.util.UUID;

public interface SEUnitService {

    SEUnitDTO save(SEUnitDTO roleDTO);

    SEUnitDTO findOneById(UUID id);

    SEUnitDTO findOneByIdOrThrow(UUID id);

    SEUnitDTO findOneByCode(String code);

    SEUnitDTO findOneByCodeOrThrow(String code);

    PaginationData<SEUnitDTO> search(SearchSEUnitRequestDTO requestDTO, boolean isCount);

}
