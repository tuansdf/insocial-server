package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.seseason.dto.SESeasonDTO;
import com.example.sbt.module.sportevent.seseason.dto.SearchSESeasonRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class SESeasonServiceImpl implements SESeasonService {

    private final CommonMapper commonMapper;
    private final SESeasonRepository seSeasonRepository;
    private final SESeasonValidator seSeasonValidator;
    private final EntityManager entityManager;

    @Override
    public SESeasonDTO save(SESeasonDTO requestDTO) {
        seSeasonValidator.cleanRequest(requestDTO);
        seSeasonValidator.validateUpdate(requestDTO);
        SESeason result = null;
        if (requestDTO.getId() != null) {
            result = seSeasonRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            seSeasonValidator.validateCreate(requestDTO);
            result = new SESeason();
            result.setCode(requestDTO.getCode());
            result.setYear(requestDTO.getYear());
        }
        result.setName(requestDTO.getName());
        result.setStartTime(requestDTO.getStartTime());
        result.setEndTime(requestDTO.getEndTime());
        result = seSeasonRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SESeasonDTO findOneById(UUID id) {
        if (id == null) return null;
        return seSeasonRepository.findById(id).map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public SESeasonDTO findOneByIdOrThrow(UUID id) {
        SESeasonDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SESeasonDTO findOneByCode(String code) {
        if (StringUtils.isBlank(code)) return null;
        return seSeasonRepository.findTopByCode(code).map(commonMapper::toDTO).orElse(null);
    }

    @Override
    public SESeasonDTO findOneByCodeOrThrow(String code) {
        SESeasonDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SESeasonDTO> search(SearchSESeasonRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESeasonDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SESeasonDTO> executeSearch(SearchSESeasonRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESeasonDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select ss.* ");
        }
        builder.append(" from se_season ss ");
        builder.append(" where 1=1 ");
        if (StringUtils.isNotBlank(requestDTO.getCode())) {
            builder.append(" and ss.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getYear() != null) {
            builder.append(" and ss.year = :year ");
            params.put("year", requestDTO.getYear());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and ss.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and ss.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by ss.code asc, ss.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_SEASON_SEARCH);
            SQLHelper.setParams(query, params);
            List<SESeasonDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
