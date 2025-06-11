package com.example.sbt.module.sportevent.selocation;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.sportevent.selocation.dto.SELocationDTO;
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
        name = "se_location",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_location_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_location_season_id_idx", columnList = "season_id"),
                @Index(name = "se_location_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.SE_LOCATION_SEARCH, classes = {
        @ConstructorResult(targetClass = SELocationDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "season_id", type = UUID.class),
                @ColumnResult(name = "season_code", type = String.class),
                @ColumnResult(name = "code", type = String.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "address", type = String.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class SELocation extends BaseEntity {

    @Column(name = "season_id", updatable = false)
    private UUID seasonId;
    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "address", columnDefinition = "text")
    private String address;

}
