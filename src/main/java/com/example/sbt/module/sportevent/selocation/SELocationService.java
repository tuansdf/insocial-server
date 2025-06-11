package com.example.sbt.module.sportevent.selocation;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.selocation.dto.SELocationDTO;
import com.example.sbt.module.sportevent.selocation.dto.SearchSELocationRequestDTO;

import java.util.UUID;

public interface SELocationService {

    SELocationDTO save(SELocationDTO roleDTO);

    SELocationDTO findOneById(UUID id);

    SELocationDTO findOneByIdOrThrow(UUID id);

    SELocationDTO findOneByCode(String code);

    SELocationDTO findOneByCodeOrThrow(String code);

    PaginationData<SELocationDTO> search(SearchSELocationRequestDTO requestDTO, boolean isCount);

}
