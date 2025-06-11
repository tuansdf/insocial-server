package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.seseason.dto.SESeasonDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SESeasonValidator {

    public void validateUpdate(SESeasonDTO requestDTO) {
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
    }

    public void validateCreate(SESeasonDTO requestDTO) {
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
        validateUpdate(requestDTO);
    }

}
