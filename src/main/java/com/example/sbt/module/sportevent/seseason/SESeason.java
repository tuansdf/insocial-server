package com.example.sbt.module.sportevent.seseason;

import com.example.sbt.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
public class SESeason extends BaseEntity {

    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;

}
