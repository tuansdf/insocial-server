package com.example.sbt.module.sportevent.seunit;

import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.util.ValidationUtils;
import com.example.sbt.module.sportevent.seunit.dto.SEUnitDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SEUnitValidator {

    public void validateUpdate(SEUnitDTO requestDTO) {
        if (StringUtils.isEmpty(requestDTO.getName()) && requestDTO.getName().length() > 255) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
    }

    public void validateCreate(SEUnitDTO requestDTO) {
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
        validateUpdate(requestDTO);
    }

}
