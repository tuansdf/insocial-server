package com.example.sbt.module.sportevent.seregion;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.seregion.dto.SERegionDTO;
import com.example.sbt.module.sportevent.seregion.dto.SearchSERegionRequestDTO;
import com.example.sbt.module.sportevent.seseason.SESeasonRepository;
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
public class SERegionServiceImpl implements SERegionService {

    private final CommonMapper commonMapper;
    private final SERegionRepository seRegionRepository;
    private final SESeasonRepository seSeasonRepository;
    private final SERegionValidator seRegionValidator;
    private final EntityManager entityManager;

    @Override
    public SERegionDTO save(SERegionDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        SERegion result = null;
        if (requestDTO.getId() != null) {
            result = seRegionRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
            seRegionValidator.validateCreate(requestDTO);
            if (seRegionRepository.existsByCode(requestDTO.getCode())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            if (!seSeasonRepository.existsById(requestDTO.getSeasonId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            result = new SERegion();
            result.setCode(requestDTO.getCode());
            result.setSeasonId(requestDTO.getSeasonId());
        } else {
            seRegionValidator.validateUpdate(requestDTO);
        }
        result.setName(requestDTO.getName());
        result = seRegionRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SERegionDTO findOneById(UUID id) {
        if (id == null) return null;
        List<SERegionDTO> result = executeSearch(SearchSERegionRequestDTO.builder().id(id).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SERegionDTO findOneByIdOrThrow(UUID id) {
        SERegionDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SERegionDTO findOneByCode(String code) {
        if (code == null) return null;
        List<SERegionDTO> result = executeSearch(SearchSERegionRequestDTO.builder().code(code).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SERegionDTO findOneByCodeOrThrow(String code) {
        SERegionDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SERegionDTO> search(SearchSERegionRequestDTO requestDTO, boolean isCount) {
        PaginationData<SERegionDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SERegionDTO> executeSearch(SearchSERegionRequestDTO requestDTO, boolean isCount) {
        PaginationData<SERegionDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select r.id, r.season_id, s.code as season_code, r.code, r.name, r.created_at, r.updated_at ");
        }
        builder.append(" from se_region r ");
        builder.append(" left join se_season s on (r.season_id = s.id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getSeasonId() != null) {
            builder.append(" and r.season_id = :seasonId ");
            params.put("seasonId", requestDTO.getSeasonId());
        }
        if (StringUtils.isNotEmpty(requestDTO.getCode())) {
            builder.append(" and r.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and r.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and r.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by r.code asc, r.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_REGION_SEARCH);
            SQLHelper.setParams(query, params);
            List<SERegionDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
