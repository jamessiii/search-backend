package com.james.core.repository;

import com.james.core.domain.entity.Search;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchRepository extends JpaRepository<Search, String> {

    Optional<Search> findById(long id);
}
