package org.lemanoman.artifactrepository.model;

import jakarta.persistence.*;
import org.lemanoman.artifactrepository.interfaces.MergeableModel;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "artifact")
public class Artifact implements Serializable, MergeableModel<Artifact, String>{

    @Id
    private String id;

    private String name;


    @Column(name = "date_created")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    private String checksum;

    @Column(name = "artifact_group")
    private String artifactGroup;

    private String description;

    private String version;

    @Column(name = "build_number")
    private String buildNumber;

    private String path;

    public String getId() {
        return id;
    }

    @Override
    public void merge(Artifact merge) {
        this.name = merge.getName();
        this.dateCreated = merge.getDateCreated();
        this.description = merge.getDescription();
        this.path = merge.getPath();
        this.checksum = merge.getChecksum();
        this.version = merge.getVersion();
        this.artifactGroup = merge.getArtifactGroup();
        this.buildNumber = merge.getBuildNumber();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public void setBuildNumber(String buildNumber) {
        this.buildNumber = buildNumber;
    }

    public String getArtifactGroup() {
        return artifactGroup;
    }

    public void setArtifactGroup(String artifactGroup) {
        this.artifactGroup = artifactGroup;
    }

    @Override
    public String toString() {
        return "Artifact{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
