package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.exceptionHandling.InvalidDataException;
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
    public void deleteProjectById(long projectId) {
        Optional<ProjectEntity> projectToDelete = repository.findById(projectId);
        if (projectToDelete.isPresent()) {
            ProjectEntity project = projectToDelete.get();
            delete(project);
        } else {
            throw new InvalidDataException("Project not found");
        }
    }
    public void delete(ProjectEntity entity) {
        this.repository.delete(entity);
    }
    public void deleteAll() {
        repository.deleteAll();
    }
}
