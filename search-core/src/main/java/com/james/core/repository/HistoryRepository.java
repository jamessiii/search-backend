package com.james.core.repository;

import com.james.core.entity.History;
import com.james.core.entity.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    Page<History> findBySearch(Search search, Pageable pageable);

}