package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.sesport.dto.SESportCategoryDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SESportCategoryValidator {

    private final SESportCategoryRepository seSportCategoryRepository;
    private final SESportRepository seSportRepository;

    public void cleanRequest(SESportCategoryDTO requestDTO) {
        if (requestDTO == null) return;
        requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
        requestDTO.setName(ConversionUtils.safeTrim(requestDTO.getName()));
        requestDTO.setRule(ConversionUtils.safeTrim(requestDTO.getRule()));
    }

    public void validateUpdate(SESportCategoryDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(requestDTO.getName()) && requestDTO.getName().length() > 255) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateCreate(SESportCategoryDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(requestDTO.getCode())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        String codeError = ValidationUtils.validateCode(requestDTO.getCode());
        if (codeError != null) {
            throw new CustomException(codeError);
        }
        if (requestDTO.getSportId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (seSportCategoryRepository.existsByCode(requestDTO.getCode())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (!seSportRepository.existsById(requestDTO.getSportId())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

}
