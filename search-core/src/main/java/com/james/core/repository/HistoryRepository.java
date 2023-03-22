package com.james.core.repository;

import com.james.core.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    Optional<List<History>> findByCreatedAtBefore(LocalDateTime localDateTime);

}