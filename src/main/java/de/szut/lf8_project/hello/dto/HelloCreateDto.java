package de.szut.lf8_project.hello.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class HelloCreateDto {
    private String message;

    @JsonCreator
    public HelloCreateDto(String message) {
        this.message = message;
    }
}
