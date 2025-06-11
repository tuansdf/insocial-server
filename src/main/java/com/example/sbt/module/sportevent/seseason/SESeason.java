package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.sportevent.seseason.dto.SESeasonDTO;
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
        name = "se_season",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_season_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_season_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.SE_SEASON_SEARCH, classes = {
        @ConstructorResult(targetClass = SESeasonDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "code", type = String.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "year", type = Integer.class),
                @ColumnResult(name = "start_time", type = Instant.class),
                @ColumnResult(name = "end_time", type = Instant.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class SESeason extends BaseEntity {

    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "year")
    private Integer year;
    @Column(name = "start_time")
    private Instant startTime;
    @Column(name = "end_time")
    private Instant endTime;

}
