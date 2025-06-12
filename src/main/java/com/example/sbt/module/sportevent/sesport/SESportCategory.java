package com.example.sbt.module.sportevent.sesport;

import com.example.sbt.common.constant.ResultSetName;
import com.example.sbt.common.entity.BaseEntity;
import com.example.sbt.module.sportevent.sesport.dto.SESportCategoryDTO;
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
        name = "se_sport_category",
        uniqueConstraints = {
                @UniqueConstraint(name = "se_sport_category_code_idx", columnNames = "code"),
        },
        indexes = {
                @Index(name = "se_sport_category_sport_id_idx", columnList = "sport_id"),
                @Index(name = "se_sport_category_created_at_idx", columnList = "created_at"),
        }
)
@SqlResultSetMapping(name = ResultSetName.SE_SPORT_CATEGORY_SEARCH, classes = {
        @ConstructorResult(targetClass = SESportCategoryDTO.class, columns = {
                @ColumnResult(name = "id", type = UUID.class),
                @ColumnResult(name = "sport_id", type = UUID.class),
                @ColumnResult(name = "sport_code", type = String.class),
                @ColumnResult(name = "code", type = String.class),
                @ColumnResult(name = "name", type = String.class),
                @ColumnResult(name = "rule", type = String.class),
                @ColumnResult(name = "created_at", type = Instant.class),
                @ColumnResult(name = "updated_at", type = Instant.class),
        })
})
public class SESportCategory extends BaseEntity {

    @Column(name = "sport_id", updatable = false)
    private UUID sportId;
    @Column(name = "code", updatable = false)
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "rule", columnDefinition = "text")
    private String rule;

}
