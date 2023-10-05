package de.szut.lf8_project.config;

import de.szut.lf8_project.lf8.project.ProjectEntity;
import de.szut.lf8_project.lf8.project.ProjectRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
public class SampleProjectDataCreator implements ApplicationRunner {
    private final ProjectRepository repository;

    public SampleProjectDataCreator(ProjectRepository repository) {
        this.repository = repository;
    }

    public void run(ApplicationArguments args) {
        repository.save(new ProjectEntity(1L, 2L, 3L, 4L, new ArrayList<>(), "Hallo Arad", LocalDate.now(), LocalDate.now().plusDays(1)));
        repository.save(new ProjectEntity(2L, 3L, 4L, 5L, new ArrayList<>(), "Hallo Jana", LocalDate.now(), LocalDate.now().plusDays(2)));
        repository.save(new ProjectEntity(3L, 4L, 5L, 6L, new ArrayList<>(), "Hallo Mika", LocalDate.now(), LocalDate.now().plusDays(3)));
        repository.save(new ProjectEntity(4L, 5L, 6L, 7L, new ArrayList<>(), "Hallo Waled", LocalDate.now(), LocalDate.now().plusDays(4)));
    }
}
