package com.example.sbt.module.sportevent.seuser;

import com.example.sbt.common.entity.BaseEntity;
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
        name = "se_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_user_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_user_user_id_idx", columnList = "user_id"),
                @Index(name = "se_user_season_id_idx", columnList = "season_id"),
                @Index(name = "se_user_region_id_idx", columnList = "region_id"),
                @Index(name = "se_user_unit_id_idx", columnList = "unit_id"),
                @Index(name = "se_user_created_at_idx", columnList = "created_at"),
        }
)
public class SEUser extends BaseEntity {

    @Column(name = "user_id", updatable = false)
    private UUID userId;
    @Column(name = "season_id", updatable = false)
    private UUID seasonId;
    @Column(name = "region_id")
    private UUID regionId;
    @Column(name = "unit_id")
    private UUID unitId;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "confirmed_at")
    private Instant confirmedAt;

}
