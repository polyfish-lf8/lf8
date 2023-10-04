package de.szut.lf8_project.lf8.project;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.lang.reflect.Array;
import java.time.LocalDateTime;
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
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
