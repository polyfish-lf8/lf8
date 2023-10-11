package de.szut.lf8_project.lf8.project;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    public ProjectEntity create(ProjectEntity entity) {
        return repository.save(entity);
    }

    public List<ProjectEntity> readAll() {
        return this.repository.findAll();
    }

    public ProjectEntity readById(long id) {
        Optional<ProjectEntity> queriedProject = this.repository.findById(id);
        return queriedProject.orElse(null);
    }

    public void delete(ProjectEntity entity) {
        this.repository.delete(entity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
