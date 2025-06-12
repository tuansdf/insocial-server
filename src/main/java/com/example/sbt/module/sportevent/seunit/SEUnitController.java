package com.example.sbt.module.sportevent.seunit;

import com.example.sbt.common.constant.PermissionCode;
import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.sportevent.seunit.dto.SEUnitDTO;
import com.example.sbt.module.sportevent.seunit.dto.SearchSEUnitRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/se/units")
public class SEUnitController {

    private final SEUnitService seUnitService;

    @GetMapping("/code/{code}")
    public ResponseEntity<CommonResponse<SEUnitDTO>> findOneByCode(@PathVariable String code) {
        try {
            var result = seUnitService.findOneByCodeOrThrow(code);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<SEUnitDTO>> findOneById(@PathVariable UUID id) {
        try {
            var result = seUnitService.findOneByIdOrThrow(id);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @PutMapping
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<SEUnitDTO>> save(@RequestBody SEUnitDTO requestDTO) {
        try {
            var result = seUnitService.save(requestDTO);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<SEUnitDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) UUID seasonId,
            @RequestParam(required = false) String seasonCode,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchSEUnitRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .seasonId(seasonId)
                    .seasonCode(seasonCode)
                    .code(code)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .build();
            var result = seUnitService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
