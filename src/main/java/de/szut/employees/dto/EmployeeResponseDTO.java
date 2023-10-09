package de.szut.employees.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;

@Setter
@Getter
@NoArgsConstructor
public class EmployeeResponseDTO {
    @NotNull
    @Min(0)
    @JsonProperty("id")
    private Long id;

    @NotNull
    @JsonProperty("lastName")
    private String lastName;

    @NotNull
    @JsonProperty("firstName")
    private String firstName;

    @NotNull
    @JsonProperty("street")
    private String street;

    @NotNull
    @Size(min = 5, max = 5)
    @JsonProperty("postcode")
    private String postcode;

    @NotNull
    @JsonProperty("city")
    private String city;

    @NotNull
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("skillSet")
    private ArrayList<String> skillSet;
}
