package com.example.sbt.module.sportevent.seathlete;

import com.example.sbt.common.dto.CommonResponse;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.dto.RequestHolder;
import com.example.sbt.common.util.ExceptionUtils;
import com.example.sbt.module.sportevent.seathlete.dto.SEAthleteDTO;
import com.example.sbt.module.sportevent.seathlete.dto.SearchSEAthleteRequestDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/se/athletes")
public class SEAthleteController {

    private final SEAthleteService seAthleteService;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<SEAthleteDTO>> findOneById(@RequestParam UUID seasonId) {
        try {
            var result = seAthleteService.findOneOrInitByUserAndSeason(RequestHolder.getContext().getUserId(), seasonId);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<CommonResponse<PaginationData<SEAthleteDTO>>> search(
            @RequestParam(required = false) Long pageNumber,
            @RequestParam(required = false) Long pageSize,
            @RequestParam(required = false) UUID seasonId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID regionId,
            @RequestParam(required = false) UUID unitId,
            @RequestParam(required = false) Instant confirmedAtFrom,
            @RequestParam(required = false) Instant confirmedAtTo,
            @RequestParam(required = false) Instant createdAtFrom,
            @RequestParam(required = false) Instant createdAtTo,
            @RequestParam(required = false, defaultValue = "false") Boolean count) {
        try {
            var requestDTO = SearchSEAthleteRequestDTO.builder()
                    .pageNumber(pageNumber)
                    .pageSize(pageSize)
                    .seasonId(seasonId)
                    .userId(userId)
                    .regionId(regionId)
                    .unitId(unitId)
                    .confirmedAtFrom(confirmedAtFrom)
                    .confirmedAtTo(confirmedAtTo)
                    .createdAtTo(createdAtTo)
                    .createdAtFrom(createdAtFrom)
                    .build();
            var result = seAthleteService.search(requestDTO, count);
            return ResponseEntity.ok(new CommonResponse<>(result));
        } catch (Exception e) {
            return ExceptionUtils.toResponseEntity(e);
        }
    }

}
