package org.lemanoman.videovizartifactrepository.service;

import org.lemanoman.videovizartifactrepository.model.Artifact;
import org.lemanoman.videovizartifactrepository.repository.ArtifactRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtifactService extends GenericService<Artifact, String>{
    private ArtifactRepository repository;

    public ArtifactService(ArtifactRepository repository) {
        super(repository);
        this.repository = repository;
    }

    public List<Artifact> findByArtifactGroup(String group) {
        return repository.findByArtifactGroup(group);
    }
    public List<String> findGroups() {
        return repository.findGroups();
    }

    public Artifact findLatestUpload() {
        return repository.findLatestUpload();
    }

    public Artifact findFirstByVersion(String version) {
        return repository.findFirstByVersion(version);
    }
}
