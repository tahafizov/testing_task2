package com.khafizov.restfull.repository;

import com.khafizov.restfull.model.CatEntity;
import com.khafizov.restfull.model.CatEntityId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface CatsRepository extends PagingAndSortingRepository<CatEntity, Long> {
    Optional<CatEntity> findByName(CatEntityId name);
}
