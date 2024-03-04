package org.lemanoman.artifactrepository.repository;

import org.lemanoman.artifactrepository.model.Artifact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtifactRepository extends JpaRepository<Artifact, String> {

    @Query("SELECT a.artifactGroup as group FROM Artifact a")
    List<String> findGroups();

    List<Artifact> findByArtifactGroup(String group);

    @Query("SELECT a FROM Artifact a order by a.dateCreated desc limit 1")
    Artifact findLatestUpload();

    Artifact findFirstByVersion(String version);
}
