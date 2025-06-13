package com.example.sbt.module.sportevent.seunit;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.seseason.SESeasonRepository;
import com.example.sbt.module.sportevent.seunit.dto.SEUnitDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SEUnitValidator {

    private final SEUnitRepository seUnitRepository;
    private final SESeasonRepository seSeasonRepository;

    public void cleanRequest(SEUnitDTO requestDTO) {
        if (requestDTO == null) return;
        requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
        requestDTO.setName(ConversionUtils.safeTrim(requestDTO.getCode()));
    }

    public void validateUpdate(SEUnitDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(requestDTO.getName()) && requestDTO.getName().length() > 255) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateCreate(SEUnitDTO requestDTO) {
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
        if (requestDTO.getSeasonId() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (seUnitRepository.existsByCode(requestDTO.getCode())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (!seSeasonRepository.existsById(requestDTO.getSeasonId())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

}
