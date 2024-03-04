package org.lemanoman.videovizartifactrepository.interfaces;

public interface MergeableModel<T, ID> {
    ID getId();
    void merge(T merge);
}
