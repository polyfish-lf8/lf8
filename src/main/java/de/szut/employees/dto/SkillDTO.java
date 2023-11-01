package de.szut.employees.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
@Setter
public class SkillDTO {
    @JsonProperty("skill")
    private String skill;

    @JsonProperty("id")
    private Long id;
}
