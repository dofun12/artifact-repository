package org.lemanoman.videovizartifactrepository.dto;

import org.lemanoman.videovizartifactrepository.model.Artifact;

import java.util.Date;

public class ArtifactDTO {
    private String key;
    private String artifactGroup;
    private String name;
    private String description;
    private String version;
    private String buildNumber;
    private Date dateCreated;

    public ArtifactDTO() {
    }

    public static ArtifactDTO fromModel(Artifact artifact){
        return new ArtifactDTO(artifact);
    }
    
    public ArtifactDTO(Artifact artifact){
        this.key = artifact.getId();
        this.artifactGroup = artifact.getArtifactGroup();
        this.name = artifact.getName();
        this.description = artifact.getDescription();
        this.version = artifact.getVersion();
        this.buildNumber = artifact.getBuildNumber();
        this.dateCreated = artifact.getDateCreated();
    }

    public Artifact toModel(){
        Artifact artifact = new Artifact();
        artifact.setId(this.key);
        artifact.setArtifactGroup(this.artifactGroup);
        artifact.setName(this.name);
        artifact.setDescription(this.description);
        artifact.setVersion(this.version);
        artifact.setBuildNumber(this.buildNumber);
        artifact.setDateCreated(this.dateCreated);
        return artifact;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getArtifactGroup() {
        return artifactGroup;
    }

    public void setArtifactGroup(String artifactGroup) {
        this.artifactGroup = artifactGroup;
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

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}
