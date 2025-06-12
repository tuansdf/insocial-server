package com.example.sbt.module.sportevent.selocation;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.selocation.dto.SELocationDTO;
import com.example.sbt.module.sportevent.selocation.dto.SearchSELocationRequestDTO;
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
public class SELocationServiceImpl implements SELocationService {

    private final CommonMapper commonMapper;
    private final SELocationRepository seLocationRepository;
    private final SESeasonRepository seSeasonRepository;
    private final SELocationValidator seLocationValidator;
    private final EntityManager entityManager;

    @Override
    public SELocationDTO save(SELocationDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        SELocation result = null;
        if (requestDTO.getId() != null) {
            result = seLocationRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
            seLocationValidator.validateCreate(requestDTO);
            if (seLocationRepository.existsByCode(requestDTO.getCode())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            if (!seSeasonRepository.existsById(requestDTO.getSeasonId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            result = new SELocation();
            result.setCode(requestDTO.getCode());
            result.setSeasonId(requestDTO.getSeasonId());
        } else {
            seLocationValidator.validateUpdate(requestDTO);
        }
        result.setName(requestDTO.getName());
        result.setAddress(requestDTO.getAddress());
        result = seLocationRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SELocationDTO findOneById(UUID id) {
        if (id == null) return null;
        List<SELocationDTO> result = executeSearch(SearchSELocationRequestDTO.builder()
                .id(id).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SELocationDTO findOneByIdOrThrow(UUID id) {
        SELocationDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SELocationDTO findOneByCode(String code) {
        if (code == null) return null;
        List<SELocationDTO> result = executeSearch(SearchSELocationRequestDTO.builder()
                .code(code).pageNumber(1L).pageSize(1L).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SELocationDTO findOneByCodeOrThrow(String code) {
        SELocationDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SELocationDTO> search(SearchSELocationRequestDTO requestDTO, boolean isCount) {
        PaginationData<SELocationDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SELocationDTO> executeSearch(SearchSELocationRequestDTO requestDTO, boolean isCount) {
        PaginationData<SELocationDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select l.id, l.season_id, s.code as season_code, l.code, l.name, l.address, l.created_at, l.updated_at ");
        }
        builder.append(" from se_location l ");
        builder.append(" left join se_season s on (l.season_id = s.id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getSeasonId() != null) {
            builder.append(" and l.season_id = :seasonId ");
            params.put("seasonId", requestDTO.getSeasonId());
        }
        if (StringUtils.isNotEmpty(requestDTO.getCode())) {
            builder.append(" and l.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and l.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and l.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by l.code asc, l.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_LOCATION_SEARCH);
            SQLHelper.setParams(query, params);
            List<SELocationDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
