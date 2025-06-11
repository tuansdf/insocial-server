package com.example.sbt.module.sportevent.seregion;

import com.example.sbt.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
        name = "se_region_sport_discipline",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_region_sport_discipline_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_region_sport_discipline_comp_id_idx", columnList = "region_id, sport_discipline_id"),
                @Index(name = "se_region_sport_discipline_created_at_idx", columnList = "created_at"),
        }
)
public class SERegionSportDiscipline extends BaseEntity {

    @Column(name = "region_id")
    private UUID regionId;
    @Column(name = "sport_discipline_id")
    private UUID sportDisciplineId;

}
