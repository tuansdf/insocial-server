package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.seseason.SESeasonRepository;
import com.example.sbt.module.sportevent.sesport.dto.SESportDTO;
import com.example.sbt.module.sportevent.sesport.dto.SearchSESportRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class SESportServiceImpl implements SESportService {

    private final CommonMapper commonMapper;
    private final SESportRepository seSportRepository;
    private final SESeasonRepository seSeasonRepository;
    private final SESportValidator seSportValidator;
    private final EntityManager entityManager;

    @Override
    public SESportDTO save(SESportDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        SESport result = null;
        if (requestDTO.getId() != null) {
            result = seSportRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
            seSportValidator.validateCreate(requestDTO);
            if (seSportRepository.existsByCode(requestDTO.getCode())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            if (!seSeasonRepository.existsById(requestDTO.getSeasonId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            result = new SESport();
            result.setCode(requestDTO.getCode());
            result.setSeasonId(requestDTO.getSeasonId());
        } else {
            seSportValidator.validateUpdate(requestDTO);
        }
        result.setName(requestDTO.getName());
        result = seSportRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SESportDTO findOneById(UUID id) {
        if (id == null) return null;
        List<SESportDTO> result = executeSearch(SearchSESportRequestDTO.builder()
                .id(id).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SESportDTO findOneByIdOrThrow(UUID id) {
        SESportDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SESportDTO findOneByCode(String code) {
        if (code == null) return null;
        List<SESportDTO> result = executeSearch(SearchSESportRequestDTO.builder()
                .code(code).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SESportDTO findOneByCodeOrThrow(String code) {
        SESportDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SESportDTO> search(SearchSESportRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESportDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SESportDTO> executeSearch(SearchSESportRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESportDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select s.id, s.season_id, s.code as season_code, s.code, s.name, s.created_at, s.updated_at ");
        }
        builder.append(" from se_sport s ");
        builder.append(" left join se_season ss on (s.season_id = ss.id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getSeasonId() != null) {
            builder.append(" and s.season_id = :seasonId ");
            params.put("seasonId", requestDTO.getSeasonId());
        }
        if (StringUtils.isNotEmpty(requestDTO.getCode())) {
            builder.append(" and s.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and s.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and s.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by s.code asc, s.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_SPORT_SEARCH);
            SQLHelper.setParams(query, params);
            List<SESportDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
