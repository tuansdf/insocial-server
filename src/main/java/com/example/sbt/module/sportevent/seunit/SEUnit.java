package com.example.sbt.module.sportevent.seunit;

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
        name = "se_unit",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_unit_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_unit_season_id_idx", columnList = "season_id"),
                @Index(name = "se_unit_created_at_idx", columnList = "created_at"),
        }
)
public class SEUnit extends BaseEntity {

    @Column(name = "season_id", updatable = false)
    private UUID seasonId;
    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;

}
