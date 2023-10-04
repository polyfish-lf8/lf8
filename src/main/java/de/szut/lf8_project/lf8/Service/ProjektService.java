package de.szut.lf8_project.lf8.Service;

import de.szut.lf8_project.hello.HelloEntity;
import de.szut.lf8_project.lf8.Entity.ProjektEntity;
import de.szut.lf8_project.lf8.Repository.ProjektRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjektService {
    private final ProjektRepository repository;

    public ProjektService(ProjektRepository repository) {
        this.repository = repository;
    }

    public ProjektEntity create(ProjektEntity entity) {
        return  repository.save(entity);
    }

    public List<ProjektEntity> readAll() {
        return this.repository.findAll();
    }

    public ProjektEntity readById(long id) {
        Optional<ProjektEntity> optionalQualification = this.repository.findById(id);
        if (optionalQualification.isEmpty()) {
            return null;
        }
        return optionalQualification.get();
    }

    public void delete(ProjektEntity entity) {
        this.repository.delete(entity);
    }
}
