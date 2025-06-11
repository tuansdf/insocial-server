package com.example.sbt.module.sportevent.selocation;

import com.example.sbt.common.constant.PermissionCode;
import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.sportevent.selocation.dto.SELocationDTO;
import com.example.sbt.module.sportevent.selocation.dto.SearchSELocationRequestDTO;
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
@RequestMapping("/v1/se/locations")
public class SELocationController {

    private final SELocationService seLocationService;

    @GetMapping("/code/{code}")
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<SELocationDTO>> findOneByCode(@PathVariable String code) {
        try {
            var result = seLocationService.findOneByCodeOrThrow(code);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/{id}")
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<SELocationDTO>> findOneById(@PathVariable UUID id) {
        try {
            var result = seLocationService.findOneByIdOrThrow(id);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @PutMapping
    @Secured({PermissionCode.SYSTEM_ADMIN})
    public ResponseEntity<CommonResponse<SELocationDTO>> save(@RequestBody SELocationDTO requestDTO) {
        try {
            var result = seLocationService.save(requestDTO);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<SELocationDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) UUID seasonId,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchSELocationRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .seasonId(seasonId)
                    .code(code)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .build();
            var result = seLocationService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
