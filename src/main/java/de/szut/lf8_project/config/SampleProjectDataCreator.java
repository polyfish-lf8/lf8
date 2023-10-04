package de.szut.lf8_project.config;

import de.szut.lf8_project.hello.HelloEntity;
import de.szut.lf8_project.hello.HelloRepository;
import de.szut.lf8_project.lf8.project.ProjectEntity;
import de.szut.lf8_project.lf8.project.ProjectRepository;
import org.springframework.boot.ApplicationArguments;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class SampleProjectDataCreator {
    private final ProjectRepository repository;

    public SampleProjectDataCreator(ProjectRepository repository) {
        this.repository = repository;
    }

    public void run(ApplicationArguments args) {
        repository.save(new ProjectEntity(1L, 2L, 3L, 4L, new ArrayList<>(), "Hallo Arad", LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
        repository.save(new ProjectEntity(2L, 3L, 4L, 6L, new ArrayList<>(), "Hallo Jana", LocalDateTime.now(), LocalDateTime.now().plusDays(2)));
        repository.save(new ProjectEntity(3L, 4L, 5L, 7L, new ArrayList<>(), "Hallo Mika", LocalDateTime.now(), LocalDateTime.now().plusDays(3)));
        repository.save(new ProjectEntity(3L, 4L, 5L, 7L, new ArrayList<>(), "Hallo Waled", LocalDateTime.now(), LocalDateTime.now().plusDays(4)));
    }
}
