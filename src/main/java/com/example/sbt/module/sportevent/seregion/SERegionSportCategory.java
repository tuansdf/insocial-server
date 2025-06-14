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
        name = "se_region_sport_category",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_region_sport_category_rel_idx", columnNames = {"region_id", "sport_category_id"}),
        },
        indexes = {
                @Index(name = "se_region_sport_category_created_at_idx", columnList = "created_at"),
        }
)
public class SERegionSportCategory extends BaseEntity {

    @Column(name = "region_id")
    private UUID regionId;
    @Column(name = "sport_category_id")
    private UUID sportCategoryId;

}
