package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.module.sportevent.sesport.dto.SESportCategoryDTO;
import com.example.sbt.module.sportevent.sesport.dto.SearchSESportCategoryRequestDTO;

import java.util.UUID;

public interface SESportCategoryService {

    SESportCategoryDTO save(SESportCategoryDTO roleDTO);

    SESportCategoryDTO findOneById(UUID id);

    SESportCategoryDTO findOneByIdOrThrow(UUID id);

    SESportCategoryDTO findOneByCode(String code);

    SESportCategoryDTO findOneByCodeOrThrow(String code);

    PaginationData<SESportCategoryDTO> search(SearchSESportCategoryRequestDTO requestDTO, boolean isCount);

}
