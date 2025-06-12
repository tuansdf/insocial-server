package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.constant.PermissionCode;
import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.sportevent.sesport.dto.SESportDTO;
import com.example.sbt.module.sportevent.sesport.dto.SearchSESportRequestDTO;
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
@RequestMapping("/v1/se/sports")
public class SESportController {

    private final SESportService seSportService;

    @GetMapping("/code/{code}")
    public ResponseEntity<CommonResponse<SESportDTO>> findOneByCode(@PathVariable String code) {
        try {
            var result = seSportService.findOneByCodeOrThrow(code);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<SESportDTO>> findOneById(@PathVariable UUID id) {
        try {
            var result = seSportService.findOneByIdOrThrow(id);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @PutMapping
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<SESportDTO>> save(@RequestBody SESportDTO requestDTO) {
        try {
            var result = seSportService.save(requestDTO);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<SESportDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) UUID seasonId,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchSESportRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .seasonId(seasonId)
                    .code(code)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .build();
            var result = seSportService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
