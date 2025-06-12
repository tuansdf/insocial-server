package com.example.sbt.module.sportevent.seunit;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.seseason.SESeasonRepository;
import com.example.sbt.module.sportevent.seunit.dto.SEUnitDTO;
import com.example.sbt.module.sportevent.seunit.dto.SearchSEUnitRequestDTO;
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
public class SEUnitServiceImpl implements SEUnitService {

    private final CommonMapper commonMapper;
    private final SEUnitRepository seUnitRepository;
    private final SESeasonRepository seSeasonRepository;
    private final SEUnitValidator seUnitValidator;
    private final EntityManager entityManager;

    @Override
    public SEUnitDTO save(SEUnitDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
        requestDTO.setName(ConversionUtils.safeTrim(requestDTO.getName()));
        SEUnit result = null;
        if (requestDTO.getId() != null) {
            result = seUnitRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            seUnitValidator.validateCreate(requestDTO);
            if (seUnitRepository.existsByCode(requestDTO.getCode())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            if (!seSeasonRepository.existsById(requestDTO.getSeasonId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            result = new SEUnit();
            result.setCode(requestDTO.getCode());
            result.setSeasonId(requestDTO.getSeasonId());
        } else {
            seUnitValidator.validateUpdate(requestDTO);
        }
        result.setName(requestDTO.getName());
        result = seUnitRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SEUnitDTO findOneById(UUID id) {
        if (id == null) return null;
        List<SEUnitDTO> result = executeSearch(SearchSEUnitRequestDTO.builder()
                .id(id).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SEUnitDTO findOneByIdOrThrow(UUID id) {
        SEUnitDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SEUnitDTO findOneByCode(String code) {
        if (code == null) return null;
        List<SEUnitDTO> result = executeSearch(SearchSEUnitRequestDTO.builder()
                .code(code).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SEUnitDTO findOneByCodeOrThrow(String code) {
        SEUnitDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SEUnitDTO> search(SearchSEUnitRequestDTO requestDTO, boolean isCount) {
        PaginationData<SEUnitDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SEUnitDTO> executeSearch(SearchSEUnitRequestDTO requestDTO, boolean isCount) {
        PaginationData<SEUnitDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select u.id, u.season_id, ss.code as season_code, u.code, u.name, u.created_at, u.updated_at ");
        }
        builder.append(" from se_unit u ");
        builder.append(" left join se_season ss on (u.season_id = ss.id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getSeasonId() != null) {
            builder.append(" and u.season_id = :seasonId ");
            params.put("seasonId", requestDTO.getSeasonId());
        }
        if (StringUtils.isNotBlank(requestDTO.getSeasonCode())) {
            builder.append(" and ss.code = :seasonCode ");
            params.put("seasonCode", requestDTO.getSeasonCode());
        }
        if (StringUtils.isNotBlank(requestDTO.getCode())) {
            builder.append(" and u.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and u.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and u.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by u.code asc, u.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_UNIT_SEARCH);
            SQLHelper.setParams(query, params);
            List<SEUnitDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
