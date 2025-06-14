package com.example.sbt.module.sportevent.seathlete;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.sportevent.seathlete.dto.SEAthleteDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "se_athlete",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_athlete_rel_idx", columnNames = {"season_id", "user_id"}),
        },
        indexes = {
                @Index(name = "se_athlete_user_id_idx", columnList = "user_id"),
                @Index(name = "se_athlete_region_id_idx", columnList = "region_id"),
                @Index(name = "se_athlete_unit_id_idx", columnList = "unit_id"),
                @Index(name = "se_athlete_confirmed_at_idx", columnList = "confirmed_at"),
                @Index(name = "se_athlete_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.SE_ATHLETE_SEARCH, classes = {
        @ConstructorResult(targetClass = SEAthleteDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "user_id", type = UUID.class),
                @ColumnResult(name = "season_id", type = UUID.class),
                @ColumnResult(name = "region_id", type = UUID.class),
                @ColumnResult(name = "unit_id", type = UUID.class),
                @ColumnResult(name = "confirmed_at", type = Instant.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
                @ColumnResult(name = "season_code", type = String.class),
                @ColumnResult(name = "region_code", type = String.class),
                @ColumnResult(name = "unit_code", type = String.class),
                @ColumnResult(name = "user_username", type = String.class),
        })
})
public class SEAthlete extends BaseEntity {

    @Column(name = "user_id", updatable = false)
    private UUID userId;
    @Column(name = "season_id", updatable = false)
    private UUID seasonId;
    @Column(name = "region_id")
    private UUID regionId;
    @Column(name = "unit_id")
    private UUID unitId;
    @Column(name = "confirmed_at")
    private Instant confirmedAt;

}
