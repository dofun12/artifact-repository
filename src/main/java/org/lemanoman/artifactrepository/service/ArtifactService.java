package org.lemanoman.artifactrepository.service;

import org.lemanoman.artifactrepository.model.Artifact;
import org.lemanoman.artifactrepository.repository.ArtifactRepository;
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
