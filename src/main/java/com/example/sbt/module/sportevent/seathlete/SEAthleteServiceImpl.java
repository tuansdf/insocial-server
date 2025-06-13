package com.example.sbt.module.sportevent.seathlete;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.dto.PaginationData;
import com.example.sbt.common.mapper.CommonMapper;
import com.example.sbt.common.util.ConversionUtils;
import com.example.sbt.common.util.SQLHelper;
import com.example.sbt.module.sportevent.seathlete.dto.SEAthleteDTO;
import com.example.sbt.module.sportevent.seathlete.dto.SearchSEAthleteRequestDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class SEAthleteServiceImpl implements SEAthleteService {

    private final CommonMapper commonMapper;
    private final SEAthleteRepository seAthleteRepository;
    private final EntityManager entityManager;

    @Override
    public SEAthleteDTO findOneOrInitByUserAndSeason(UUID userId, UUID seasonId) {
        SEAthlete result = seAthleteRepository.findTopByUserIdAndSeasonId(userId, seasonId).orElse(null);
        if (result != null) {
            return commonMapper.toDTO(result);
        }
        result = new SEAthlete();
        result.setUserId(userId);
        result.setSeasonId(seasonId);
        result = seAthleteRepository.save(result);
        return commonMapper.toDTO(result);
    }

    @Override
    public PaginationData<SEAthleteDTO> search(SearchSEAthleteRequestDTO requestDTO, boolean isCount) {
        PaginationData<SEAthleteDTO> result = executeSearch(requestDTO, true);
        if (!isCount && result.getTotalItems() > 0) {
            result.setItems(executeSearch(requestDTO, false).getItems());
        }
        return result;
    }

    private PaginationData<SEAthleteDTO> executeSearch(SearchSEAthleteRequestDTO requestDTO, boolean isCount) {
        PaginationData<SEAthleteDTO> result = SQLHelper.initData(requestDTO.getPageNumber(), requestDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        if (isCount) {
            builder.append(" select count(*) ");
        } else {
            builder.append(" select a.id, a.user_id, a.season_id, a.region_id, a.unit_id, a.confirmed_at, a.created_at, a.updated_at, ");
            builder.append(" ss.code as season_code, r.code as region_code, u.code as unit_code, us.username as user_username ");
        }
        builder.append(" from se_athlete as a ");
        builder.append(" inner join ( ");
        {
            builder.append(" select a.id ");
            builder.append(" from se_athlete as a ");
            if (!isCount) {
                builder.append(" left join _user as us on (us.id = a.user_id) ");
            }
            builder.append(" where 1=1 ");
            if (requestDTO.getSeasonId() != null) {
                builder.append(" and a.season_id = :seasonId ");
                params.put("seasonId", requestDTO.getSeasonId());
            }
            if (requestDTO.getUserId() != null) {
                builder.append(" and a.user_id = :userId ");
                params.put("userId", requestDTO.getSeasonId());
            }
            if (requestDTO.getRegionId() != null) {
                builder.append(" and a.region_id = :regionId ");
                params.put("regionId", requestDTO.getSeasonId());
            }
            if (requestDTO.getUnitId() != null) {
                builder.append(" and a.unit_id = :unitId ");
                params.put("unitId", requestDTO.getSeasonId());
            }
            if (requestDTO.getConfirmedAtFrom() != null) {
                builder.append(" and a.confirmed_at >= :confirmedAtFrom ");
                params.put("confirmedAtFrom", requestDTO.getConfirmedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
            }
            if (requestDTO.getConfirmedAtTo() != null) {
                builder.append(" and a.confirmed_at <= :confirmedAtTo ");
                params.put("confirmedAtTo", requestDTO.getConfirmedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
            }
            if (requestDTO.getCreatedAtFrom() != null) {
                builder.append(" and a.created_at >= :createdAtFrom ");
                params.put("createdAtFrom", requestDTO.getCreatedAtFrom().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
            }
            if (requestDTO.getCreatedAtTo() != null) {
                builder.append(" and a.created_at <= :createdAtTo ");
                params.put("createdAtTo", requestDTO.getCreatedAtTo().truncatedTo(SQLHelper.MIN_TIME_PRECISION));
            }
            if (!isCount) {
                builder.append(" order by us.username asc, a.id asc ");
                builder.append(SQLHelper.toLimitOffset(result.getPageNumber(), result.getPageSize()));
            }
        }
        builder.append(" ) as filter on (filter.id = a.id) ");
        if (!isCount) {
            builder.append(" left join se_season as ss on (ss.id = a.season_id) ");
            builder.append(" left join se_unit as u on (u.id = a.unit_id) ");
            builder.append(" left join se_region as r on (r.id = a.region_id) ");
            builder.append(" left join _user as us on (us.id = a.user_id) ");
            builder.append(" order by us.username asc, a.id asc ");
        }
        if (isCount) {
            Query query = entityManager.createNativeQuery(builder.toString());
            SQLHelper.setParams(query, params);
            long count = ConversionUtils.safeToLong(query.getSingleResult());
            result.setTotalItems(count);
            result.setTotalPages(SQLHelper.toPages(count, result.getPageSize()));
        } else {
            Query query = entityManager.createNativeQuery(builder.toString(), ResultSetName.SE_ATHLETE_SEARCH);
            SQLHelper.setParams(query, params);
            List<SEAthleteDTO> items = query.getResultList();
            result.setItems(items);
        }
        return result;
    }

}
