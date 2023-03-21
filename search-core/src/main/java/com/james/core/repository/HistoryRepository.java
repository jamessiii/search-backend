package com.james.core.repository;

import com.james.core.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface HistoryRepository extends JpaRepository<History, String> {

    List<History> findByCreatedAtBefore(LocalDateTime localDateTime);

}