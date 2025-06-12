package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.sesport.dto.SESportDTO;
import com.example.sbt.module.sportevent.sesport.dto.SearchSESportRequestDTO;

import java.util.UUID;

public interface SESportService {

    SESportDTO save(SESportDTO roleDTO);

    SESportDTO findOneById(UUID id);

    SESportDTO findOneByIdOrThrow(UUID id);

    SESportDTO findOneByCode(String code);

    SESportDTO findOneByCodeOrThrow(String code);

    PaginationData<SESportDTO> search(SearchSESportRequestDTO requestDTO, boolean isCount);

}
