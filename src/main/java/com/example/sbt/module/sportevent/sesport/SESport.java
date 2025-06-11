package com.example.sbt.module.sportevent.sesport;

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
        name = "se_sport",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_sport_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_sport_season_id_idx", columnList = "season_id"),
                @Index(name = "se_sport_created_at_idx", columnList = "created_at"),
        }
)
public class SESport extends BaseEntity {

    @Column(name = "season_id", updatable = false)
    private UUID seasonId;
    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;

}
