package com.khafizov.restfull.model;

import com.khafizov.restfull.enums.PostgreSQLEnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

import com.khafizov.restfull.enums.CatColor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "cats")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
public class CatEntity {

    @EmbeddedId
    private CatEntityId name;

    @Enumerated
    @Column(columnDefinition = "cat_color")
    @Type(type = "pgsql_enum")
    private CatColor color;

    @Column(name = "tail_length")
    private Integer tailLength;

    @Column(name = "whiskers_length")
    private Integer whiskersLength;

}
