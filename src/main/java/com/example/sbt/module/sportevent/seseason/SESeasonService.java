package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.seseason.dto.SESeasonDTO;
import com.example.sbt.module.sportevent.seseason.dto.SearchSESeasonRequestDTO;

import java.util.UUID;

public interface SESeasonService {

    SESeasonDTO save(SESeasonDTO roleDTO);

    SESeasonDTO findOneById(UUID id);

    SESeasonDTO findOneByIdOrThrow(UUID id);

    SESeasonDTO findOneByCode(String code);

    SESeasonDTO findOneByCodeOrThrow(String code);

    PaginationData<SESeasonDTO> search(SearchSESeasonRequestDTO requestDTO, boolean isCount);

}
