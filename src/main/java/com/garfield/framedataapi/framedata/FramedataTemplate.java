package com.garfield.framedataapi.framedata;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashMap;
import java.util.Map;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FramedataTemplate {

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @NotEmpty
    private Map<String, Object> attributes = new HashMap<>();

    public boolean hasProperty(String key) {
        return this.attributes.containsKey(key);
    }

}
