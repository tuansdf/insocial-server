package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.seseason.dto.SESeasonDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Component
public class SESeasonValidator {

    private final SESeasonRepository seSeasonRepository;

    public void cleanRequest(SESeasonDTO requestDTO) {
        if (requestDTO == null) return;
        requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
        requestDTO.setName(ConversionUtils.safeTrim(requestDTO.getName()));
        requestDTO.setStartTime(requestDTO.getStartTime().truncatedTo(ChronoUnit.SECONDS));
        requestDTO.setEndTime(requestDTO.getEndTime().truncatedTo(ChronoUnit.SECONDS));
    }

    public void validateUpdate(SESeasonDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isEmpty(requestDTO.getName())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getName().length() > 255) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getStartTime() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getEndTime() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getEndTime().isBefore(requestDTO.getStartTime())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateCreate(SESeasonDTO requestDTO) {
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
        if (requestDTO.getYear() == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (requestDTO.getYear() <= 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        if (seSeasonRepository.existsByCode(requestDTO.getCode())) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

}
