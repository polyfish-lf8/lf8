package de.szut.lf8_project.lf8.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long responsibleCustomerEmployeeId;
    private Long responsibleEmployeeId;

    @ElementCollection
    @CollectionTable
    private List<Long> employees = new ArrayList<>();

    @ElementCollection
    @CollectionTable
    private List<String> skillSet = new ArrayList<>();
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
