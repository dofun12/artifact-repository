package org.lemanoman.artifactrepository.interfaces;

public interface MergeableModel<T, ID> {
    ID getId();
    void merge(T merge);
}
