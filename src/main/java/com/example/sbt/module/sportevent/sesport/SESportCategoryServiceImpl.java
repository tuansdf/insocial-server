package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.exception.CustomException;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.sesport.dto.SESportCategoryDTO;
import com.example.sbt.module.sportevent.sesport.dto.SearchSESportCategoryRequestDTO;
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
public class SESportCategoryServiceImpl implements SESportCategoryService {

    private final CommonMapper commonMapper;
    private final SESportCategoryRepository seSportCategoryRepository;
    private final SESportRepository seSportRepository;
    private final SESportCategoryValidator seSportCategoryValidator;
    private final EntityManager entityManager;

    @Override
    public SESportCategoryDTO save(SESportCategoryDTO requestDTO) {
        if (requestDTO == null) {
            throw new CustomException(HttpStatus.BAD_REQUEST);
        }
        SESportCategory result = null;
        if (requestDTO.getId() != null) {
            result = seSportCategoryRepository.findById(requestDTO.getId()).orElse(null);
        }
        if (result == null) {
            requestDTO.setCode(ConversionUtils.safeTrim(requestDTO.getCode()).toUpperCase());
            seSportCategoryValidator.validateCreate(requestDTO);
            if (seSportCategoryRepository.existsByCode(requestDTO.getCode())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            if (!seSportRepository.existsById(requestDTO.getSportId())) {
                throw new CustomException(HttpStatus.BAD_REQUEST);
            }
            result = new SESportCategory();
            result.setCode(requestDTO.getCode());
            result.setSportId(requestDTO.getSportId());
        } else {
            seSportCategoryValidator.validateUpdate(requestDTO);
        }
        result.setName(requestDTO.getName());
        result.setRule(requestDTO.getRule());
        result = seSportCategoryRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public SESportCategoryDTO findOneById(UUID id) {
        if (id == null) return null;
        List<SESportCategoryDTO> result = executeSearch(SearchSESportCategoryRequestDTO.builder()
                .id(id).pageNumber(1L).pageSize(1L).isDetail(true).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SESportCategoryDTO findOneByIdOrThrow(UUID id) {
        SESportCategoryDTO result = findOneById(id);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public SESportCategoryDTO findOneByCode(String code) {
        if (code == null) return null;
        List<SESportCategoryDTO> result = executeSearch(SearchSESportCategoryRequestDTO.builder()
                .code(code).pageNumber(1L).pageSize(1L).isDetail(true).build(), false).getItems();
        if (CollectionUtils.isEmpty(result)) return null;
        return result.getFirst();
    }

    @Override
    public SESportCategoryDTO findOneByCodeOrThrow(String code) {
        SESportCategoryDTO result = findOneByCode(code);
        if (result == null) {
            throw new CustomException(HttpStatus.NOT_FOUND);
        }
        return result;
    }

    @Override
    public PaginationData<SESportCategoryDTO> search(SearchSESportCategoryRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESportCategoryDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SESportCategoryDTO> executeSearch(SearchSESportCategoryRequestDTO requestDTO, boolean isCount) {
        PaginationData<SESportCategoryDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select sc.id, sc.sport_id, ss.code as sport_code, sc.code, sc.name, sc.created_at, sc.updated_at, ");
            if (Boolean.TRUE.equals(requestDTO.getIsDetail())) {
                builder.append(" sc.rule ");
            } else {
                builder.append(" null as rule ");
            }
        }
        builder.append(" from se_sport_category sc ");
        builder.append(" left join se_sport ss on (sc.sport_id = ss.id) ");
        builder.append(" where 1=1 ");
        if (requestDTO.getSportId() != null) {
            builder.append(" and sc.sport_id = :sportId ");
            params.put("sportId", requestDTO.getSportId());
        }
        if (StringUtils.isNotEmpty(requestDTO.getCode())) {
            builder.append(" and sc.code = :code ");
            params.put("code", requestDTO.getCode());
        }
        if (requestDTO.getCreatedAtFrom() != null) {
            builder.append(" and sc.created_at >= :createdAtFrom ");
            params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (requestDTO.getCreatedAtTo() != null) {
            builder.append(" and sc.created_at <= :createdAtTo ");
            params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
        }
        if (!isCount) {
            builder.append(" order by sc.code asc, sc.id asc ");
            builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_SPORT_CATEGORY_SEARCH);
            SQLHelper.setParams(query, params);
            List<SESportCategoryDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
