package de.szut.lf8_project.config;

import de.szut.lf8_project.lf8.project.ProjectEntity;
import de.szut.lf8_project.lf8.project.ProjectRepository;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementEntity;
import de.szut.lf8_project.lf8.timemanagement.TimeManagementRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Component
public class SampleProjectDataCreator implements ApplicationRunner {
    private final ProjectRepository repository;
    private final TimeManagementRepository tmRepository;

    public void run(ApplicationArguments args) {
        var p1 = repository.save(new ProjectEntity(1L, 2L, 3L, 4L, new HashSet<>(), new HashSet<>(Set.of(1L)), "Hallo Arad", LocalDate.now(), LocalDate.now().plusDays(1)));
        var p2 = repository.save(new ProjectEntity(2L, 3L, 4L, 5L, new HashSet<>(), new HashSet<>(Set.of(1L)), "Hallo Jana", LocalDate.now().plusDays(2), LocalDate.now().plusDays(3)));
        var p3 = repository.save(new ProjectEntity(3L, 4L, 5L, 6L, new HashSet<>(), new HashSet<>(Set.of(1L)), "Hallo Mika", LocalDate.now().plusDays(4), LocalDate.now().plusDays(5)));
        var p4 = repository.save(new ProjectEntity(4L, 5L, 6L, 7L, new HashSet<>(), new HashSet<>(Set.of(1L)), "Hallo Waled", LocalDate.now().plusDays(6), LocalDate.now().plusDays(7)));

        var tm = tmRepository.saveAll(
                List.of(
                        new TimeManagementEntity(0L, p1.getId(), 1L, p1.getStartDate(), p1.getEndDate()),
                        new TimeManagementEntity(0L, p2.getId(), 1L, p2.getStartDate(), p2.getEndDate()),
                        new TimeManagementEntity(0L, p3.getId(), 1L, p3.getStartDate(), p3.getEndDate()),
                        new TimeManagementEntity(0L, p4.getId(), 1L, p4.getStartDate(), p4.getEndDate())
                )
        );

        p1.getEmployees().add(tm.get(0).getId());
        p2.getEmployees().add(tm.get(1).getId());
        p3.getEmployees().add(tm.get(2).getId());
        p4.getEmployees().add(tm.get(3).getId());

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
        repository.save(p4);
    }
}
