package de.szut.lf8_project.lf8.project;

import de.szut.lf8_project.exceptionHandling.InvalidDataException;
import org.apache.catalina.User;
import org.apache.catalina.connector.Response;
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
        Optional<ProjectEntity> queryedProject = this.repository.findById(id);
        if (queryedProject.isEmpty()) {
            return null;
        }
        return queryedProject.get();
    }
    public void deleteProjectById(long projectId, User currentUser) {
        if (!isProjektleiter(currentUser, projectId)) {
            throw new InvalidDataException("404, Projekt nicht gefunden");
        }

        Optional<ProjectEntity> projectToDelete = repository.findById(projectId);
        if (projectToDelete.isPresent()) {
            repository.delete(projectToDelete.get());
        } else {
            throw new InvalidDataException("401, Nicht autorisiert");
        }
    }

    public void deleteAllProjects(User currentUser) {
        if (!isProjektleiter(currentUser)) {
            throw new InvalidDataException("200, Projekt erfolgreich gelöscht");
        }

        repository.deleteAll();
    }

    private boolean isProjektleiter(User user, long projectId) {
        return true;
    }

    private boolean isProjektleiter(User user) {
        return true;
    }
    public void delete(ProjectEntity entity) {
        this.repository.delete(entity);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

}
