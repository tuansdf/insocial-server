package com.example.sbt.module.sportevent.sesportdiscipline;

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
        name = "se_sport_discipline",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_sport_discipline_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_sport_discipline_sport_id_idx", columnList = "sport_id"),
                @Index(name = "se_sport_discipline_created_at_idx", columnList = "created_at"),
        }
)
public class SESportDiscipline extends BaseEntity {

    @Column(name = "sport_id")
    private UUID sportId;
    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "rule", columnDefinition = "text")
    private String rule;

}
