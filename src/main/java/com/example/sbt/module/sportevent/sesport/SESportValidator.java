package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.seseason.SESeasonRepository;
import com.example.sbt.module.sportevent.sesport.dto.SESportDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SESportValidator {

    private final SESportRepository seSportRepository;
    private final SESeasonRepository seSeasonRepository;

    public void cleanRequest(SESportDTO request) {
        if (request == null) return;
        request.setCode(ConversionUtils.safeTrim(request.getCode()).toUpperCase());
        request.setName(ConversionUtils.safeTrim(request.getName()));
    }

    public void validateUpdate(SESportDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(requestDTO.getName()) && requestDTO.getName().length() > 255) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateCreate(SESportDTO requestDTO) {
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
        if (!seSeasonRepository.existsById(requestDTO.getId())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (seSportRepository.existsByCode(requestDTO.getCode())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

}
