package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Project")
public class ProjectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Long responsibleCustomerEmployeeId;
    private Long responsibleEmployeeId;

    @OneToMany()
    private Set<TimeManagementEntity> employees = new HashSet<>();

    @ElementCollection
    @CollectionTable
    private Set<Long> skillSet = new HashSet<>();
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
}
